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
-- Table structure for table `accessory_skills`
--

DROP TABLE IF EXISTS `accessory_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accessory_skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accessory_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `damage_low` int(11) DEFAULT NULL,
  `damage_high` int(11) DEFAULT NULL,
  `damage_type` varchar(255) DEFAULT NULL,
  `damage_subtype` varchar(255) DEFAULT NULL,
  `spell_target` varchar(255) DEFAULT NULL,
  `multiplier` decimal(10,0) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `weight` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessory_skills`
--

LOCK TABLES `accessory_skills` WRITE;
/*!40000 ALTER TABLE `accessory_skills` DISABLE KEYS */;
INSERT INTO `accessory_skills` VALUES (1,518,'Bleed','Warrior has a chance to leave a bleed effect on target.',0,0,'Physical','None','Enemy',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(2,442,'Combat Vigor','Titan\'s Maul increases Warrior\'s armor by a medium amount.',0,0,'Physical','None','Self',0,'2011-12-17 02:32:20','0000-00-00 00:00:00',0),(3,516,'Serrated','Warrior\'s physical crit chance increased.',0,0,'Physical','None','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(4,522,'Sharpen','Rogue\'s physical crit chance increased.',0,0,'Physical','None','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(5,520,'Evasion','Rogue\'s dodge chance increased.',0,0,'Physical','None','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(6,524,'Advanced Poison','Poisons will deal more damage.',0,0,'Physical','Poison','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(7,526,'Concentration','Mana regeneration increased.',0,0,'Magic','None','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(8,530,'Blood Magic','Deathbolt and Blood Tap have increased effects.',0,0,'Magic','Blood','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(9,528,'Incinerate','Fire spells increased in damage.',0,0,'Magic','Fire','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(10,594,'Devotion','Increases effectiveness of healing spells.',0,0,'Magic','Heal','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(11,592,'Rage','Rage generation increased.',0,0,'Magic','None','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(12,600,'Survival Kit','Increases spell crit by a medium amount.',0,0,'Magic','None','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(13,598,'Summoner','All summon skills have been buffed.',0,0,'Ranged','Summon','Self',0,'2011-12-17 02:32:19','0000-00-00 00:00:00',0),(14,596,'Dexterity','Increases agility by a medium amount.',0,0,'Ranged','None','Self',0,'2011-12-17 02:32:20','0000-00-00 00:00:00',0),(15,590,'Toughness','Increases armor by a substantial amount.',0,0,'Magic','None','Self',0,'2011-12-17 02:32:20','0000-00-00 00:00:00',0);
/*!40000 ALTER TABLE `accessory_skills` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `idonia`.`update_accessory_skills_on_insert`
AFTER INSERT ON `idonia`.`accessory_skills`
FOR EACH ROW
UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 4 */;;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `idonia`.`update_accessory_skills_on_update`
AFTER UPDATE ON `idonia`.`accessory_skills`
FOR EACH ROW
UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 4 */;;
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
