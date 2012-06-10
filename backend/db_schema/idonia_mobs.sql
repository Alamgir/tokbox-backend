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
-- Table structure for table `mobs`
--

DROP TABLE IF EXISTS `mobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(255) NOT NULL,
  `level` int(11) NOT NULL,
  `strength` int(11) NOT NULL,
  `agility` float NOT NULL,
  `vitality` int(11) NOT NULL,
  `intelligence` int(11) NOT NULL,
  `will` int(11) NOT NULL,
  `armor` float NOT NULL,
  `dodge` float NOT NULL,
  `physical_crit` float NOT NULL,
  `spell_crit` float NOT NULL,
  `money` int(11) NOT NULL,
  `experience` int(11) NOT NULL,
  `mob_type` varchar(24) NOT NULL,
  `difficulty_type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `mob_index` (`mob_type`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mobs`
--

LOCK TABLES `mobs` WRITE;
/*!40000 ALTER TABLE `mobs` DISABLE KEYS */;
INSERT INTO `mobs` VALUES (45,'Ninja','Meh...',1,11,8,9,1,20,6,5,5,0,12,23,'Ninja','Normal'),(46,'Abomination','I\'m space man.',1,10,4,10,1,20,10,1,2,0,11,22,'Abomination','Normal'),(47,'Ghost','Furry little bastard.',1,1,1,8,10,20,3,1,0,10,10,20,'Ghost','Normal'),(48,'Succubus','wtf...',1,1,1,8,10,10,4,3,1,6,9,18,'Succubus','Normal'),(49,'Hortel','Rugged looking fellow.',5,42,14,45,0,500,17,3,10,0,125,667,'Hortel','Boss'),(50,'Jag','Big Pussy.',7,50,5,50,1,500,10,5,15,0,328,770,'Jag','Boss'),(51,'Rykor','Undead Sorcerer.',9,1,1,40,55,500,11,5,0,10,235,873,'Rykor','Boss'),(52,'Viz','Viz',11,45,5,80,45,500,10,5,10,10,430,976,'Viz','Boss'),(53,'Hulio','Romeo.',13,45,20,50,1,500,1,1,10,0,250,1079,'Hulio','Boss'),(54,'Julianna','Bitch.',13,1,1,50,45,500,1,1,0,15,270,1079,'Julianna','Boss'),(55,'Rocko','Rock creature with mega attack.',15,70,5,115,1,500,35,5,10,0,519,1181,'Rocko','Boss'),(56,'Musou','Ancient wizard specializing in illlusions.',17,1,1,130,65,500,0,100,1,25,579,1284,'Musou','Boss'),(57,'Cajal','Heretic Paladin.',19,1,20,140,70,500,30,1,20,0,639,1387,'Cajal','Boss'),(58,'Muck','Yucky.',1,15,4,9,10,20,3,1,5,5,11,21,'Muck','Normal'),(59,'Priestess','Evil Healer',1,0,0,8,10,500,3,1,0,6,10,20,'Priestess','Normal');
/*!40000 ALTER TABLE `mobs` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-23 21:44:01
