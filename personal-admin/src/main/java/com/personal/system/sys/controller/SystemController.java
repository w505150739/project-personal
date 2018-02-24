package com.personal.system.sys.controller;

import com.personal.common.base.BaseController;
import com.personal.common.util.LogUtil;
import com.personal.common.util.constants.GlobalConstants;
import com.personal.common.util.result.ResultData;
import com.personal.menu.entity.TSysMenuEntity;
import com.personal.menu.reqbean.MenuReqBean;
import com.personal.menu.service.TSysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(GlobalConstants.PREFIX + "sys")
public class SystemController extends BaseController {

    private static final LogUtil logger = LogUtil.getLogger(SystemController.class);

    @Resource
    private TSysMenuService menuService;

    /**
     * 根据登录用户和角色获取菜单
     * @param request 获取token
     * @return result
     */
    @ResponseBody
    @RequestMapping(value = "/menulist.do")
    public ResultData queryMenuList(HttpServletRequest request){
        Map<String,Object> params = this.getAllParams(request);
        params.put("pid","0");
        logger.info("根据当前登录用户获取菜单 params = " + params.toString());
        String userId = this.parseToken(request).get("userId").toString();
        //String roleId = this.parseToken(request).get("roleId").toString();
        List<TSysMenuEntity> tSysMenuList = menuService.queryList(params);
        ResultData result = new ResultData().successResult("菜单获取成功",getChildren(tSysMenuList));
        return result;
    }

    /**
     * 递归获取菜单
     * @param tSysMenuList 父菜单
     * @return 返回菜单树
     */
    private List<MenuReqBean> getChildren(List<TSysMenuEntity> tSysMenuList){
        Map<String,Object> params = new HashMap<String,Object>();
        List<MenuReqBean> returnList = new ArrayList<MenuReqBean>();
        if(tSysMenuList != null && !tSysMenuList.isEmpty()){
            for (TSysMenuEntity menu : tSysMenuList) {
                MenuReqBean reqBean = new MenuReqBean();
                reqBean.setName(menu.getMenuName());
                reqBean.setTitle(menu.getMenuTitle());
                reqBean.setIcon(menu.getMenuIcon());
                reqBean.setJump(menu.getMenuUrl());
                params.put("pid",menu.getId());
                reqBean.setList(getChildren(menuService.queryList(params)));
                returnList.add(reqBean);
            }
        }
        return returnList;
    }
}
