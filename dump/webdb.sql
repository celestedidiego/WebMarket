-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: webdb
-- ------------------------------------------------------
-- Server version	9.3.0

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
-- Table structure for table `amministratore`
--

DROP TABLE IF EXISTS `amministratore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amministratore` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amministratore`
--

LOCK TABLES `amministratore` WRITE;
/*!40000 ALTER TABLE `amministratore` DISABLE KEYS */;
INSERT INTO `amministratore` VALUES (1,'admin@gmail.com','5353c9c10f5bc84f8c3635afdcea6e1d9d70f63b9afbbf3d911a5e8cd968e02f8f07956a34f4722540eca5b413bb21e5');
/*!40000 ALTER TABLE `amministratore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caratteristica`
--

DROP TABLE IF EXISTS `caratteristica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caratteristica` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `categoriaNipoteID` int NOT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `caratteristica_ibfk_1` (`categoriaNipoteID`),
  CONSTRAINT `caratteristica_ibfk_1` FOREIGN KEY (`categoriaNipoteID`) REFERENCES `categorianipote` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caratteristica`
--

LOCK TABLES `caratteristica` WRITE;
/*!40000 ALTER TABLE `caratteristica` DISABLE KEYS */;
INSERT INTO `caratteristica` VALUES (1,'RAM',1,0),(2,'Colore',2,0),(4,'Potenza',4,0),(5,'Inclinazione',6,0),(6,'Misura',7,0),(7,'Dimensione',2,0);
/*!40000 ALTER TABLE `caratteristica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `caratteristicarichiesta`
--

DROP TABLE IF EXISTS `caratteristicarichiesta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caratteristicarichiesta` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `richiestaOrdineID` int NOT NULL,
  `caratteristicaID` int NOT NULL,
  `valore` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `caratteristicarichiesta_ibfk_2` (`caratteristicaID`),
  KEY `caratteristicarichiesta_ibfk_1` (`richiestaOrdineID`),
  CONSTRAINT `caratteristicarichiesta_ibfk_1` FOREIGN KEY (`richiestaOrdineID`) REFERENCES `richiestaordine` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `caratteristicarichiesta_ibfk_2` FOREIGN KEY (`caratteristicaID`) REFERENCES `caratteristica` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `caratteristicarichiesta`
--

LOCK TABLES `caratteristicarichiesta` WRITE;
/*!40000 ALTER TABLE `caratteristicarichiesta` DISABLE KEYS */;
INSERT INTO `caratteristicarichiesta` VALUES (1,1,2,'Fucsia',0),(2,1,7,'75',0),(3,2,1,'16',0);
/*!40000 ALTER TABLE `caratteristicarichiesta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoriafiglio`
--

DROP TABLE IF EXISTS `categoriafiglio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoriafiglio` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `categoriaPadreID` int NOT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `categoriafiglio_ibfk_1` (`categoriaPadreID`),
  CONSTRAINT `categoriafiglio_ibfk_1` FOREIGN KEY (`categoriaPadreID`) REFERENCES `categoriapadre` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoriafiglio`
--

LOCK TABLES `categoriafiglio` WRITE;
/*!40000 ALTER TABLE `categoriafiglio` DISABLE KEYS */;
INSERT INTO `categoriafiglio` VALUES (1,'Computer',1,0),(2,'Fitness',2,0),(3,'Pallavolo',2,0),(4,'Audio',3,0);
/*!40000 ALTER TABLE `categoriafiglio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorianipote`
--

DROP TABLE IF EXISTS `categorianipote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorianipote` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `categoriaFiglioID` int NOT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `categorianipote_ibfk_1` (`categoriaFiglioID`),
  CONSTRAINT `categorianipote_ibfk_1` FOREIGN KEY (`categoriaFiglioID`) REFERENCES `categoriafiglio` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorianipote`
--

LOCK TABLES `categorianipote` WRITE;
/*!40000 ALTER TABLE `categorianipote` DISABLE KEYS */;
INSERT INTO `categorianipote` VALUES (1,'Notebook',1,0),(2,'Fitball',2,0),(4,'Cassa',4,0),(6,'Tapis Roulant',2,0),(7,'Divisa',3,0);
/*!40000 ALTER TABLE `categorianipote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoriapadre`
--

DROP TABLE IF EXISTS `categoriapadre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoriapadre` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoriapadre`
--

LOCK TABLES `categoriapadre` WRITE;
/*!40000 ALTER TABLE `categoriapadre` DISABLE KEYS */;
INSERT INTO `categoriapadre` VALUES (1,'Informatica',0),(2,'Sport',0),(3,'Elettronica',0);
/*!40000 ALTER TABLE `categoriapadre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordinante`
--

DROP TABLE IF EXISTS `ordinante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordinante` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ID_ufficio` int DEFAULT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordinante`
--

LOCK TABLES `ordinante` WRITE;
/*!40000 ALTER TABLE `ordinante` DISABLE KEYS */;
INSERT INTO `ordinante` VALUES (1,'ordinante@gmail.com','bb010c3c810899de41d3c1f99fb8fd1e3179964d8e972880fbf0c96d2cc557bc468aae6a7d0a5daf52b24fae52e3f238',NULL,0);
/*!40000 ALTER TABLE `ordinante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordine`
--

DROP TABLE IF EXISTS `ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordine` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `stato_consegna` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tecnicoID` int DEFAULT NULL,
  `propostaAcquistoID` int DEFAULT NULL,
  `data_di_consegna` date DEFAULT NULL,
  `risposta` text COLLATE utf8mb4_unicode_ci,
  `version` bigint DEFAULT '0',
  `ordinanteID` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `tecnicoID` (`tecnicoID`),
  KEY `propostaAcquistoID` (`propostaAcquistoID`),
  KEY `fk_ordine_ordinante` (`ordinanteID`),
  CONSTRAINT `fk_ordine_ordinante` FOREIGN KEY (`ordinanteID`) REFERENCES `ordinante` (`ID`),
  CONSTRAINT `ordine_ibfk_1` FOREIGN KEY (`tecnicoID`) REFERENCES `tecnico` (`ID`),
  CONSTRAINT `ordine_ibfk_2` FOREIGN KEY (`propostaAcquistoID`) REFERENCES `propostaacquisto` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine`
--

LOCK TABLES `ordine` WRITE;
/*!40000 ALTER TABLE `ordine` DISABLE KEYS */;
INSERT INTO `ordine` VALUES (1,'Consegnato',1,1,'2025-06-08',NULL,2,1),(2,'Presa in carico',1,3,NULL,NULL,0,1);
/*!40000 ALTER TABLE `ordine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propostaacquisto`
--

DROP TABLE IF EXISTS `propostaacquisto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `propostaacquisto` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `codice_prodotto` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `produttore` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `prezzo` decimal(10,2) DEFAULT NULL,
  `nome_prodotto` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `URL` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stato_proposta` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `motivazione` text COLLATE utf8mb4_unicode_ci,
  `richiestaPresaInCaricoID` int DEFAULT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `richiestaPresaInCaricoID` (`richiestaPresaInCaricoID`),
  CONSTRAINT `propostaacquisto_ibfk_1` FOREIGN KEY (`richiestaPresaInCaricoID`) REFERENCES `richiestapresaincarico` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propostaacquisto`
--

LOCK TABLES `propostaacquisto` WRITE;
/*!40000 ALTER TABLE `propostaacquisto` DISABLE KEYS */;
INSERT INTO `propostaacquisto` VALUES (1,'6821854932','FFitness','Incluso di attrezzatura',16.00,'Total Body Balance Ball','https://www.example.com','Accettato',NULL,1,1),(2,'9450125089','Lenovo','50 euro in più della richiesta',250.00,'Thinkpad','https://www.example.com','Rifiutato','Ho chiesto un notebook che costi meno di 200 euro',2,1),(3,'8552452846','Pinstone','',143.00,'ANL5-N4000','https://www.example.com','Accettato',NULL,2,1);
/*!40000 ALTER TABLE `propostaacquisto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `richiestaordine`
--

DROP TABLE IF EXISTS `richiestaordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `richiestaordine` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `codice_richiesta` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `data` date DEFAULT NULL,
  `version` bigint DEFAULT '0',
  `ordinanteID` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `codice_richiesta` (`codice_richiesta`),
  KEY `fk_richiestaordine_ordinante` (`ordinanteID`),
  CONSTRAINT `fk_richiestaordine_ordinante` FOREIGN KEY (`ordinanteID`) REFERENCES `ordinante` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `richiestaordine`
--

LOCK TABLES `richiestaordine` WRITE;
/*!40000 ALTER TABLE `richiestaordine` DISABLE KEYS */;
INSERT INTO `richiestaordine` VALUES (1,'5297829736','Voglio che sia incluso anche la pompa','2025-06-08',0,1),(2,'5505956991','Voglio che costi meno di 200 euro','2025-06-08',0,1);
/*!40000 ALTER TABLE `richiestaordine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `richiestapresaincarico`
--

DROP TABLE IF EXISTS `richiestapresaincarico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `richiestapresaincarico` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `tecnicoID` int NOT NULL,
  `richiestaOrdineID` int NOT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `tecnicoID` (`tecnicoID`),
  KEY `richiestaOrdineID` (`richiestaOrdineID`),
  CONSTRAINT `richiestapresaincarico_ibfk_1` FOREIGN KEY (`tecnicoID`) REFERENCES `tecnico` (`ID`),
  CONSTRAINT `richiestapresaincarico_ibfk_2` FOREIGN KEY (`richiestaOrdineID`) REFERENCES `richiestaordine` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `richiestapresaincarico`
--

LOCK TABLES `richiestapresaincarico` WRITE;
/*!40000 ALTER TABLE `richiestapresaincarico` DISABLE KEYS */;
INSERT INTO `richiestapresaincarico` VALUES (1,1,1,0),(2,1,2,0);
/*!40000 ALTER TABLE `richiestapresaincarico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tecnico`
--

DROP TABLE IF EXISTS `tecnico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tecnico` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `version` bigint DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tecnico`
--

LOCK TABLES `tecnico` WRITE;
/*!40000 ALTER TABLE `tecnico` DISABLE KEYS */;
INSERT INTO `tecnico` VALUES (1,'tecnico@gmail.com','c6b7adec269f50bc588b02e1f7286ef448efd823a3c3a0da48064b72e419cd7b00bf31ddc39ecb1f27e06c2a8e80e0e3',0);
/*!40000 ALTER TABLE `tecnico` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-08  3:14:11
