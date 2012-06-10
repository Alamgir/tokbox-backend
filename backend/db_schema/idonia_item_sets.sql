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
-- Table structure for table `item_sets`
--

DROP TABLE IF EXISTS `item_sets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_sets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `level` int(11) NOT NULL,
  `character_id` int(11) NOT NULL,
  `armor_accessory_id` int(11) NOT NULL DEFAULT '0',
  `helmet_accessory_id` int(11) NOT NULL DEFAULT '0',
  `weapon_accessory_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_sets`
--

LOCK TABLES `item_sets` WRITE;
/*!40000 ALTER TABLE `item_sets` DISABLE KEYS */;
INSERT INTO `item_sets` VALUES (35,'Marksman Set',18,5,673,680,610),(36,'Bear Fur Set',6,5,674,681,612),(37,'Nature Set',9,5,675,682,613),(38,'Ranger Set',3,5,676,684,609),(39,'Hunter Set',15,5,677,683,611),(40,'Shadow Set',20,5,678,685,615),(41,'Feral Set',12,5,679,686,614),(42,'Alchemist Set',3,3,689,698,625),(43,'Inferno Set',19,3,690,700,619),(44,'Magician Set',7,3,692,696,620),(45,'Magi Set',16,3,693,697,622),(46,'Elite Set',20,3,694,701,623),(47,'Vagabond Set',10,3,695,702,624),(48,'Princess Set',3,4,706,718,633),(49,'Angel Set',11,4,707,719,631),(50,'Royal Set',5,4,708,713,630),(51,'Flame Set',20,4,709,716,632),(52,'Crusader Set',14,4,710,714,629),(53,'Elegant Set',8,4,711,717,628),(54,'Wicked Set',17,4,712,715,634),(55,'Thief Set',3,2,723,729,639),(56,'Pirate Set',9,2,724,732,640),(57,'Slayer Set',12,2,725,731,643),(58,'Imperial Set',15,2,726,733,642),(59,'Shadow Hide Set',6,2,728,730,644),(60,'Knight Set',3,1,738,751,653),(61,'Gladiator Set',5,1,739,745,652),(62,'Undead Set',17,1,740,746,650),(63,'Hero Set',14,1,741,747,649),(64,'Spike Set',8,1,742,748,651),(65,'Paladin Set',11,1,743,749,654),(66,'Dragon Set',20,1,744,750,648),(67,'Panther Set',18,2,727,734,641),(68,'Armored Set',13,3,691,699,621),(69,'Assassin Set',20,2,761,762,638);
/*!40000 ALTER TABLE `item_sets` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-23 18:38:42
