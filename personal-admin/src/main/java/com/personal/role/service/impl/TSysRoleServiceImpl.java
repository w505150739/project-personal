package com.personal.role.service.impl;

import com.personal.common.base.BaseMapper;
import com.personal.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.common.util.LogUtil;

import com.personal.role.mapper.TSysRoleMapper;
import com.personal.role.entity.TSysRoleEntity;
import com.personal.role.service.TSysRoleService;



@Service("tSysRoleService")
public class TSysRoleServiceImpl extends BaseServiceImpl<TSysRoleEntity,Long> implements TSysRoleService {

    private final LogUtil logger = LogUtil.getLogger(this.getClass());

    @Autowired
    private TSysRoleMapper tSysRoleMapper;

    @Override
    public BaseMapper<TSysRoleEntity,Long> getMapper(){
        return tSysRoleMapper;
    }

}
