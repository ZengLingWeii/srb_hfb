/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.28 : Database - db2023_srb_core
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db2023_srb_core` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `db2023_srb_core`;

/*Table structure for table `borrow_info` */

DROP TABLE IF EXISTS `borrow_info`;

CREATE TABLE `borrow_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '借款用户id',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '借款金额',
  `period` int DEFAULT NULL COMMENT '借款期限',
  `borrow_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `return_method` tinyint DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
  `money_use` tinyint DEFAULT NULL COMMENT '资金用途',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0：未提交，1：审核中， 2：审核通过， -1：审核不通过）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='借款信息表';

/*Data for the table `borrow_info` */

insert  into `borrow_info`(`id`,`user_id`,`amount`,`period`,`borrow_year_rate`,`return_method`,`money_use`,`status`,`create_time`,`update_time`,`is_deleted`) values (6,2,'100000.00',6,'0.10',1,6,2,'2023-05-09 19:06:31','2023-05-09 19:06:31',0);

/*Table structure for table `borrower` */

DROP TABLE IF EXISTS `borrower`;

CREATE TABLE `borrower` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户id',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '身份证号',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机',
  `sex` tinyint DEFAULT NULL COMMENT '性别（1：男 0：女）',
  `age` tinyint DEFAULT NULL COMMENT '年龄',
  `education` tinyint DEFAULT NULL COMMENT '学历',
  `is_marry` tinyint(1) DEFAULT NULL COMMENT '是否结婚（1：是 0：否）',
  `industry` tinyint DEFAULT NULL COMMENT '行业',
  `income` tinyint DEFAULT NULL COMMENT '月收入',
  `return_source` tinyint DEFAULT NULL COMMENT '还款来源',
  `contacts_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系人名称',
  `contacts_mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '联系人手机',
  `contacts_relation` tinyint DEFAULT NULL COMMENT '联系人关系',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0：未认证，1：认证中， 2：认证通过， -1：认证失败）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='借款人';

/*Data for the table `borrower` */

insert  into `borrower`(`id`,`user_id`,`name`,`id_card`,`mobile`,`sex`,`age`,`education`,`is_marry`,`industry`,`income`,`return_source`,`contacts_name`,`contacts_mobile`,`contacts_relation`,`status`,`create_time`,`update_time`,`is_deleted`) values (4,2,'曾忆远','421081196801241441','15926555568',1,55,1,1,6,4,1,'曾令炜','13886592587',4,2,'2023-05-09 15:13:46','2023-05-09 15:13:46',0);

/*Table structure for table `borrower_attach` */

DROP TABLE IF EXISTS `borrower_attach`;

CREATE TABLE `borrower_attach` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `borrower_id` bigint NOT NULL DEFAULT '0' COMMENT '借款人id',
  `image_type` varchar(999) DEFAULT NULL,
  `image_url` varchar(999) DEFAULT NULL,
  `image_name` varchar(999) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_borrower_id` (`borrower_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='借款人上传资源表';

/*Data for the table `borrower_attach` */

insert  into `borrower_attach`(`id`,`borrower_id`,`image_type`,`image_url`,`image_name`,`create_time`,`update_time`,`is_deleted`) values (13,4,'idCard1','https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/idCard1/2023/05/09/87d69464-4649-4769-ae36-10a24dfde1a2.png','idCard1.png','2023-05-09 15:13:46','2023-05-23 17:16:04',0),(14,4,'idCard2','https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/idCard2/2023/05/09/300cc0d4-8047-4f29-af11-bee9187fa335.png','idCard2.png','2023-05-09 15:13:46','2023-05-23 17:16:10',0),(15,4,'house','https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/house/2023/05/09/2130334d-3e10-4e34-94cc-79722c11cac3.png','house.png','2023-05-09 15:13:46','2023-05-23 17:16:17',0),(16,4,'car','https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/car/2023/05/09/3d8694b8-bab2-42a7-847c-c059e864e25d.png','car.png','2023-05-09 15:13:46','2023-05-23 17:16:24',0);

/*Table structure for table `dict` */

DROP TABLE IF EXISTS `dict`;

CREATE TABLE `dict` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '上级id',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `value` int DEFAULT NULL COMMENT '值',
  `dict_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '编码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_parent_id_value` (`parent_id`,`value`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=83157 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='数据字典';

/*Data for the table `dict` */

insert  into `dict`(`id`,`parent_id`,`name`,`value`,`dict_code`,`create_time`,`update_time`,`is_deleted`) values (1,0,'全部分类',NULL,'ROOT','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(20000,1,'行业',NULL,'industry','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(20001,20000,'IT',1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(20002,20000,'医生',2,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(20003,20000,'教师',3,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(20004,20000,'导游',4,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(20005,20000,'律师',5,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(20006,20000,'其他',6,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(30000,1,'学历',NULL,'education','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(30001,30000,'高中',1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(30002,30000,'大专',2,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(30003,30000,'本科',3,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(30004,30000,'研究生',4,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(30005,30000,'其他',5,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(40000,1,'收入',NULL,'income','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(40001,40000,'0-3000',1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(40002,40000,'3000-5000',2,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(40003,40000,'5000-10000',3,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(40004,40000,'10000以上',4,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(50000,1,'收入来源',NULL,'returnSource','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(50001,50000,'工资',1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(50002,50000,'股票',2,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(50003,50000,'兼职',3,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(60000,1,'关系',NULL,'relation','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(60001,60000,'夫妻',1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(60002,60000,'兄妹',2,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(60003,60000,'父母',3,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(60004,60000,'其他',4,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(70000,1,'还款方式',NULL,'returnMethod','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(70001,70000,'等额本息',1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(70002,70000,'等额本金',2,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(70003,70000,'每月还息一次还本',3,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(70004,70000,'一次还本还息',4,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(80000,1,'资金用途',NULL,'moneyUse','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(80001,80000,'旅游',1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(80002,80000,'买房',2,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(80003,80000,'装修',3,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(80004,80000,'医疗',4,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(80005,80000,'美容',5,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(80006,80000,'其他',6,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(81000,1,'借款状态',NULL,'borrowStatus','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(81001,81000,'待审核',0,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(81002,81000,'审批通过',1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(81003,81000,'还款中',2,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(81004,81000,'结束',3,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(81005,81000,'审批不通过',-1,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(82000,1,'学校性质',NULL,'SchoolStatus','2023-04-02 19:30:32','2023-04-02 19:30:32',0),(82001,82000,'211/985',NULL,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(82002,82000,'一本',NULL,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(82003,82000,'二本',NULL,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(82004,82000,'三本',NULL,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(82005,82000,'高职高专',NULL,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(82006,82000,'中职中专',NULL,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0),(82007,82000,'高中及以下',NULL,NULL,'2023-04-02 19:30:32','2023-04-02 19:30:32',0);

/*Table structure for table `integral_grade` */

DROP TABLE IF EXISTS `integral_grade`;

CREATE TABLE `integral_grade` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `integral_start` int DEFAULT NULL COMMENT '积分区间开始',
  `integral_end` int DEFAULT NULL COMMENT '积分区间结束',
  `borrow_amount` decimal(10,2) DEFAULT NULL COMMENT '借款额度',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='积分等级表';

/*Data for the table `integral_grade` */

insert  into `integral_grade`(`id`,`integral_start`,`integral_end`,`borrow_amount`,`create_time`,`update_time`,`is_deleted`) values (1,10,50,'10000.00','2020-12-08 17:02:29','2023-03-28 19:15:35',0),(2,51,100,'30000.00','2020-12-08 17:02:42','2023-03-28 19:15:37',0),(3,101,2000,'100000.00','2020-12-08 17:02:57','2023-05-24 16:19:52',1),(4,1,1,'1.00','2023-03-28 20:11:10','2023-03-28 20:11:10',0),(5,2,3,'1.00','2023-05-24 16:17:25','2023-05-24 16:17:25',0),(6,NULL,NULL,'1001.00','2023-05-24 16:19:47','2023-05-24 16:19:47',0);

/*Table structure for table `lend` */

DROP TABLE IF EXISTS `lend`;

CREATE TABLE `lend` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint DEFAULT NULL COMMENT '借款用户id',
  `borrow_info_id` bigint DEFAULT NULL COMMENT '借款信息id',
  `lend_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标的编号',
  `title` varchar(999) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL COMMENT '标的金额',
  `period` int DEFAULT NULL COMMENT '投资期数',
  `lend_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `service_rate` decimal(10,2) DEFAULT NULL COMMENT '平台服务费率',
  `return_method` tinyint DEFAULT NULL COMMENT '还款方式',
  `lowest_amount` decimal(10,2) DEFAULT NULL COMMENT '最低投资金额',
  `invest_amount` decimal(10,2) DEFAULT NULL COMMENT '已投金额',
  `invest_num` int DEFAULT NULL COMMENT '投资人数',
  `publish_date` datetime DEFAULT NULL COMMENT '发布日期',
  `lend_start_date` date DEFAULT NULL COMMENT '开始日期',
  `lend_end_date` date DEFAULT NULL COMMENT '结束日期',
  `lend_info` varchar(999) DEFAULT NULL,
  `expect_amount` decimal(10,2) DEFAULT NULL COMMENT '平台预期收益',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际收益',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
  `check_time` datetime DEFAULT NULL COMMENT '审核时间',
  `check_admin_id` bigint DEFAULT NULL COMMENT '审核用户id',
  `payment_time` datetime DEFAULT NULL COMMENT '放款时间',
  `payment_admin_id` datetime DEFAULT NULL COMMENT '放款人id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_lend_no` (`lend_no`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_borrow_info_id` (`borrow_info_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='标的准备表';

/*Data for the table `lend` */

insert  into `lend`(`id`,`user_id`,`borrow_info_id`,`lend_no`,`title`,`amount`,`period`,`lend_year_rate`,`service_rate`,`return_method`,`lowest_amount`,`invest_amount`,`invest_num`,`publish_date`,`lend_start_date`,`lend_end_date`,`lend_info`,`expect_amount`,`real_amount`,`status`,`check_time`,`check_admin_id`,`payment_time`,`payment_admin_id`,`create_time`,`update_time`,`is_deleted`) values (4,2,6,'LEND20230510152915620','曾忆远的标','100000.00',6,'0.10','0.05',1,'100.00','100000.00',3,'2023-05-10 15:29:16','2023-05-09','2023-11-09','','2500.00','2500.00',2,'2023-05-10 15:29:16',1,'2023-05-16 16:03:40',NULL,'2023-05-10 15:29:16','2023-05-23 17:24:07',0);

/*Table structure for table `lend_item` */

DROP TABLE IF EXISTS `lend_item`;

CREATE TABLE `lend_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `lend_item_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '投资编号',
  `lend_id` bigint NOT NULL DEFAULT '0' COMMENT '标的id',
  `invest_user_id` bigint DEFAULT NULL COMMENT '投资用户id',
  `invest_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '投资人名称',
  `invest_amount` decimal(10,2) DEFAULT NULL COMMENT '投资金额',
  `lend_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `invest_time` datetime DEFAULT NULL COMMENT '投资时间',
  `lend_start_date` date DEFAULT NULL COMMENT '开始日期',
  `lend_end_date` date DEFAULT NULL COMMENT '结束日期',
  `expect_amount` decimal(10,2) DEFAULT NULL COMMENT '预期收益',
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际收益',
  `status` tinyint DEFAULT NULL COMMENT '状态（0：默认 1：已支付 2：已还款）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_lend_item_no` (`lend_item_no`) USING BTREE,
  KEY `idx_lend_id` (`lend_id`) USING BTREE,
  KEY `idx_invest_user_id` (`invest_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='标的出借记录表';

/*Data for the table `lend_item` */

insert  into `lend_item`(`id`,`lend_item_no`,`lend_id`,`invest_user_id`,`invest_name`,`invest_amount`,`lend_year_rate`,`invest_time`,`lend_start_date`,`lend_end_date`,`expect_amount`,`real_amount`,`status`,`create_time`,`update_time`,`is_deleted`) values (7,'INVEST20230512145425399',4,1,'13886592587','100.00','0.10','2023-05-12 14:54:25','2023-05-09','2023-11-09','2.92','0.00',1,'2023-05-12 14:54:25','2023-05-12 14:54:25',0),(8,'INVEST20230516101138398',4,1,'曾令炜','300.00','0.10','2023-05-16 10:11:39','2023-05-09','2023-11-09','8.78','0.00',1,'2023-05-16 10:11:38','2023-05-16 10:11:38',0),(9,'INVEST20230516103657272',4,1,'曾令炜','99600.00','0.10','2023-05-16 10:36:58','2023-05-09','2023-11-09','2925.06','0.00',1,'2023-05-16 10:36:57','2023-05-16 10:36:57',0);

/*Table structure for table `lend_item_return` */

DROP TABLE IF EXISTS `lend_item_return`;

CREATE TABLE `lend_item_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `lend_return_id` bigint DEFAULT NULL COMMENT '标的还款id',
  `lend_item_id` bigint DEFAULT NULL COMMENT '标的项id',
  `lend_id` bigint NOT NULL DEFAULT '0' COMMENT '标的id',
  `invest_user_id` bigint DEFAULT NULL COMMENT '出借用户id',
  `invest_amount` decimal(10,2) DEFAULT NULL COMMENT '出借金额',
  `current_period` int DEFAULT NULL COMMENT '当前的期数',
  `lend_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `return_method` tinyint DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
  `principal` decimal(10,2) DEFAULT NULL COMMENT '本金',
  `interest` decimal(10,2) DEFAULT NULL COMMENT '利息',
  `total` decimal(10,2) DEFAULT NULL COMMENT '本息',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '手续费',
  `return_date` date DEFAULT NULL COMMENT '还款时指定的还款日期',
  `real_return_time` datetime DEFAULT NULL COMMENT '实际发生的还款时间',
  `is_overdue` tinyint(1) DEFAULT NULL COMMENT '是否逾期',
  `overdue_total` decimal(10,2) DEFAULT NULL COMMENT '逾期金额',
  `status` tinyint DEFAULT NULL COMMENT '状态（0-未归还 1-已归还）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_lend_return_id` (`lend_return_id`) USING BTREE,
  KEY `idx_lend_item_id` (`lend_item_id`) USING BTREE,
  KEY `idx_lend_id` (`lend_id`) USING BTREE,
  KEY `idx_invest_user_id` (`invest_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='标的出借回款记录表';

/*Data for the table `lend_item_return` */

insert  into `lend_item_return`(`id`,`lend_return_id`,`lend_item_id`,`lend_id`,`invest_user_id`,`invest_amount`,`current_period`,`lend_year_rate`,`return_method`,`principal`,`interest`,`total`,`fee`,`return_date`,`real_return_time`,`is_overdue`,`overdue_total`,`status`,`create_time`,`update_time`,`is_deleted`) values (16,64,7,4,1,'100.00',1,'0.10',1,'16.33','0.83','17.16','0.00','2023-06-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(17,65,7,4,1,'100.00',2,'0.10',1,'16.47','0.69','17.16','0.00','2023-07-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(18,66,7,4,1,'100.00',3,'0.10',1,'16.60','0.56','17.16','0.00','2023-08-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(19,67,7,4,1,'100.00',4,'0.10',1,'16.74','0.42','17.16','0.00','2023-09-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(20,68,7,4,1,'100.00',5,'0.10',1,'16.88','0.28','17.16','0.00','2023-10-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(21,69,7,4,1,'100.00',6,'0.10',1,'17.00','0.14','17.14','0.00','2023-11-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(22,64,8,4,1,'300.00',1,'0.10',1,'48.98','2.49','51.47','0.00','2023-06-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(23,65,8,4,1,'300.00',2,'0.10',1,'49.38','2.09','51.47','0.00','2023-07-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(24,66,8,4,1,'300.00',3,'0.10',1,'49.79','1.68','51.47','0.00','2023-08-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(25,67,8,4,1,'300.00',4,'0.10',1,'50.21','1.26','51.47','0.00','2023-09-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(26,68,8,4,1,'300.00',5,'0.10',1,'50.63','0.84','51.47','0.00','2023-10-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(27,69,8,4,1,'300.00',6,'0.10',1,'51.02','0.42','51.44','0.00','2023-11-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(28,64,9,4,1,'99600.00',1,'0.10',1,'16257.52','829.99','17087.51','0.00','2023-06-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(29,65,9,4,1,'99600.00',2,'0.10',1,'16392.99','694.52','17087.51','0.00','2023-07-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(30,66,9,4,1,'99600.00',3,'0.10',1,'16529.60','557.91','17087.51','0.00','2023-08-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(31,67,9,4,1,'99600.00',4,'0.10',1,'16667.35','420.16','17087.51','0.00','2023-09-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(32,68,9,4,1,'99600.00',5,'0.10',1,'16806.24','281.27','17087.51','0.00','2023-10-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(33,69,9,4,1,'99600.00',6,'0.10',1,'16946.28','141.21','17087.49','0.00','2023-11-09',NULL,0,NULL,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0);

/*Table structure for table `lend_return` */

DROP TABLE IF EXISTS `lend_return`;

CREATE TABLE `lend_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `lend_id` bigint DEFAULT NULL COMMENT '标的id',
  `borrow_info_id` bigint NOT NULL DEFAULT '0' COMMENT '借款信息id',
  `return_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '还款批次号',
  `user_id` bigint DEFAULT NULL COMMENT '借款人用户id',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '借款金额',
  `base_amount` decimal(10,2) DEFAULT NULL COMMENT '计息本金额',
  `current_period` int DEFAULT NULL COMMENT '当前的期数',
  `lend_year_rate` decimal(10,2) DEFAULT NULL COMMENT '年化利率',
  `return_method` tinyint DEFAULT NULL COMMENT '还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本',
  `principal` decimal(10,2) DEFAULT NULL COMMENT '本金',
  `interest` decimal(10,2) DEFAULT NULL COMMENT '利息',
  `total` decimal(10,2) DEFAULT NULL COMMENT '本息',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '手续费',
  `return_date` date DEFAULT NULL COMMENT '还款时指定的还款日期',
  `real_return_time` datetime DEFAULT NULL COMMENT '实际发生的还款时间',
  `is_overdue` tinyint(1) DEFAULT NULL COMMENT '是否逾期',
  `overdue_total` decimal(10,2) DEFAULT NULL COMMENT '逾期金额',
  `is_last` tinyint(1) DEFAULT NULL COMMENT '是否最后一次还款',
  `status` tinyint DEFAULT NULL COMMENT '状态（0-未归还 1-已归还）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_return_no` (`return_no`) USING BTREE,
  KEY `idx_lend_id` (`lend_id`) USING BTREE,
  KEY `idx_borrow_info_id` (`borrow_info_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='还款记录表';

/*Data for the table `lend_return` */

insert  into `lend_return`(`id`,`lend_id`,`borrow_info_id`,`return_no`,`user_id`,`amount`,`base_amount`,`current_period`,`lend_year_rate`,`return_method`,`principal`,`interest`,`total`,`fee`,`return_date`,`real_return_time`,`is_overdue`,`overdue_total`,`is_last`,`status`,`create_time`,`update_time`,`is_deleted`) values (64,4,6,'RETURN20230516160340929',2,'100000.00','100000.00',1,'0.10',1,'16322.83','833.31','17156.14','0.00','2023-06-09',NULL,0,NULL,0,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(65,4,6,'RETURN20230516160340425',2,'100000.00','100000.00',2,'0.10',1,'16458.84','697.30','17156.14','0.00','2023-07-09',NULL,0,NULL,0,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(66,4,6,'RETURN20230516160340947',2,'100000.00','100000.00',3,'0.10',1,'16595.99','560.15','17156.14','0.00','2023-08-09',NULL,0,NULL,0,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(67,4,6,'RETURN20230516160340789',2,'100000.00','100000.00',4,'0.10',1,'16734.30','421.84','17156.14','0.00','2023-09-09',NULL,0,NULL,0,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(68,4,6,'RETURN20230516160340801',2,'100000.00','100000.00',5,'0.10',1,'16873.75','282.39','17156.14','0.00','2023-10-09',NULL,0,NULL,0,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0),(69,4,6,'RETURN20230516160340952',2,'100000.00','100000.00',6,'0.10',1,'17014.29','141.77','17156.06','0.00','2023-11-09',NULL,0,NULL,1,0,'2023-05-16 16:03:40','2023-05-16 16:03:40',0);

/*Table structure for table `trans_flow` */

DROP TABLE IF EXISTS `trans_flow`;

CREATE TABLE `trans_flow` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户id',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名称',
  `trans_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '交易单号',
  `trans_type` tinyint NOT NULL DEFAULT '0' COMMENT '交易类型（1：充值 2：提现 3：投标 4：投资回款 ...）',
  `trans_type_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '交易类型名称',
  `trans_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `memo` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_trans_no` (`trans_no`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='交易流水表';

/*Data for the table `trans_flow` */

insert  into `trans_flow`(`id`,`user_id`,`user_name`,`trans_no`,`trans_type`,`trans_type_name`,`trans_amount`,`memo`,`create_time`,`update_time`,`is_deleted`) values (57,1,'曾令炜','20230511201837522',1,'充值','10000.00','充值','2023-05-11 20:19:04','2023-05-11 20:19:04',0),(58,1,'曾令炜','20230511201954363',1,'充值','10000.00','充值','2023-05-11 20:20:13','2023-05-11 20:20:13',0),(59,1,'曾令炜','20230511202258289',1,'充值','100.00','充值','2023-05-11 20:23:14','2023-05-11 20:23:14',0),(60,1,'曾令炜','INVEST20230512145425399',2,'投标锁定','100.00','投标项目编号：LEND20230510152915620,项目名称：曾忆远的标的','2023-05-12 14:54:36','2023-05-12 14:54:36',0),(61,1,'曾令炜','20230515205026447',1,'充值','100.00','充值','2023-05-15 20:50:30','2023-05-15 20:50:30',0),(62,1,'曾令炜','20230515205702302',1,'充值','100.00','充值','2023-05-15 20:57:06','2023-05-15 20:57:06',0),(63,1,'曾令炜','20230516085200944',1,'充值','100.00','充值','2023-05-16 08:52:09','2023-05-16 08:52:09',0),(65,1,'曾令炜','INVEST20230516101138398',2,'投标锁定','300.00','投标项目编号：LEND20230510152915620,项目名称：曾忆远的标的','2023-05-16 10:11:42','2023-05-16 10:11:42',0),(66,1,'曾令炜','20230516102121037',1,'充值','100.00','充值','2023-05-16 10:21:24','2023-05-16 10:21:24',0),(67,1,'曾令炜','20230516102559387',1,'充值','500.00','充值','2023-05-16 10:26:03','2023-05-16 10:26:03',0),(68,1,'曾令炜','20230516103128687',1,'充值','1000000.00','充值','2023-05-16 10:31:31','2023-05-16 10:31:31',0),(69,1,'曾令炜','INVEST20230516103657272',2,'投标锁定','99600.00','投标项目编号：LEND20230510152915620,项目名称：曾忆远的标的','2023-05-16 10:37:03','2023-05-16 10:37:03',0),(106,2,'曾忆远','LOAN20230516160339186',5,'放款到账','100000.00','借款放款到账，编号：LEND20230510152915620','2023-05-16 16:03:40','2023-05-16 16:03:40',0),(107,1,'曾令炜','TRANS20230516160340942',3,'放款解锁','100.00','冻结资金转出，出借放款，编号：LEND20230510152915620','2023-05-16 16:03:40','2023-05-16 16:03:40',0),(108,1,'曾令炜','TRANS20230516160340256',3,'放款解锁','300.00','冻结资金转出，出借放款，编号：LEND20230510152915620','2023-05-16 16:03:40','2023-05-16 16:03:40',0),(109,1,'曾令炜','TRANS20230516160340547',3,'放款解锁','99600.00','冻结资金转出，出借放款，编号：LEND20230510152915620','2023-05-16 16:03:40','2023-05-16 16:03:40',0),(110,1,'曾令炜','20230524100352234',1,'充值','100.00','充值','2023-05-24 10:04:18','2023-05-24 10:04:18',0),(111,1,'曾令炜','20230524101949153',1,'充值','100.00','充值','2023-05-24 10:20:01','2023-05-24 10:20:01',0),(112,1,'曾令炜','20230524103631415',1,'充值','1.00','充值','2023-05-24 10:36:53','2023-05-24 10:36:53',0);

/*Table structure for table `user_account` */

DROP TABLE IF EXISTS `user_account`;

CREATE TABLE `user_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户id',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '帐户可用余额',
  `freeze_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  `version` int NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户账户';

/*Data for the table `user_account` */

insert  into `user_account`(`id`,`user_id`,`amount`,`freeze_amount`,`create_time`,`update_time`,`is_deleted`,`version`) values (1,1,'921201.00','0.00','2023-04-24 15:05:59','2023-05-24 10:36:53',0,0),(2,2,'100000.00','0.00','2023-04-24 08:55:15','2023-05-16 16:03:40',0,0),(9,9,'0.00','0.00','2023-05-24 15:01:30','2023-05-24 15:01:30',0,0),(10,10,'0.00','0.00','2023-05-24 15:03:15','2023-05-24 15:03:15',0,0);

/*Table structure for table `user_bind` */

DROP TABLE IF EXISTS `user_bind`;

CREATE TABLE `user_bind` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户姓名',
  `id_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '身份证号',
  `bank_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '银行卡号',
  `bank_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '银行类型',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '绑定账户协议号',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户绑定表';

/*Data for the table `user_bind` */

insert  into `user_bind`(`id`,`user_id`,`name`,`id_card`,`bank_no`,`bank_type`,`mobile`,`bind_code`,`status`,`create_time`,`update_time`,`is_deleted`) values (11,2,'曾忆远','421081196801241441','620666666666666666','农业银行','15926555568','c61f97ab80d142acbd631a8318362987',1,'2023-05-09 10:49:48','2023-05-09 10:49:48',0),(12,1,'曾令炜','421081199907064554','6222222222222222222','招商银行','13886592587','9184d8f90ea744bcb027d5a717536913',1,'2023-05-11 19:43:26','2023-05-11 19:43:26',0);

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_type` tinyint NOT NULL DEFAULT '0' COMMENT '1：出借人 2：借款人',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户密码',
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户昵称',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户姓名',
  `id_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '身份证号',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `openid` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信用户标识openid',
  `head_img` varchar(999) DEFAULT NULL,
  `bind_status` tinyint NOT NULL DEFAULT '0' COMMENT '绑定状态（0：未绑定，1：绑定成功 -1：绑定失败）',
  `borrow_auth_status` tinyint NOT NULL DEFAULT '0' COMMENT '借款人认证状态（0：未认证 1：认证中 2：认证通过 -1：认证失败）',
  `bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '绑定账户协议号',
  `integral` int NOT NULL DEFAULT '0' COMMENT '用户积分',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态（0：锁定 1：正常）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `uk_mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户基本信息';

/*Data for the table `user_info` */

insert  into `user_info`(`id`,`user_type`,`mobile`,`password`,`nick_name`,`name`,`id_card`,`email`,`openid`,`head_img`,`bind_status`,`borrow_auth_status`,`bind_code`,`integral`,`status`,`create_time`,`update_time`,`is_deleted`) values (1,1,'13886592587','96e79218965eb72c92a549dd5a330112','13886592587','曾令炜','421081199907064554',NULL,NULL,'https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/avater/aliyun.png',1,0,'9184d8f90ea744bcb027d5a717536913',0,1,'2023-04-24 08:55:15','2023-05-23 17:24:46',0),(2,2,'15926555568','96e79218965eb72c92a549dd5a330112','15926555568','曾忆远','421081196801241441',NULL,NULL,'https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/avater/aliyun.png',1,2,'c61f97ab80d142acbd631a8318362987',630,1,'2023-04-24 15:05:59','2023-05-23 17:24:59',0),(9,1,'13247190989','e10adc3949ba59abbe56e057f20f883e','13247190989','杨肖',NULL,NULL,NULL,'https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/avater/aliyun.png',0,0,NULL,0,1,'2023-05-24 15:01:30','2023-05-24 15:37:37',0),(10,1,'18872559253','e10adc3949ba59abbe56e057f20f883e','18872559253','郝冉',NULL,NULL,NULL,'https://zlw-srb-file.oss-cn-hangzhou.aliyuncs.com/avater/aliyun.png',0,0,NULL,0,1,'2023-05-24 15:03:15','2023-05-24 15:37:07',0);

/*Table structure for table `user_integral` */

DROP TABLE IF EXISTS `user_integral`;

CREATE TABLE `user_integral` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `integral` int DEFAULT NULL COMMENT '积分',
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '获取积分说明',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户积分记录表';

/*Data for the table `user_integral` */

insert  into `user_integral`(`id`,`user_id`,`integral`,`content`,`create_time`,`update_time`,`is_deleted`) values (21,2,30,'借款人基本信息','2023-05-09 15:56:47','2023-05-09 15:56:47',0),(22,2,30,'借款人身份证信息','2023-05-09 15:56:47','2023-05-09 15:56:47',0),(23,2,100,'借款人房产信息','2023-05-09 15:56:47','2023-05-09 15:56:47',0),(24,2,60,'借款人车辆信息','2023-05-09 15:56:47','2023-05-09 15:56:47',0),(25,2,220,'借款人基本信息','2023-05-10 15:15:19','2023-05-10 15:15:19',0),(26,2,30,'借款人身份证信息','2023-05-10 15:15:19','2023-05-10 15:15:19',0),(27,2,100,'借款人房产信息','2023-05-10 15:15:19','2023-05-10 15:15:19',0),(28,2,60,'借款人车辆信息','2023-05-10 15:15:19','2023-05-10 15:15:19',0);

/*Table structure for table `user_login_record` */

DROP TABLE IF EXISTS `user_login_record`;

CREATE TABLE `user_login_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户登录记录表';

/*Data for the table `user_login_record` */

insert  into `user_login_record`(`id`,`user_id`,`ip`,`create_time`,`update_time`,`is_deleted`) values (1,1,'0:0:0:0:0:0:0:1','2023-04-24 10:12:46','2023-04-24 15:09:20',0),(2,1,'127.0.0.1','2023-04-24 10:56:01','2023-04-24 15:09:22',0),(3,7,'0:0:0:0:0:0:0:1','2023-04-24 11:00:29','2023-04-24 15:09:18',0),(32,2,'0:0:0:0:0:0:0:1','2023-04-24 15:59:04','2023-04-24 15:59:04',0),(33,2,'10.118.76.91','2023-05-08 10:09:31','2023-05-08 10:09:31',0),(34,2,'10.118.76.91','2023-05-09 08:31:17','2023-05-09 08:31:17',0),(35,2,'10.118.76.91','2023-05-09 15:00:23','2023-05-09 15:00:23',0),(36,2,'10.118.76.91','2023-05-09 19:04:04','2023-05-09 19:04:04',0),(37,1,'10.117.205.126','2023-05-10 19:08:39','2023-05-10 19:08:39',0),(38,2,'10.117.205.126','2023-05-10 19:35:51','2023-05-10 19:35:51',0),(39,1,'10.119.231.114','2023-05-11 19:39:39','2023-05-11 19:39:39',0),(40,1,'10.119.231.114','2023-05-15 20:50:09','2023-05-15 20:50:09',0),(41,1,'10.119.231.114','2023-05-16 08:51:22','2023-05-16 08:51:22',0),(42,2,'10.119.231.114','2023-05-16 15:30:06','2023-05-16 15:30:06',0),(43,2,'0:0:0:0:0:0:0:1','2023-05-16 15:33:14','2023-05-16 15:33:14',0),(44,1,'10.119.231.114','2023-05-16 16:34:12','2023-05-16 16:34:12',0),(45,1,'113.57.237.93','2023-05-23 09:11:15','2023-05-23 09:11:15',0),(46,1,'113.57.237.93','2023-05-23 11:00:53','2023-05-23 11:00:53',0),(47,1,'172.22.35.84','2023-05-23 16:58:46','2023-05-23 16:58:46',0),(48,1,'172.22.35.84','2023-05-23 17:20:07','2023-05-23 17:20:07',0),(49,1,'172.22.35.84','2023-05-23 17:22:26','2023-05-23 17:22:26',0),(50,1,'172.22.35.84','2023-05-23 17:26:41','2023-05-23 17:26:41',0),(51,1,'172.22.35.84','2023-05-23 17:30:52','2023-05-23 17:30:52',0),(52,1,'172.22.35.84','2023-05-23 17:47:48','2023-05-23 17:47:48',0),(53,2,'172.22.35.84','2023-05-23 17:53:45','2023-05-23 17:53:45',0),(54,1,'172.22.35.84','2023-05-23 21:32:55','2023-05-23 21:32:55',0),(55,1,'172.22.35.84','2023-05-23 21:33:25','2023-05-23 21:33:25',0),(56,1,'172.22.35.84','2023-05-24 10:19:30','2023-05-24 10:19:30',0),(57,1,'172.22.35.84','2023-05-24 10:34:56','2023-05-24 10:34:56',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
