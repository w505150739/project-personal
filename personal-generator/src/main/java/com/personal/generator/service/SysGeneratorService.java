package com.personal.generator.service;

import java.util.List;
import java.util.Map;

/**
 * @author liuyuzhu
 * @description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/1/21 0:40
 */
public interface SysGeneratorService {

    List<Map<String, Object>> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);

    /**
     * 生成代码
     * @param tableNames 表名集
     * @param genType 生成方式，详见Constant枚举类 0本地 1 web形式
     * @return
     */
    byte[] generatorCode(String[] tableNames,int genType);
}
