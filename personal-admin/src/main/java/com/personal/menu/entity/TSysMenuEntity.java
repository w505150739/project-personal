package com.personal.menu.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


/**
 * 系统菜单表
 * 
 * @author liuyuzhu
 * @email liuyuzhu.1314@gmail.com
 * @date 2018-02-08 16:36:33
 */
@Setter
@Getter
@ToString
public class TSysMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//菜单名称
	private String menuName;

	//菜单title
	private String menuTitle;

	//父级菜单id
	private String pMenuId;
	//菜单图标
	private String menuIcon;
	//菜单层级
	private Integer menuLevel;
	//菜单排序
	private Integer menuSort;
	//菜单跳转url
	private String menuUrl;
	//功能说明
	private String menuDesc;
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
