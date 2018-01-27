package com.personal.system.login.service.impl;

import com.personal.common.base.BaseMapper;
import com.personal.common.service.impl.BaseServiceImpl;
import com.personal.system.login.dao.UserMapper;
import com.personal.system.login.entity.UserEntity;
import com.personal.system.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuyuzhu
 * @description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/1/27 22:50
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserEntity,Long> implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public BaseMapper<UserEntity, Long> getMapper() {
        return userMapper;
    }
}
