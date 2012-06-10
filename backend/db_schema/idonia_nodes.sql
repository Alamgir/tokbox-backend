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
-- Table structure for table `nodes`
--

DROP TABLE IF EXISTS `nodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nodes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `num_waves` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `description` text,
  `node_type` int(11) DEFAULT '0',
  `region` int(11) DEFAULT NULL,
  `mob_id` varchar(255) DEFAULT '0',
  `child_nodes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nodes`
--

LOCK TABLES `nodes` WRITE;
/*!40000 ALTER TABLE `nodes` DISABLE KEYS */;
INSERT INTO `nodes` VALUES (2,'Hortel',1,'2012-01-22 07:30:18','0000-00-00 00:00:00','He once was a revered member of the Dark Council, but fell after First Disbanding. Hortel is now the leader of the Dark Legion, a position he earned out of pity.',0,2,'49','0'),(3,'Erador',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Decades of war has been fought on Idonia\'s soil and it is now your turn to fight back. You are needed desperately on the front lines of Urudin.',1,1,'0','4'),(4,'Perucian Hills',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','The Mordilian Legion has attempted to flank the Idonian Army. Get there fast enough to stop them from gaining ground and invading Eir.',1,1,'0','5'),(5,'Temples of Bahar',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','The Temples of Bahar used to be serene and filled with the soft air of chanting. The Dark War has turned this place of worship into a bloodbath.',1,2,'0','6'),(6,'Eiran Farmlands',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','A few enemy squads have been reported terrorizing farmland in this area.',1,2,'0','9,7'),(7,'Minas Zenoth',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Minas Zenith provides crucial visibility for miles around. Force the Dark Counsel out of the area and give Eir the eyes it needs.',1,2,'0','36,8'),(8,'Irbryn',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','The Irbryn Peninsula was known for its vibrant coastal culture. Put an end to the recent attacks on its beaches.',1,2,'0','2'),(9,'Faerhorn',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Every morning, the Monks of Faerhorn blow massive horns that can be heard hundreds of miles away. The horns haven\'t been heard for a few days now...',1,2,'0','11'),(10,'Brookden',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','A whimsical and bright place, the Dark Council has a special hatred for Brookden\'s happy atmosphere.',1,1,'0','35'),(11,'Jag',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Jag started as an abandoned feline whom the legendary sorceror, Azfor, kept as a pet.',0,4,'50','15'),(12,'Ianthian Border',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','The enemies along the Ianthian and Urudin border carry a strange message that death is to the North...',1,1,'0','41,10'),(13,'Musou',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','An insane sorceress who speaks of a magical talisman that gives divine power. Musou has spent her entire life searching for this talisman. This has sent her into a fury.',0,1,'56','0'),(14,'Verisia',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','The Verisian Light\'s perpetual glow has turned its people into insomniacs. Enemy forces are trying to cover the Light and plunge Verisia into darkness.',1,4,'0','12'),(15,'Onasus',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Onasus is a central point for Idonian forces as it is a production center for weapons, potions, and magical objects. However, it has made Onasus a target for Dark Council leaders.',1,4,'0','14,17,22,16'),(16,'Cajal',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','Condemned by the Holy Circle of Hildegard, Cajal was once a noble knight who was corrupted by the greed of the Dark Council.',0,4,'57','38'),(17,'Ianthia',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Welcome to Ianthia, the largest province in Idonia with the most powerful warriors. Hail Hildegard!',1,3,'0','18,21'),(18,'Ordalea',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Home to the Faction of Ordalea, an elite group of warriors priding itself on the slaughter of Dark Forces. ',1,3,'0','19'),(19,'Frosthaven',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','With an icy climate, Frosthaven is the closest Ianthian establishment to The White Peaks...and Mount Etosus.',1,3,'0','20,37'),(20,'Rocko',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Rocko\'s origins herald from the White Peaks. He is made of granite, diamond, crystal, and limestone; how he became so is a mystery.',0,3,'55','0'),(21,'Viz',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','Viz was once a revered and iconic warrior of Ianthia who became disgruntled after the sorcerors of the Dark Forces cast spells on him to lose his body, but retain his spirit.',0,4,'52','19'),(22,'Rykor',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','As Azfor\'s personal assistant, Rykor sought to have as much power as Azfor, and after pestering his superior enough times, Azfor granted Rykor life beyond death.',0,4,'51','23'),(23,'Port of Syana',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Syana is a host to many trading ships that carry exotic foods, animals, and materials from far away lands.',1,5,'0','43,45'),(24,'Ithe',1,'2011-12-17 03:18:19','0000-00-00 00:00:00','Ithe has been a trading capital since Atla\'s establishment. It currently only trades in war and destruction.',1,5,'0','25,47'),(25,'Hulio and Julianna',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','A union of love and destruction, they were acquainted during a raid in Ithe, and the rest of their tale tells of corruption, greed, and hatred.',0,5,'53,54','0'),(35,'Treasure of Urudin',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','A special box that is full of surprises. Rewards include gold, tokens, and a good chance to earn an epic item!',2,1,'0','0'),(36,'Treasure of Eir',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','A special box that is full of surprises. Rewards include gold, tokens, and a good chance to earn an epic item!',2,2,'0','0'),(37,'Treasure of Ianthia',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','A special box that is full of surprises. Rewards include gold, tokens, and a good chance to earn an epic item!',2,3,'0','0'),(38,'Treasure of Linnaean Sands',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','A special box that is full of surprises. Rewards include gold, tokens, and a good chance to earn an epic item!',2,4,'0','0'),(39,'Treasure of Atla',1,'2011-12-17 03:18:20','0000-00-00 00:00:00','A special box that is full of surprises. Rewards include gold, tokens, and a good chance to earn an epic item!',2,5,'0','0'),(40,'Booty',3,'2011-12-17 03:18:19','0000-00-00 00:00:00','Victory! Your journey is now complete. Wait another day and you will be able to try again!',3,5,'0','0'),(41,'Milstone',3,'2011-12-17 03:18:19','0000-00-00 00:00:00','Although this town is located in Ianthia, Milstone\'s inhabitants are mostly skilled woodsmen. ',1,3,'0','42'),(42,'Furwood',3,'2011-12-17 03:18:19','0000-00-00 00:00:00','After the invasion by Azfor\'s minions, Furwood is slowly recovering its population and moral. ',1,1,'0','13'),(43,'Roda',3,'2011-12-17 03:18:19','0000-00-00 00:00:00','Roda is the information capital of Idonia. Mercenaries, spies, and assassins call this place home.   ',1,5,'0','44'),(44,'Forbidden Pier',3,'2011-12-17 03:18:19','0000-00-00 00:00:00','Little is known about this ghost town. However,  rumor has it that treasure may lie somewhere nearby.',1,5,'0','39'),(45,'Tristle',3,'2011-12-17 03:18:20','0000-00-00 00:00:00','The most liberal city in idonia. Members of different races and species come here to elope.',1,5,'0','46'),(46,'X',3,'2011-12-17 03:18:20','0000-00-00 00:00:00','X was named after the classic symbol for assassins. Two bloody daggers were crossed to warn weaklings to stay away.',1,5,'0','24'),(47,'First Base',3,'2012-01-03 22:47:06','0000-00-00 00:00:00','The first step to treasure is always the hardest.',4,5,'0','48'),(48,'Second Base',3,'2012-01-03 22:47:06','0000-00-00 00:00:00','Although tiring and unrewarding, it is necessary to just suck it up and keep going.',4,5,'0','49'),(49,'Third Base',1,'2012-01-03 22:47:06','0000-00-00 00:00:00','Things are starting to perk up. One more step to victory.',4,5,'0','40');
/*!40000 ALTER TABLE `nodes` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_nodes_on_insert AFTER INSERT on nodes FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 5 */;;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER update_nodes_on_update AFTER UPDATE on nodes FOR EACH ROW UPDATE versions SET versions.version = versions.version + 1 WHERE versions.id = 5 */;;
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

-- Dump completed on 2012-01-24 19:41:05
