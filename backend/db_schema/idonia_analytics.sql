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
-- Table structure for table `analytics`
--

DROP TABLE IF EXISTS `analytics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analytics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `analytics`
--

LOCK TABLES `analytics` WRITE;
/*!40000 ALTER TABLE `analytics` DISABLE KEYS */;
INSERT INTO `analytics` VALUES (1,'Gold Count','Counts the amount of gold that the user has spent','2011-08-18 07:14:12','2011-08-18 07:14:12'),(2,'Skill Swap Count','Counts how many times the user has swapped skills','2011-08-18 10:52:18','2011-08-18 10:52:18'),(3,'Skills Bought Count','Counts how many skills the user has purchased','2011-08-18 10:52:35','2011-08-18 10:52:35'),(4,'Accessory Swap Count','Counts how many times the user has swapped accessories','2011-08-18 10:53:00','2011-08-18 10:53:00'),(5,'Accessories Bought Count','Counts how many accessories the user has purchased','2011-08-18 10:53:21','2011-08-18 10:53:33'),(7,'Token Count','Counts the number of tokens spent','2011-08-21 06:01:04','2011-08-21 06:01:04'),(8,'Character Swap Count','Counts the number of times characters are swapped in the lineup','2011-08-21 06:01:41','2011-08-21 06:01:41'),(9,'Login Count','Number of times the user has logged in','2011-08-21 06:04:18','2011-08-21 06:04:18'),(10,'Weapon Upgrade Count','Number of times the user has upgraded weapons','2011-08-21 06:05:09','2011-08-21 06:05:09'),(11,'Purchase Shop Count','Number of purchases made in the purchase shop','2011-08-21 06:05:46','2011-08-21 06:05:46'),(12,'Mob Wave Count','Number of waves of mobs destroyed in PvE','2011-08-21 06:06:08','2011-08-21 06:06:08'),(13,'Breadstick Count','Number of times breadsticks are applied','2011-08-21 06:22:57','2011-08-21 06:22:57');
/*!40000 ALTER TABLE `analytics` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-20  2:54:45
