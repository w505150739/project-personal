package com.personal.system.login.controller;

import com.personal.common.base.BaseController;
import com.personal.common.exception.BusinessException;
import com.personal.common.exception.ParameterException;
import com.personal.common.util.LogUtil;
import com.personal.common.util.result.ResultData;
import com.personal.system.login.entity.UserEntity;
import com.personal.system.login.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author liuyuzhu
 * @description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/1/27 22:54
 */
@Controller
@RequestMapping("api/user")
public class UserController extends BaseController{

    private static final LogUtil logger = LogUtil.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/login")
    public ResultData login(HttpServletRequest request){
        Map<String,Object> params = this.getAllParams(request);
        ResultData result = new ResultData(true,"登陆成功");
        if(!params.containsKey("userName")){
            result.setResult(false);
            result.setMessage("请输入用户名");
            return  result;
        }
        if(!params.containsKey("password")){
            result.setResult(false);
            result.setMessage("请输入密码");
            return  result;
        }
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(params.get("userName").toString(),params.get("password").toString());

        try {
            subject.login(token);  //认证
            subject.hasRole("admin"); //授权--执行授权代码并保存到内存中
        } catch (UnknownAccountException ex) {
            ex.printStackTrace();
            result.setResult(false);
            result.setMessage("用户名或密码错误");
            return  result;
        } catch (IncorrectCredentialsException ex) {
            ex.printStackTrace();
            result.setResult(false);
            result.setMessage("用户名或密码错误");
            return  result;
        }catch (AuthenticationException e) {
            e.printStackTrace();
            result.setResult(false);
            result.setMessage("用户名或密码错误");
            return  result;
        }
        if(subject.isAuthenticated()){
            result.setData(subject.getSession().getId());
            return result;
        }else {
            result.setResult(false);
            result.setMessage("user isn't administrators!");
            return  result;
        }
    }
}
