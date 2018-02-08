package com.personal.dept.service.impl;

import com.personal.common.base.BaseMapper;
import com.personal.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.common.util.LogUtil;

import com.personal.dept.mapper.TSysDeptMapper;
import com.personal.dept.entity.TSysDeptEntity;
import com.personal.dept.service.TSysDeptService;



@Service("tSysDeptService")
public class TSysDeptServiceImpl extends BaseServiceImpl<TSysDeptEntity,Long> implements TSysDeptService {

    private final LogUtil logger = LogUtil.getLogger(this.getClass());

    @Autowired
    private TSysDeptMapper tSysDeptMapper;

    @Override
    public BaseMapper<TSysDeptEntity,Long> getMapper(){
        return tSysDeptMapper;
    }

}
