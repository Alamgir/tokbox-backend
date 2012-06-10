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
-- Table structure for table `skills`
--

DROP TABLE IF EXISTS `skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `character_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) NOT NULL,
  `damage_low` int(11) NOT NULL,
  `damage_high` int(11) NOT NULL,
  `damage_type` varchar(45) NOT NULL,
  `spell_target` varchar(45) NOT NULL,
  `level_requirement` int(11) NOT NULL,
  `cost` int(11) NOT NULL,
  `mana_points` int(11) NOT NULL,
  `multiplier` float NOT NULL,
  `special` tinyint(1) NOT NULL,
  `damage_subtype` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `character_skill_id` (`character_id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skills`
--

LOCK TABLES `skills` WRITE;
/*!40000 ALTER TABLE `skills` DISABLE KEYS */;
INSERT INTO `skills` VALUES (17,1,'Bond','Warrior shares half of the damage dealt to an ally. Lasts 8 turns.  Prevents assassination by the Rogue.',0,0,'Physical','All Allies',5,735,13,0,0,'None'),(19,1,'Strong Arm','Warrior increases strength of an ally. Lasts 18 turns.',0,0,'Physical','Ally',17,2499,9,0,0,'None'),(20,1,'Bloody Strike','A large laceration that causes one enemy to bleed health over 3 turns.',7,9,'Physical','Enemy',7,1029,16,0.8,0,'None'),(21,1,'Amor Break','An armor breaking slash that reduces an enemy\'s armor for 12 turns.',11,15,'Physical','Enemy',13,1911,17,0.8,0,'None'),(22,1,'Heavy Strike','An all in attack on one enemy that is heavily reliant on the Warrior\'s strength.',8,13,'Physical','Enemy',1,147,16,1,0,'None'),(23,1,'Spike Aura','All physical attacks on Warrior will reflect damage dealt. (Passive Effect)',0,0,'Physical','All Allies',9,1323,0,0,0,'None'),(24,1,'Shield Aura','An aura that increases the armor of all allies. (Passive Effect)',0,0,'Physical','All Allies',2,294,0,0,0,'None'),(25,1,'Critical Aura','An aura to increase critical strike chance. (Passive Effect)',0,0,'Physical','All Allies',15,2205,0,0,0,'None'),(26,1,'Shield Wall','Invincibility lies in the defense; the possibility of victory in the attack. All allies are immune to damage for 6 turns.',0,0,'Physical','All Allies',19,2793,4,0,1,'None'),(27,1,'Dual Edge','You have a choice. Live or die. Just make sure you take somebody down with you.',1,1,'Physical','Enemy',12,1764,3,0.5,1,'Dual Edge'),(29,2,'Open Wounds','Tears open old wounds. Deals extra damage for each effect. Removes all effects on successful attacks.',15,22,'Physical','Enemy',12,1764,17,0.7,0,'None'),(30,2,'Multi Thrust','A flurry of three blows dealt in quick succession on an enemy.',1,5,'Physical','Enemy',1,147,5,0.1,0,'Multi'),(31,3,'Fireball','A ball of flame that burns one enemy for 3 turns.',12,18,'Magic','Enemy',1,147,14,0.75,0,'Fire'),(33,4,'Heal','Replenish a portion of an ally\'s health.',-10,-15,'Magic','Ally',1,147,20,1.4,0,'Heal'),(34,2,'Pinpoint Strike','Your weapons sink deep into the flesh of an enemy. Chance of a critical strike increased.',12,20,'Physical','Enemy',8,1176,14,0.5,0,'None'),(35,2,'Rust','A ball of armor-melting dust is thrown onto one enemy.',0,0,'Physical','Enemy',2,294,8,0,0,'None'),(36,2,'Bubble Burst','A sharp thrust that pokes a hole in one enemy\'s mana storage.',4,7,'Physical','Enemy',16,2352,10,0.2,0,'None'),(37,2,'Weaken','Crazy powder is thrown at one enemy, reducing strength and agility for 6 turns.',0,0,'Physical','Enemy',14,2058,10,0,0,'None'),(38,2,'Blood Poison','Infected blood seeps under one enemy\'s skin, dealing damage for 7 turns.',0,0,'Physical','Self',10,1470,0,0,0,'Poison'),(39,2,'Quick Poison','Weapon\'s dipped in this poison will increase damage dealt. (Passive Effect)',0,0,'Physical','Self',4,588,0,0,0,'Poison'),(40,2,'Infection','Infected wounds will allow poisons to do more damage.',0,0,'Physical','Enemy',6,882,8,0,0,'None'),(41,2,'Mana Leak','Power leaks from one enemy, reducing total mana over 4 turns.',0,0,'Physical','Enemy',18,2646,6,0,0,'None'),(42,5,'Precision Shot','A well placed arrow that strikes the heart of one enemy.',5,10,'Ranged','Enemy',1,147,12,1,0,'None'),(44,5,'Shadow Step','Doubles the dodging ability of one ally for 12 turns.',0,0,'Ranged','Ally',5,735,8,0,0,'None'),(45,5,'Ethereal','The Archer enters a trance, increasing intelligence but disabling ranged abilities. Lasts 13 turns.',0,0,'Magic','Self',13,1911,14,0,0,'None'),(46,5,'Bandage','Replenish a portion of an ally\'s health points.',-5,-10,'Magic','Ally',2,294,10,1,0,'Heal'),(47,4,'Rejuvenate','Replenish a portion of an ally\'s health per turn. Lasts 6 turns.',0,0,'Magic','Ally',2,294,21,0.25,0,'Heal'),(48,4,'Revivify','Replenish a portion of health for all allies.',-5,-15,'Magic','All Allies',10,1470,20,1,0,'Heal'),(50,4,'Pray','A ball of light that circles one ally. Any healing will increase by half. Lasts 14 turns.',0,0,'Magic','Ally',8,1176,7,0,0,'None'),(51,4,'Enlighten','Increases an ally\'s intelligence. Lasts 15 turns.',0,0,'Magic','Ally',14,2058,13,0,0,'None'),(52,4,'Holy Snap','A spark of holy light that deals damage to all enemies.',10,20,'Magic','All Enemies',16,2352,20,0.5,0,'None'),(53,4,'Mystify','Cause confusion and decrease intelligence. The enemy will hurt itself for 6 turns. ',5,10,'Magic','Enemy',12,1764,15,0.4,0,'None'),(54,4,'Prayer of Shield','The Healer continuously regenerates health points every turn. (Passive Effect)',0,0,'Magic','Self',4,588,0,0,0,'Heal'),(55,4,'Prayer of Power','All allies will have a boost to strength and intelligence. (Passive Effect)',0,0,'Magic','All Allies',18,2646,0,0,0,'None'),(56,5,'Mend Wound','The Archer uses survival skills to remove all negative skill effects from one ally.',0,0,'Magic','Ally',9,1323,8,0,0,'None'),(58,3,'Ice Bolt','A bolt of ice that reduces strength and agility for 12 turns.',9,13,'Magic','Enemy',2,294,9,0.6,0,'None'),(59,3,'Thunderbolt','A shock of electricity that damages all enemies.',5,8,'Magic','All Enemies',9,1323,22,0.4,0,'None'),(60,3,'Death Bolt','A shadowy ball of magic that heals you for dealing damage on an enemy.',8,12,'Magic','Enemy',15,2205,14,0.6,0,'Blood'),(61,3,'Blood Tap','Suck away an enemy\'s health and give it to yourself. Lasts 5 turns.',0,0,'Magic','Enemy',7,1029,15,0,0,'Blood'),(62,3,'Dispel','Removes all non-permanent positive effects from an enemy.',0,0,'Magic','Enemy',5,735,9,0,0,'None'),(63,3,'Lingering Fear','Fear strikes the heart of one enemy, dealing damage over time. Lasts 12 turns.',0,0,'Magic','Enemy',17,2499,10,0,0,'None'),(64,3,'Blood Spikes','Reflect half of the next attack on the Mage back to the enemy. Lasts 19 turns.',0,0,'Magic','Self',19,2793,8,0,0,'None'),(65,3,'Curse of Magic','An eerie shadow lingers. Health is sucked from all enemies who cast a spell. (Passive Effect)',0,0,'Magic','All Enemies',3,441,0,0,0,'None'),(66,3,'Curse of Haunting','The mage will haunt all enemies, reducing all non-primary stats by a small amount. (Passive Effect)',0,0,'Magic','Self',13,1911,0,0,0,'None'),(67,5,'Explosive Shot','Launch an explosive arrow which increases subsequent damage. Lasts 9 turns.',15,20,'Ranged','Enemy',17,2499,15,0.3,0,'None'),(68,5,'Wolf Cry','A cry to the wild. Increases healing on all allies for 13 turns.',0,0,'Magic','All Allies',11,1617,20,0,0,'None'),(70,3,'Redemption','The greatest honor in battle is achieved through sacrifice. Only this time your life force revives allies.',0,0,'Magic','All Allies',15,2205,4,0,1,'Sacrifice'),(71,1,'Attack','A basic combat manuever with no mana cost.',5,8,'Physical','Enemy',1,147,0,0.4,0,'Base'),(72,1,'Defend','Damage dealt to you will be reduced for 8 turns.',0,0,'Physical','Self',1,147,0,0,0,'Base'),(73,2,'Attack','A basic combat manuever with no mana cost.',5,8,'Physical','Enemy',1,147,0,0.4,0,'Base'),(74,3,'Attack','A basic combat manuever with no mana cost.',5,8,'Magic','Enemy',1,147,0,0.4,0,'Base'),(75,4,'Attack','A basic combat manuever with no mana cost.',5,8,'Magic','Enemy',1,147,0,0.4,0,'Base'),(76,5,'Attack','A basic combat manuever with no mana cost.',5,8,'Ranged','Enemy',1,147,0,0.4,0,'Base'),(77,4,'Prayer of Focus','The Healer surges with magical energy. Mana regeneration is increased for all allies. (Passive Effect)',0,0,'Magic','All Allies',9,1323,0,0,0,'None'),(78,2,'Blood Poison Effect','Blood Poison Effect.',0,0,'Physical','Enemy',10,1470,0,0,0,'Poison'),(79,2,'Quick Poison Effect','Quick Poison Effect.',0,0,'Physical','Enemy',4,588,0,0,0,'Poison'),(80,2,'Defend','Damage dealt to you will be reduced for 8 turns.',0,0,'Physical','Self',1,147,0,0,0,'Base'),(81,3,'Defend','Damage dealt to you will be reduced for 8 turns.',0,0,'Physical','Self',1,147,0,0,0,'Base'),(82,4,'Defend','Damage dealt to you will be reduced for 8 turns.',0,0,'Physical','Self',1,147,0,0,0,'Base'),(83,5,'Defend','Damage dealt to you will be reduced for 8 turns.',0,0,'Physical','Self',1,147,0,0,0,'Base'),(84,2,'Assassinate','as·sas·si·nate -verb: To murder premeditatedly and treacherously. Instantly kills targets under half health.',30,45,'Physical','Enemy',19,2793,4,1.5,1,'None'),(85,2,'Silence','Enemies wil not be able to cast any magical spells for 6 turns. This skill only works in the Colosseum.',0,0,'Physical','Enemy',11,1617,1,0,1,'None'),(86,2,'Shatter','Shattering the enemy\'s defense shatters their morale, will, and strength. Lasts 6 turns.',20,25,'Physical','Enemy',7,1029,1,0.1,1,'None'),(88,3,'Feedback','You provoked me, you unleashed the beast! All enemy spells are reflected. Lasts 6 turns.',0,0,'Magic','All Enemies',11,1617,1,0,1,'None'),(89,3,'Arcane Bolt','An eerie bolt that deals damage and silences an enemy for 6 turns. This skill only works in the Colosseum.',30,40,'Magic','Enemy',7,1029,2,0.7,1,'None'),(90,5,'Power Shot','Don\'t worry, this won\'t hurt a bit...',15,20,'Ranged','Enemy',9,1323,1,1,1,'None'),(91,5,'Rain Dance','Have you seen it? The eternal Dove of healing! All allies will regenerate a portion of their health over 6 turns.',0,0,'Magic','All Allies',6,882,2,0,1,'Heal'),(92,4,'Godhand','O Gods of Idonia, lend me your power, help me heal in the face of battle! Completely restores health of an ally.',-100000,-100000,'Magic','Ally',5,735,3,0,1,'Heal'),(93,4,'Empower','Focus on salvation as well as physical healing. Restores half of an ally\'s mana.',0,0,'Magic','Ally',17,2499,2,0,1,'None'),(94,4,'Blasphemy','All Enemies will have healing spells deal damage instead.',0,0,'Magic','All Enemies',19,2793,4,0,1,'None'),(95,1,'Berserk','Blind rage increases strength and damage taken. Heals for a small amount.',-20,-30,'Magic','Self',14,2058,12,0.2,0,'Heal'),(97,5,'Rain of Arrows','A shadow descends upon the land as the sky is filled with arrows.',1,10,'Ranged','All Enemies',19,2793,4,0.1,1,'Timer'),(99,5,'Piercing Volley','A volley that pierces all enemies, causing them to bleed for 3 turns.',7,12,'Ranged','All Enemies',7,1029,27,0.4,0,'None'),(100,5,'Summon Dove','A blessing from a dove increases healing for all allies. (Passive Effect)',0,0,'Magic','All Allies',4,588,0,0,0,'Summon'),(101,5,'Summon Falcon','An agile falcon will boost agility for all allies. (Passive Effect)',0,0,'Ranged','All Allies',15,2205,0,0,0,'Summon'),(102,1,'Whirlwind','They sowed the wind and now they are going to reap the wrath.',4,8,'Physical','All Enemies',11,1617,22,0.5,0,'None'),(103,1,'Rally','We must all band together, or assuredly we shall all hang separately.',0,0,'Physical','All Allies',6,882,1,0,1,'Rally');
/*!40000 ALTER TABLE `skills` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_skills_on_insert AFTER INSERT on skills FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 2 */;;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_skills_on_update AFTER UPDATE on skills FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 2 */;;
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
