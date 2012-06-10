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
-- Current Database: `idonia`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `idonia` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `idonia`;

--
-- Table structure for table `accessories`
--

DROP TABLE IF EXISTS `accessories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accessories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `character_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `accessory_type` varchar(255) DEFAULT NULL,
  `vitality` int(11) DEFAULT NULL,
  `will` int(11) DEFAULT NULL,
  `strength` int(11) DEFAULT NULL,
  `agility` int(11) DEFAULT NULL,
  `intelligence` int(11) DEFAULT NULL,
  `armor` decimal(10,0) DEFAULT NULL,
  `dodge` decimal(10,0) DEFAULT NULL,
  `physical_crit` decimal(10,0) DEFAULT NULL,
  `level_requirement` int(11) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `damage_high` int(11) DEFAULT NULL,
  `damage_low` int(11) DEFAULT NULL,
  `spell_crit` decimal(10,0) DEFAULT NULL,
  `generated` tinyint(1) DEFAULT '0',
  `rarity` varchar(255) DEFAULT NULL,
  `tier` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=844 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accessories`
--

LOCK TABLES `accessories` WRITE;
/*!40000 ALTER TABLE `accessories` DISABLE KEYS */;
INSERT INTO `accessories` VALUES (1,1,'Plate Armor','A basic piece of chainmail.','Armor',3,0,2,0,0,8,0,0,1,0,'2010-11-16 14:52:56','2011-04-14 08:53:19',0,0,0,0,'Common',0),(2,2,'Assassin Leather','Torn and worn, the material does not protect very well in battle.','Armor',1,0,4,0,0,4,2,0,1,0,'2010-11-16 14:55:26','2011-11-16 11:43:10',0,0,0,0,'Common',0),(3,3,'Apprentice Cloth','A used piece of clothing passed down through many generations.','Armor',0,2,0,0,2,3,0,0,1,0,'2010-11-16 14:57:50','2011-06-21 09:15:42',0,0,1,0,'Common',0),(4,4,'Healer\'s Garb','A ripped shirt. One might as well not wear anything at all.','Armor',0,4,0,0,1,3,0,0,1,0,'2010-11-16 14:59:59','2011-06-21 09:16:04',0,0,0,0,'Common',0),(7,3,'Broken Staff','A piece of rotted driftwood, it doesn\'t hold magic very well.','Weapon',0,0,0,0,2,0,0,0,1,0,'2010-11-16 15:02:51','2011-11-16 11:46:55',5,1,0,0,'Common',0),(8,4,'Healer\'s Wand','The standard wand issued by the priests of Eir, it\'s scratched and used.','Weapon',0,1,0,0,1,0,0,0,1,0,'2010-11-16 15:03:20','2011-11-16 11:48:04',3,2,0,0,'Common',0),(11,3,'Orb','A magical trinket that helps in casting spells.','Accessory',0,0,0,0,1,0,0,0,1,0,'2010-11-16 15:07:22','2011-11-16 11:46:00',0,0,0,0,'Common',0),(12,4,'Necklace','A ring in the shape of an O.','Accessory',0,1,0,0,0,0,0,0,1,0,'2010-11-16 15:08:50','2011-04-27 13:15:52',0,0,0,0,'Common',0),(13,5,'Ranger Armor','The standard issue getup from the Urudinian Archery Forces.','Armor',0,0,0,4,0,5,0,1,1,0,'2010-12-07 14:14:40','2011-11-16 11:51:32',0,0,0,0,'Common',0),(14,5,'Cheap Bow','A bow created out of twigs and vines, it is quite ineffective in battle.','Weapon',0,0,0,2,0,0,0,0,1,0,'2010-12-07 14:15:47','2011-11-16 11:51:05',6,2,0,0,'Common',0),(15,5,'Eyepatch','A ring to signify a man\'s love for his little rooster.','Accessory',0,0,0,1,0,0,0,0,1,0,'2010-12-07 14:17:32','2011-04-27 13:16:27',0,0,0,0,'Common',0),(17,2,'Plain Daggers','Plain daggers that you found in a cesspool. They are rusted, but this is what makes them deadly.','Weapon',0,0,1,1,0,0,0,0,1,0,'2011-01-20 16:37:52','2011-11-16 11:43:49',6,4,0,0,'Common',0),(18,5,'Survivalist\'s Longbow','Forged from the fibers of many different Urudinian trees, it provides a magical boost to its user.','Weapon',1,1,0,1,4,0,0,0,3,0,'2011-01-20 16:43:40','2011-11-16 13:58:03',8,5,0,0,'Common',1),(437,1,'Training Longsword','A wooden sword given to those entering knighthood. It is light and agile, but breaks easily.','Weapon',1,0,1,0,0,0,0,0,1,0,'2011-03-19 09:58:13','2011-07-20 11:08:10',4,3,0,0,'Common',0),(438,1,'Ring','A shiny stone.','Accessory',0,0,1,0,0,0,0,0,1,0,'2011-03-19 09:59:43','2011-04-27 13:16:04',0,0,0,0,'Common',0),(439,2,'Earrings','A pretty necklace.','Accessory',0,0,1,0,0,0,0,0,1,0,'2011-03-19 10:00:41','2011-04-27 13:16:16',0,0,0,0,'Common',0),(440,1,'Titan\'s Maul','A maul forged to protect the people of Ianthia. The wielder will gain superior defense and health.','Weapon',5,1,1,0,0,0,0,0,3,0,'2011-04-14 05:43:54','2011-09-09 11:29:57',6,5,0,0,'Common',1),(441,1,'Titan\'s Maul','A hammer forged to protect the people of Iantha. It provides the wielder with superior defense and vigor.','Weapon',7,1,2,0,0,1,0,0,10,1500,'2011-04-14 05:48:14','2011-07-20 11:13:58',9,7,0,0,'Unique',2),(442,1,'Titan\'s Maul','A hammer forged to protect the people of Iantha. It provides the wielder with superior defense and vigor.','Weapon',10,2,3,0,0,2,0,0,20,3200,'2011-04-14 05:49:28','2011-06-20 08:22:17',12,8,0,0,'Epic',3),(443,1,'Justice','Forged only for the mightiest warriors, Justice will grant strength to its wielder.','Weapon',1,0,5,1,0,0,0,0,3,0,'2011-04-14 05:55:05','2011-06-20 08:13:47',9,6,0,0,'Common',1),(444,1,'Blood Reaper','A warlord\'s weapon of choice. The wielder will sacrifice defense for all out offense.','Weapon',0,0,2,2,0,0,0,3,3,0,'2011-04-14 06:04:18','2011-06-20 08:14:08',12,4,0,0,'Common',1),(445,0,'Eyepatch','Eyepatch','Accessory',2,0,0,0,0,0,0,0,1,1500,'2011-04-27 08:09:06','2011-08-18 13:29:10',0,0,0,1,'Unique',0),(446,0,'Ring','Ring','Accessory',0,0,1,0,0,0,0,0,1,25,'2011-04-27 11:04:05','2011-04-27 11:04:05',0,0,0,1,'Common',0),(452,0,'Necklace','Necklace','Accessory',0,1,0,0,1,0,0,0,2,55,'2011-04-28 07:31:38','2011-04-28 07:31:38',0,0,0,1,'Common',0),(455,0,'Earrings','Earrings','Accessory',0,1,1,0,0,0,0,0,2,35,'2011-04-28 07:46:57','2011-04-28 07:46:57',0,0,0,1,'Common',0),(457,0,'Eyepatch','Eyepatch','Accessory',0,0,0,1,1,0,0,0,2,50,'2011-05-02 06:15:54','2011-05-02 06:15:54',0,0,0,1,'Common',0),(458,0,'Earrings','Earrings','Accessory',0,0,0,1,0,1,0,0,2,30,'2011-05-02 06:30:38','2011-05-02 06:30:38',0,0,0,1,'Common',0),(459,0,'Orb','Orb','Accessory',0,0,0,0,0,1,0,0,2,22,'2011-05-03 11:22:21','2011-05-03 11:22:21',0,0,1,1,'Common',0),(460,0,'Eyepatch','Eyepatch','Accessory',0,2,0,0,0,0,0,1,3,30,'2011-05-03 11:37:03','2011-05-03 11:37:03',0,0,0,1,'Common',0),(461,0,'Eyepatch','Eyepatch','Accessory',2,0,0,0,2,0,0,1,3,1500,'2011-05-03 11:50:47','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(462,0,'Earrings','Earrings','Accessory',2,0,2,0,0,0,0,0,2,1500,'2011-05-03 11:51:22','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(463,0,'Orb','Orb','Accessory',1,1,0,0,0,0,0,0,3,55,'2011-05-03 11:53:11','2011-05-03 11:53:11',0,0,1,1,'Common',0),(464,0,'Orb','Orb','Accessory',1,0,0,0,1,0,0,0,3,57,'2011-05-03 11:55:03','2011-05-03 11:55:03',0,0,0,1,'Common',0),(465,0,'Orb','Orb','Accessory',0,0,0,0,1,0,0,0,1,30,'2011-05-03 11:59:28','2011-05-03 11:59:28',0,0,0,1,'Common',0),(466,0,'Earrings','Earrings','Accessory',0,0,0,0,0,0,0,1,1,9,'2011-05-04 07:17:29','2011-05-04 07:17:29',0,0,0,1,'Common',0),(467,0,'Ring','Ring','Accessory',0,0,1,0,0,0,0,0,1,50,'2011-05-05 12:23:52','2011-05-05 12:23:52',0,0,0,1,'Common',0),(471,0,'Orb','Orb','Accessory',0,2,0,0,7,0,1,0,6,1500,'2011-05-06 12:05:28','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(473,0,'Earrings','Earrings','Accessory',0,0,0,1,0,0,0,0,1,25,'2011-05-10 05:18:50','2011-05-10 05:18:50',0,0,0,1,'Common',0),(474,0,'Ring','Ring','Accessory',0,0,1,1,0,0,0,0,2,42,'2011-05-10 05:21:15','2011-05-10 05:21:15',0,0,0,1,'Common',0),(478,0,'Necklace','Necklace','Accessory',2,2,0,0,2,0,0,0,5,1500,'2011-05-13 17:31:52','2011-06-21 12:19:57',0,0,3,1,'Unique',0),(479,0,'Orb','Orb','Accessory',1,1,0,0,1,1,0,0,6,100,'2011-05-15 04:52:55','2011-05-15 04:52:55',0,0,1,1,'Common',0),(480,0,'Necklace','Necklace','Accessory',2,1,0,0,1,0,0,0,5,110,'2011-05-15 05:21:47','2011-05-15 05:21:47',0,0,1,1,'Common',0),(482,2,'White Silver Scimitars','Legendary swords used by pirates of Paruga. Ideal for a fighter who prefers a balanced fighting style.','Weapon',2,0,2,2,0,0,0,0,3,0,'2011-05-15 11:49:25','2011-10-24 05:12:09',12,9,0,0,'Common',1),(483,2,'Shivs of the Goddess','These weapons were forged from white silver and poison. Grants strength and advanced poison.','Weapon',0,0,6,0,0,0,0,1,3,0,'2011-05-15 12:02:57','2011-11-16 11:44:28',8,7,0,0,'Common',1),(484,2,'Blades of Atla','Issued by the King himself, these blades are held by those most fit to be called Protectors of Idonia. Defense is key.','Weapon',4,0,3,0,0,0,0,0,3,0,'2011-05-15 12:08:31','2011-10-24 05:24:45',10,8,0,0,'Common',1),(485,3,'Staff of Fire','A staff crafted with holy fire. With proper training, enemies will crumble to the Mage\'s fire spells.','Weapon',0,0,0,0,6,0,0,0,3,0,'2011-05-15 12:15:08','2011-06-20 08:31:31',15,3,1,0,'Common',1),(486,3,'Staff of Mana','A staff infused with a mana stone, allowing the wielder to last longer in battle.','Weapon',1,4,0,0,2,0,0,0,3,0,'2011-05-15 12:17:29','2011-06-20 08:30:50',9,5,0,0,'Common',1),(487,3,'Staff of the Void','A dark aura hovers over this mystical staff, protecting its wielder from incoming threats.','Weapon',5,1,0,0,1,0,0,0,3,0,'2011-05-15 12:20:35','2011-05-15 12:20:35',8,4,0,0,'Common',1),(488,4,'Wand of Healing','A wand bound with blue sapphires passed down through Eiran history, it specializes in healing spells.','Weapon',0,3,0,0,4,0,0,0,3,0,'2011-05-15 12:24:34','2011-11-16 11:48:27',5,4,0,0,'Common',1),(489,4,'Wand of Temperament','A wand filled with rage that specializes in the offenses.','Weapon',0,0,0,0,7,0,0,0,3,0,'2011-05-15 12:27:22','2011-11-16 11:50:19',7,5,0,0,'Common',1),(490,4,'Wand of Protection','As this wand is upgraded and the symbolic eggs hatch, it adds to armor significantly.','Weapon',5,1,0,0,0,0,0,0,3,0,'2011-05-15 12:28:48','2011-11-16 11:49:30',8,5,0,0,'Common',1),(491,5,'Summoner\'s Shortbow','Being light and small due to its construction from hollow bones, this bow increases agility.','Weapon',2,0,0,3,2,0,0,0,3,0,'2011-05-15 13:00:36','2011-11-16 13:56:15',9,7,0,0,'Common',1),(492,5,'Marksman\'s Recurve Bow','Made out of the fibers of the Brookden Oak tree, it deals great damage.','Weapon',0,0,0,7,0,0,0,1,3,0,'2011-05-15 13:02:06','2011-11-16 13:55:06',11,6,0,0,'Common',1),(496,0,'Earrings','Earrings','Accessory',0,0,2,2,0,0,1,3,6,1500,'2011-05-16 05:25:48','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(497,0,'Eyepatch','Eyepatch','Accessory',2,0,0,1,1,0,0,1,5,100,'2011-05-16 05:31:45','2011-05-16 05:31:45',0,0,0,1,'Common',0),(498,0,'Eyepatch','Eyepatch','Accessory',1,1,0,1,0,0,0,1,5,80,'2011-05-16 05:35:49','2011-05-16 05:35:49',0,0,0,1,'Common',0),(499,0,'Ring','Ring','Accessory',0,0,3,1,0,1,0,0,5,100,'2011-05-16 05:41:07','2011-05-16 05:41:07',0,0,0,1,'Common',0),(505,0,'Necklace','Necklace','Accessory',1,1,0,0,1,0,0,0,3,75,'2011-05-17 13:41:48','2011-05-17 13:41:48',0,0,0,1,'Common',0),(512,0,'Necklace','Necklace','Accessory',0,2,0,0,2,1,1,0,5,1500,'2011-05-22 12:46:26','2011-06-21 12:19:57',0,0,1,1,'Unique',0),(513,0,'Ring','Ring','Accessory',1,0,0,1,0,0,1,1,5,62,'2011-05-22 12:49:54','2011-05-22 12:49:54',0,0,0,1,'Common',0),(514,0,'Ring','Ring','Accessory',2,0,1,2,0,0,0,0,5,110,'2011-05-22 13:22:02','2011-05-22 13:22:02',0,0,0,1,'Common',0),(515,1,'Blood Reaper','A warlord\'s weapon of choice. The wielder will sacrifice defense for all out offense.','Weapon',1,0,5,2,0,0,0,5,10,1500,'2011-05-23 09:02:46','2011-06-21 12:19:57',15,6,0,0,'Unique',2),(516,1,'Blood Reaper','A warlord\'s weapon of choice. The wielder will sacrifice defense for all out offense.','Weapon',1,0,8,5,0,0,0,7,15,3200,'2011-05-23 09:04:47','2011-06-21 12:20:21',17,8,0,0,'Epic',3),(517,1,'Justice','Forged only for the mightiest warriors, Justice will grant strength to its wielder.','Weapon',1,0,7,3,0,1,0,2,10,1500,'2011-05-23 09:07:26','2011-06-21 12:19:57',12,8,0,0,'Unique',2),(518,1,'Justice','Forged only for the mightiest warriors, Justice will grant strength to its wielder.','Weapon',2,0,9,5,0,2,0,2,15,3200,'2011-05-23 09:13:05','2011-06-21 12:20:21',14,9,0,0,'Epic',3),(519,2,'Blades of Atla','Issued by the King himself, these blades are held by those most fit to be called Protectors of Idonia. Defense is key.','Weapon',6,0,4,0,0,0,1,0,10,1500,'2011-05-23 09:16:03','2011-10-24 05:24:45',12,9,0,0,'Unique',2),(520,2,'Blades of Atla','Issued by the King himself, these blades are held by those most fit to be called Protectors of Idonia. Defense is key.','Weapon',8,1,6,0,0,0,1,1,15,3200,'2011-05-23 09:24:43','2011-10-24 05:24:45',14,10,0,0,'Epic',3),(521,2,'White Silver Scimitars','Legendary swords used by pirates of Paruga. Ideal for a fighter who prefers a balanced fighting style.','Weapon',2,1,4,3,0,0,0,0,10,1500,'2011-05-23 09:25:58','2011-10-24 05:12:09',14,9,0,0,'Unique',2),(522,2,'White Silver Scimitars','Legendary swords used by pirates of Paruga. Ideal for a fighter who prefers a balanced fighting style.','Weapon',3,3,6,4,0,1,1,1,15,3200,'2011-05-23 09:28:23','2011-10-24 05:12:09',15,9,0,0,'Epic',3),(523,2,'Shivs of the Goddess','These weapons were forged from white silver and poison. Grants strength and advanced poison.','Weapon',0,0,8,2,0,0,0,2,10,1500,'2011-05-23 09:31:47','2011-11-16 11:45:01',10,8,0,0,'Unique',2),(524,2,'Shivs of the Goddess','These weapons were forged from white silver and poison. Grants strength and advanced poison.','Weapon',0,0,10,4,0,0,0,3,15,3200,'2011-05-23 09:32:45','2011-11-16 11:45:17',14,10,0,0,'Epic',3),(525,3,'Staff of Mana','A staff infused with a mana stone, allowing the wielder to last longer in battle.','Weapon',2,6,0,0,3,0,0,0,10,1500,'2011-05-23 09:34:30','2011-09-02 15:04:58',11,7,0,0,'Unique',2),(526,3,'Staff of Mana','A staff infused with a mana stone, allowing the wielder to last longer in battle.','Weapon',5,8,0,0,5,0,0,0,15,3200,'2011-05-23 09:35:32','2011-09-02 15:05:39',12,9,1,0,'Epic',3),(527,3,'Staff of Fire','A staff crafted with holy fire. With proper training, enemies will crumble to the Mage\'s fire spells.','Weapon',1,1,0,0,8,0,0,0,10,1500,'2011-05-23 09:39:37','2011-06-21 12:19:57',16,4,2,0,'Unique',2),(528,3,'Staff of Fire','A staff crafted with holy fire. With proper training, enemies will crumble to the Mage\'s fire spells.','Weapon',2,2,0,0,11,0,0,0,15,3200,'2011-05-23 09:40:33','2011-07-23 17:46:32',17,5,3,0,'Epic',3),(529,3,'Staff of the Void','A dark aura hovers over this mystical staff, protecting its wielder from incoming threats.','Weapon',7,2,0,0,2,0,0,0,10,1500,'2011-05-23 09:45:02','2011-06-21 12:19:57',11,6,0,0,'Unique',2),(530,3,'Staff of the Void','A dark aura hovers over this mystical staff, protecting its wielder from incoming threats.','Weapon',10,3,0,0,4,1,0,0,15,3200,'2011-05-23 09:46:21','2011-06-21 12:20:21',13,8,1,0,'Epic',3),(532,0,'Eyepatch','Eyepatch','Accessory',0,0,0,4,2,0,0,0,3,1500,'2011-05-24 12:50:55','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(533,0,'Earrings','Earrings','Accessory',3,2,2,2,0,0,0,0,5,1500,'2011-05-24 12:51:05','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(535,0,'Orb','Orb','Accessory',0,0,0,0,1,0,0,0,1,60,'2011-05-25 05:18:22','2011-05-25 05:18:22',0,0,0,1,'Common',0),(536,0,'Necklace','Necklace','Accessory',1,0,0,0,0,0,0,0,1,20,'2011-05-27 09:42:34','2011-05-27 09:42:34',0,0,0,1,'Common',0),(537,0,'Earrings','Earrings','Accessory',1,0,0,0,0,0,0,1,2,29,'2011-05-27 12:59:30','2011-05-27 12:59:30',0,0,0,1,'Common',0),(538,0,'Necklace','Necklace','Accessory',0,1,0,0,1,0,0,0,2,55,'2011-05-30 11:44:55','2011-05-30 11:44:55',0,0,0,1,'Common',0),(539,0,'Earrings','Earrings','Accessory',1,1,2,1,0,0,0,0,5,105,'2011-06-02 08:20:30','2011-06-02 08:20:30',0,0,0,1,'Common',0),(540,0,'Eyepatch','Eyepatch','Accessory',9,2,0,5,1,1,0,1,20,390,'2011-06-02 09:35:46','2011-06-02 09:35:46',0,0,0,1,'Common',0),(541,0,'Eyepatch','Eyepatch','Accessory',7,0,0,8,2,0,0,2,20,450,'2011-06-02 09:36:33','2011-06-02 09:36:33',0,0,0,1,'Common',0),(542,0,'Eyepatch','Eyepatch','Accessory',2,4,0,7,5,0,0,1,20,410,'2011-06-02 10:19:09','2011-06-02 10:19:09',0,0,0,1,'Common',0),(543,0,'Ring','Ring','Accessory',9,2,10,3,0,1,1,1,20,1500,'2011-06-02 12:27:17','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(544,0,'Necklace','Necklace','Accessory',0,0,0,0,3,0,0,0,2,1500,'2011-06-05 05:02:54','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(545,0,'Earrings','Earrings','Accessory',1,0,0,0,0,0,0,0,1,20,'2011-06-06 16:53:21','2011-06-06 16:53:21',0,0,0,1,'Common',0),(569,0,'Earrings','Earrings','Accessory',3,0,0,2,0,1,1,0,5,1500,'2011-06-09 17:19:35','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(570,0,'Earrings','Earrings','Accessory',3,0,2,0,0,0,1,0,5,1500,'2011-06-09 17:19:58','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(571,0,'Necklace','Necklace','Accessory',0,0,0,0,0,0,0,0,0,0,'2011-06-09 21:16:36','2011-06-09 21:16:36',0,0,0,1,'Common',0),(572,0,'Orb','Orb','Accessory',2,3,0,0,2,0,1,0,5,1500,'2011-06-09 21:26:13','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(573,0,'Necklace','Necklace','Accessory',0,0,0,0,0,0,0,0,0,0,'2011-06-11 10:12:19','2011-06-11 10:12:19',0,0,0,1,'Common',0),(574,0,'Orb','Orb','Accessory',0,0,0,0,0,0,0,0,0,0,'2011-06-11 10:23:06','2011-06-11 10:23:06',0,0,0,1,'Common',0),(575,0,'Necklace','Necklace','Accessory',0,0,0,0,0,0,0,0,0,1500,'2011-06-11 13:00:52','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(576,0,'Earrings','Earrings','Accessory',0,0,0,0,0,0,0,0,0,0,'2011-06-11 13:01:58','2011-06-11 13:01:58',0,0,0,1,'Common',0),(577,0,'Earrings','Earrings','Accessory',0,0,0,0,0,0,0,0,0,0,'2011-06-11 13:42:58','2011-06-11 13:42:58',0,0,0,1,'Common',0),(586,0,'Eyepatch','Eyepatch','Accessory',2,2,0,2,4,0,0,1,8,1500,'2011-06-19 17:28:20','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(587,0,'Necklace','Necklace','Accessory',2,0,0,0,5,0,1,0,5,1500,'2011-06-20 04:25:00','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(588,0,'Ring','Ring','Accessory',6,2,2,2,0,1,0,0,8,1500,'2011-06-20 04:34:11','2011-06-21 12:19:57',0,0,0,1,'Unique',0),(589,4,'Wand of Protection','As this wand is upgraded and the symbolic eggs hatch, it adds to armor significantly.','Weapon',7,3,0,0,1,0,0,0,10,1500,'2011-06-20 07:59:47','2011-11-16 11:49:44',10,7,0,0,'Unique',2),(590,4,'Wand of Protection','As this wand is upgraded and the symbolic eggs hatch, it adds to armor significantly.','Weapon',9,5,0,0,2,1,0,0,15,3200,'2011-06-20 08:01:04','2011-11-16 11:49:57',12,9,1,0,'Epic',3),(591,4,'Wand of Temperament','A wand filled with rage that specializes in the offenses.','Weapon',1,2,0,0,9,0,0,0,10,1500,'2011-06-20 08:02:19','2011-11-16 11:50:31',10,6,1,0,'Unique',2),(592,4,'Wand of Temperament','A wand filled with rage that specializes in the offenses.','Weapon',2,4,0,0,11,0,0,0,15,3200,'2011-06-20 08:03:19','2011-11-16 11:50:41',13,7,1,0,'Epic',3),(593,4,'Wand of Healing','A wand bound with blue sapphires passed down through Eiran history, it specializes in healing spells.','Weapon',0,6,0,0,5,0,0,0,10,1500,'2011-06-20 08:04:51','2011-11-16 11:48:47',7,6,0,0,'Unique',2),(594,4,'Wand of Healing','A wand bound with blue sapphires passed down through Eiran history, it specializes in healing spells.','Weapon',2,9,0,0,6,0,0,0,15,3200,'2011-06-20 08:05:46','2011-11-16 11:49:01',9,8,1,0,'Epic',3),(595,5,'Marksman\'s Recurve Bow','Made out of the fibers of the Brookden Oak tree, it deals great damage.','Weapon',0,0,0,10,0,0,0,2,10,1500,'2011-06-20 08:41:52','2011-11-16 13:55:18',13,7,0,0,'Unique',2),(596,5,'Marksman\'s Recurve Bow','Made out of the fibers of the Brookden Oak tree, it deals great damage.','Weapon',1,0,0,14,0,0,0,4,15,3200,'2011-06-20 08:42:55','2011-11-16 13:55:27',14,8,0,0,'Epic',3),(597,5,'Summoner\'s Shortbow','Being light and small due to its construction from hollow bones, this bow increases agility.','Weapon',3,1,0,5,4,0,0,0,10,1500,'2011-06-20 08:44:21','2011-11-16 13:56:30',10,8,0,0,'Unique',2),(598,5,'Summoner\'s Shortbow','Being light and small due to its construction from hollow bones, this bow increases agility.','Weapon',4,3,0,7,6,0,1,0,15,3200,'2011-06-20 08:45:41','2011-11-16 13:56:41',11,9,0,0,'Epic',3),(599,5,'Survivalist\'s Longbow','Forged from the fibers of many different Urudinian trees, it provides a magical boost to its user.','Weapon',1,2,0,1,7,0,0,0,10,1500,'2011-06-20 08:47:27','2011-11-16 13:58:25',9,7,1,0,'Unique',2),(600,5,'Survivalist\'s Longbow','Forged from the fibers of many different Urudinian trees, it provides a magical boost to its user.','Weapon',2,3,0,2,10,0,1,0,15,3200,'2011-06-20 08:48:20','2011-11-16 13:58:52',12,8,2,0,'Epic',3),(601,0,'Eyepatch','Eyepatch','Accessory',0,0,0,0,0,0,0,0,0,0,'2011-06-21 07:25:30','2011-06-21 07:25:30',0,0,0,1,'Common',0),(602,0,'Eyepatch','Eyepatch','Accessory',0,0,0,0,0,0,0,0,0,0,'2011-06-21 08:07:22','2011-06-21 08:07:22',0,0,0,1,'Common',0),(603,0,'Ring','Ring','Accessory',0,0,0,0,0,0,0,0,0,0,'2011-06-21 08:33:49','2011-06-21 08:33:49',0,0,0,1,'Common',0),(611,0,'Earrings','Earrings','Accessory',0,0,2,1,0,1,0,0,4,161,'2011-06-22 04:15:21','2011-06-22 04:15:21',0,0,0,1,'Common',0),(619,0,'Earrings','Earrings','Accessory',0,1,1,2,0,0,0,1,5,188,'2011-06-22 12:07:45','2011-06-22 12:07:45',0,0,0,1,'Common',0),(620,0,'Necklace','Necklace','Accessory',0,0,0,0,2,0,0,0,2,85,'2011-06-22 12:34:24','2011-06-22 12:34:24',0,0,2,1,'Epic',0),(621,0,'Eyepatch','Eyepatch','Accessory',4,3,0,16,4,1,0,1,20,910,'2011-06-22 13:06:30','2011-06-22 13:06:30',0,0,0,1,'Unique',0),(622,0,'Orb','Orb','Accessory',2,5,0,0,5,1,1,0,20,775,'2011-06-22 13:29:47','2011-06-22 13:29:47',0,0,4,1,'Common',0),(624,0,'Earrings','Earrings','Accessory',3,3,7,6,0,1,0,0,20,841,'2011-06-23 07:52:30','2011-06-23 07:52:30',0,0,0,1,'Common',0),(625,0,'Orb','Orb','Accessory',6,2,0,0,7,1,2,0,20,815,'2011-06-23 09:28:13','2011-06-23 09:28:13',0,0,0,1,'Common',0),(626,0,'Earrings','Earrings','Accessory',5,3,6,13,0,1,0,0,20,861,'2011-06-23 10:24:49','2011-06-23 10:24:49',0,0,0,1,'Unique',0),(627,0,'Orb','Orb','Accessory',6,9,0,0,5,1,1,0,20,750,'2011-06-23 10:24:54','2011-06-23 10:24:54',0,0,1,1,'Unique',0),(629,0,'Earrings','Earrings','Accessory',5,3,6,13,0,1,0,0,20,861,'2011-06-23 10:27:35','2011-06-23 10:27:35',0,0,0,1,'Unique',0),(630,0,'Ring','Ring','Accessory',1,0,1,0,0,0,0,0,2,100,'2011-06-23 13:27:41','2011-06-23 13:27:41',0,0,0,1,'Common',0),(631,0,'Eyepatch','Eyepatch','Accessory',3,2,0,7,0,0,1,0,8,350,'2011-06-23 14:15:42','2011-06-23 14:15:42',0,0,0,1,'Unique',0),(632,0,'Earrings','Earrings','Accessory',2,3,4,2,0,0,1,0,8,291,'2011-06-23 14:17:12','2011-06-23 14:17:12',0,0,0,1,'Unique',0),(633,0,'Necklace','Necklace','Accessory',3,3,0,0,5,0,0,0,8,385,'2011-06-23 14:21:01','2011-06-23 14:21:01',0,0,1,1,'Unique',0),(634,0,'Orb','Orb','Accessory',2,5,0,0,8,0,1,0,20,880,'2011-06-24 10:09:34','2011-06-24 10:09:34',0,0,2,1,'Common',0),(635,0,'Eyepatch','Eyepatch','Accessory',2,0,0,0,0,0,0,0,1,40,'2011-06-26 03:49:22','2011-06-26 03:49:22',0,0,0,1,'Unique',0),(637,0,'Ring','Ring','Accessory',0,0,1,0,0,0,0,0,1,50,'2011-06-27 08:12:25','2011-06-27 08:12:25',0,0,0,1,'Common',0),(638,0,'Ring','Ring','Accessory',0,0,0,0,0,1,0,0,2,30,'2011-06-27 09:12:29','2011-06-27 09:12:29',0,0,0,1,'Common',0),(641,0,'Orb','Orb','Accessory',1,1,0,0,0,0,0,0,3,110,'2011-06-27 10:15:28','2011-06-27 10:15:28',0,0,1,1,'Common',0),(642,0,'Eyepatch','Eyepatch','Accessory',0,0,0,1,1,0,0,0,2,100,'2011-06-27 11:11:44','2011-06-27 11:11:44',0,0,0,1,'Common',0),(644,0,'Earrings','Earrings','Accessory',0,0,0,2,0,0,0,0,1,50,'2011-06-27 11:21:04','2011-06-27 11:21:04',0,0,0,1,'Unique',0),(646,0,'Ring','Ring','Accessory',1,0,1,0,0,0,0,0,3,115,'2011-06-27 12:43:29','2011-06-27 12:43:29',0,0,0,1,'Common',0),(647,0,'Eyepatch','Eyepatch','Accessory',0,0,0,1,1,1,0,0,3,110,'2011-06-27 13:00:37','2011-06-27 13:00:37',0,0,0,1,'Common',0),(648,0,'Eyepatch','Eyepatch','Accessory',2,0,0,0,0,0,1,0,2,50,'2011-06-27 13:20:30','2011-06-27 13:20:30',0,0,0,1,'Unique',0),(649,0,'Orb','Orb','Accessory',1,1,0,0,0,0,0,0,2,80,'2011-06-27 13:26:15','2011-06-27 13:26:15',0,0,0,1,'Common',0),(650,0,'Eyepatch','Eyepatch','Accessory',0,1,0,0,2,0,0,0,3,100,'2011-06-27 13:45:56','2011-06-27 13:45:56',0,0,0,1,'Common',0),(653,0,'Orb','Orb','Accessory',2,3,0,0,0,1,0,0,5,165,'2011-06-27 15:15:49','2011-06-27 15:15:49',0,0,1,1,'Unique',0),(654,0,'Ring','Ring','Accessory',0,0,3,0,0,0,0,0,2,100,'2011-06-27 15:40:33','2011-06-27 15:40:33',0,0,0,1,'Unique',0),(655,0,'Earrings','Earrings','Accessory',3,0,3,0,0,1,0,4,8,245,'2011-06-27 17:57:59','2011-06-27 17:57:59',0,0,0,1,'Unique',0),(656,0,'Earrings','Earrings','Accessory',0,0,1,1,0,0,0,0,2,100,'2011-06-28 03:41:08','2011-06-28 03:41:08',0,0,0,1,'Common',0),(662,0,'Necklace','Necklace','Accessory',0,0,0,0,1,0,0,0,1,55,'2011-06-29 07:30:21','2011-06-29 07:30:21',0,0,0,1,'Common',0),(664,0,'Earrings','Earrings','Accessory',3,2,3,3,0,1,1,1,10,340,'2011-06-29 13:32:43','2011-06-29 13:32:43',0,0,0,1,'Unique',0),(665,0,'Eyepatch','Eyepatch','Accessory',4,4,0,5,2,1,0,1,12,430,'2011-06-30 11:57:08','2011-06-30 11:57:08',0,0,0,1,'Unique',0),(668,0,'Eyepatch','Eyepatch','Accessory',0,8,0,4,2,1,0,0,10,290,'2011-07-01 05:03:25','2011-07-01 05:03:25',0,0,0,1,'Unique',0),(670,0,'Orb','Orb','Accessory',9,3,0,0,5,2,1,0,20,705,'2011-07-01 12:45:50','2011-07-01 12:45:50',0,0,3,1,'Unique',0),(673,0,'Earrings','Earrings','Accessory',0,3,4,5,0,0,0,0,8,340,'2011-07-10 10:00:57','2011-07-10 10:00:57',0,0,0,1,'Unique',0),(674,0,'Orb','Orb','Accessory',0,4,0,0,0,0,0,0,5,180,'2011-07-10 10:16:48','2011-07-10 10:16:48',0,0,2,1,'Unique',0),(675,0,'Ring','Ring','Accessory',1,0,0,0,0,1,0,0,2,65,'2011-07-13 14:07:17','2011-07-13 14:07:17',0,0,0,1,'Common',0),(676,0,'Eyepatch','Eyepatch','Accessory',0,0,0,1,0,0,0,1,2,80,'2011-07-17 01:52:31','2011-07-17 01:52:31',0,0,0,1,'Common',0),(680,0,'Earrings','Earrings','Accessory',4,2,7,7,0,2,1,3,20,720,'2011-07-24 05:45:53','2011-07-24 05:45:53',0,0,0,1,'Unique',0),(681,0,'Necklace','Necklace','Accessory',0,0,0,0,0,0,0,0,2,40,'2011-07-26 07:14:04','2011-07-26 07:14:04',0,0,1,1,'Common',0),(682,0,'Orb','Orb','Accessory',5,3,0,0,5,1,0,0,11,744,'2011-07-31 02:48:59','2011-07-31 02:48:59',0,0,1,1,'Unique',0),(684,0,'Eyepatch','Eyepatch','Accessory',7,2,0,12,2,4,1,2,20,1088,'2011-08-05 11:43:13','2011-08-05 11:43:13',0,0,0,1,'Epic',0),(688,0,'Necklace','Necklace','Accessory',5,13,0,0,12,1,1,0,20,1464,'2011-08-06 08:05:23','2011-08-06 08:05:23',0,0,4,1,'Epic',0),(691,0,'Necklace','Necklace','Accessory',0,0,0,0,0,0,0,0,1,10,'2011-08-06 08:48:19','2011-08-06 08:48:19',0,0,0,1,'Common',0),(692,0,'Necklace','Necklace','Accessory',0,0,0,0,0,0,0,0,1,10,'2011-08-06 08:48:23','2011-08-06 08:48:23',0,0,0,1,'Common',0),(693,0,'Necklace','Necklace','Accessory',0,0,0,0,2,0,0,0,2,110,'2011-08-06 09:07:13','2011-08-06 09:07:13',0,0,0,1,'Common',0),(694,0,'Earrings','Earrings','Accessory',3,3,5,4,0,0,0,5,20,720,'2011-08-06 16:19:09','2011-08-06 16:19:09',0,0,0,1,'Common',0),(695,0,'Earrings','Earrings','Accessory',2,1,2,4,0,0,0,2,11,436,'2011-08-07 07:46:35','2011-08-07 07:46:35',0,0,0,1,'Common',0),(696,0,'Earrings','Earrings','Accessory',2,1,2,4,0,0,0,2,11,436,'2011-08-07 07:46:49','2011-08-07 07:46:49',0,0,0,1,'Common',0),(697,0,'Ring','Ring','Accessory',0,0,0,0,0,0,0,1,1,10,'2011-08-07 08:55:27','2011-08-07 08:55:27',0,0,0,1,'Common',0),(698,0,'Orb','Orb','Accessory',1,0,0,0,0,0,0,0,1,40,'2011-08-08 10:39:28','2011-08-08 10:39:28',0,0,0,1,'Common',0),(699,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,64,'2011-08-08 10:39:47','2011-08-08 10:39:47',0,0,0,1,'Unique',0),(700,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,64,'2011-08-08 10:42:43','2011-08-08 10:42:43',0,0,0,1,'Unique',0),(701,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,64,'2011-08-08 10:44:08','2011-08-08 10:44:08',0,0,0,1,'Unique',0),(702,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,64,'2011-08-08 10:45:32','2011-08-08 10:45:32',0,0,0,1,'Unique',0),(703,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,64,'2011-08-08 10:47:07','2011-08-08 10:47:07',0,0,0,1,'Unique',0),(704,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,64,'2011-08-08 10:48:42','2011-08-08 10:48:42',0,0,0,1,'Unique',0),(705,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,64,'2011-08-08 10:50:14','2011-08-08 10:50:14',0,0,0,1,'Unique',0),(706,0,'Ring','Ring','Accessory',1,0,0,0,0,0,0,0,1,50,'2011-08-08 10:52:06','2011-08-08 10:52:06',0,0,0,1,'Common',0),(707,0,'Orb','Orb','Accessory',0,0,0,0,2,0,0,0,1,96,'2011-08-08 11:03:56','2011-08-08 11:03:56',0,0,0,1,'Unique',0),(708,0,'Ring','Ring','Accessory',0,0,1,0,0,0,0,0,1,50,'2011-08-08 11:04:44','2011-08-08 11:04:44',0,0,0,1,'Common',0),(709,0,'Ring','Ring','Accessory',0,0,2,0,0,0,0,0,1,80,'2011-08-08 11:38:34','2011-08-08 11:38:34',0,0,0,1,'Unique',0),(710,0,'Necklace','Necklace','Accessory',0,0,0,0,1,0,0,0,1,55,'2011-08-08 11:39:01','2011-08-08 11:39:01',0,0,0,1,'Common',0),(711,0,'Necklace','Necklace','Accessory',0,0,0,0,2,0,0,0,1,88,'2011-08-09 12:29:46','2011-08-09 12:29:46',0,0,0,1,'Epic',0),(712,0,'Necklace','Necklace','Accessory',0,0,0,0,2,0,0,0,1,88,'2011-08-09 12:30:00','2011-08-09 12:30:00',0,0,0,1,'Epic',0),(713,0,'Necklace','Necklace','Accessory',0,0,0,0,2,0,0,0,1,88,'2011-08-09 12:32:05','2011-08-09 12:32:05',0,0,0,1,'Epic',0),(714,0,'Necklace','Necklace','Accessory',0,0,0,0,2,0,0,0,1,88,'2011-08-09 12:36:59','2011-08-09 12:36:59',0,0,0,1,'Epic',0),(715,0,'Necklace','Necklace','Accessory',0,0,0,0,2,0,0,0,1,88,'2011-08-09 12:40:36','2011-08-09 12:40:36',0,0,0,1,'Epic',0),(716,0,'Eyepatch','Eyepatch','Accessory',0,0,0,2,0,0,0,0,1,96,'2011-08-09 12:43:53','2011-08-09 12:43:53',0,0,0,1,'Unique',0),(717,0,'Eyepatch','Eyepatch','Accessory',0,0,0,2,0,0,0,0,1,96,'2011-08-09 12:44:08','2011-08-09 12:44:08',0,0,0,1,'Unique',0),(734,0,'Orb','Orb','Accessory',1,0,0,0,0,0,0,0,1,60,'2011-08-17 07:46:38','2011-08-17 07:46:38',0,0,0,1,'Common',0),(735,0,'Necklace','Necklace','Accessory',1,0,0,0,0,0,0,0,1,60,'2011-08-17 07:46:42','2011-08-17 07:46:42',0,0,0,1,'Common',0),(737,0,'Eyepatch','Eyepatch','Accessory',1,1,0,0,0,0,0,0,2,90,'2011-08-17 10:39:58','2011-08-17 10:39:58',0,0,0,1,'Common',0),(738,0,'Earrings','Earrings','Accessory',0,0,0,0,0,0,0,1,1,27,'2011-08-20 09:18:30','2011-08-20 09:18:30',0,0,0,1,'Common',0),(739,0,'Earrings','Earrings','Accessory',0,0,0,0,0,0,0,1,1,27,'2011-08-20 09:18:35','2011-08-20 09:18:35',0,0,0,1,'Common',0),(740,0,'Earrings','Earrings','Accessory',0,0,0,0,0,0,0,1,1,27,'2011-08-20 09:18:40','2011-08-20 09:18:40',0,0,0,1,'Common',0),(741,0,'Orb','Orb','Accessory',0,1,0,0,0,0,0,0,1,60,'2011-08-20 09:24:09','2011-08-20 09:24:09',0,0,0,1,'Common',0),(742,0,'Orb','Orb','Accessory',0,1,0,0,0,0,0,0,1,60,'2011-08-20 09:24:14','2011-08-20 09:24:14',0,0,0,1,'Common',0),(743,0,'Ring','Ring','Accessory',0,0,0,0,0,0,0,0,1,22,'2011-08-20 09:34:17','2011-08-20 09:34:17',0,0,0,1,'Common',0),(744,0,'Orb','Orb','Accessory',0,0,0,0,1,0,0,0,1,90,'2011-08-20 09:34:25','2011-08-20 09:34:25',0,0,0,1,'Common',0),(745,0,'Earrings','Earrings','Accessory',0,0,0,0,0,0,0,0,1,16,'2011-08-20 09:38:58','2011-08-20 09:38:58',0,0,0,1,'Common',0),(746,0,'Orb','Orb','Accessory',2,0,0,0,0,0,0,0,1,100,'2011-08-20 09:39:23','2011-08-20 09:39:23',0,0,0,1,'Epic',0),(747,0,'Earrings','Earrings','Accessory',0,0,0,0,0,0,0,1,1,27,'2011-08-20 10:10:04','2011-08-20 10:10:04',0,0,0,1,'Common',0),(748,0,'Orb','Orb','Accessory',2,0,0,0,0,0,0,0,1,100,'2011-08-20 10:10:11','2011-08-20 10:10:11',0,0,0,1,'Epic',0),(749,0,'Ring','Ring','Accessory',0,1,0,0,0,0,0,0,1,37,'2011-08-20 12:36:34','2011-08-20 12:36:34',0,0,0,1,'Common',0),(750,0,'Ring','Ring','Accessory',0,0,1,0,0,0,0,0,1,75,'2011-08-20 12:36:40','2011-08-20 12:36:40',0,0,0,1,'Common',0),(756,0,'Ring','Ring','Accessory',0,2,0,0,0,0,0,0,1,62,'2011-08-26 10:54:48','2011-08-26 10:54:48',0,0,0,1,'Unique',0),(757,0,'Ring','Ring','Accessory',0,2,0,0,0,0,0,0,1,62,'2011-08-26 10:54:52','2011-08-26 10:54:52',0,0,0,1,'Unique',0),(758,0,'Ring','Ring','Accessory',0,2,0,0,0,0,0,0,1,62,'2011-08-26 10:54:56','2011-08-26 10:54:56',0,0,0,1,'Unique',0),(759,0,'Ring','Ring','Accessory',0,2,0,0,0,0,0,0,1,62,'2011-08-26 10:55:01','2011-08-26 10:55:01',0,0,0,1,'Unique',0),(760,0,'Necklace','Necklace','Accessory',0,5,0,0,0,0,0,0,5,562,'2011-08-27 11:53:51','2011-08-27 11:53:51',0,0,3,1,'Unique',0),(763,0,'Necklace','Necklace','Accessory',10,5,0,0,6,0,2,0,19,1887,'2011-09-04 07:26:13','2011-09-04 07:26:13',0,0,3,1,'Unique',0),(764,0,'Earrings','Earrings','Accessory',2,3,3,5,0,0,1,1,10,897,'2011-09-04 09:02:41','2011-09-04 09:02:41',0,0,0,1,'Unique',0),(765,0,'Eyepatch','Eyepatch','Accessory',2,3,0,2,2,0,2,0,7,500,'2011-09-04 13:37:52','2011-09-04 13:37:52',0,0,0,1,'Unique',0),(766,0,'Ring','Ring','Accessory',3,2,6,5,0,1,0,1,13,1225,'2011-09-06 04:23:15','2011-09-06 04:23:15',0,0,0,1,'Unique',0),(767,0,'Ring','Ring','Accessory',5,0,6,4,0,0,1,0,11,1175,'2011-09-06 14:08:48','2011-09-06 14:08:48',0,0,0,1,'Unique',0),(768,0,'Orb','Orb','Accessory',8,2,0,0,7,1,0,0,13,1412,'2011-09-06 14:48:27','2011-09-06 14:48:27',0,0,1,1,'Unique',0),(770,0,'Orb','Orb','Accessory',3,8,0,0,18,0,1,0,20,2275,'2011-09-09 07:51:29','2011-09-09 07:51:29',0,0,2,1,'Epic',0),(771,0,'Orb','Orb','Accessory',0,0,0,0,0,0,0,0,1,75,'2011-09-11 09:17:11','2011-09-11 09:17:11',0,0,1,1,'Epic',0),(773,0,'Orb','Orb','Accessory',0,0,0,0,2,0,0,0,1,150,'2011-09-11 09:17:54','2011-09-11 09:17:54',0,0,0,1,'Unique',0),(774,0,'Earrings','Earrings','Accessory',0,0,1,0,0,0,0,0,1,75,'2011-09-13 06:44:49','2011-09-13 06:44:49',0,0,0,1,'Common',0),(775,0,'Earrings','Earrings','Accessory',6,2,9,16,0,1,0,4,20,2092,'2011-09-14 03:19:23','2011-09-14 03:19:23',0,0,0,1,'Epic',0),(776,0,'Earrings','Earrings','Accessory',6,2,9,16,0,1,0,4,20,2092,'2011-09-14 03:30:08','2011-09-14 03:30:08',0,0,0,1,'Epic',0),(777,0,'Orb','Orb','Accessory',3,1,0,0,9,1,0,0,20,1297,'2011-09-14 13:21:24','2011-09-14 13:21:24',0,0,3,1,'Common',0),(778,0,'Orb','Orb','Accessory',3,1,0,0,9,1,0,0,20,1297,'2011-09-14 13:21:48','2011-09-14 13:21:48',0,0,3,1,'Common',0),(779,0,'Orb','Orb','Accessory',2,3,0,0,8,5,1,0,20,1575,'2011-09-14 13:25:25','2011-09-14 13:25:25',0,0,6,1,'Epic',0),(781,0,'Eyepatch','Eyepatch','Accessory',0,0,0,1,1,0,0,0,2,150,'2011-09-15 06:10:48','2011-09-15 06:10:48',0,0,0,1,'Common',0),(782,0,'Ring','Ring','Accessory',10,4,6,2,0,1,2,1,20,1825,'2011-09-16 12:51:33','2011-09-16 12:51:33',0,0,0,1,'Unique',0),(785,0,'Orb','Orb','Accessory',6,6,0,0,8,1,3,0,20,3262,'2011-10-19 13:28:49','2011-10-19 13:28:49',0,0,3,1,'Epic',0),(786,0,'Orb','Orb','Accessory',8,8,0,0,4,2,1,0,20,3082,'2011-10-19 13:31:22','2011-10-19 13:31:22',0,0,3,1,'Epic',0),(788,0,'Orb','Orb','Accessory',3,2,0,0,5,1,1,0,9,900,'2011-10-20 05:43:48','2011-10-20 05:43:48',0,0,1,1,'Unique',0),(789,0,'Necklace','Necklace','Accessory',0,5,0,0,2,1,0,0,9,875,'2011-10-20 06:01:02','2011-10-20 06:01:02',0,0,5,1,'Unique',0),(790,0,'Eyepatch','Eyepatch','Accessory',1,2,0,5,4,1,1,2,20,990,'2011-10-21 03:34:47','2011-10-21 03:34:47',0,0,0,1,'Common',0),(791,0,'Ring','Ring','Accessory',10,3,6,3,0,1,2,1,20,1850,'2011-10-21 03:35:14','2011-10-21 03:35:14',0,0,0,1,'Unique',0),(792,0,'Earrings','Earrings','Accessory',0,5,4,9,0,0,0,2,20,1179,'2011-10-21 03:37:02','2011-10-21 03:37:02',0,0,0,1,'Common',0),(793,0,'Eyepatch','Eyepatch','Accessory',1,2,0,5,4,1,1,2,20,990,'2011-10-21 03:37:47','2011-10-21 03:37:47',0,0,0,1,'Common',0),(795,0,'Orb','Orb','Accessory',0,0,0,0,0,0,0,0,1,45,'2011-10-21 06:58:02','2011-10-21 06:58:02',0,0,1,1,'Common',0),(796,0,'Orb','Orb','Accessory',1,0,0,0,0,0,0,0,1,60,'2011-10-21 07:29:14','2011-10-21 07:29:14',0,0,0,1,'Common',0),(797,0,'Ring','Ring','Accessory',6,4,4,0,0,1,1,2,20,1012,'2011-10-21 08:00:39','2011-10-21 08:00:39',0,0,0,1,'Common',0),(799,0,'Ring','Ring','Accessory',6,4,4,0,0,1,1,2,20,1012,'2011-10-21 08:01:08','2011-10-21 08:01:08',0,0,0,1,'Common',0),(800,0,'Ring','Ring','Accessory',6,4,4,0,0,1,1,2,20,1012,'2011-10-21 08:01:19','2011-10-21 08:01:19',0,0,0,1,'Common',0),(801,0,'Earrings','Earrings','Accessory',3,2,4,5,0,0,0,0,9,1000,'2011-10-22 09:11:56','2011-10-22 09:11:56',0,0,0,1,'Unique',0),(802,0,'Eyepatch','Eyepatch','Accessory',6,4,0,1,3,1,0,2,20,900,'2011-10-22 13:55:04','2011-10-22 13:55:04',0,0,0,1,'Common',0),(803,0,'Necklace','Necklace','Accessory',7,8,0,0,8,1,1,0,20,3600,'2011-10-25 08:05:08','2011-10-25 08:05:08',0,0,11,1,'Epic',0),(804,0,'Ring','Ring','Accessory',10,3,11,2,0,7,1,1,20,3240,'2011-10-25 08:05:24','2011-10-25 08:05:24',0,0,0,1,'Epic',0),(806,0,'Necklace','Necklace','Accessory',0,0,0,0,0,0,0,0,1,45,'2011-10-25 14:05:06','2011-10-25 14:05:06',0,0,1,1,'Common',0),(808,0,'Orb','Orb','Accessory',2,6,0,0,5,0,1,0,20,1177,'2011-10-28 07:06:16','2011-10-28 07:06:16',0,0,3,1,'Common',0),(811,0,'Eyepatch','Eyepatch','Accessory',1,0,0,0,1,0,0,1,3,150,'2011-10-28 09:41:32','2011-10-28 09:41:32',0,0,0,1,'Common',0),(813,0,'Orb','Orb','Accessory',2,6,0,0,5,0,1,0,20,1177,'2011-10-28 11:29:27','2011-10-28 11:29:27',0,0,3,1,'Common',0),(814,0,'Eyepatch','Eyepatch','Accessory',0,3,0,0,0,0,0,1,3,150,'2011-10-28 11:29:55','2011-10-28 11:29:55',0,0,0,1,'Unique',0),(818,0,'Eyepatch','Eyepatch','Accessory',1,0,0,0,2,0,0,0,3,180,'2011-11-03 03:54:13','2011-11-03 03:54:13',0,0,0,1,'Common',0),(819,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,180,'2011-11-03 04:59:59','2011-11-03 04:59:59',0,0,0,1,'Epic',0),(820,0,'Orb','Orb','Accessory',0,0,0,0,0,0,0,0,1,45,'2011-11-17 16:39:45','2011-11-17 16:39:45',0,0,1,1,'Common',0),(821,0,'Orb','Orb','Accessory',0,0,0,0,0,0,0,0,1,45,'2011-11-17 16:40:01','2011-11-17 16:40:01',0,0,1,1,'Common',0),(822,0,'Eyepatch','Eyepatch','Accessory',1,0,0,0,0,0,0,0,1,60,'2011-11-17 16:40:14','2011-11-17 16:40:14',0,0,0,1,'Common',0),(823,0,'Orb','Orb','Accessory',0,2,0,0,0,0,0,0,1,160,'2011-11-19 04:58:09','2011-11-19 04:58:09',0,0,0,1,'Unique',0),(824,0,'Orb','Orb','Accessory',2,0,0,0,0,0,0,0,1,160,'2011-11-23 02:17:46','2011-11-23 02:17:46',0,0,0,1,'Unique',0),(825,0,'Earrings','Earrings','Accessory',1,0,0,1,0,0,0,0,2,135,'2011-11-23 05:57:45','2011-11-23 05:57:45',0,0,0,1,'Common',0),(826,0,'Eyepatch','Eyepatch','Accessory',0,0,0,2,2,0,0,0,2,650,'2011-11-23 06:14:32','2011-11-23 06:14:32',0,0,0,1,'Epic',0),(827,0,'Eyepatch','Eyepatch','Accessory',0,1,0,0,0,0,0,0,1,30,'2011-11-23 08:43:02','2011-11-23 08:43:02',0,0,0,1,'Common',0),(828,0,'Ring','Ring','Accessory',2,0,0,0,0,0,0,0,1,325,'2011-11-23 08:43:28','2011-11-23 08:43:28',0,0,0,1,'Epic',0),(829,0,'Necklace','Necklace','Accessory',0,0,0,0,0,1,0,0,1,15,'2011-11-23 13:46:14','2011-11-23 13:46:14',0,0,0,1,'Common',0),(831,0,'Ring','Ring','Accessory',3,0,2,0,0,0,0,0,3,600,'2011-11-25 21:29:58','2011-11-25 21:29:58',0,0,0,1,'Unique',0),(832,0,'Earrings','Earrings','Accessory',0,0,0,1,0,0,0,0,1,75,'2011-11-25 23:07:01','2011-11-25 23:07:01',0,0,0,1,'Common',0),(833,0,'Necklace','Necklace','Accessory',1,0,0,0,0,0,0,0,1,60,'2011-11-25 23:07:50','2011-11-25 23:07:50',0,0,0,1,'Common',0),(834,0,'Necklace','Necklace','Accessory',0,2,0,0,4,0,0,0,4,1267,'2011-11-25 23:10:33','2011-11-25 23:10:33',0,0,2,1,'Epic',0),(835,0,'Orb','Orb','Accessory',1,1,0,0,3,0,0,0,6,412,'2011-11-26 00:16:06','2011-11-26 00:16:06',0,0,0,1,'Common',0),(836,0,'Orb','Orb','Accessory',2,2,0,0,2,1,0,0,4,1007,'2011-11-26 00:57:56','2011-11-26 00:57:56',0,0,0,1,'Epic',0),(837,0,'Earrings','Earrings','Accessory',0,2,0,3,0,1,0,0,7,318,'2011-11-27 10:37:16','2011-11-27 10:37:16',0,0,0,1,'Common',0),(838,0,'Earrings','Earrings','Accessory',0,2,0,3,0,1,0,0,7,318,'2011-11-27 10:38:03','2011-11-27 10:38:03',0,0,0,1,'Common',0),(839,0,'Ring','Ring','Accessory',0,0,4,0,0,0,0,0,4,300,'2011-11-27 12:00:08','2011-11-27 12:00:08',0,0,0,1,'Common',0),(840,0,'Orb','Orb','Accessory',0,0,0,0,4,1,1,0,4,975,'2011-11-27 12:00:29','2011-11-27 12:00:29',0,0,0,1,'Epic',0),(841,0,'Orb','Orb','Accessory',0,0,0,0,4,1,1,0,4,975,'2011-11-27 12:00:35','2011-11-27 12:00:35',0,0,0,1,'Epic',0),(842,0,'Eyepatch','Eyepatch','Accessory',0,0,0,0,0,0,1,0,1,15,'2011-11-28 07:02:10','2011-11-28 07:02:10',0,0,0,1,'Common',0),(843,0,'Necklace','Necklace','Accessory',0,0,0,0,0,1,0,0,1,15,'2011-11-28 07:02:22','2011-11-28 07:02:22',0,0,0,1,'Common',0);
/*!40000 ALTER TABLE `accessories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `heartbeats`
--

DROP TABLE IF EXISTS `heartbeats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `heartbeats` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `current_server_status` tinyint(1) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `heartbeats`
--

LOCK TABLES `heartbeats` WRITE;
/*!40000 ALTER TABLE `heartbeats` DISABLE KEYS */;
INSERT INTO `heartbeats` VALUES (1,1,'2011-10-05 03:21:32','2011-10-05 03:23:49');
/*!40000 ALTER TABLE `heartbeats` ENABLE KEYS */;
UNLOCK TABLES;

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
INSERT INTO `purchases` VALUES (1,'Character Slot','Add an additional character to your team! Max number of team members is 5.',50,'Token','2011-02-17 09:22:36','2011-09-06 13:17:01','CHS',19),(2,'Weapon Purchase','Purchase an additional weapon for one fighter. Weapons will be showcased here, but actual forging must be done in the weapon shop.',20,'Token','2011-02-17 09:35:30','2011-10-25 03:45:16','WPN',25),(3,'Gold Mine','Convert your tokens into solid gold! Current Exchange Rate: 300 Gold for 1 Token.',10,'Token','2011-02-17 09:36:02','2011-11-28 10:43:59','GOM',79),(4,'Breadstick','A special type of bread that increases your team\'s experience gain by 20%. Lasts for 5 battles.',10,'Token','2011-02-17 09:38:25','2011-10-11 06:48:17','BRE',13),(5,'Handful of Goodies','This Handful of Goodies holds 20 Tokens. Trade them in for prizes at the market.',1,'Money','2011-02-17 09:42:12','2011-10-31 13:36:45','HFG',3),(6,'Mouthful of Goodies','This Mouthful of Tokens holds 110 Tokens. You get 10 Tokens for free!',5,'Money','2011-02-17 10:04:38','2011-10-18 08:06:09','MFG',0),(7,'Small Bag of Goodies','This Small Bag of Goodies holds 220 Tokens. You get 20 Tokens for free!',10,'Money','2011-02-17 10:06:11','2011-10-18 08:05:53','SBG',0),(8,'Amnesia','Wish you could go back and allocate stat points differently? Restore all stat points for one character.',10,'Token','2011-02-25 13:21:32','2011-11-27 10:18:45','AMN',55),(9,'Reforge Weapon','Accidentally allocated your weapon points to the wrong stats? Give yourself another chance.',5,'Token','2011-05-09 04:02:08','2011-10-07 08:37:21','WRT',25),(10,'Reforge Armor','Accidentally allocated your armor points to the wrong stats? Give yourself another chance.',5,'Token','2011-05-16 07:41:21','2011-10-07 08:37:31','WSA',9),(11,'Bread Slice','A slice of bread that increases your team\'s experience gain by 40%. Lasts for 3 battles.	',10,'Token','2011-10-11 06:47:49','2011-11-01 14:46:32','BSL',4),(12,'Bread Loaf','A loaf of bread that increases your team\'s experience gain by 100%. Lasts for 1 battle.	',10,'Token','2011-10-11 06:48:49','2011-11-28 09:34:10','BLF',19),(14,'Fresh Booty','Revive all Treasure Nodes and reap the bounty of Gold, Tokens, and Epic items once again.',40,'Token','2011-10-11 07:20:12','2011-10-25 08:06:16','FRB',4),(15,'Expanded Sack','Gain additional inventory slots so you can carry more weapons and accessories.',10,'Token','2011-10-15 06:14:57','2011-11-25 21:56:00','SCK',6),(16,'Box of Goodies','This Box of Goodies holds 440 Tokens. You get 40 Tokens for free!',20,'Money','2011-10-18 08:01:41','2011-10-18 08:05:33','BOG',0),(17,'Giant Sack of Goodies','This Giant Sack of Goodies holds 1100 Tokens. You get 100 Tokens for free!',50,'Money','2011-10-18 08:02:28','2011-10-18 08:05:15','GSG',0),(18,'Massive Sack of Goodies','This Massive Sack of Goodies holds 2200 Tokens. You get 200 Tokens for free!',100,'Money','2011-10-18 08:03:24','2011-10-18 08:05:01','MSG',0),(19,'Earn Tokens','Would you like to earn new tokens for free?',0,'Money','2011-11-16 09:01:33','2011-11-16 09:01:33','ETK',0);
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_accessories`
--

DROP TABLE IF EXISTS `user_accessories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_accessories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `temp_accessories` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_accessories`
--

LOCK TABLES `user_accessories` WRITE;
/*!40000 ALTER TABLE `user_accessories` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_accessories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_achievement_user_analytics`
--

DROP TABLE IF EXISTS `user_achievement_user_analytics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_achievement_user_analytics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_achievement_id` int(11) DEFAULT NULL,
  `user_analytic_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_achievement_user_analytics`
--

LOCK TABLES `user_achievement_user_analytics` WRITE;
/*!40000 ALTER TABLE `user_achievement_user_analytics` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_achievement_user_analytics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_achievements`
--

DROP TABLE IF EXISTS `user_achievements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_achievements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `achievement_id` int(11) DEFAULT NULL,
  `complete` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_achievements`
--

LOCK TABLES `user_achievements` WRITE;
/*!40000 ALTER TABLE `user_achievements` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_achievements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_analytics`
--

DROP TABLE IF EXISTS `user_analytics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_analytics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `analytic_id` int(11) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_analytics`
--

LOCK TABLES `user_analytics` WRITE;
/*!40000 ALTER TABLE `user_analytics` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_analytics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_banner_user_achievements`
--

DROP TABLE IF EXISTS `user_banner_user_achievements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_banner_user_achievements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_banner_id` int(11) DEFAULT NULL,
  `user_achievement_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_banner_user_achievements`
--

LOCK TABLES `user_banner_user_achievements` WRITE;
/*!40000 ALTER TABLE `user_banner_user_achievements` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_banner_user_achievements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_character_accessories`
--

DROP TABLE IF EXISTS `user_character_accessories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_character_accessories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_character_id` int(11) DEFAULT NULL,
  `accessory_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `level` int(11) DEFAULT '1',
  `strength` int(11) DEFAULT NULL,
  `vitality` int(11) DEFAULT NULL,
  `agility` int(11) DEFAULT NULL,
  `intelligence` int(11) DEFAULT NULL,
  `will` int(11) DEFAULT NULL,
  `armor` decimal(10,0) DEFAULT NULL,
  `dodge` decimal(10,0) DEFAULT NULL,
  `physical_crit` decimal(10,0) DEFAULT NULL,
  `spell_crit` decimal(10,0) DEFAULT NULL,
  `experience` int(11) DEFAULT '0',
  `max_experience` int(11) DEFAULT NULL,
  `stats` int(11) DEFAULT '0',
  `tier` int(11) DEFAULT '1',
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_character_accessories`
--

LOCK TABLES `user_character_accessories` WRITE;
/*!40000 ALTER TABLE `user_character_accessories` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_character_accessories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_character_skills`
--

DROP TABLE IF EXISTS `user_character_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_character_skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_character_id` int(11) DEFAULT NULL,
  `skill_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `in_use` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_character_skills`
--

LOCK TABLES `user_character_skills` WRITE;
/*!40000 ALTER TABLE `user_character_skills` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_character_skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_characters`
--

DROP TABLE IF EXISTS `user_characters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_characters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `character_id` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `strength` int(11) DEFAULT NULL,
  `agility` int(11) DEFAULT NULL,
  `intelligence` int(11) DEFAULT NULL,
  `armor` decimal(10,0) DEFAULT NULL,
  `dodge` decimal(10,0) DEFAULT NULL,
  `physical_crit` decimal(10,0) DEFAULT NULL,
  `vitality` int(11) DEFAULT NULL,
  `will` int(11) DEFAULT NULL,
  `experience` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `max_experience` int(11) DEFAULT NULL,
  `in_lineup` tinyint(1) DEFAULT '1',
  `spell_crit` decimal(10,0) DEFAULT '0',
  `stats` int(11) DEFAULT '0',
  `weapon_swap` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_characters`
--

LOCK TABLES `user_characters` WRITE;
/*!40000 ALTER TABLE `user_characters` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_characters` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_nodes`
--

DROP TABLE IF EXISTS `user_nodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_nodes` (
  `user_id` int(11) NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `foreign_key_nodes` (`user_id`),
  CONSTRAINT `foreign_key_nodes` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_nodes`
--

LOCK TABLES `user_nodes` WRITE;
/*!40000 ALTER TABLE `user_nodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_nodes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_playlists`
--

DROP TABLE IF EXISTS `user_playlists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_playlists` (
  `user_id` int(11) NOT NULL,
  `playlist_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `index2` (`playlist_id`,`rating`),
  KEY `key_id_playlists` (`user_id`),
  CONSTRAINT `key_id_playlists` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_playlists`
--

LOCK TABLES `user_playlists` WRITE;
/*!40000 ALTER TABLE `user_playlists` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_playlists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_quests`
--

DROP TABLE IF EXISTS `user_quests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_quests` (
  `user_id` int(11) NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  KEY `id_foreign` (`user_id`),
  CONSTRAINT `id_foreign` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_quests`
--

LOCK TABLES `user_quests` WRITE;
/*!40000 ALTER TABLE `user_quests` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_quests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_tutorials`
--

DROP TABLE IF EXISTS `user_tutorials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_tutorials` (
  `user_id` int(11) NOT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_tutorials`
--

LOCK TABLES `user_tutorials` WRITE;
/*!40000 ALTER TABLE `user_tutorials` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_tutorials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password_hashed` varchar(128) NOT NULL DEFAULT '',
  `reset_password_token` varchar(255) DEFAULT NULL,
  `remember_created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `sign_in_count` int(11) DEFAULT '0',
  `current_sign_in_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_sign_in_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `current_sign_in_ip` varchar(255) DEFAULT NULL,
  `last_sign_in_ip` varchar(255) DEFAULT NULL,
  `failed_attempts` int(11) DEFAULT '0',
  `unlock_token` varchar(255) DEFAULT NULL,
  `authentication_token` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `money` int(11) DEFAULT NULL,
  `role` varchar(255) DEFAULT 'user',
  `tokens` int(11) DEFAULT '0',
  `breadstick` int(11) DEFAULT '0',
  `character_slot` int(11) DEFAULT '0',
  `bread_slice` int(11) DEFAULT '0',
  `bread_loaf` int(11) DEFAULT '0',
  `inventory_spots` int(11) DEFAULT '9',
  `username` varchar(255) DEFAULT 'n/a',
  `tapjoy_tokens` int(11) DEFAULT '0',
  `message_of_the_day` text,
  `first_day` tinyint(1) DEFAULT '0',
  `second_day` tinyint(1) DEFAULT '0',
  `third_day` tinyint(1) DEFAULT '0',
  `locked_at` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_users_on_reset_password_token` (`reset_password_token`),
  UNIQUE KEY `index_users_on_unlock_token` (`unlock_token`),
  KEY `index_users_by_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `versions`
--

DROP TABLE IF EXISTS `versions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `versions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `call_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `versions`
--

LOCK TABLES `versions` WRITE;
/*!40000 ALTER TABLE `versions` DISABLE KEYS */;
INSERT INTO `versions` VALUES (1,'mobs/details','GET on Mobs',31,'2011-09-17 13:32:56','2011-11-28 08:31:59'),(2,'skills/details','GET on Skills',20,'2011-09-17 15:14:35','2011-11-18 07:04:17'),(3,'purchases/details','GET on Purchases',41,'2011-09-17 15:15:03','2011-11-16 09:01:33'),(4,'accessory_skills/details','GET on Accessory Skills',10,'2011-09-17 15:16:14','2011-11-16 14:18:53'),(5,'nodes/details','GET on Nodes',91,'2011-09-17 15:16:30','2011-11-22 14:40:14'),(6,'playlists/details','GET on Playlists',5,'2011-09-17 15:16:57','2011-10-11 03:32:54'),(7,'banners/details','GET on Banners',5,'2011-09-17 15:17:12','2011-10-11 03:32:54'),(8,'accessories/details','GET on Accessories',34,'2011-09-17 15:17:33','2011-11-16 13:58:52'),(9,'characters/details','GET on Characters',19,'2011-09-17 15:17:50','2011-10-26 04:43:12'),(10,'Version','iPhone app version',1,'2011-11-16 08:38:23','2011-11-16 08:38:23');
/*!40000 ALTER TABLE `versions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-12-04 23:56:31
