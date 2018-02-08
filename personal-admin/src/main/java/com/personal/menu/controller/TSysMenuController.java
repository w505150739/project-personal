package com.personal.menu.controller;

import java.util.List;
import java.util.Map;

import com.personal.common.util.LogUtil;
import com.personal.common.util.result.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.personal.common.util.constants.GlobalConstants;

import com.personal.common.base.BaseController;
import com.personal.menu.entity.TSysMenuEntity;
import com.personal.menu.service.TSysMenuService;
import com.personal.common.util.PageUtil;
import com.personal.common.util.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 系统菜单表
 * 
 * @author liuyuzhu
 * @email liuyuzhu.1314@gmail.com
 * @date 2018-02-08 16:36:33
 */
@RestController
@RequestMapping(GlobalConstants.PREFIX + "tsysmenu")
public class TSysMenuController extends BaseController{

    private static LogUtil logger = LogUtil.getLogger(TSysMenuController.class);

	@Autowired
	private TSysMenuService tSysMenuService;
	
	/**
	 * 列表
	 */
    @ResponseBody
	@RequestMapping(value = "/list.do",method = RequestMethod.POST)
	public PageUtil list(@RequestParam Map<String, Object> params){
        ResultData result=new ResultData();
		//查询列表数据
        Query query = new Query(params);

		List<TSysMenuEntity> tSysMenuList = tSysMenuService.queryList(query);
		int total = tSysMenuService.queryTotal(query);

        PageUtil pageUtil = new PageUtil(0, "查询成功", total, tSysMenuList);
		return pageUtil;
	}
	
	
	/**
	 * 信息
	 */
    @ResponseBody
	@RequestMapping(value = "/info.do",method = RequestMethod.POST)
	public ResultData info(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> params = this.getAllParams(request);
        logger.info("  TSysMenuController info params:" + params.toString());

	    ResultData result = new ResultData();

		TSysMenuEntity tSysMenu = tSysMenuService.get(Long.valueOf(params.get("id").toString()));

		result.setData(tSysMenu);
		return result;
	}
	
	/**
	 * 保存
	 */
    @ResponseBody
	@RequestMapping(value = "/save.do",method = RequestMethod.POST)
	public ResultData save(TSysMenuEntity tSysMenu){
        ResultData result = new ResultData();

		tSysMenuService.save(tSysMenu);

		result.setMessage("保存成功！");

		return result;
	}
	
	/**
	 * 修改
	 */
    @ResponseBody
	@RequestMapping(value = "/update.do",method = RequestMethod.POST)
	public ResultData update(TSysMenuEntity tSysMenu){

	    ResultData result = new ResultData();

		tSysMenuService.update(tSysMenu);

        result.setMessage("更新成功！");

		return result;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete.do",method = RequestMethod.POST)
    @ResponseBody
	public ResultData delete(HttpServletRequest request){

        Map<String,Object> params = this.getAllParams(request);
        logger.info("  TSysMenuController deleteById params:" + params.toString());

        ResultData result = new ResultData();
		Long id = Long.valueOf(params.get("id").toString());
		tSysMenuService.deleteById(id);

        result.setMessage("删除成功！");
		return result;
	}
	
}
