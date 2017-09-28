-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: mercurydb
-- ------------------------------------------------------
-- Server version	5.7.15-log

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
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Series Americanas'),(2,'Anime'),(3,'Películas'),(4,'Comics'),(5,'Cosplay'),(6,'Porcelana Fría'),(7,'Dije'),(8,'Ciencia Ficción'),(9,'Bijou'),(10,'Pelucas');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,2003,0,1,1,'clientin_el_mejor@hotmail.com','Clientín',2),(2,2005,0,0,1,'clienton_el_mas_k-po@hotmail.com.ar','Clientín',3),(3,2002,0,1,1,'clientazo@gmail.com','Clientín',5),(4,2014,1,0,1,'clientecito@yahoo.com.ar','Clientecito',6),(5,2012,0,1,1,'cliente@yahoo.com.ar','Cli Ente',23),(6,2016,0,1,1,'el_cliente@cliente','El Cliente',1),(7,1987,0,0,1,'aky@aky.com','Erika',5),(8,1987,0,1,1,'akire@akire.com','Akire',23),(9,2017,0,1,1,'oscar@botta','Oscar Botta',11),(10,2017,1,0,1,'aloz@atun.con','Arroz con Atún',1),(11,2010,1,1,1,'vader@darkside.com','Darth Vader',5),(12,2017,0,0,1,'beicon_elgenio@gmail.com','Beicon Rodríguez',5),(13,2015,0,1,1,'carlitos@gmail.com','Carlitos',1),(14,1940,0,1,1,'arkanthos@aom.com','Arkanthos',22),(15,1941,0,1,1,'ajax@aom.com','Ajax',9),(16,1943,1,0,0,'amana@aom.com','Amanra',16),(17,2010,0,0,1,'miaro@hotmail.com','Mia Rodríguez',5),(18,1976,0,1,1,'jose@magodeoz.com','José Andrea',1);
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `client_purchase`
--

LOCK TABLES `client_purchase` WRITE;
/*!40000 ALTER TABLE `client_purchase` DISABLE KEYS */;
INSERT INTO `client_purchase` VALUES (18,1),(18,2),(18,3),(18,4),(18,5),(18,6),(18,7),(18,8);
/*!40000 ALTER TABLE `client_purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `material`
--

LOCK TABLES `material` WRITE;
/*!40000 ALTER TABLE `material` DISABLE KEYS */;
INSERT INTO `material` VALUES (1,10,'Dije Rel. de la Muerte Grande'),(2,10,'Dije Sailor Moon'),(3,20,'Argollas Plateadas Chiquitas'),(4,50,'Horas de Trabajo'),(5,180,'Peluca Toyokalon'),(6,150,'Peluca Kanekalon');
/*!40000 ALTER TABLE `material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `material_category`
--

LOCK TABLES `material_category` WRITE;
/*!40000 ALTER TABLE `material_category` DISABLE KEYS */;
INSERT INTO `material_category` VALUES (2,2),(1,3),(6,5),(1,7),(2,7),(1,9),(2,9),(3,9),(5,10),(6,10);
/*!40000 ALTER TABLE `material_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `materialquantity`
--

LOCK TABLES `materialquantity` WRITE;
/*!40000 ALTER TABLE `materialquantity` DISABLE KEYS */;
INSERT INTO `materialquantity` VALUES (1,1,2,1),(2,0.25,4,1),(3,1,1,2),(4,0.25,4,2),(5,1,5,3),(6,1,4,4);
/*!40000 ALTER TABLE `materialquantity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `materialstock`
--

LOCK TABLES `materialstock` WRITE;
/*!40000 ALTER TABLE `materialstock` DISABLE KEYS */;
INSERT INTO `materialstock` VALUES (1,10,'Unidades',1),(2,20,'Unidades',2),(3,2,'Bolsitas',3),(4,100,'Unidades',4),(5,15,'Unidades',5),(6,10,'Unidades',6);
/*!40000 ALTER TABLE `materialstock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Choker Sailor Moon',100),(2,'Choker Rel de la Muerte',60),(3,'Peluca Toyokalon',350),(4,'Corte Simple',150);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` VALUES (1,2),(2,3),(3,5),(4,5),(1,7),(2,7),(1,9),(2,9),(3,10),(4,10);
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `product_material`
--

LOCK TABLES `product_material` WRITE;
/*!40000 ALTER TABLE `product_material` DISABLE KEYS */;
INSERT INTO `product_material` VALUES (2,1),(1,2),(1,4),(2,4),(4,4),(3,5);
/*!40000 ALTER TABLE `product_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `productstock`
--

LOCK TABLES `productstock` WRITE;
/*!40000 ALTER TABLE `productstock` DISABLE KEYS */;
INSERT INTO `productstock` VALUES (1,10,'Unidades',1),(2,10,'Unidades',2),(3,15,'Unidades',3),(4,20,'Unidades',4);
/*!40000 ALTER TABLE `productstock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `province`
--

LOCK TABLES `province` WRITE;
/*!40000 ALTER TABLE `province` DISABLE KEYS */;
INSERT INTO `province` VALUES (1,'Buenos Aires'),(2,'Catamarca'),(3,'Chaco'),(4,'Chubut'),(5,'Córdoba'),(6,'Corrientes'),(7,'Entre Ríos'),(8,'Formosa'),(9,'Jujuy'),(10,'La Pampa'),(11,'La Rioja'),(12,'Mendoza'),(13,'Misiones'),(14,'Neuquen'),(15,'Río Negro'),(16,'Salta'),(17,'San Juan'),(18,'San Luis'),(19,'Santa Cruz'),(20,'Santa Fe'),(21,'Santiago del Estero'),(22,'Tierra del Fuego'),(23,'Tucumán');
/*!40000 ALTER TABLE `province` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `purchase`
--

LOCK TABLES `purchase` WRITE;
/*!40000 ALTER TABLE `purchase` DISABLE KEYS */;
INSERT INTO `purchase` VALUES (1,'2017-09-27'),(2,'2017-09-27'),(3,'2017-09-27'),(4,'2017-09-27'),(5,'2017-09-27'),(6,'2017-09-27'),(7,'2017-09-27'),(8,'2017-09-27');
/*!40000 ALTER TABLE `purchase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `purchase_purchasedetail`
--

LOCK TABLES `purchase_purchasedetail` WRITE;
/*!40000 ALTER TABLE `purchase_purchasedetail` DISABLE KEYS */;
INSERT INTO `purchase_purchasedetail` VALUES (1,1),(2,2),(3,3),(3,4),(4,5),(5,6),(6,7),(7,8),(8,9);
/*!40000 ALTER TABLE `purchase_purchasedetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `purchasedetail`
--

LOCK TABLES `purchasedetail` WRITE;
/*!40000 ALTER TABLE `purchasedetail` DISABLE KEYS */;
INSERT INTO `purchasedetail` VALUES (1,1,350,3),(2,1,100,1),(3,1,350,3),(4,1,150,4),(5,1,400,3),(6,3,60,2),(7,4,60,2),(8,4,100,1),(9,1,350,3);
/*!40000 ALTER TABLE `purchasedetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `supplier_category`
--

LOCK TABLES `supplier_category` WRITE;
/*!40000 ALTER TABLE `supplier_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplier_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'mercurydb'
--

--
-- Dumping routines for database 'mercurydb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-27 23:25:33
