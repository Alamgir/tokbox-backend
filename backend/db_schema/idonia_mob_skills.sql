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
-- Table structure for table `mob_skills`
--

DROP TABLE IF EXISTS `mob_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mob_skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `damage_low` int(11) DEFAULT NULL,
  `damage_high` int(11) DEFAULT NULL,
  `damage_type` varchar(255) DEFAULT NULL,
  `damage_subtype` varchar(255) DEFAULT NULL,
  `spell_target` varchar(255) DEFAULT NULL,
  `multiplier` decimal(10,0) DEFAULT NULL,
  `mana_points` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `weight` int(11) DEFAULT NULL,
  `difficulty_type` varchar(255) DEFAULT NULL,
  `mob_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mob_skills`
--

LOCK TABLES `mob_skills` WRITE;
/*!40000 ALTER TABLE `mob_skills` DISABLE KEYS */;
INSERT INTO `mob_skills` VALUES (60,'Punch','Slap from behind.',1,20,'Physical','None','Enemy',1,1,'2011-06-06 06:58:55','2011-07-21 17:56:34',50,'Normal','Abomination'),(61,'Thrash','hehe.',2,6,'Physical','Multi','Enemy',0,1,'2011-06-06 07:01:44','2011-07-21 18:06:12',50,'Normal','Ninja'),(62,'Gust of Wind','Furry ball of magic.',5,15,'Magic','Fire','Enemy',1,1,'2011-06-06 07:10:01','2011-07-21 17:57:09',50,'Normal','Ghost'),(63,'Death Bolt','Do the funky.',8,12,'Magic','None','Enemy',1,1,'2011-06-06 15:32:02','2011-08-26 08:46:16',50,'Normal','Succubus'),(64,'Heavy Strike','Slash.',4,14,'Physical','None','Enemy',1,1,'2011-06-09 14:51:55','2011-11-16 14:07:22',50,'Boss','Hortel'),(65,'Activate Spikes','Spikes.',0,0,'Physical','None','Self',0,1,'2011-06-09 14:53:49','2011-11-16 14:27:38',0,'Boss','Hortel'),(66,'Whirlwind','Whirlwind.',2,8,'Physical','None','All Enemies',1,1,'2011-06-09 14:54:57','2011-06-27 15:32:53',35,'Boss','Hortel'),(68,'Multi Thrust','3 times',1,7,'Physical','Multi','Enemy',0,1,'2011-06-09 15:04:54','2011-11-16 14:08:12',35,'Boss','Hortel'),(69,'Roar','Roar..',0,0,'Physical','None','All Allies',0,1,'2011-06-19 03:43:13','2011-06-19 03:43:13',30,'Boss','Jag'),(70,'Claw','Claw at your face.',5,14,'Physical','None','Enemy',1,1,'2011-06-19 03:44:01','2011-10-20 05:19:51',50,'Boss','Jag'),(71,'Juxtapose','Images.',0,0,'Physical','Split','Self',0,1,'2011-06-19 03:45:48','2011-06-19 03:45:48',0,'Boss','Jag'),(72,'Shred','Shred the target.',5,20,'Physical','None','Enemy',0,1,'2011-06-19 03:52:04','2011-10-20 05:20:28',50,'Boss','Jag'),(73,'Poison Gas','Big fart.',1,2,'Physical','Poison','All Enemies',1,1,'2011-06-20 13:12:32','2011-07-21 17:17:47',50,'Normal','Abomination'),(74,'Diseased Punch','uh...',6,14,'Physical','Poison','Enemy',1,1,'2011-06-20 13:25:28','2011-07-21 18:32:05',50,'Normal','Abomination'),(75,'Sharpen Claws','Increase strength and agility.',0,0,'Physical','None','Self',0,1,'2011-06-20 13:55:00','2011-07-21 17:17:58',50,'Normal','Ninja'),(76,'Pinpoint Strike','High Crit and Bleed',4,11,'Physical','None','Enemy',1,1,'2011-06-20 13:59:22','2011-07-21 17:56:47',50,'Normal','Ninja'),(77,'Blood Tap','Suck blood.',0,0,'Magic','None','Enemy',0,1,'2011-06-27 08:45:59','2011-07-21 17:18:10',50,'Normal','Succubus'),(78,'Heal','Heal',-5,-15,'Magic','Heal','Ally',1,1,'2011-06-27 08:48:49','2011-07-21 18:05:00',50,'Normal','Succubus'),(79,'Arcane Shield','Immune to damage.',0,0,'Magic','None','Self',0,1,'2011-06-29 07:16:18','2011-08-30 11:00:31',0,'Boss','Rykor'),(80,'Mass Lingering Fear','Suck blood.',0,0,'Magic','None','All Enemies',0,1,'2011-06-29 07:24:24','2011-08-30 11:00:31',30,'Boss','Rykor'),(81,'Mass Mana Leak','uh oh. hot dog.',0,0,'Magic','None','All Enemies',0,1,'2011-06-29 07:37:04','2011-08-30 11:00:31',50,'Boss','Rykor'),(82,'Summon Demons','Summon.',0,0,'Magic','None','Self',0,1,'2011-06-29 07:42:21','2011-08-30 11:00:31',0,'Boss','Rykor'),(83,'Fireball','Ball of Fire.',5,15,'Magic','Fire','Enemy',1,1,'2011-06-29 07:45:07','2011-10-20 05:32:14',50,'Boss','Rykor'),(84,'Battle Stance Physical','Physical Stance',0,0,'Physical','None','Self',0,1,'2011-06-29 14:36:36','2011-11-16 14:28:27',0,'Boss','Viz'),(85,'Battle Stance Magic','Magic',0,0,'Magic','None','Self',0,1,'2011-06-29 14:37:23','2011-11-16 14:28:39',0,'Boss','Viz'),(86,'Lifesteal','steal hp.',8,17,'Physical','None','Enemy',1,1,'2011-06-29 14:48:45','2011-09-04 14:03:31',50,'Boss','Viz'),(87,'Whirlwind','Spin spin.',5,15,'Physical','None','All Enemies',0,1,'2011-06-29 14:50:16','2011-09-04 13:51:12',50,'Boss','Viz'),(88,'Thunderbolt','hit everyone',2,24,'Magic','None','All Enemies',0,1,'2011-06-29 14:53:00','2011-09-04 13:51:32',50,'Boss','Viz'),(89,'Rejuvenate','heal over time.',0,0,'Magic','Heal','Self',0,1,'2011-06-29 14:54:02','2011-06-29 14:54:02',50,'Boss','Viz'),(90,'Battle Stance Charged','Both Physical / Magic',0,0,'Ranged','None','Self',0,1,'2011-06-30 06:59:52','2011-11-16 14:28:13',0,'Boss','Viz'),(91,'Silence','No magic.',0,0,'Magic','None','Enemy',0,1,'2011-07-22 05:08:26','2011-07-22 05:08:26',30,'Normal','Ghost'),(92,'Mana Leak','Lose mana.',0,0,'Magic','None','Enemy',0,1,'2011-07-22 05:10:24','2011-07-22 05:10:24',40,'Normal','Ghost'),(93,'Love Bite','Quick, sharp zap on target.',5,20,'Magic','None','Enemy',1,1,'2011-08-31 06:27:49','2011-09-01 14:06:21',50,'Boss','Julianna'),(94,'Renew','Heal all friendlies.',5,15,'Magic','Heal','All Allies',1,1,'2011-08-31 06:30:08','2011-10-20 05:20:44',20,'Boss','Julianna'),(95,'Soul Drain','Drain life, heal caster.',4,10,'Magic','Blood','All Enemies',0,1,'2011-08-31 06:31:33','2011-08-31 06:47:50',30,'Boss','Julianna'),(96,'Lover\'s Thrust','Poke with weapon.',3,24,'Physical','None','Enemy',1,1,'2011-08-31 06:34:01','2011-09-01 14:06:51',50,'Boss','Hulio'),(97,'Bond','Share damage between two inseperable lovers.',0,0,'Physical','None','All Allies',0,1,'2011-08-31 06:35:07','2011-08-31 06:35:07',30,'Boss','Hulio'),(98,'Assassinate','Chance to kill. Or miserably fail.',1,2,'Physical','None','Enemy',0,1,'2011-08-31 06:37:58','2011-09-09 07:38:19',35,'Boss','Hulio'),(99,'Ground Pound','Pound the ground. Hits all enemies.',20,30,'Physical','None','All Enemies',1,1,'2011-08-31 09:34:53','2011-09-01 13:20:31',50,'Boss','Rocko'),(100,'Triple Punch','3 massive hits.',20,40,'Physical','None','Enemy',2,1,'2011-08-31 09:38:23','2011-09-01 13:20:46',50,'Boss','Rocko'),(101,'Rock Armor','Increase Armor.',0,0,'Physical','None','Self',0,1,'2011-08-31 09:39:31','2011-08-31 09:39:31',20,'Boss','Rocko'),(102,'Mega Bash','Super Hit',30,50,'Physical','None','All Enemies',3,1,'2011-08-31 09:40:24','2011-09-06 14:54:35',0,'Boss','Rocko'),(103,'Freebie','Defend spell buffed to enemies.',0,0,'Physical','None','Self',0,1,'2011-08-31 10:10:17','2011-08-31 10:11:14',0,'Boss','Rocko'),(104,'Create Illusion','Create illusion from one enemy.',0,0,'Magic','None','Enemy',0,1,'2011-08-31 12:14:19','2011-08-31 12:14:19',0,'Boss','Musou'),(105,'Regenerate','Heals Summons',-15,-30,'Magic','Heal','Ally',1,1,'2011-08-31 12:16:50','2011-09-01 07:35:05',50,'Boss','Musou'),(106,'Enrage','Buffs Strength and Magic',0,0,'Magic','None','All Allies',0,1,'2011-08-31 12:21:24','2011-09-01 07:35:22',25,'Boss','Musou'),(107,'Wind Slash','Slash all targets.',10,20,'Magic','None','All Enemies',1,1,'2011-08-31 12:22:41','2011-09-01 07:41:03',50,'Boss','Musou'),(108,'Blasphemy','Reverse damage and healing',0,0,'Magic','None','All Enemies',0,1,'2011-09-02 11:13:34','2011-09-02 11:13:34',0,'Boss','Cajal'),(109,'Blessing of Light','Group Heal on Enemy.',-40,-55,'Magic','Heal','All Enemies',1,0,'2011-09-02 11:25:44','2011-09-04 07:27:18',0,'Boss','Cajal'),(110,'Sacred Slash','Hit with Dot.',10,20,'Magic','None','Enemy',1,1,'2011-09-02 11:29:51','2011-10-21 02:26:20',50,'Boss','Cajal'),(111,'Holy Protection','Heal and Buff Heals. No Subtype set or else overpowered.',-50,-100,'Magic','Heal','Self',1,0,'2011-09-02 11:38:39','2011-09-04 07:27:42',35,'Boss','Cajal'),(112,'Divine Charge','Drains Percentage of Mana.',10,15,'Magic','None','Enemy',0,1,'2011-09-02 13:21:53','2011-10-21 02:26:11',40,'Boss','Cajal'),(113,'Morph','Changeling.',0,0,'Physical','None','Self',0,1,'2011-12-29 23:11:57','0000-00-00 00:00:00',0,'Normal','Muck'),(114,'Holy Snap','Hits all targets',5,10,'Magic','None','All Enemies',1,1,'2012-01-22 20:29:44','0000-00-00 00:00:00',50,'Normal','Priestess'),(115,'Reverse','Blasphemy on 1 target',0,0,'Magic','None','Enemy',0,1,'2012-01-22 20:29:44','0000-00-00 00:00:00',25,'Normal','Priestess'),(116,'Spike Wall','Ally will reflect damage back to attacker',0,0,'Magic','None','Enemy',0,1,'2012-01-22 20:29:44','0000-00-00 00:00:00',50,'Normal','Priestess');
/*!40000 ALTER TABLE `mob_skills` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-23 21:44:13
