/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50640
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50640
File Encoding         : 65001

Date: 2018-05-02 17:27:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `USER_ID` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户名称',
  `PASSWORD` varchar(128) NOT NULL COMMENT '密码',
  `EMAIL` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '手机号',
  `STATUS` char(1) NOT NULL DEFAULT '1' COMMENT '状态',
  `SEX` char(1) DEFAULT '1' COMMENT '性别',
  `AVATAR` varchar(100) DEFAULT NULL COMMENT '头像',
  `REMARK` varchar(255) DEFAULT NULL COMMENT '备注',
  `CREATE_USER_ID` int(10) NOT NULL DEFAULT '-1' COMMENT '创建人ID',
  `UPDATE_USER_ID` int(10) DEFAULT NULL COMMENT '修改人ID',
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATE_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `LAST_LOGIN_TIME` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次登录时间',
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
