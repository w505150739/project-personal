/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50638
Source Host           : localhost:3306
Source Database       : personal

Target Server Type    : MYSQL
Target Server Version : 50638
File Encoding         : 65001

Date: 2018-02-09 16:41:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_p_user
-- ----------------------------
DROP TABLE IF EXISTS `t_p_user`;
CREATE TABLE `t_p_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `realname` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `sex` tinyint(2) DEFAULT NULL COMMENT '性别 1男 2女',
  `birthday` timestamp NULL DEFAULT NULL COMMENT '出生日期',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `user_name` varchar(100) DEFAULT NULL COMMENT '账号',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态 1正常 0 删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  `salt` varchar(50) DEFAULT NULL COMMENT '密码盐值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_p_user
-- ----------------------------
INSERT INTO `t_p_user` VALUES ('1', '白领', '百灵鸟', '1', '2018-01-27 23:08:46', '18811111111', '147852369@qq.com', 'de288386eef694c38fc79b4f3b314918', 'admin', '1', '2018-01-27 23:09:17', '2018-02-06 16:24:04', null, null, 'VALak5ckgVsCbTWs25hmdgjv');

-- ----------------------------
-- Table structure for t_sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dept`;
CREATE TABLE `t_sys_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父部门',
  `dept_tel` varchar(22) DEFAULT NULL COMMENT '部门电话',
  `dept_fax` varchar(22) DEFAULT NULL COMMENT '部门传真',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `leader` bigint(20) DEFAULT NULL COMMENT '部门主管（关联t_p_user的id）',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态 1正常 0 删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统部门表';

-- ----------------------------
-- Records of t_sys_dept
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(255) DEFAULT '' COMMENT '菜单名称',
  `p_menu_id` varchar(255) DEFAULT '' COMMENT '父级菜单id',
  `menu_icon` varchar(255) DEFAULT '' COMMENT '菜单图标',
  `menu_level` int(10) DEFAULT NULL COMMENT '菜单层级',
  `menu_sort` int(10) DEFAULT NULL COMMENT '菜单排序',
  `menu_url` varchar(255) DEFAULT NULL COMMENT '菜单跳转url',
  `menu_desc` varchar(255) DEFAULT NULL COMMENT '功能说明',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态 1正常 0 删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='系统菜单表';

-- ----------------------------
-- Records of t_sys_menu
-- ----------------------------
INSERT INTO `t_sys_menu` VALUES ('1', '测试菜单1级', '0', '21', '22', '22', '1111', '11', '1', '2018-02-09 01:22:00', '2018-02-09 01:27:59', null, null);
INSERT INTO `t_sys_menu` VALUES ('2', '测试菜单2级', '1', '11', '111', '11', '1111111', '11', '1', '2018-02-09 01:28:17', '2018-02-09 01:31:15', null, null);
INSERT INTO `t_sys_menu` VALUES ('3', '测试菜单1级', '0', '21', '22', '22', '1111', '11', '1', '2018-02-09 01:22:00', '2018-02-09 01:45:32', null, null);
INSERT INTO `t_sys_menu` VALUES ('4', '测试菜单2级', '3', '11', '111', '11', '1111111', '11', '1', '2018-02-09 01:28:17', '2018-02-09 01:31:15', null, null);

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT '' COMMENT '角色名称',
  `role_desc` varchar(255) DEFAULT '' COMMENT '角色说明',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态 1正常 0 删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统角色表';

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_menu`;
CREATE TABLE `t_sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单id',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态 1正常 0 删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_user` bigint(20) DEFAULT NULL,
  `update_user` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单关联表';

-- ----------------------------
-- Records of t_sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `role_id` varchar(64) NOT NULL DEFAULT '' COMMENT '角色id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户权限关系表';

-- ----------------------------
-- Records of t_sys_user_role
-- ----------------------------
