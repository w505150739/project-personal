package com.personal.system.user.entity;

import com.personal.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author liuyuzhu
 * @description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/1/27 22:02
 */
@Setter
@Getter
@ToString
public class UserEntity extends BaseEntity{

    private Long id;

    private String userName;

    private String password;

    private String realName;

    private String nickName;

    private Integer sex;

    private Timestamp brithDay;

    private String mobile;

    private String email;

    private String salt;

}
