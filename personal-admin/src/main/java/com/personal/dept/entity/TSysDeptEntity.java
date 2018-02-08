package com.personal.dept.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;



/**
 * 系统部门表
 * 
 * @author liuyuzhu
 * @email liuyuzhu.1314@gmail.com
 * @date 2018-02-08 15:11:38
 */
@Setter
@Getter
@ToString
public class TSysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//部门名称
	private String deptName;
	//父部门
	private Long parentId;
	//部门电话
	private String deptTel;
	//部门传真
	private String deptFax;
	//备注
	private String remark;
	//部门主管（关联t_p_user的id）
	private Long leader;
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
