package com.personal.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author liuyuzhu
 * @description: 基础service
 * @date 2018/1/20 1:28
 */
public interface BaseService<T,PK extends Serializable> {

    int save(T t);

    int saveByMap(Map<String, Object> map);

    int update(T t);

    int updateByMap(Map<String, Object> map);

    int deleteById(PK id);

    int delete(Map<String, Object> map);

    T get(PK id);

    List<T> queryList(Map<String, Object> map);

    List<Map<String,Object>> queryListMap(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    T findByMap(Map<String, Object> map);
}
