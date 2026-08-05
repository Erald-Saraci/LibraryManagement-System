CREATE DATABASE  IF NOT EXISTS `library` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `library`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: library-db.c7c6emwos2zq.eu-north-1.rds.amazonaws.com    Database: library
-- ------------------------------------------------------
-- Server version	8.0.45

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `AID` varchar(60) NOT NULL,
  `userID` int NOT NULL,
  PRIMARY KEY (`AID`),
  UNIQUE KEY `AID` (`AID`),
  KEY `userID` (`userID`),
  CONSTRAINT `admin_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `ISBN` varchar(60) NOT NULL,
  `Title` varchar(60) NOT NULL,
  `Author` varchar(60) NOT NULL,
  `Genre` varchar(60) NOT NULL,
  `PublishYear` int DEFAULT NULL,
  `Availability` tinyint(1) NOT NULL,
  PRIMARY KEY (`ISBN`),
  UNIQUE KEY `ISBN` (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `borrowed`
--

DROP TABLE IF EXISTS `borrowed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrowed` (
  `BorrowID` varchar(60) NOT NULL,
  `CID` varchar(60) NOT NULL,
  `BorrowDate` datetime NOT NULL,
  `ReturnDate` datetime NOT NULL,
  `ISBN` varchar(60) NOT NULL,
  PRIMARY KEY (`BorrowID`),
  UNIQUE KEY `BorrowID` (`BorrowID`),
  KEY `ISBN` (`ISBN`),
  KEY `CID` (`CID`),
  CONSTRAINT `borrowed_ibfk_1` FOREIGN KEY (`ISBN`) REFERENCES `books` (`ISBN`),
  CONSTRAINT `borrowed_ibfk_2` FOREIGN KEY (`CID`) REFERENCES `customer` (`CID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `CID` varchar(60) NOT NULL,
  `userID` int NOT NULL,
  `MembershipID` varchar(60) NOT NULL,
  `MembershipDate` date DEFAULT NULL,
  `MembershipEndDate` date DEFAULT NULL,
  PRIMARY KEY (`CID`),
  UNIQUE KEY `CID` (`CID`),
  KEY `userID` (`userID`),
  KEY `MembershipID` (`MembershipID`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `user` (`ID`),
  CONSTRAINT `customer_ibfk_2` FOREIGN KEY (`MembershipID`) REFERENCES `membership` (`MID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `InID` varchar(60) NOT NULL,
  `Date` date DEFAULT NULL,
  `Amount` double NOT NULL,
  `AID` varchar(60) NOT NULL,
  `CID` varchar(60) NOT NULL,
  PRIMARY KEY (`InID`),
  UNIQUE KEY `InID` (`InID`),
  KEY `AID` (`AID`),
  KEY `CID` (`CID`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`AID`) REFERENCES `admin` (`AID`),
  CONSTRAINT `invoice_ibfk_2` FOREIGN KEY (`CID`) REFERENCES `customer` (`CID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `membership`
--

DROP TABLE IF EXISTS `membership`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `membership` (
  `MID` varchar(60) NOT NULL,
  `MembershipType` varchar(60) NOT NULL,
  `MembershipCost` double NOT NULL,
  PRIMARY KEY (`MID`),
  UNIQUE KEY `MID` (`MID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `ReservationID` varchar(60) NOT NULL,
  `CID` varchar(60) NOT NULL,
  `ISBN` varchar(60) NOT NULL,
  `ReservedDate` date NOT NULL,
  `Status` varchar(20) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`ReservationID`),
  KEY `CID` (`CID`),
  KEY `ISBN` (`ISBN`),
  CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`CID`) REFERENCES `customer` (`CID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`ISBN`) REFERENCES `books` (`ISBN`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Email` varchar(50) NOT NULL,
  `PhoneNumber` varchar(30) NOT NULL,
  `Role` varchar(50) NOT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Username` (`Username`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-05 17:35:04
