package com.personal.menu.service.impl;

import com.personal.common.base.BaseMapper;
import com.personal.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.common.util.LogUtil;

import com.personal.menu.mapper.TSysMenuMapper;
import com.personal.menu.entity.TSysMenuEntity;
import com.personal.menu.service.TSysMenuService;



@Service("tSysMenuService")
public class TSysMenuServiceImpl extends BaseServiceImpl<TSysMenuEntity,Long> implements TSysMenuService {

    private final LogUtil logger = LogUtil.getLogger(this.getClass());

    @Autowired
    private TSysMenuMapper tSysMenuMapper;

    @Override
    public BaseMapper<TSysMenuEntity,Long> getMapper(){
        return tSysMenuMapper;
    }

}
