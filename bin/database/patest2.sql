/*
Navicat MySQL Data Transfer

Source Server         : 222.196.35.228
Source Server Version : 50560
Source Host           : 222.196.35.228:3306
Source Database       : patest2

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2018-04-27 13:30:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for academy_info
-- ----------------------------
DROP TABLE IF EXISTS `academy_info`;
CREATE TABLE `academy_info` (
  `aca_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `academy_name` varchar(255) NOT NULL COMMENT '学院名字',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`aca_id`),
  UNIQUE KEY `academy_name` (`academy_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1505 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for course_info
-- ----------------------------
DROP TABLE IF EXISTS `course_info`;
CREATE TABLE `course_info` (
  `cou_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `course_name` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`cou_id`),
  UNIQUE KEY `course_name` (`course_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ep_apply_info
-- ----------------------------
DROP TABLE IF EXISTS `ep_apply_info`;
CREATE TABLE `ep_apply_info` (
  `apply_id` int(11) NOT NULL AUTO_INCREMENT,
  `exam_name` varchar(255) NOT NULL,
  `people_number` int(11) NOT NULL,
  `is_problem` int(11) NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `ep_user_id` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0：申请中，1申请通过，代付款，-1未通过，2已付款，-2未付款',
  `exam_id` int(11) DEFAULT NULL COMMENT 'status为2，考试id才有效',
  `reason` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`apply_id`),
  KEY `ep_user_id` (`ep_user_id`) USING BTREE,
  KEY `exam_id` (`exam_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ep_notice
-- ----------------------------
DROP TABLE IF EXISTS `ep_notice`;
CREATE TABLE `ep_notice` (
  `epno_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ep系统公告',
  `title` varchar(255) NOT NULL COMMENT '公告标题',
  `content` longtext NOT NULL COMMENT '公告内容',
  `create_time` datetime NOT NULL COMMENT '添加时间',
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`epno_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ep_order_info
-- ----------------------------
DROP TABLE IF EXISTS `ep_order_info`;
CREATE TABLE `ep_order_info` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `apply_id` int(11) NOT NULL,
  `order_num` varchar(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `money` double(16,2) NOT NULL,
  `ep_user_id` int(11) NOT NULL,
  `is_pay` int(1) NOT NULL DEFAULT '0' COMMENT '0未支付，1已支付',
  `pay_money` double(16,2) NOT NULL DEFAULT '0.00',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `apply_id` (`apply_id`) USING BTREE,
  KEY `ep_user_id` (`ep_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ep_user_info
-- ----------------------------
DROP TABLE IF EXISTS `ep_user_info`;
CREATE TABLE `ep_user_info` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(225) NOT NULL,
  `password` varchar(225) NOT NULL,
  `email` varchar(225) NOT NULL,
  `phone` varchar(225) NOT NULL,
  `unit` varchar(255) NOT NULL COMMENT '联系地址',
  `real_name` varchar(225) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for exam_info
-- ----------------------------
DROP TABLE IF EXISTS `exam_info`;
CREATE TABLE `exam_info` (
  `exam_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '考试id',
  `title` varchar(255) NOT NULL COMMENT '考试标题',
  `description` longtext NOT NULL COMMENT '考试描述',
  `start_time` datetime NOT NULL COMMENT '考试开始时间',
  `end_time` datetime NOT NULL COMMENT '考试结束时间',
  `allow_ip` varchar(255) DEFAULT NULL COMMENT '允许登陆的IP地址段',
  `allow_judger_ids` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`exam_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for exam_notice
-- ----------------------------
DROP TABLE IF EXISTS `exam_notice`;
CREATE TABLE `exam_notice` (
  `exa_not_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `exam_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` longtext NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`exa_not_id`),
  KEY `Ann_announcement_6b01437f` (`exam_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for exam_paper
-- ----------------------------
DROP TABLE IF EXISTS `exam_paper`;
CREATE TABLE `exam_paper` (
  `exa_pap_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '考试试卷id',
  `exam_id` int(11) NOT NULL COMMENT '考卷属于考试的id',
  `user_id` int(11) NOT NULL COMMENT '使用这考卷的用户id',
  `classroom` varchar(255) DEFAULT NULL,
  `score` double(16,2) NOT NULL DEFAULT '0.00' COMMENT '考卷总分',
  `aced_count` int(11) NOT NULL DEFAULT '0' COMMENT '考卷的ac数量',
  `used_time` int(11) NOT NULL DEFAULT '0' COMMENT '完成考卷的耗时',
  `is_marked` int(1) NOT NULL DEFAULT '0' COMMENT '考卷是否被批阅',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`exa_pap_id`),
  UNIQUE KEY `user_id` (`user_id`,`exam_id`) USING BTREE,
  KEY `Exam_paper_6b01437f` (`exam_id`) USING BTREE,
  KEY `Exam_paper_fbfc09f1` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3026 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for exam_param
-- ----------------------------
DROP TABLE IF EXISTS `exam_param`;
CREATE TABLE `exam_param` (
  `exa_par_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '考试模板id',
  `exam__id` int(11) NOT NULL COMMENT '模板对应的考试',
  `name` varchar(255) DEFAULT NULL,
  `know_id` int(11) DEFAULT NULL COMMENT '模板的考点',
  `problem_id` int(11) DEFAULT NULL,
  `level_id` int(11) DEFAULT NULL COMMENT '模板的难度',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '模板所占的分数',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`exa_par_id`),
  KEY `Exam_problemtemplate_6b01437f` (`exam__id`) USING BTREE,
  KEY `Exam_problemtemplate_3747b463` (`know_id`) USING BTREE,
  KEY `problem_id` (`problem_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for exam_problem
-- ----------------------------
DROP TABLE IF EXISTS `exam_problem`;
CREATE TABLE `exam_problem` (
  `exa_pro_id` int(11) NOT NULL AUTO_INCREMENT,
  `problem_id` int(11) NOT NULL,
  `exam_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`exa_pro_id`),
  UNIQUE KEY `problem_id` (`problem_id`,`exam_id`) USING BTREE,
  KEY `problem_id_2` (`problem_id`) USING BTREE,
  KEY `exam_id` (`exam_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for judger_info
-- ----------------------------
DROP TABLE IF EXISTS `judger_info`;
CREATE TABLE `judger_info` (
  `jud_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '题目选用编译器id',
  `name` varchar(16) NOT NULL COMMENT '编译器名字',
  `repr` varchar(16) NOT NULL COMMENT '编译器名字',
  `file_name` varchar(255) NOT NULL COMMENT '主食符',
  `exec_file_name` varchar(255) NOT NULL,
  `suffix` varchar(255) NOT NULL COMMENT '后缀',
  `enabled` int(11) NOT NULL COMMENT '编译器是否开启',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`jud_id`),
  UNIQUE KEY `Problem_judger_52094d6e` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for knowledge_info
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_info`;
CREATE TABLE `knowledge_info` (
  `know_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '考点id',
  `know_name` varchar(32) NOT NULL COMMENT '考点名字',
  `course_id` int(11) NOT NULL COMMENT '上一级节点',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`know_id`),
  UNIQUE KEY `Problem_tag_52094d6e` (`know_name`,`course_id`) USING BTREE,
  KEY `course_id` (`course_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for level_type
-- ----------------------------
DROP TABLE IF EXISTS `level_type`;
CREATE TABLE `level_type` (
  `lev_typ_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`lev_typ_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for major_info
-- ----------------------------
DROP TABLE IF EXISTS `major_info`;
CREATE TABLE `major_info` (
  `maj_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `major_name` varchar(255) NOT NULL,
  `academy_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`maj_id`),
  UNIQUE KEY `major_name` (`major_name`,`academy_id`) USING BTREE,
  KEY `academy_id` (`academy_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for paper_problem
-- ----------------------------
DROP TABLE IF EXISTS `paper_problem`;
CREATE TABLE `paper_problem` (
  `pap_pro_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '考卷题目id',
  `exam_id` int(11) NOT NULL COMMENT '考试id',
  `exam_paper_id` int(11) NOT NULL COMMENT '考卷id',
  `exam_param_id` int(11) NOT NULL COMMENT '考卷题目模板id',
  `problem_id` int(11) NOT NULL COMMENT '考卷题目对应题库题目的id',
  `order` int(11) NOT NULL COMMENT '题目的顺序',
  `last_submit_time` datetime DEFAULT NULL COMMENT '题目最后提交时间',
  `submit_id` int(11) DEFAULT NULL,
  `is_aced` int(1) NOT NULL DEFAULT '0' COMMENT '这到题目是否ac',
  `used_time` int(11) NOT NULL DEFAULT '0' COMMENT '这道题的耗时',
  `submit_count` int(11) NOT NULL DEFAULT '0' COMMENT '这道题的提交次数',
  `score` double(16,2) NOT NULL DEFAULT '0.00' COMMENT '这道考题的分数',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`pap_pro_id`),
  KEY `Exam_paperproblem_c2d3d2bb` (`exam_paper_id`) USING BTREE,
  KEY `exam_id` (`exam_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11213 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for problem_info
-- ----------------------------
DROP TABLE IF EXISTS `problem_info`;
CREATE TABLE `problem_info` (
  `prob_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '题目id',
  `title` varchar(32) NOT NULL COMMENT '题目标题',
  `description` longtext NOT NULL COMMENT '题目描述',
  `level_id` int(11) NOT NULL COMMENT '题目的难度',
  `memory_limit` int(11) NOT NULL COMMENT '题目限制内存',
  `time_limit` int(11) NOT NULL COMMENT '题目限制时间',
  `code_limit` int(11) NOT NULL DEFAULT '5000' COMMENT '代码长度限制，单位B',
  `input_tip` text NOT NULL COMMENT '题目输入提示',
  `output_tip` text NOT NULL COMMENT '题目输出提示',
  `input_sample` text NOT NULL COMMENT '题目样例输入',
  `output_sample` text NOT NULL COMMENT '题目样例输出',
  `standard_source` longtext NOT NULL COMMENT '标程',
  `know_id` int(11) DEFAULT NULL,
  `testdata_num` int(11) NOT NULL DEFAULT '0' COMMENT '测试数据数量',
  `score_ratio` varchar(255) NOT NULL COMMENT '分数比例',
  `judge_model` int(11) NOT NULL DEFAULT '0' COMMENT '判题程序：0:standard，1：spical Judge',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `submit_num` int(11) NOT NULL DEFAULT '0' COMMENT '题目提交的次数',
  `accepted_num` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL COMMENT '题目加入题库时间',
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`prob_id`),
  KEY `know_id` (`know_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=819 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for result_info
-- ----------------------------
DROP TABLE IF EXISTS `result_info`;
CREATE TABLE `result_info` (
  `resu_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `short_name` varchar(10) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`resu_id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for site_info
-- ----------------------------
DROP TABLE IF EXISTS `site_info`;
CREATE TABLE `site_info` (
  `site_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '1',
  `display` varchar(255) NOT NULL,
  `value` longtext NOT NULL,
  `extra` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`site_id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for submit_info
-- ----------------------------
DROP TABLE IF EXISTS `submit_info`;
CREATE TABLE `submit_info` (
  `subm_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '提交id',
  `user_id` int(11) NOT NULL COMMENT '提交用户id',
  `exam_id` int(11) DEFAULT NULL COMMENT '提交的考试id',
  `exam_paper_id` int(11) DEFAULT NULL COMMENT '提交的考卷id',
  `exam_param_id` int(11) DEFAULT NULL,
  `paper_problem_id` int(11) DEFAULT NULL COMMENT '提交的考题id（试卷）',
  `problem_id` int(11) NOT NULL COMMENT '提交的问题的id(题库）',
  `judger_id` int(11) NOT NULL COMMENT '选择的编译器的类型',
  `ip` varchar(255) NOT NULL COMMENT '提交的ip',
  `source` longtext NOT NULL COMMENT '源码',
  `is_teacher_test` int(11) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `used_time` int(11) DEFAULT NULL,
  `used_memory` int(11) DEFAULT NULL,
  `err_msg` text,
  `create_time` datetime NOT NULL COMMENT '提交的时间',
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`subm_id`),
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `exam_id` (`exam_id`) USING BTREE,
  KEY `problem_id` (`problem_id`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `judger_id` (`judger_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=204897 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for submit_result
-- ----------------------------
DROP TABLE IF EXISTS `submit_result`;
CREATE TABLE `submit_result` (
  `sub_res_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '提交结果id',
  `submit_id` int(11) NOT NULL COMMENT '提交id',
  `test_id` int(11) NOT NULL COMMENT '提交的题目的测试编号',
  `status` int(11) NOT NULL COMMENT '提交结果显示的状态',
  `used_time` int(11) NOT NULL COMMENT '提交结果显示的使用的时间',
  `used_memory` int(11) NOT NULL COMMENT '提交结果显示的使用的内存',
  `err_msg` text COMMENT '显示错误的信息',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`sub_res_id`),
  KEY `submit_id` (`submit_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=649397 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for submit_similarity
-- ----------------------------
DROP TABLE IF EXISTS `submit_similarity`;
CREATE TABLE `submit_similarity` (
  `sub_sim_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '相识比较id',
  `exam_id` int(11) NOT NULL,
  `submit_id1` int(11) NOT NULL COMMENT '提交1',
  `submit_id2` int(11) NOT NULL COMMENT '提交2',
  `similarity` double NOT NULL COMMENT '提交相识度（同一场考试的同一道通过的两个互不相同的提交）',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`sub_sim_id`),
  KEY `Exam_similarity_2b77db57` (`submit_id1`) USING BTREE,
  KEY `Exam_similarity_e9f21117` (`submit_id2`) USING BTREE,
  KEY `exam_id` (`exam_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71941 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_profile_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL COMMENT '例：2013+这年的第几场考试（001）+这场考试的学生的第几个（001）',
  `password` varchar(255) NOT NULL,
  `is_teacher` int(1) NOT NULL DEFAULT '0',
  `is_admin` int(1) NOT NULL DEFAULT '0',
  `is_lock` int(11) NOT NULL DEFAULT '0',
  `is_active` int(1) NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `exam_id` int(11) DEFAULT NULL COMMENT '考试ID',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  KEY `user_profile_id` (`user_profile_id`) USING BTREE,
  KEY `is_teacher` (`is_teacher`,`exam_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3028 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log` (
  `use_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `login_ip` char(39) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`use_log_id`),
  KEY `User_loginlog_fbfc09f1` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6248 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `use_pro_id` int(11) NOT NULL AUTO_INCREMENT,
  `real_name` varchar(255) NOT NULL,
  `is_student` int(11) NOT NULL COMMENT '1为是学生，0为不是学生',
  `class_name` varchar(255) DEFAULT NULL,
  `student_number` varchar(255) NOT NULL COMMENT '学号',
  `entrance_year` int(11) DEFAULT NULL,
  `major_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`use_pro_id`),
  UNIQUE KEY `student_number` (`student_number`) USING BTREE,
  KEY `real_name` (`real_name`) USING BTREE,
  KEY `major_id` (`major_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7725 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `use_rol_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`use_rol_id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for visit_path
-- ----------------------------
DROP TABLE IF EXISTS `visit_path`;
CREATE TABLE `visit_path` (
  `vis_pat_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `description` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `role_ids` varchar(255) CHARACTER SET utf8 NOT NULL,
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  `modify_user_id` int(11) NOT NULL,
  PRIMARY KEY (`vis_pat_id`),
  UNIQUE KEY `url` (`url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=latin1;
