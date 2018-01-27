package com.personal.system.shiro.realm;


import com.personal.common.util.LogUtil;
import com.personal.system.login.entity.UserEntity;
import com.personal.system.login.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: UserRealm 
 * @描述: 自定义域，实现
 * @author malp@wiseyq.com
 * @date 2015年5月19日 上午10:59:36
 */
public class UserRealm extends AuthorizingRealm {
	
	private final LogUtil logger = LogUtil.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;

	//一、用户认证管理
	/**
	 * 返回认证信息。通过username从数据源中获取用户信息，用于验证登录信息是否于数据源中信息一致。
	 * 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
	 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) 
    		throws AuthenticationException {

    	String userName = (String)token.getPrincipal();

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("userName",userName);
    	UserEntity user = userService.findByMap(params);
    	System.out.print("==============="+user.getNickName());
    	SimpleAuthenticationInfo info = null;
    	
    	if(null != user && StringUtils.isNotBlank(user.getId()+"")){
    		info = new SimpleAuthenticationInfo(
            		user, //身份(principal)
            		user.getPassword(), //密码
                    getName()  //realm name
            	);
            //设置密码加密的干扰数据
            //info.setCredentialsSalt( ByteSource.Util.bytes(username.getBytes()));
    	}
        return info;
    }

    /**
     * 生成用户认证信息保存在内存中的key，解决使用不同权限验证方式产生的可以不一致问题。
     */
    @Override
    protected Object getAuthenticationCacheKey(AuthenticationToken token) {
        return token != null ? "authen-"+token.getPrincipal() : null;
    }
    
    //二、用户授权管理
  	/**
  	 * 返回授权信息
  	 */
  	@Override
  	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
  		/**
  		User user = (User) principals.getPrimaryPrincipal(); //获取第一个身份
  		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.addRole(user.getRole());
          
        //用户的角色
        //info.setRoles(userService.findRolesByUserId(user.getId()));
        //用户的权限
        //info.setStringPermissions(userService.findPermissionsByUserId(user.getId()));
        return info;
         */
  		return null;
	}
  	
  	/**
  	 * 生成用户权限保存在内存中的key，解决使用不同权限验证方式产生的可以不一致问题。
  	 */
  	@Override
  	public Object getAuthorizationCacheKey(PrincipalCollection principals){

  	    /**
      	User user = (User) principals.getPrimaryPrincipal();
      	String key = "author-"+user.getMobile();
      	logger.info("get user cache key");
      	return key;
         */
  	    return null;
      }
    
    /**
     * 清除用户授权信息
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }
    
    public void clearCachedAuthenticationInfo(String userName){
    	 Cache<Object, AuthenticationInfo> cache = super.getAuthenticationCache();
         if (cache != null) {
             cache.remove(userName);
         }
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}

