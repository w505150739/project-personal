package com.personal.system.user.controller;

import com.personal.common.base.BaseController;
import com.personal.common.jwt.JWTHelper;
import com.personal.common.util.DeployInfoUtil;
import com.personal.common.util.LogUtil;
import com.personal.common.util.RandomUtil;
import com.personal.common.util.constants.GlobalConstants;
import com.personal.common.util.crypt.CryptTool;
import com.personal.common.util.crypt.MD5Util;
import com.personal.common.util.result.ResultData;
import com.personal.system.user.entity.UserEntity;
import com.personal.system.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyuzhu
 * @description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/1/27 22:54
 */
@Controller
@RequestMapping(GlobalConstants.PREFIX + "user")
public class UserController extends BaseController{

    private static final LogUtil logger = LogUtil.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * @param request 入参
     * @return 登录结果
     */
    @ResponseBody
    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    public ResultData login(HttpServletRequest request){
        Map<String,Object> params = this.getAllParams(request);
        logger.info("Login params:" + params.toString());
        if(!params.containsKey("userName")){
            return  new ResultData().errorResult("请输入用户名");
        }
        if(!params.containsKey("password")){
            return  new ResultData().errorResult("请输入密码");
        }

        UserEntity user = userService.findByMap(params);
        if(user == null ){
            return  new ResultData().errorResult("用户名不存在");
        }
        /**
         * 生成密码随机盐值
         */
        String salt = RandomUtil.generateString(24);
        /** 用户输入的密码 **/
        String password = params.get("password").toString();
        /** 经过盐值加密后的密码 **/
        password = MD5Util.getMD5(user.getSalt() + password);
        if(!password.equals(user.getPassword())){
            return  new ResultData().errorResult("用户名或密码错误");
        }
        String accessToken = this.getTokenByUser(user.getId()+"",user.getId()+"", CryptTool.CRYPT_KEY);
        logger.info("token===" + accessToken);
        Map<String,Object> data = new HashMap<String,Object>();
        data.put(GlobalConstants.TOKEN,accessToken);
        return new ResultData().successResult("登录成功",data);
    }

    /**
     * 退出操作
     * @param request 入参
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/loginout.do",method = RequestMethod.POST)
    public ResultData loginOut(HttpServletRequest request){
        Map<String,Object> params = this.getAllParams(request);
        logger.info("logginOut params:" + params.toString());
        ResultData result = new ResultData();
        return result;
    }
}
