package com.personal.generator.controller;

import com.alibaba.fastjson.JSON;
import com.personal.common.base.BaseController;
import com.personal.common.filter.XssHttpServletRequestWrapper;
import com.personal.common.util.PageUtil;
import com.personal.common.util.Query;
import com.personal.common.util.constants.GlobalConstants;
import com.personal.common.util.result.ResultData;
import com.personal.generator.service.SysGeneratorService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author liuyuzhu
 * @description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/1/26 14:58
 */
@Controller
@RequestMapping(GlobalConstants.PREFIX + "generator")
public class GeneratorController extends BaseController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping(value = "/genlist.do",method = RequestMethod.POST)
    public PageUtil list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(query);
        Integer total = sysGeneratorService.queryTotal(query);

        PageUtil pageUtil = new PageUtil(0, "查询成功", total, list);

        return pageUtil;
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code.do")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        //获取表名，不进行xss过滤
        HttpServletRequest orgRequest = XssHttpServletRequestWrapper.getOrgRequest(request);
        String tables = orgRequest.getParameter("tables");
        tableNames = tables.split(",");

        try {
            byte[] data = sysGeneratorService.generatorCode(tableNames, GlobalConstants.genType.webDown.getValue());

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"personal.zip\"");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");

            IOUtils.write(data, response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
