package com.personal.role.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;



/**
 * 系统角色表
 * 
 * @author liuyuzhu
 * @email liuyuzhu.1314@gmail.com
 * @date 2018-02-08 16:36:33
 */
@Setter
@Getter
@ToString
public class TSysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//角色名称
	private String roleName;
	//角色说明
	private String roleDesc;
	//状态 1正常 0 删除
	private Integer status;
	//创建时间
	private Timestamp createTime;
	//更新时间
	private Timestamp updateTime;
	//
	private Long createUser;
	//
	private Long updateUser;


}
