package com.personal.role.controller;

import java.util.List;
import java.util.Map;

import com.personal.common.util.LogUtil;
import com.personal.common.util.result.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.personal.common.util.constants.GlobalConstants;

import com.personal.common.base.BaseController;
import com.personal.role.entity.TSysRoleEntity;
import com.personal.role.service.TSysRoleService;
import com.personal.common.util.PageUtil;
import com.personal.common.util.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 系统角色表
 * 
 * @author liuyuzhu
 * @email liuyuzhu.1314@gmail.com
 * @date 2018-02-08 16:36:33
 */
@RestController
@RequestMapping(GlobalConstants.PREFIX + "tsysrole")
public class TSysRoleController extends BaseController{

    private static LogUtil logger = LogUtil.getLogger(TSysRoleController.class);

	@Autowired
	private TSysRoleService tSysRoleService;
	
	/**
	 * 列表
	 */
    @ResponseBody
	@RequestMapping(value = "/list.do",method = RequestMethod.POST)
	public PageUtil list(@RequestParam Map<String, Object> params){
        ResultData result=new ResultData();
		//查询列表数据
        Query query = new Query(params);

		List<TSysRoleEntity> tSysRoleList = tSysRoleService.queryList(query);
		int total = tSysRoleService.queryTotal(query);

        PageUtil pageUtil = new PageUtil(0, "查询成功", total, tSysRoleList);
		return pageUtil;
	}
	
	
	/**
	 * 信息
	 */
    @ResponseBody
	@RequestMapping(value = "/info.do",method = RequestMethod.POST)
	public ResultData info(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> params = this.getAllParams(request);
        logger.info("  TSysRoleController info params:" + params.toString());

	    ResultData result = new ResultData();

		TSysRoleEntity tSysRole = tSysRoleService.get(Long.valueOf(params.get("id").toString()));

		result.setData(tSysRole);
		return result;
	}
	
	/**
	 * 保存
	 */
    @ResponseBody
	@RequestMapping(value = "/save.do",method = RequestMethod.POST)
	public ResultData save(TSysRoleEntity tSysRole){
        ResultData result = new ResultData();

		tSysRoleService.save(tSysRole);

		result.setMessage("保存成功！");

		return result;
	}
	
	/**
	 * 修改
	 */
    @ResponseBody
	@RequestMapping(value = "/update.do",method = RequestMethod.POST)
	public ResultData update(TSysRoleEntity tSysRole){

	    ResultData result = new ResultData();

		tSysRoleService.update(tSysRole);

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
        logger.info("  TSysRoleController deleteById params:" + params.toString());

        ResultData result = new ResultData();
		Long id = Long.valueOf(params.get("id").toString());
		tSysRoleService.deleteById(id);

        result.setMessage("删除成功！");
		return result;
	}
	
}
