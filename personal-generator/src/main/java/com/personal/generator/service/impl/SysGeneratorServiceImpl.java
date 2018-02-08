package com.personal.generator.service.impl;

import com.personal.generator.mapper.SysGeneratorDao;
import com.personal.generator.service.SysGeneratorService;
import com.personal.generator.utils.GeneratorUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author liuyuzhu
 * @description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/1/21 0:42
 */
@Service("sysGeneratorService")
public class SysGeneratorServiceImpl implements SysGeneratorService{
    @Autowired
    private SysGeneratorDao sysGeneratorDao;

    @Override
    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        return sysGeneratorDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysGeneratorDao.queryTotal(map);
    }

    @Override
    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorDao.queryTable(tableName);
    }

    @Override
    public List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorDao.queryColumns(tableName);
    }

    @Override
    public byte[] generatorCode(String[] tableNames,int genType) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for(String tableName : tableNames){
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //取得根目录路径
            String rootPath=getClass().getResource("/").getFile().toString();
            //生成代码
            GeneratorUtils.generatorCode(table, columns, zip, genType);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

}
