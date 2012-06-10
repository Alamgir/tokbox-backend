CREATE DATABASE  IF NOT EXISTS `idonia` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `idonia`;
-- MySQL dump 10.13  Distrib 5.5.15, for Win32 (x86)
--
-- Host: idoniadb.cavauzh7fnej.us-east-1.rds.amazonaws.com    Database: idonia
-- ------------------------------------------------------
-- Server version	5.5.8-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `mob_skill_effects`
--

DROP TABLE IF EXISTS `mob_skill_effects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mob_skill_effects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mob_skill_id` int(11) DEFAULT NULL,
  `strength` decimal(10,0) DEFAULT NULL,
  `intelligence` decimal(10,0) DEFAULT NULL,
  `vitality` decimal(10,0) DEFAULT NULL,
  `will` decimal(10,0) DEFAULT NULL,
  `physical_crit` decimal(10,0) DEFAULT NULL,
  `skill_crit` decimal(10,0) DEFAULT NULL,
  `armor` decimal(10,0) DEFAULT NULL,
  `dodge` decimal(10,0) DEFAULT NULL,
  `turn` int(11) DEFAULT NULL,
  `damage` int(11) DEFAULT NULL,
  `agility` decimal(10,0) DEFAULT NULL,
  `multiplier` decimal(10,0) DEFAULT NULL,
  `effect_type` varchar(255) DEFAULT NULL,
  `subtype` varchar(255) DEFAULT NULL,
  `duration_type` varchar(255) DEFAULT NULL,
  `remove_number` int(11) DEFAULT NULL,
  `stackable` tinyint(1) DEFAULT NULL,
  `stack_limit` int(11) DEFAULT NULL,
  `proc` decimal(10,0) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mob_skill_effects`
--

LOCK TABLES `mob_skill_effects` WRITE;
/*!40000 ALTER TABLE `mob_skill_effects` DISABLE KEYS */;
INSERT INTO `mob_skill_effects` VALUES (36,62,0,0,0,0,0,0,0,0,3,2,0,0,'Debuff','None','Turn',0,0,0,1,'2011-06-06 07:12:00','2011-06-27 12:56:20'),(37,65,0,0,0,0,0,0,0,0,1000,0,0,1,'Buff','Reflect Physical','Turn',0,1,1000,1,'2011-06-09 15:00:46','2011-11-23 08:56:22'),(39,69,0,0,0,0,0,0,0,0,1000,0,0,0,'Buff','DebuffRemove','Turn',10,1,2,1,'2011-06-19 03:50:35','2011-10-20 05:24:47'),(40,72,0,0,0,0,0,0,0,0,4,3,0,0,'Debuff','Damage','Turn',0,0,3,1,'2011-06-19 03:53:16','2011-06-19 03:53:16'),(41,73,0,0,0,0,0,0,0,0,6,0,0,1,'Debuff','Poison','Turn',0,1,3,1,'2011-06-20 13:29:29','2011-11-19 04:51:38'),(42,75,0,0,0,0,0,0,0,0,18,0,0,0,'Buff','None','Turn',0,1,100,1,'2011-06-20 14:01:29','2011-06-21 08:34:53'),(43,76,0,0,0,0,1,0,0,0,0,0,0,0,'Instant','Crit','Turn',0,0,0,1,'2011-06-20 14:03:41','2011-06-20 14:03:41'),(44,63,0,0,0,0,0,0,0,0,1,0,0,1,'Instant','Drain','Turn',0,0,0,1,'2011-06-27 08:50:35','2011-06-27 08:50:35'),(45,77,0,0,0,0,0,0,0,0,5,1,0,0,'Debuff','Drain','Turn',0,0,0,1,'2011-06-27 08:51:29','2011-07-19 14:10:49'),(46,80,0,0,0,0,0,0,0,0,5,1,0,0,'Debuff','None','Turn',0,0,0,1,'2011-06-29 07:33:33','2011-06-29 13:01:17'),(47,83,0,0,0,0,0,0,0,0,3,1,0,0,'Debuff','None','Turn',0,0,0,1,'2011-06-29 07:45:53','2011-06-29 07:45:53'),(48,81,0,0,0,0,0,0,0,0,5,0,0,0,'Debuff','Mana','Turn',0,0,0,1,'2011-06-29 07:47:31','2011-06-29 07:47:31'),(49,79,0,0,0,0,0,0,0,0,1000,0,0,0,'Buff','Immune','Permanent',0,0,0,1,'2011-06-29 07:49:31','2011-06-29 07:49:31'),(50,84,0,0,0,0,0,0,1,0,1000,0,0,1,'Buff','Reflect Physical','Permanent',0,0,0,1,'2011-06-29 14:56:21','2011-09-04 14:07:53'),(51,85,0,0,0,0,0,0,0,0,1000,0,0,1,'Buff','Reflect Magic','Permanent',0,0,0,1,'2011-06-29 14:56:44','2011-09-04 14:08:09'),(52,86,0,0,0,0,0,0,0,0,1,0,0,1,'Instant','Drain','Turn',0,0,0,1,'2011-06-29 14:58:08','2011-06-30 11:25:34'),(53,89,0,0,0,0,0,0,0,0,5,-5,0,0,'Buff','None','Turn',0,0,0,1,'2011-06-29 14:59:17','2011-06-29 14:59:17'),(54,90,0,0,0,0,0,0,1,0,1000,0,0,1,'Buff','Reflect All','Permanent',0,0,0,1,'2011-06-30 07:01:41','2011-09-04 14:08:26'),(55,91,0,0,0,0,0,0,0,0,6,0,0,0,'Debuff','Silence','Turn',0,0,0,1,'2011-07-22 05:11:30','2011-07-22 05:11:30'),(56,92,0,0,0,0,0,0,0,0,4,0,0,0,'Debuff','Mana','Turn',0,0,0,1,'2011-07-22 05:12:33','2011-07-25 08:31:39'),(57,94,0,0,0,0,0,0,0,0,6,-5,0,0,'Buff','None','Turn',0,0,0,1,'2011-08-31 06:40:36','2011-08-31 08:47:23'),(58,95,0,0,0,0,0,0,0,0,6,5,0,0,'Debuff','Drain','Turn',0,0,0,1,'2011-08-31 06:42:12','2011-09-01 14:07:19'),(59,97,0,0,0,0,0,0,0,0,8,0,0,1,'Buff','Diversion','Turn',0,0,0,1,'2011-08-31 06:43:46','2011-08-31 06:43:46'),(60,101,0,0,0,0,0,0,1,0,6,0,0,0,'Buff','None','Turn',0,0,0,1,'2011-08-31 09:41:57','2011-08-31 09:42:28'),(61,106,0,0,0,0,0,0,0,0,6,0,0,0,'Buff','None','Turn',0,0,0,1,'2011-08-31 12:23:25','2011-08-31 12:23:25'),(62,106,0,0,0,0,0,0,0,0,6,0,0,0,'Buff','None','Turn',0,0,0,1,'2011-08-31 12:23:25','2011-08-31 12:23:25'),(63,108,0,0,0,0,0,0,0,0,7,0,0,0,'Debuff','Anti Heal','Turn',0,0,0,1,'2011-09-02 11:16:10','2011-09-04 06:52:14'),(64,110,0,0,0,0,0,0,0,0,6,5,0,0,'Debuff','None','Turn',0,0,0,1,'2011-09-02 13:23:04','2011-09-02 13:23:04'),(65,111,0,0,0,0,0,0,0,0,8,0,0,0,'Buff','Heal','Turn',0,1,3,1,'2011-09-02 13:28:36','2011-09-03 07:52:41'),(66,112,0,0,0,0,0,0,0,0,1,0,0,0,'Instant','Mana','Turn',0,0,0,1,'2011-09-02 13:31:32','2011-09-18 02:08:52'),(67,116,0,0,0,0,0,0,0,0,10,0,0,1,'Buff','Reflect All','Turn',0,0,0,1,'2012-01-22 20:34:43','0000-00-00 00:00:00'),(68,115,0,0,0,0,0,0,0,0,7,0,0,0,'Debuff','Anti Heal','Turn',0,0,0,1,'2012-01-22 20:34:44','0000-00-00 00:00:00');
/*!40000 ALTER TABLE `mob_skill_effects` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-23 21:44:26
