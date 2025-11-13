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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'test@example.com','Test User','$2a$10$1qoVCRGRP4oKYXVFkbbAyO1Ks7A7faOGz7AtSO5mJm204ScBzsevW','STUDENT'),(2,'admin@example.com','Admin User','$2a$10$GzydmpJgur4M9iebJGeBTOQ7HRh3Bd15A2ncBXxoAXixGfvKn/zHu','ADMIN'),(3,'alice@example.com','Alice','$2a$10$qo6CKaM/ZUmVlnT0Q3dcUuVlCAVKSIwW/jGIcd4tG3sACX0zg7ioq','STUDENT'),(4,'sanika4562@gmail.com','Sanika Aher','$2a$10$wK730cOniPWz.dSqYB.FUuWTm971h1a98oFGO44uGFm/iLG/h7paO','ADMIN'),(5,'sita@gmail.com','sita aher ','$2a$10$HFjx.sTOVXm6eA4woWIz4.FDJ8/H89vk4bHQStaLcl.o88BZ.cw9O','CUSTOMER'),(6,'sid20@gmail.com','siddhu','$2a$10$9T7V1IB5a0djxHz7g64XsOpKW2c6T3Wkie4PwKGpf6QxmRU3/SY/W','CUSTOMER'),(7,'sid19@gmail.com','siddhesh','$2a$10$WSZqT359vMNRAfnQ1GIWlu/qGTFyIcHZJ.ZYHpQx41FGA3vG3.jJK','CUSTOMER'),(8,'aher34@gmail.com','aher','$2a$10$DIGbEdcYIkhnfz/WAEi26OCF4aKRVtOCOH49sFQeeOGpK1XraAyo.','CUSTOMER'),(9,'customer1@gmail.com','customer','$2a$10$YwrcOfyCgRfEO/ibOoDsI.hFexyRbOTe2mb4eJd/qQyrm2xd/rXRC','CUSTOMER'),(10,'admin2@gmail.com','admin2','$2a$10$fSzyo3RJLRJSphqFSYNqaO9A0e53qPGzil18C1m2j6RMES0mX.4Hi','CUSTOMER'),(11,'admin3@gmail.com','Admin3','$2a$10$YU63ah0qenjgM.dHG.2BDO5pSqLVLkEWSd917x1xLH1IO1SkjNpUK','CUSTOMER'),(13,'adminmain@gmail.com','Main Admin','$2a$12$U2Pp9wCO4enKmI5o.QUkV.9xYS5gdx7o4s2ZPeLMuFd1l88nqA5kG','ADMIN'),(14,'kush@gmail.com','kush1','$2a$10$peYlMqByOmmfteSLGIm3TuGOSzZURNMqylNh5GdKWfL6sFoVzaqkO','KITCHEN_STAFF'),(15,'ajitaher21@gmail.com','darshan staff','$2a$10$L1.B40pHp8RvWCUz/pFIpejwJo4WCkJbxxXWoftFUAXBhsvKIyWAi','KITCHEN_STAFF'),(16,'siddhi@gmail.com','siddhi aher','$2a$10$F.87ziadDs2P8eabPEvzQ.AmPBFGtXwybcOQKRTOr05hlcW7dJQRa','CUSTOMER'),(17,'neha2@gmail.com','neha','$2a$10$SlFzXbxe/ty6dmvft2JVWepCBy7L6Iy4y9pE0JVCpccfgZHQcr.fK','CUSTOMER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
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
