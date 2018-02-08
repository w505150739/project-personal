package com.personal.dept.controller;

import java.util.List;
import java.util.Map;

import com.personal.common.util.LogUtil;
import com.personal.common.util.constants.GlobalConstants;
import com.personal.common.util.result.ResultData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.personal.common.base.BaseController;
import com.personal.dept.entity.TSysDeptEntity;
import com.personal.dept.service.TSysDeptService;
import com.personal.common.util.PageUtil;
import com.personal.common.util.Query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 系统部门表
 * 
 * @author liuyuzhu
 * @email liuyuzhu.1314@gmail.com
 * @date 2018-02-08 15:11:38
 */
@RestController
@RequestMapping(GlobalConstants.PREFIX + "tsysdept")
public class TSysDeptController extends BaseController{

    private static LogUtil logger = LogUtil.getLogger(TSysDeptController.class);

	@Autowired
	private TSysDeptService tSysDeptService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping(value = "/list.do",method = RequestMethod.POST)
	public PageUtil list(@RequestParam Map<String, Object> params){
        ResultData result=new ResultData();
		//查询列表数据
        Query query = new Query(params);

		List<TSysDeptEntity> tSysDeptList = tSysDeptService.queryList(query);
		int total = tSysDeptService.queryTotal(query);

        PageUtil pageUtil = new PageUtil(0, "查询成功", total, tSysDeptList);
		return pageUtil;
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping(value = "/info.do",method = RequestMethod.POST)
	public ResultData info(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> params = this.getAllParams(request);
        logger.info("  TSysDeptController info params:" + params.toString());

	    ResultData result = new ResultData();

		TSysDeptEntity tSysDept = tSysDeptService.get(Long.valueOf(params.get("id").toString()));

		result.setData(tSysDept);
		return result;
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping(value = "/save.do",method = RequestMethod.POST)
	public ResultData save(TSysDeptEntity tSysDept){
        ResultData result = new ResultData();

		tSysDeptService.save(tSysDept);

		result.setMessage("保存成功！");

		return result;
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value = "/update.do",method = RequestMethod.POST)
	public ResultData update(TSysDeptEntity tSysDept){

	    ResultData result = new ResultData();

		tSysDeptService.update(tSysDept);

        result.setMessage("更新成功！");

		return result;
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteById.do",method = RequestMethod.POST)
	public ResultData deleteById(HttpServletRequest request){
		Map<String,Object> params = this.getAllParams(request);
		logger.info("  TSysDeptController deleteById params:" + params.toString());
        ResultData result = new ResultData();
		Long id = Long.valueOf(params.get("id").toString());
		tSysDeptService.deleteById(id);

        result.setMessage("删除成功！");
		return result;
	}
	
}
