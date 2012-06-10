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
-- Table structure for table `skill_effects`
--

DROP TABLE IF EXISTS `skill_effects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skill_effects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `skill_id` int(11) DEFAULT NULL,
  `strength` decimal(10,0) DEFAULT NULL,
  `intelligence` decimal(10,0) DEFAULT NULL,
  `vitality` decimal(10,0) DEFAULT NULL,
  `will` decimal(10,0) DEFAULT NULL,
  `physical_crit` decimal(10,0) DEFAULT NULL,
  `skill_crit` decimal(10,0) DEFAULT NULL,
  `armor` decimal(10,0) DEFAULT NULL,
  `dodge` decimal(10,0) DEFAULT NULL,
  `turn` int(11) DEFAULT NULL,
  `effect_type` varchar(255) DEFAULT NULL,
  `damage` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `agility` decimal(10,0) DEFAULT NULL,
  `multiplier` decimal(10,0) DEFAULT NULL,
  `subtype` varchar(255) DEFAULT NULL,
  `duration_type` varchar(255) DEFAULT NULL,
  `remove_number` int(11) DEFAULT '0',
  `stackable` tinyint(1) DEFAULT '0',
  `stack_limit` int(11) DEFAULT '0',
  `proc` decimal(10,0) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `index_skill_id` (`skill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill_effects`
--

LOCK TABLES `skill_effects` WRITE;
/*!40000 ALTER TABLE `skill_effects` DISABLE KEYS */;
INSERT INTO `skill_effects` VALUES (2,24,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Permanent',0,0,0,1),(4,31,0,0,0,0,0,0,0,0,3,'Debuff',1,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(6,35,0,0,0,0,0,0,-1,0,9,'Debuff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(7,34,0,0,0,0,1,0,0,0,0,'Instant',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'Crit','Turn',0,0,0,1),(8,21,0,0,0,0,0,0,0,0,6,'Debuff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(9,44,0,0,0,0,0,0,0,1,12,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(10,37,0,0,0,0,0,0,0,0,12,'Debuff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(11,17,0,0,0,0,0,0,0,0,12,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,1,'Diversion','Turn',0,0,0,1),(13,19,0,0,0,0,0,0,0,0,18,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(14,20,0,0,0,0,0,0,0,0,3,'Debuff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'Damage','Turn',0,0,0,1),(15,40,0,0,0,0,0,0,0,0,18,'Debuff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,1,'Poison','Turn',0,0,0,1),(16,41,0,0,0,0,0,0,0,0,4,'Debuff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'Mana','Turn',0,0,0,1),(18,56,0,0,0,0,0,0,0,0,1,'Instant',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'DebuffRemove','Turn',10,0,0,1),(19,51,0,0,0,0,0,0,0,0,15,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(20,36,0,0,0,0,0,0,0,0,1,'Instant',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'Mana','Turn',0,0,0,1),(21,58,0,0,0,0,0,0,0,0,12,'Debuff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Frost','Turn',0,0,0,1),(22,61,0,0,0,0,0,0,0,0,5,'Debuff',5,'2011-12-17 04:07:43','0000-00-00 00:00:00',0,0,'Drain','Turn',0,0,0,1),(23,60,0,0,0,0,0,0,0,0,1,'Instant',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,1,'Drain','Turn',0,0,0,1),(24,23,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'Reflect Physical','Permanent',0,0,0,1),(25,25,0,0,0,0,1,1,0,0,1000,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Permanent',0,0,0,1),(26,29,0,0,0,0,0,0,0,0,1,'Instant',8,'2011-12-17 04:07:41','0000-00-00 00:00:00',1,0,'DebuffRemove','Turn',10,0,0,1),(28,62,0,0,0,0,0,0,0,0,1,'Instant',10,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'BuffRemove','Turn',0,0,0,1),(29,47,0,0,0,0,0,0,0,0,6,'Buff',-5,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(30,50,0,0,0,0,0,0,0,0,14,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,1,'Heal','Turn',0,0,0,1),(31,54,0,0,0,0,0,0,0,0,1000,'Buff',-4,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Permanent',0,0,0,1),(32,53,0,0,0,0,0,0,0,0,6,'Debuff',3,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(33,64,0,0,0,0,0,0,0,0,19,'Buff',0,'2011-12-17 04:07:41','0000-00-00 00:00:00',0,0,'Reflect All','Turn',0,0,0,1),(34,55,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Permanent',0,0,0,1),(35,67,0,0,0,0,0,0,0,0,9,'Debuff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(36,68,0,0,0,0,0,0,0,0,13,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Heal','Turn',0,0,0,1),(37,45,0,0,0,0,0,0,0,0,13,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Ethereal','Turn',0,0,0,1),(38,77,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Permanent',0,0,0,1),(39,38,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Apply Attack','Permanent',0,0,0,1),(40,39,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Apply Attack','Permanent',0,0,0,1),(41,79,0,0,0,0,0,0,0,0,1,'Instant',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Damage','Turn',0,0,0,1),(42,78,0,0,0,0,0,0,0,0,7,'Debuff',3,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Turn',0,1,3,1),(43,72,0,0,0,0,0,0,0,0,8,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Defend','Turn',0,0,0,1),(44,80,0,0,0,0,0,0,0,0,8,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Defend','Turn',0,0,0,1),(45,81,0,0,0,0,0,0,0,0,8,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Defend','Turn',0,0,0,1),(46,82,0,0,0,0,0,0,0,0,8,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Defend','Turn',0,0,0,1),(47,83,0,0,0,0,0,0,0,0,8,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Defend','Turn',0,0,0,1),(48,85,0,0,0,0,0,0,0,0,6,'Debuff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Silence','Turn',0,0,0,1),(49,86,0,0,0,0,0,0,-1,0,6,'Debuff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(50,65,0,0,0,0,0,0,0,0,1000,'Debuff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Cast','Permanent',0,0,0,1),(51,66,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Apply Dead','Permanent',0,0,0,1),(53,88,0,0,0,0,0,0,0,0,12,'Debuff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,1,'Cast','Turn',0,0,0,1),(54,89,0,0,0,0,0,0,0,0,6,'Debuff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Silence','Turn',0,0,0,1),(55,90,0,0,0,0,0,0,0,0,3,'Debuff',3,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(56,91,0,0,0,0,0,0,0,0,6,'Buff',-1,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(57,26,0,0,0,0,0,0,0,0,6,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Immune','Turn',0,0,0,1),(58,93,0,0,0,0,0,0,0,0,1,'Instant',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,-1,'Mana','Turn',0,0,0,1),(61,63,0,0,0,0,0,0,0,0,12,'Debuff',6,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(64,99,0,0,0,0,0,0,0,0,3,'Debuff',5,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(65,95,0,0,0,0,0,0,-1,0,13,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Turn',0,0,0,1),(66,100,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Heal','Permanent',0,0,0,1),(67,101,0,0,0,0,0,0,0,0,1000,'Buff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'None','Permanent',0,0,0,1),(68,94,0,0,0,0,0,0,0,0,9,'Debuff',0,'2011-12-17 04:07:42','0000-00-00 00:00:00',0,0,'Anti Heal','Turn',0,0,0,1);
/*!40000 ALTER TABLE `skill_effects` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_skill_effects_on_insert AFTER INSERT on skill_effects FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 2 */;;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_skill_effects_on_update AFTER UPDATE on skill_effects FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 2 */;;
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

-- Dump completed on 2012-01-20  2:54:45
