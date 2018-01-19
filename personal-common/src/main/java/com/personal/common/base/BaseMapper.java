package com.personal.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author liuyuzhu
 * @description: 基础Mapper
 * @date 2018/1/20 1:00
 */
public interface BaseMapper<T,PK extends Serializable> {

    int save(T t);

    int save(Map<String, Object> map);

    int update(T t);

    int update(Map<String, Object> map);

    int delete(PK id);

    int delete(Map<String, Object> map);

    T get(PK id);

    List<T> queryList(Map<String, Object> map);

    List<Map<String,Object>> queryListMap(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

}
