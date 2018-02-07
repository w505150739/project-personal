package com.personal.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.personal.common.jwt.JWTHelper;
import com.personal.common.util.DeployInfoUtil;
import com.personal.common.util.IPUtil;
import com.personal.common.util.LogUtil;
import com.personal.common.util.crypt.CryptTool;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SystemInterceptor extends HandlerInterceptorAdapter {

    private static final LogUtil logger = LogUtil.getLogger(SystemInterceptor.class);

    private List<String> excludeUrls;

    /**
     * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("==============执行顺序: 1、preHandle================");
        String requestUri = request.getRequestURI();
        if(excludeUrls.indexOf(requestUri) != -1){
            return true;
        }
        String headToken = request.getHeader("access_token");
        logger.info("登录拦截器requestUri:" + requestUri);
        logger.info("登录拦截器ip地址:" + IPUtil.getRealIP(request));
        if(!StringUtils.isNotBlank(headToken)){
            logger.info("Interceptor：token 为null 跳转到login页面！");
            response.sendRedirect(DeployInfoUtil.getLoginUrl());
            return false;
        }

        try {
            Claims claims = JWTHelper.parseToken(headToken,CryptTool.CRYPT_KEY);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("token 失效或错误");
            response.sendRedirect(DeployInfoUtil.getLoginUrl());
            return false;
        }
    }

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
}
