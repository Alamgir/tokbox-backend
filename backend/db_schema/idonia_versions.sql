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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `versions`
--

LOCK TABLES `versions` WRITE;
/*!40000 ALTER TABLE `versions` DISABLE KEYS */;
INSERT INTO `versions` VALUES (1,'mobs/details','GET on Mobs',34,'2011-12-29 23:11:57','2011-11-28 08:31:59'),(2,'skills/details','GET on Skills',25,'2011-12-29 22:58:15','2011-11-18 07:04:17'),(3,'purchases/details','GET on Purchases',46,'2011-12-29 22:58:15','2011-11-16 09:01:33'),(4,'accessory_skills/details','GET on Accessory Skills',11,'2011-12-29 22:58:15','2011-11-16 14:18:53'),(5,'nodes/details','GET on Nodes',96,'2012-01-03 22:47:06','2011-11-22 14:40:14'),(6,'playlists/details','GET on Playlists',6,'2011-12-29 22:58:15','2011-10-11 03:32:54'),(7,'banners/details','GET on Banners',5,'2011-09-17 15:17:12','2011-10-11 03:32:54'),(8,'accessories/details','GET on Accessories',291,'2012-01-20 04:39:30','2011-11-16 13:58:52'),(9,'characters/details','GET on Characters',20,'2011-12-29 22:58:15','2011-10-26 04:43:12'),(10,'Version','iPhone app version',1,'2011-11-16 08:38:23','2011-11-16 08:38:23'),(11,'souls/details','GET on Souls',2,'2012-01-12 04:24:02','0000-00-00 00:00:00');
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

-- Dump completed on 2012-01-20  2:54:45
