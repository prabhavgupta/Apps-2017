-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.9-MariaDB


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema lba_retail
--

CREATE DATABASE IF NOT EXISTS lba_retail;
USE lba_retail;

--
-- Definition of table `adimages`
--

DROP TABLE IF EXISTS `adimages`;
CREATE TABLE `adimages` (
  `imageId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adsId` int(10) unsigned DEFAULT NULL,
  `img` longblob,
  `iname` varchar(45) DEFAULT NULL,
  `udate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`imageId`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `adimages`
--

/*!40000 ALTER TABLE `adimages` DISABLE KEYS */;
/*!40000 ALTER TABLE `adimages` ENABLE KEYS */;


--
-- Definition of table `adsvisit`
--

DROP TABLE IF EXISTS `adsvisit`;
CREATE TABLE `adsvisit` (
  `visitId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adsId` int(10) unsigned DEFAULT NULL,
  `userId` int(10) unsigned DEFAULT NULL,
  `udate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`visitId`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `adsvisit`
--

/*!40000 ALTER TABLE `adsvisit` DISABLE KEYS */;
INSERT INTO `adsvisit` (`visitId`,`adsId`,`userId`,`udate`) VALUES 
 (1,6,2,'2015-03-19 20:27:48'),
 (2,2,2,'2015-03-19 20:28:00'),
 (3,6,2,'2015-03-20 11:25:21'),
 (4,6,2,'2015-03-20 11:44:31'),
 (5,6,2,'2015-03-20 12:49:22'),
 (6,6,1,'2015-03-20 15:12:08'),
 (7,3,7,'2015-03-20 19:22:08');
/*!40000 ALTER TABLE `adsvisit` ENABLE KEYS */;


--
-- Definition of table `advertisement`
--

DROP TABLE IF EXISTS `advertisement`;
CREATE TABLE `advertisement` (
  `adsId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adsDesc` varchar(512) DEFAULT NULL,
  `retailerId` int(10) unsigned DEFAULT NULL,
  `adTitle` varchar(512) DEFAULT NULL,
  `product` varchar(45) DEFAULT NULL,
  `fdate` datetime DEFAULT NULL,
  `tdate` datetime DEFAULT NULL,
  `advDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`adsId`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `advertisement`
--

/*!40000 ALTER TABLE `advertisement` DISABLE KEYS */;
INSERT INTO `advertisement` (`adsId`,`adsDesc`,`retailerId`,`adTitle`,`product`,`fdate`,`tdate`,`advDate`) VALUES 
 (6,'Clothes',4,'offer','1','2015-03-16 00:00:00','2015-03-27 00:00:00','2015-03-20 12:37:32'),
 (2,'Reebok and nike',2,'20% off on all Winter Ware','1','2015-03-16 00:00:00','2015-03-27 00:00:00','2015-03-20 12:37:28'),
 (3,'sadfsad',2,'sadfas','2','2015-03-15 00:00:00','2015-03-27 00:00:00','2015-03-20 12:37:28'),
 (7,'Test2',5,'test','2','2015-03-20 00:00:00','2015-03-25 00:00:00','2015-03-20 15:13:29'),
 (8,'All accessories',7,'30% flat off','1','2015-03-21 00:00:00','2015-03-30 00:00:00','2015-03-20 18:50:02');
/*!40000 ALTER TABLE `advertisement` ENABLE KEYS */;


--
-- Definition of table `products`
--

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `productId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `productName` varchar(45) NOT NULL,
  PRIMARY KEY (`productId`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `products`
--

/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` (`productId`,`productName`) VALUES 
 (1,'Clothing'),
 (2,'Cosmetics'),
 (3,'Electronics'),
 (4,'Food'),
 (5,'Sports ');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;


--
-- Definition of table `retailermaster`
--

DROP TABLE IF EXISTS `retailermaster`;
CREATE TABLE `retailermaster` (
  `retailerId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ownername` varchar(512) DEFAULT NULL,
  `shopname` varchar(512) DEFAULT NULL,
  `shopaddress` varchar(512) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `emailId` varchar(45) DEFAULT NULL,
  `pass` varchar(45) DEFAULT NULL,
  `products` varchar(45) DEFAULT NULL,
  `licenseNo` varchar(45) DEFAULT NULL,
  `shopLat` varchar(45) DEFAULT NULL,
  `shopLng` varchar(45) DEFAULT NULL,
  `regdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `imei` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`retailerId`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `retailermaster`
--

/*!40000 ALTER TABLE `retailermaster` DISABLE KEYS */;
INSERT INTO `retailermaster` (`retailerId`,`ownername`,`shopname`,`shopaddress`,`phone`,`emailId`,`pass`,`products`,`licenseNo`,`shopLat`,`shopLng`,`regdate`,`imei`) VALUES 
 (1,'Amar Kothari','Amar Hardware','Right Bhusari Colony,Kothrud Depo,Pune','9809093333','amar@gmail.com','amar','1','123456','18.56778','73.234555','2015-03-12 22:35:51',NULL),
 (2,'Amit Deshpande','Persistent Pvt Ltd','Hinjewadi Pune','9766750001','r@gmail.com','rajesh','1,3,5','12345668','18.597059000000002','73.718823299999997','2015-03-16 12:12:31',''),
 (3,'test Owenre','Test S','Shivajinagar Pune','1234567890','r@gm.com','rajesh','','12121212','18.530822499999999','73.847464700000003','2015-03-13 20:18:47',''),
 (4,'sandesh','shop','Kothrud, Pune, Maharashtra, India','9175080777','nimhansandesh8@gnail com','123','','123','18.507398500000001','73.8076504','2015-03-16 16:39:46',''),
 (5,'rajesh','TechnoWIngs Inc','Right Bhusari Colony','9766750000','m@gmail.com','rajesh','','12121212','0','0','2015-03-20 12:39:45',''),
 (6,'','','','','','','','','0','0','2015-03-20 11:46:41',''),
 (7,'ron','rihnoz','Kothrud, Pune, Maharashtra, India','8087294853','rihno008@gmail.com','vamp','','abc123','18.507398500000001','73.8076504','2015-03-20 18:47:27','359462041039040');
/*!40000 ALTER TABLE `retailermaster` ENABLE KEYS */;


--
-- Definition of table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
CREATE TABLE `useraccount` (
  `userid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(256) DEFAULT NULL,
  `pass` varchar(45) DEFAULT NULL,
  `emailid` varchar(255) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `products` varchar(45) DEFAULT NULL,
  `imei` varchar(45) DEFAULT NULL,
  `roleId` varchar(1) DEFAULT 'U',
  PRIMARY KEY (`userid`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `useraccount`
--

/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` (`userid`,`username`,`pass`,`emailid`,`phone`,`products`,`imei`,`roleId`) VALUES 
 (1,'neha','neha','rajesh@gmail.com','9922000017','1,2','911478800637270','U'),
 (2,'rajesh','rajesh','mail@gmail.com','9860923474','1,3,5','','U'),
 (3,'rajesh','rajesh','m@gmail.com','9766750021',NULL,'','U'),
 (4,'','','','',NULL,'','U'),
 (5,'shibani','123','shibanidhore','1234567890','1','','U'),
 (6,'admin','admin','nimhansandesh8@gmail.com','9922000017',NULL,'911478800637270','A'),
 (7,'sandesh','123','nimhansandesh8@gmail.com','9922000017','1','865645025450129','U'),
 (8,'jyoti','jyoti','jyotsthorat22@gmail.com','8600910179',NULL,'','U');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;


--
-- Definition of table `whishlist`
--

DROP TABLE IF EXISTS `whishlist`;
CREATE TABLE `whishlist` (
  `adsId` int(10) unsigned DEFAULT NULL,
  `userId` int(10) unsigned DEFAULT NULL,
  `wishId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `udate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`wishId`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `whishlist`
--

/*!40000 ALTER TABLE `whishlist` DISABLE KEYS */;
INSERT INTO `whishlist` (`adsId`,`userId`,`wishId`,`udate`) VALUES 
 (6,2,1,'2015-03-20 14:56:53'),
 (3,2,2,'2015-03-20 15:09:51'),
 (6,1,3,'2015-03-20 15:12:14');
/*!40000 ALTER TABLE `whishlist` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
