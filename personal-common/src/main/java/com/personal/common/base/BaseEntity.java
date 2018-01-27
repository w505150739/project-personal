package com.personal.common.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author liuyuzhu
 * @description: 实体基类
 * @date 2018/1/20 0:56
 */
@Setter
@Getter
@ToString
public class BaseEntity implements Serializable {

    private Long createUser;

    private Long updateUser;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Integer status;
}
