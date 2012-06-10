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
-- Table structure for table `user_accessories`
--

DROP TABLE IF EXISTS `user_accessories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_accessories` (
  `user_id` int(11) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `temp_accessories` blob NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_accessories`
--

LOCK TABLES `user_accessories` WRITE;
/*!40000 ALTER TABLE `user_accessories` DISABLE KEYS */;
INSERT INTO `user_accessories` VALUES (10,'2012-01-20 08:24:17','\nNecklace\0\0\0\0\0 \0*Common0Ê=\0\0\0\0@M\0\0\0\0U\0\0\0\0ZNecklace`j	Accessorypx\0€\nNecklace\0\0\0\0 \0*Common0÷=\0\0\0\0@\0M\0\0\0\0U\0\0\0\0ZNecklace`j	Accessorypx\0€\nEarrings\0\0\0\0\0 *Common0Ò=\0\0\0\0@M\0\0\0\0U\0\0\0\0ZEarrings`j	Accessoryp\0x€\nEarrings\0\0\0\0 \0*Common0¥=\0\0\0\0@M\0\0\0\0U\0\0\0\0ZEarrings`j	Accessoryp\0x€\nNecklace\0\0\0\0 \0*Common0÷=\0\0\0\0@\0M\0\0\0\0U\0\0\0\0ZNecklace`j	Accessorypx\0€\nEarrings\0\0\0\0\0 *Common0±=\0\0€?@\0M\0\0\0\0U\0\0\0\0ZEarrings`j	Accessoryp\0x\0€\nNecklace\0\0\0\0 \0*Common0á=\0\0\0\0@M\0\0\0\0U\0\0\0\0ZNecklace`j	Accessoryp\0x\0€\nOrb\0\0\0\0 \0*Common0Ò=\0\0\0\0@M\0\0\0\0U\0\0\0\0ZOrb`j	Accessorypx\0€\nOrb\0\0\0\0\0 \0*Common0á=\0\0\0\0@\0M\0\0\0\0UÍÌL?ZOrb`j	Accessorypx\0€\nOrbš™? \0*Unique0Ì=\0\0\0\0@\0M\0\0\0\0U\0\0\0\0ZOrb`j	Accessorypx\0€');
/*!40000 ALTER TABLE `user_accessories` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-20  2:54:46
