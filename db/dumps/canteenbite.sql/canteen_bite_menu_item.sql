-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: canteen_bite
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `menu_item`
--

DROP TABLE IF EXISTS `menu_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=703 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_item`
--

LOCK TABLES `menu_item` WRITE;
/*!40000 ALTER TABLE `menu_item` DISABLE KEYS */;
INSERT INTO `menu_item` VALUES (1,_binary '','Burger','Delicious veggie burger','Veg Burger',3.5),(101,_binary '','South Indian','Dosa, Idli, Uttapam...','Dosa',50),(102,_binary '','South Indian','Dosa, Idli, Uttapam...','Idli',30),(103,_binary '','South Indian','Dosa, Idli, Uttapam...','Uttapam',45),(201,_binary '','Parathas','Aloo, Paneer, Mix Veg...','Aloo Paratha',40),(202,_binary '','Parathas','Aloo, Paneer, Mix Veg...','Paneer Paratha',60),(301,_binary '','Snacks','Samosa, Sandwich, Fries...','Samosa',20),(302,_binary '','Snacks','Samosa, Sandwich, Fries...','Sandwich',35),(303,_binary '','Snacks','Samosa, Sandwich, Fries...','Fries',30),(401,_binary '','Maggies','Masala Maggi, Cheese Maggi...','Masala Maggi',35),(402,_binary '','Maggies','Masala Maggi, Cheese Maggi...','Cheese Maggi',45),(501,_binary '','Chinese','Noodles, Manchurian...','Noodles',60),(502,_binary '','Chinese','Noodles, Manchurian...','Manchurian',70),(601,_binary '','Drinks','Cold Drink, Juice, Coffee...','Cold Drink',20),(602,_binary '','Drinks','Cold Drink, Juice, Coffee...','Juice',25),(603,_binary '','Drinks','Cold Drink, Juice, Coffee...','Coffee',30),(701,_binary '','Desserts','Ice Cream, Gulab Jamun...','Ice Cream',40),(702,_binary '','Desserts','Ice Cream, Gulab Jamun...','Gulab Jamun',35);
/*!40000 ALTER TABLE `menu_item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-13 17:05:58
