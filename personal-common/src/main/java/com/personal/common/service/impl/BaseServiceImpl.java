package com.personal.common.service.impl;

import com.personal.common.base.BaseMapper;
import com.personal.common.service.BaseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author liuyuzhu
 * @description: 基础service 实现类
 * @date 2018/1/20 1:29
 */
public abstract class BaseServiceImpl<T,PK extends Serializable> implements BaseService<T,PK>{

    public abstract BaseMapper<T, PK> getMapper();

    @Override
    public int save(T t) {
        return this.getMapper().save(t);
    }

    @Override
    public int saveByMap(Map<String, Object> map) {
        return this.getMapper().saveByMap(map);
    }

    @Override
    public int update(T t) {
        return this.getMapper().update(t);
    }

    @Override
    public int updateByMap(Map<String, Object> map) {
        return this.getMapper().updateByMap(map);
    }

    @Override
    public int deleteById(PK id) {
        return this.getMapper().deleteById(id);
    }

    @Override
    public int delete(Map<String, Object> map) {
        return this.getMapper().delete(map);
    }

    @Override
    public T get(PK id) {
        return this.getMapper().get(id);
    }

    @Override
    public List<T> queryList(Map<String, Object> map) {
        return this.getMapper().queryList(map);
    }

    @Override
    public List<Map<String, Object>> queryListMap(Map<String, Object> map) {
        return this.getMapper().queryListMap(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return this.getMapper().queryTotal(map);
    }

    @Override
    public T findByMap(Map<String, Object> map) {
        return this.getMapper().findByMap(map);
    }
}
