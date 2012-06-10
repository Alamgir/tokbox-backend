CREATE DATABASE  IF NOT EXISTS `idonia` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `idonia`;
-- MySQL dump 10.13  Distrib 5.5.15, for Win32 (x86)
--
-- Host: localhost    Database: idonia
-- ------------------------------------------------------
-- Server version	5.5.17

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
-- Table structure for table `purchases`
--

DROP TABLE IF EXISTS `purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `purchase_type` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `purchase_abbr` varchar(255) DEFAULT NULL,
  `total` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchases`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
INSERT INTO `purchases` VALUES (1,'Character Slot','Add an additional character to your team! Max number of team members is 5.',50,'Token','2011-02-17 09:22:36','2011-09-06 13:17:01','CHS',19),(3,'Gold Mine','Convert your tokens into solid gold! Current Exchange Rate: 300 Gold for 1 Token.',10,'Token','2011-02-17 09:36:02','2011-11-28 10:43:59','GOM',79),(4,'Breadstick','A special type of bread that increases your team\'s experience gain by 20%. Lasts for 5 battles.',10,'Token','2011-02-17 09:38:25','2011-10-11 06:48:17','BRE',13),(5,'Handful of Goodies','This Handful of Goodies holds 20 Tokens. Trade them in for prizes at the market.',1,'Money','2011-02-17 09:42:12','2011-10-31 13:36:45','HFG',3),(6,'Mouthful of Goodies','This Mouthful of Tokens holds 110 Tokens. You get 10 Tokens for free!',5,'Money','2011-02-17 10:04:38','2011-10-18 08:06:09','MFG',0),(7,'Small Bag of Goodies','This Small Bag of Goodies holds 220 Tokens. You get 20 Tokens for free!',10,'Money','2011-02-17 10:06:11','2011-10-18 08:05:53','SBG',0),(8,'Amnesia','Wish you could go back and allocate stat points differently? Restore all stat points for one character.',10,'Token','2011-02-25 13:21:32','2011-11-27 10:18:45','AMN',55),(11,'Bread Slice','A slice of bread that increases your team\'s experience gain by 40%. Lasts for 3 battles.	',10,'Token','2011-10-11 06:47:49','2011-11-01 14:46:32','BSL',4),(12,'Bread Loaf','A loaf of bread that increases your team\'s experience gain by 100%. Lasts for 1 battle.	',10,'Token','2011-10-11 06:48:49','2011-11-28 09:34:10','BLF',19),(14,'Fresh Booty','Revive all Treasure Nodes and reap the bounty of Gold, Tokens, and Epic items once again.',40,'Token','2011-10-11 07:20:12','2011-10-25 08:06:16','FRB',4),(15,'Expanded Sack','Gain additional inventory slots so you can carry more weapons and accessories.',10,'Token','2011-10-15 06:14:57','2011-11-25 21:56:00','SCK',6),(16,'Box of Goodies','This Box of Goodies holds 440 Tokens. You get 40 Tokens for free!',20,'Money','2011-10-18 08:01:41','2011-10-18 08:05:33','BOG',0),(17,'Giant Sack of Goodies','This Giant Sack of Goodies holds 1100 Tokens. You get 100 Tokens for free!',50,'Money','2011-10-18 08:02:28','2011-10-18 08:05:15','GSG',0),(18,'Massive Sack of Goodies','This Massive Sack of Goodies holds 2200 Tokens. You get 200 Tokens for free!',100,'Money','2011-10-18 08:03:24','2011-10-18 08:05:01','MSG',0),(19,'Earn Tokens','Would you like to earn new tokens for free?',0,'Money','2011-11-16 09:01:33','2011-11-16 09:01:33','ETK',0);
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_purchases_on_insert AFTER INSERT on purchases FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 3 */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_purchases_on_update AFTER UPDATE on purchases FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 3 */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-20  2:54:44
