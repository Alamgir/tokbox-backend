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
-- Table structure for table `achievements`
--

DROP TABLE IF EXISTS `achievements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `achievements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `name` varchar(255) DEFAULT NULL,
  `analytic_id` int(11) DEFAULT NULL,
  `max_count` int(11) DEFAULT NULL,
  `task` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `achievements`
--

LOCK TABLES `achievements` WRITE;
/*!40000 ALTER TABLE `achievements` DISABLE KEYS */;
INSERT INTO `achievements` VALUES (1,'You\'ve spent 5,000 gold!','2011-08-08 10:32:29','2011-08-26 11:01:45','Spend that Dough!',1,5000,'Spend 5,000 gold.'),(2,'Step 1: Hold gold in hand. Step 2: Throw gold into the air. You\'ve spent 15,000 in gold! ','2011-08-09 12:45:04','2011-09-09 10:01:16','Makin\' It Rain!',1,15000,'Spend 15,000 gold.'),(3,'Riding in stretch carriages, sipping wine, and attending lavish gatherings. You\'ve spent 25,000 gold! ','2011-08-10 08:01:06','2011-08-23 06:06:23','Ballin\'',1,25000,'Spend 25,000 gold.'),(4,'You\'ve spent 20 tokens! Keep it coming, there\'s so much more in the purchase shop!','2011-08-10 12:13:39','2011-09-09 10:06:04','Blue Money',7,20,'Spend 20 tokens.'),(5,'See steps for Makin\' it Rain, but repeat with Tokens. You\'ve spent 50 tokens!','2011-08-10 12:58:14','2011-09-09 10:06:46','Blue Rain',7,50,'Spend 50 tokens.'),(9,'No, not that type of Blue Ballin\'! Get your mind out of the gutter. You\'ve spent 100 tokens!','2011-08-22 09:26:18','2011-09-09 10:06:59','Blue Ballin\'',7,100,'Spend 100 tokens.'),(10,'Just looking around? Thanks for your first purchase!','2011-08-22 09:26:59','2011-08-23 06:07:24','Window Shopping',11,1,'Make your first purchase in the purchase shop.'),(11,'You\'re becoming one of our best customers! Keep coming back! You\'ve made 3 purchases!','2011-08-22 09:29:22','2011-08-23 06:07:46','Loyal Customer',11,3,'Make 3 purchases in the purchase shop.'),(12,'Holy trinkets! You\'ve made 10 purchases in our shop! You are our best customer!','2011-08-22 09:31:25','2011-08-23 06:08:06','Shopping Spree',11,10,'Make 10 purchases in the purchase shop.'),(13,'Embrace it, feel it...ah what the heck. Change up your skills to see what works best.','2011-08-22 09:32:51','2011-09-09 10:07:33','Change is Good',2,10,'Swap 10 skills.'),(14,'Wow, you\'ve changed skills 50 times. Maybe try getting another character slot?','2011-08-22 09:33:47','2011-09-09 10:09:48','Decisions Decisions...',2,50,'Swap 50 skills.'),(15,'You\'ve reached 100 skill swaps! Strategizing is an important part of this game.','2011-08-22 09:34:33','2011-09-09 10:08:44','Make Up Your Mind!',2,100,'Swap 100 skills.'),(16,'New skills = new ways to destroy your enemies! Enjoy.','2011-08-22 09:38:32','2011-09-30 02:25:07','Gearing Up',3,5,'Purchase 5 skills.'),(17,'You\'re really ramping up your arsenal! Keep going and get all of them!','2011-08-22 09:39:29','2011-08-23 06:10:55','Who Got Skills?',3,20,'Purchase 20 skills.'),(18,'40 skills bought in total! Make sure you have the correct balance of skills, though!','2011-08-22 09:40:36','2011-08-23 06:10:40','You Got Skills!',3,40,'Purchase 40 skills.'),(19,'Don\'t like your accessories? Go buy more! Remember that the accessories in the shop change everyday!','2011-08-22 09:42:57','2011-09-09 10:10:08','Match the Outfit',4,10,'Swap 10 accessories.'),(20,'Either you can\'t find the right accessory, or you keep finding better ones. Either way, keep going back to the accessory shop!','2011-08-22 09:44:34','2011-09-09 10:10:46','The Wrong Pair',4,25,'Swap 25 accessories.'),(21,'You really like to change up your accessories! You\'ve swapped 50 accessories! ','2011-08-22 09:51:27','2011-09-09 10:11:03','Where Are They?',4,50,'Swap 50 accessories.'),(22,'You\'ve bought 5 accessories! ','2011-08-22 09:54:39','2011-08-23 06:11:53','Diamonds Last Forever',5,5,'Purchase 5 accessories.'),(23,'You\'ve bought 15 accessories! Something tells me that you like jewelry?','2011-08-22 09:55:30','2011-08-23 06:12:06','Ooh! Shiny!',5,15,'Purchase 15 accessories.'),(24,'The only thing left to buy is a diamond-studded suit! You\'ve bought 30 accessories!','2011-08-22 09:56:25','2011-08-23 06:12:21','Bling Bling!',5,30,'Purchase 30 accessories.'),(25,'I need somebody else to do the job, not you!','2011-08-22 09:57:22','2011-08-23 06:12:43','You\'re Fired!',8,1,'Swap 1 character from your lineup.'),(26,'Either your character sucks, or you\'re changing up your strategy. Both ways let you fight better.','2011-08-22 09:58:24','2011-09-09 10:11:45','I\'m Replacing You!',8,25,'Swap characters from your lineup 25x.'),(27,'Obviously, you haven\'t found a solid team yet. Keep looking or maybe get another character slot.','2011-08-22 10:00:03','2011-09-09 10:12:03','Is Anybody Any Good?',8,50,'Swap characters from your lineup 50x.'),(28,'Thanks for coming back! If you could, please leave us a review and tell others about Idonia!','2011-08-22 10:01:00','2011-08-23 06:13:56','Loyalty',9,5,'Login into Idonia 5x.'),(29,'You\'ve logged in 30 times! Thanks for your support!','2011-08-22 10:01:57','2011-08-31 09:05:13','Hooked',9,30,'Login into Idonia 30x.'),(30,'You\'ve logged in 100 times!! Look inside your email for a nice gift from us!','2011-08-22 10:02:51','2011-08-23 06:14:39','Addicted',9,100,'Login into Idonia 100x and get a special gift in your email.'),(31,'Cast the molten metal into the mold and temper the blade.','2011-08-22 10:25:07','2011-08-23 06:15:17','Sharpen the Steel',10,1,'Upgrade your weapon for the first time.'),(32,'So that when you strike, your grip is strong and your impact is fierce.','2011-08-22 10:25:54','2011-08-23 06:15:31','Tighten the Hilt',10,5,'Upgrade your weapons 5x.'),(33,'Weapons upgraded and grim faces alike, it is time to reap destruction.','2011-08-22 10:28:09','2011-08-23 06:15:49','Time to Pillage',10,9,'Upgrade your weapons 9x.'),(34,'Your first Breadstick always tastes the best.','2011-08-22 10:29:19','2011-08-23 06:16:21','Om Nom Nom...',13,1,'Consume your first breadstick. Psst...they\'re in the purchase shop!'),(35,'5 breadsticks consumed! Be careful, carbs are your enemy.','2011-08-22 10:30:25','2011-08-23 06:16:40','Raid the Rations',13,5,'Consume 5 breadsticks.'),(36,'10 breadsticks?!?! You know...you don\'t have to eat ALL OF THEM!','2011-08-22 10:31:25','2011-08-23 06:17:03','What a Fat@$$',13,10,'Consume 10 breadsticks.'),(37,'Thanks for signing up for an account!  Welcome to the world of Idonia!','2011-08-30 09:47:30','2011-08-30 09:47:30','Welcome to Idonia!',9,1,'Create an Account!');
/*!40000 ALTER TABLE `achievements` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-20  2:54:44
