/*
 Navicat Premium Data Transfer

 Source Server         : MySQL本地
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : user

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 20/06/2021 01:02:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `sex` tinyint(1) UNSIGNED NOT NULL COMMENT '性别',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
  PRIMARY KEY (`id`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'name1', 1, 'phone1', 'address1');
INSERT INTO `user` VALUES (2, 'name2', 2, 'phone2', 'address2');
INSERT INTO `user` VALUES (3, 'name3', 3, 'phone3', 'address3');
INSERT INTO `user` VALUES (4, 'name4', 4, 'phone4', 'address4');
INSERT INTO `user` VALUES (5, 'name5', 5, 'phone5', 'address5');
INSERT INTO `user` VALUES (6, 'name6', 6, 'phone6', 'address6');
INSERT INTO `user` VALUES (7, 'name7', 7, 'phone7', 'address7');
INSERT INTO `user` VALUES (8, 'name8', 8, 'phone8', 'address8');
INSERT INTO `user` VALUES (9, 'name9', 9, 'phone9', 'address9');
INSERT INTO `user` VALUES (10, 'name10', 10, 'phone10', 'address10');
INSERT INTO `user` VALUES (11, 'name11', 11, 'phone11', 'address11');
INSERT INTO `user` VALUES (12, 'name12', 12, 'phone12', 'address12');
INSERT INTO `user` VALUES (13, 'name13', 13, 'phone13', 'address13');

SET FOREIGN_KEY_CHECKS = 1;
