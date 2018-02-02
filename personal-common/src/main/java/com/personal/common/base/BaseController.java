package com.personal.common.base;

import com.personal.common.jwt.JWTHelper;
import com.personal.common.util.DeployInfoUtil;
import com.personal.common.util.crypt.CryptTool;
import com.personal.common.util.crypt.CryptUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyuzhu
 * @description: controller 基类
 * @date 2018/1/23 13:59
 */
public class BaseController {

    protected String getStringSession(HttpServletRequest request, String name) {
        Object obj = getSession(request).getAttribute(name);
        return obj == null ? null : obj.toString();
    }

    protected Long getLongSession(HttpServletRequest request, String name) {
        Object obj = getSession(request).getAttribute(name);
        return obj == null ? null : Long.parseLong(obj.toString());
    }
    protected Integer getIntegerSession(HttpServletRequest request, String name) {
        Object obj = getSession(request).getAttribute(name);
        return obj == null ? null : Integer.parseInt(obj.toString());
    }

    protected HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }

    public Map<String, Object> getAllParams(HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();

            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }

    /**
     * 生产token
     * @param userId 用户id
     * @param roleId 角色id
     * @param salt 用户盐值
     * @return token
     */
    public String getTokenByUser(String userId,String roleId,String salt){
        Map<String,Object> tokenMap = new HashMap<String,Object>();
        tokenMap.put("userId",userId);
        tokenMap.put("roles",roleId);
        //过期时间
        Long expire = System.currentTimeMillis() + 1000*60*Integer.parseInt(DeployInfoUtil.getTokenExpire());
        /** 生成token **/
        return JWTHelper.createJwt(tokenMap,salt,expire);
    }
}
