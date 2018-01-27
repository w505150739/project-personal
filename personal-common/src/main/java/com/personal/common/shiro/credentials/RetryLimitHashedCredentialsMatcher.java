package com.personal.common.shiro.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @描述: 密码验证
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }
    
    /**
     * 通过缓存来保存密码效验次数
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        username = "count-"+username;
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        //密码重试次数限制为 5次
        if(retryCount.incrementAndGet() > 5) {
            throw new ExcessiveAttemptsException();
        }

        boolean matches = doTokenAndInfoCredentialsMatch(token, info);
        
        if(matches) {
            passwordRetryCache.remove(username);
        }
        return matches;
    }
   
    /**
     * 
     * 手机端是散列值匹配，web端是明文匹配;
     * @param token 用户提交的验证信息
     * @param info 来自Realm数据源
     * @return
     */
   public boolean doTokenAndInfoCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
	    boolean match = false;
	    
	    //基于配置的密码验证服务
        match = super.doCredentialsMatch(token, info);
        
        //token密码和info密码直接比较
        if(!match && token instanceof UsernamePasswordToken){
        	String tokenPassword= new String(((UsernamePasswordToken)token).getPassword());
        	String infoPassword = new String(info.getCredentials().toString());
        	
        	if(tokenPassword.trim().equals(infoPassword.trim())){
        		return true;
        	}
        }
        return match;
    }
}
