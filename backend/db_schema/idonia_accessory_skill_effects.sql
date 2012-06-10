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
-- Table structure for table `accessory_skill_effects`
--

DROP TABLE IF EXISTS `accessory_skill_effects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accessory_skill_effects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accessory_skill_id` int(11) DEFAULT NULL,
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
  `remove_number` int(11) DEFAULT '0',
  `stackable` tinyint(1) DEFAULT '0',
  `stack_limit` int(11) DEFAULT '0',
  `proc` decimal(10,0) DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory_skill_effects`
--

LOCK TABLES `accessory_skill_effects` WRITE;
/*!40000 ALTER TABLE `accessory_skill_effects` DISABLE KEYS */;
INSERT INTO `accessory_skill_effects` VALUES (1,1,0,0,0,0,0,0,0,0,3,1,0,0,'Debuff','None','Turn',0,0,0,0,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(2,2,0,0,0,0,0,0,0,0,1000,100,0,0,'Buff','Health','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(3,3,0,0,0,0,1,0,0,0,1000,0,0,0,'Buff','Crit','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(4,5,0,0,0,0,0,0,0,1,1000,0,0,0,'Buff','None','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(5,14,0,0,0,0,0,0,0,0,1000,0,0,0,'Buff','None','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(6,12,0,0,0,0,0,1,0,0,1000,0,0,0,'Buff','None','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(7,4,0,0,0,0,1,0,0,0,1000,0,0,0,'Buff','None','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(8,7,0,0,0,0,0,0,0,0,1000,0,0,0,'Buff','None','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(9,11,0,0,0,0,0,0,0,0,1000,0,0,0,'Buff','Rage','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00'),(10,15,0,0,0,0,0,0,2,0,1000,0,0,0,'Buff','None','Permanent',0,0,0,1,'2011-12-17 01:48:54','0000-00-00 00:00:00');
/*!40000 ALTER TABLE `accessory_skill_effects` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_accessory_skill_effects_on_insert AFTER INSERT on accessory_skill_effects FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 8 */;;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_accessory_skill_effects_on_update AFTER UPDATE on accessory_skill_effects FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 8 */;;
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

-- Dump completed on 2012-01-20  2:54:46
