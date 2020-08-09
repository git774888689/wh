/*
SQLyog Ultimate v8.32 
MySQL - 5.5.27 : Database - day07
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`day07` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `day07`;

/*Table structure for table `tb_major_day` */

DROP TABLE IF EXISTS `tb_major_day`;

CREATE TABLE `tb_major_day` (
  `md_id` int(11) NOT NULL AUTO_INCREMENT,
  `mdname` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`md_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `tb_major_day` */

insert  into `tb_major_day`(`md_id`,`mdname`) values (1,'大数据'),(2,'物联网'),(3,'全栈'),(4,NULL);

/*Table structure for table `tb_stu_day` */

DROP TABLE IF EXISTS `tb_stu_day`;

CREATE TABLE `tb_stu_day` (
  `sd_id` int(11) NOT NULL AUTO_INCREMENT,
  `sdname` varchar(20) DEFAULT NULL,
  `sdsex` varchar(20) DEFAULT NULL,
  `sdhobby` varchar(20) DEFAULT NULL,
  `sdbirth` date DEFAULT NULL,
  `mid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sd_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `tb_stu_day` */

insert  into `tb_stu_day`(`sd_id`,`sdname`,`sdsex`,`sdhobby`,`sdbirth`,`mid`) values (1,'张毅','男','篮球','2020-01-01',1),(3,'张毅','男','篮球,足球,排球','2020-08-12',3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
