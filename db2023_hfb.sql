/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 8.0.28 : Database - db2023_hfb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db2023_hfb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `db2023_hfb`;

/*Table structure for table `user_account` */

DROP TABLE IF EXISTS `user_account`;

CREATE TABLE `user_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '用户code',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '帐户可用余额',
  `freeze_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '投资中冻结金额',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户账号表';

/*Data for the table `user_account` */

insert  into `user_account`(`id`,`user_code`,`amount`,`freeze_amount`,`create_time`,`update_time`,`is_deleted`) values (17,'56e73f76ead04fcc9fdbcd0014c8af9c','0.00','0.00','2023-05-09 10:51:33','2023-05-09 10:51:33',0),(19,'c61f97ab80d142acbd631a8318362987','97500.00','0.00','2023-05-09 11:00:43','2023-05-09 11:00:43',0),(20,'9184d8f90ea744bcb027d5a717536913','941201.00','0.00','2023-05-11 19:43:44','2023-05-11 19:43:44',0);

/*Table structure for table `user_bind` */

DROP TABLE IF EXISTS `user_bind`;

CREATE TABLE `user_bind` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `agent_id` int NOT NULL DEFAULT '0' COMMENT '商户id',
  `agent_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT 'P2P商户的用户id',
  `personal_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户姓名',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `id_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '身份证号',
  `bank_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `return_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `notify_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `timestamp` bigint DEFAULT NULL,
  `bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '绑定的汇付宝id',
  `pay_passwd` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '支付密码',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户绑定表';

/*Data for the table `user_bind` */

insert  into `user_bind`(`id`,`agent_id`,`agent_user_id`,`personal_name`,`mobile`,`id_card`,`bank_no`,`email`,`return_url`,`notify_url`,`timestamp`,`bind_code`,`pay_passwd`,`status`,`create_time`,`update_time`,`is_deleted`) values (19,999888,'2','曾忆远','15926555568','421081196801241441','620666666666666666',NULL,'http://localhost:3000/user','http://localhost/api/core/userBind/notify',1683601237275,'c61f97ab80d142acbd631a8318362987','123456',0,'2023-05-09 11:00:43','2023-05-09 11:00:43',0),(20,999888,'1','曾令炜','13886592587','421081199907064554','6222222222222222222',NULL,'http://localhost:3000/user','http://localhost/api/core/userBind/notify',1683805406163,'9184d8f90ea744bcb027d5a717536913','123456',0,'2023-05-11 19:43:44','2023-05-11 19:43:44',0);

/*Table structure for table `user_invest` */

DROP TABLE IF EXISTS `user_invest`;

CREATE TABLE `user_invest` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `vote_bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '投资人绑定协议号',
  `benefit_bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '借款人绑定协议号',
  `agent_project_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '项目编号',
  `agent_project_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '项目名称',
  `agent_bill_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '商户订单号',
  `vote_amt` decimal(10,2) DEFAULT NULL COMMENT '投资金额',
  `vote_prize_amt` decimal(10,2) DEFAULT NULL COMMENT '投资奖励金额',
  `vote_fee_amt` decimal(10,2) DEFAULT '0.00' COMMENT 'P2P商户手续费',
  `project_amt` decimal(10,2) DEFAULT NULL COMMENT '项目总金额',
  `note` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '投资备注',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0：默认 1：已放款 -1：已撤标）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户投资表';

/*Data for the table `user_invest` */

insert  into `user_invest`(`id`,`vote_bind_code`,`benefit_bind_code`,`agent_project_code`,`agent_project_name`,`agent_bill_no`,`vote_amt`,`vote_prize_amt`,`vote_fee_amt`,`project_amt`,`note`,`status`,`create_time`,`update_time`,`is_deleted`) values (7,'9184d8f90ea744bcb027d5a717536913','c61f97ab80d142acbd631a8318362987','LEND20230510152915620','曾忆远的标的','INVEST20230512145425399','100.00','0.00','0.00','100000.00','',1,'2023-05-12 14:54:35','2023-05-16 10:52:32',0),(8,'9184d8f90ea744bcb027d5a717536913','c61f97ab80d142acbd631a8318362987','LEND20230510152915620','曾忆远的标的','INVEST20230516101138398','300.00','0.00','0.00','100000.00','',1,'2023-05-16 10:11:41','2023-05-16 10:52:32',0),(9,'9184d8f90ea744bcb027d5a717536913','c61f97ab80d142acbd631a8318362987','LEND20230510152915620','曾忆远的标的','INVEST20230516103657272','99600.00','0.00','0.00','100000.00','',1,'2023-05-16 10:37:02','2023-05-16 10:52:32',0);

/*Table structure for table `user_item_return` */

DROP TABLE IF EXISTS `user_item_return`;

CREATE TABLE `user_item_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_return_id` bigint NOT NULL DEFAULT '0' COMMENT '还款id',
  `agent_project_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '还款项目编号',
  `vote_bill_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '投资单号',
  `to_bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0.00' COMMENT '收款人（投资人）',
  `transit_amt` decimal(10,2) DEFAULT NULL COMMENT '还款金额',
  `base_amt` decimal(10,2) DEFAULT NULL COMMENT '还款本金',
  `benifit_amt` decimal(10,2) DEFAULT NULL COMMENT '还款利息',
  `fee_amt` decimal(10,2) DEFAULT NULL COMMENT '商户手续费',
  `note` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户还款明细表';

/*Data for the table `user_item_return` */

/*Table structure for table `user_return` */

DROP TABLE IF EXISTS `user_return`;

CREATE TABLE `user_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `agent_goods_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '商户商品名称',
  `agent_batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '批次号',
  `from_bind_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '还款人绑定协议号',
  `total_amt` decimal(10,2) DEFAULT '0.00' COMMENT '还款总额',
  `vote_fee_amt` decimal(10,2) DEFAULT NULL COMMENT 'P2P商户手续费',
  `data` json DEFAULT NULL COMMENT '还款明细数据',
  `note` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除(1:已删除，0:未删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户还款表';

/*Data for the table `user_return` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
