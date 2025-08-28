-- MySQL dump 10.13  Distrib 8.0.42, for macos15.2 (arm64)
--
-- Host: barongp.ct084e0esz1q.ap-northeast-2.rds.amazonaws.com    Database: gugus
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accessory_codes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accessory_codes` (
  `accessory_code_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `code` varchar(6) NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`accessory_code_no`),
  UNIQUE KEY `accessory_codes_unique` (`code`),
  KEY `accessory_codes_list_order_IDX` (`list_order`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_histories`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_histories` (
  `appraisal_history_no` bigint NOT NULL AUTO_INCREMENT,
  `appraisal_no` bigint NOT NULL,
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ORIGINAL',
  `has_nfc` tinyint(1) NOT NULL DEFAULT '0',
  `serial_no` varchar(100) DEFAULT NULL,
  `brand_model_no` varchar(100) DEFAULT NULL,
  `goods_name` varchar(100) DEFAULT NULL,
  `origin_no` int DEFAULT NULL,
  `grade_code_no` int DEFAULT NULL,
  `manufacture_year` varchar(4) DEFAULT NULL,
  `material_no` int DEFAULT NULL,
  `color_no` int DEFAULT NULL,
  `point_list_order_confirm` tinyint(1) NOT NULL DEFAULT '0',
  `appraisal_result` varchar(16) DEFAULT NULL,
  `manual_appraisal_result` varchar(16) DEFAULT NULL,
  `manual_appraisal_status` varchar(16) DEFAULT NULL,
  `manual_at` timestamp NULL DEFAULT NULL,
  `manual_by` bigint DEFAULT NULL,
  `request_status` varchar(16) NOT NULL DEFAULT 'TEMP',
  `submitted_at` timestamp NULL DEFAULT NULL,
  `score` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`appraisal_history_no`),
  KEY `appraisal_histories_appraisals_FK` (`appraisal_no`),
  KEY `appraisal_histories_materials_FK` (`material_no`),
  KEY `appraisal_histories_origin_countries_FK` (`origin_no`),
  KEY `appraisal_histories_color_chips_FK` (`color_no`),
  KEY `appraisal_histories_users_FK` (`created_by`),
  KEY `appraisal_histories_users_FK_1` (`updated_by`),
  KEY `appraisal_histories_grade_codes_FK` (`grade_code_no`),
  KEY `appraisal_histories_users_FK_2` (`manual_by`),
  CONSTRAINT `appraisal_histories_appraisals_FK` FOREIGN KEY (`appraisal_no`) REFERENCES `appraisals` (`appraisal_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appraisal_histories_color_chips_FK` FOREIGN KEY (`color_no`) REFERENCES `color_chips` (`color_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appraisal_histories_grade_codes_FK` FOREIGN KEY (`grade_code_no`) REFERENCES `grade_codes` (`grade_code_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appraisal_histories_materials_FK` FOREIGN KEY (`material_no`) REFERENCES `materials` (`material_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appraisal_histories_origin_countries_FK` FOREIGN KEY (`origin_no`) REFERENCES `origin_countries` (`origin_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appraisal_histories_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `appraisal_histories_users_FK_1` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `appraisal_histories_users_FK_2` FOREIGN KEY (`manual_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_histories_accessory_codes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_histories_accessory_codes` (
  `appraisal_history_no` bigint NOT NULL,
  `accessory_code_no` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  PRIMARY KEY (`appraisal_history_no`,`accessory_code_no`),
  KEY `appraisal_histories_accessories_accessory_codes_FK` (`accessory_code_no`),
  CONSTRAINT `appraisal_histories_accessories_accessory_codes_FK` FOREIGN KEY (`accessory_code_no`) REFERENCES `accessory_codes` (`accessory_code_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `appraisal_histories_accessories_appraisal_histories_FK` FOREIGN KEY (`appraisal_history_no`) REFERENCES `appraisal_histories` (`appraisal_history_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_histories_steps`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_histories_steps` (
  `appraisal_history_no` bigint NOT NULL,
  `step_no` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `complete` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`appraisal_history_no`,`step_no`),
  KEY `appraisals_steps_appraisal_steps_FK` (`step_no`),
  CONSTRAINT `appraisal_histories_steps_appraisal_histories_FK` FOREIGN KEY (`appraisal_history_no`) REFERENCES `appraisal_histories` (`appraisal_history_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appraisals_steps_appraisal_steps_FK` FOREIGN KEY (`step_no`) REFERENCES `appraisal_steps` (`step_no`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_history_point_images`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_history_point_images` (
  `image_no` bigint NOT NULL AUTO_INCREMENT,
  `history_point_no` bigint NOT NULL,
  `image_url` text NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`image_no`),
  KEY `appraisals_history_point_images_appraisals_history_points_FK` (`history_point_no`),
  CONSTRAINT `appraisals_history_point_images_appraisals_history_points_FK` FOREIGN KEY (`history_point_no`) REFERENCES `appraisal_history_points` (`history_point_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_history_point_results`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_history_point_results` (
  `result_no` bigint NOT NULL AUTO_INCREMENT,
  `history_point_no` bigint NOT NULL,
  `formula_name` varchar(32) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  PRIMARY KEY (`result_no`),
  KEY `appraisal_history_point_results_appraisal_history_points_FK` (`history_point_no`),
  CONSTRAINT `appraisal_history_point_results_appraisal_history_points_FK` FOREIGN KEY (`history_point_no`) REFERENCES `appraisal_history_points` (`history_point_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_history_points`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_history_points` (
  `history_point_no` bigint NOT NULL AUTO_INCREMENT,
  `appraisal_history_no` bigint NOT NULL,
  `point_no` bigint NOT NULL,
  `brand_point_no` bigint DEFAULT NULL,
  `model_point_no` bigint DEFAULT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `score` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`history_point_no`),
  KEY `appraisals_history_points_appraisal_histories_FK` (`appraisal_history_no`),
  KEY `appraisals_history_points_appraisal_points_FK` (`point_no`),
  KEY `appraisals_history_points_model_appraisal_points_FK` (`model_point_no`),
  KEY `appraisals_history_points_brand_appraisal_points_FK` (`brand_point_no`),
  CONSTRAINT `appraisals_history_points_appraisal_histories_FK` FOREIGN KEY (`appraisal_history_no`) REFERENCES `appraisal_histories` (`appraisal_history_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appraisals_history_points_appraisal_points_FK` FOREIGN KEY (`point_no`) REFERENCES `appraisal_points` (`point_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appraisals_history_points_brand_appraisal_points_FK` FOREIGN KEY (`brand_point_no`) REFERENCES `brand_appraisal_points` (`brand_point_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appraisals_history_points_model_appraisal_points_FK` FOREIGN KEY (`model_point_no`) REFERENCES `model_appraisal_points` (`model_point_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_history_results`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_history_results` (
  `result_no` bigint NOT NULL AUTO_INCREMENT,
  `appraisal_history_no` bigint NOT NULL,
  `formula_name` varchar(32) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  PRIMARY KEY (`result_no`),
  KEY `appraisal_history_results_appraisal_histories_FK` (`appraisal_history_no`),
  CONSTRAINT `appraisal_history_results_appraisal_histories_FK` FOREIGN KEY (`appraisal_history_no`) REFERENCES `appraisal_histories` (`appraisal_history_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_points`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_points` (
  `point_no` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name_english` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `required` tinyint(1) DEFAULT '1',
  `use_type` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'USE',
  `image_count` int NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`point_no`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisal_steps`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisal_steps` (
  `step_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `code` varchar(16) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`step_no`),
  UNIQUE KEY `appraisal_steps_unique` (`code`),
  KEY `appraisal_steps_users_FK` (`created_by`),
  KEY `appraisal_steps_users_FK_1` (`updated_by`),
  CONSTRAINT `appraisal_steps_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `appraisal_steps_users_FK_1` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appraisals`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appraisals` (
  `appraisal_no` bigint NOT NULL AUTO_INCREMENT,
  `category_no` int NOT NULL,
  `brand_no` int NOT NULL,
  `model_no` bigint NOT NULL,
  `appraisal_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'APPRAISAL',
  `collection_status` varchar(16) DEFAULT NULL,
  `model_name_input` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`appraisal_no`),
  KEY `appraisals_categories_FK` (`category_no`),
  KEY `appraisals_brands_FK` (`brand_no`),
  KEY `appraisals_models_FK` (`model_no`),
  KEY `appraisals_users_FK` (`created_by`),
  KEY `appraisals_users_FK_1` (`updated_by`),
  CONSTRAINT `appraisals_brands_FK` FOREIGN KEY (`brand_no`) REFERENCES `brands` (`brand_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appraisals_categories_FK` FOREIGN KEY (`category_no`) REFERENCES `categories` (`category_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appraisals_models_FK` FOREIGN KEY (`model_no`) REFERENCES `models` (`model_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appraisals_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `appraisals_users_FK_1` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_logs`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `log_no` bigint NOT NULL AUTO_INCREMENT,
  `table_name` varchar(64) NOT NULL COMMENT '테이블명',
  `record_id` varchar(100) NOT NULL COMMENT '레코드 ID (복합키의 경우 JSON)',
  `action_type` varchar(10) NOT NULL COMMENT 'UPDATE, INSERT, DELETE',
  `old_values` json DEFAULT NULL COMMENT '변경 전 값',
  `new_values` json DEFAULT NULL COMMENT '변경 후 값',
  `user_no` bigint DEFAULT NULL COMMENT '작업한 사용자',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_no`),
  KEY `audit_logs_table_record_idx` (`table_name`,`record_id`),
  KEY `audit_logs_user_idx` (`user_no`),
  KEY `audit_logs_created_idx` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=493 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brand_appraisal_points`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand_appraisal_points` (
  `brand_point_no` bigint NOT NULL AUTO_INCREMENT,
  `point_no` bigint NOT NULL,
  `categories_brands_no` bigint NOT NULL,
  `category_point_no` bigint DEFAULT NULL,
  `required` tinyint(1) NOT NULL DEFAULT '1',
  `use_type` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'USE',
  `image_count` int NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `guide_image_url` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`brand_point_no`),
  KEY `brand_appraisal_points_appraisal_points_FK` (`point_no`),
  KEY `brand_appraisal_points_category_appraisal_points_FK` (`category_point_no`),
  KEY `brand_appraisal_points_categories_brands_FK` (`categories_brands_no`),
  KEY `brand_appraisal_points_users_FK` (`updated_by`),
  KEY `brand_appraisal_points_users_FK_1` (`created_by`),
  CONSTRAINT `brand_appraisal_points_appraisal_points_FK` FOREIGN KEY (`point_no`) REFERENCES `appraisal_points` (`point_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `brand_appraisal_points_categories_brands_FK` FOREIGN KEY (`categories_brands_no`) REFERENCES `categories_brands` (`categories_brands_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `brand_appraisal_points_category_appraisal_points_FK` FOREIGN KEY (`category_point_no`) REFERENCES `category_appraisal_points` (`category_point_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `brand_appraisal_points_users_FK` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `brand_appraisal_points_users_FK_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brands`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `brand_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `code` varchar(32) NOT NULL,
  `activated` tinyint(1) NOT NULL DEFAULT '1',
  `popular` tinyint(1) NOT NULL DEFAULT '1',
  `require_serial_no` tinyint(1) NOT NULL DEFAULT '0',
  `require_brand_model_no` tinyint(1) NOT NULL DEFAULT '0',
  `logo_domain` varchar(255) DEFAULT NULL,
  `logo` text,
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`brand_no`),
  UNIQUE KEY `brands_unique` (`code`),
  KEY `brands_users_FK` (`created_by`),
  KEY `brands_users_FK_1` (`updated_by`),
  CONSTRAINT `brands_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `brands_users_FK_1` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `category_no` int NOT NULL AUTO_INCREMENT,
  `parent_category_no` int DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `code` varchar(32) NOT NULL,
  `activated` tinyint(1) NOT NULL DEFAULT '1',
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`category_no`),
  UNIQUE KEY `categories_unique` (`code`),
  KEY `categories_categories_fk` (`parent_category_no`),
  KEY `categories_users_FK` (`updated_by`),
  KEY `categories_users_FK_1` (`created_by`),
  CONSTRAINT `categories_categories_fk` FOREIGN KEY (`parent_category_no`) REFERENCES `categories` (`category_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `categories_users_FK` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `categories_users_FK_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories_accessory_codes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories_accessory_codes` (
  `category_no` int NOT NULL,
  `accessory_code_no` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`category_no`,`accessory_code_no`),
  KEY `categories_accessory_codes_accessory_codes_FK` (`accessory_code_no`),
  KEY `categories_accessory_codes_users_FK` (`created_by`),
  KEY `categories_accessory_codes_list_order_IDX` (`list_order`) USING BTREE,
  CONSTRAINT `categories_accessory_codes_accessory_codes_FK` FOREIGN KEY (`accessory_code_no`) REFERENCES `accessory_codes` (`accessory_code_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `categories_accessory_codes_categories_FK` FOREIGN KEY (`category_no`) REFERENCES `categories` (`category_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `categories_accessory_codes_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories_brands`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories_brands` (
  `categories_brands_no` bigint NOT NULL AUTO_INCREMENT,
  `category_no` int NOT NULL,
  `brand_no` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `disconnected` tinyint(1) NOT NULL DEFAULT '0',
  `ai_appraisal_enable` tinyint(1) NOT NULL DEFAULT '0',
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `list_order` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`categories_brands_no`),
  UNIQUE KEY `categories_brands_unique` (`category_no`,`brand_no`),
  KEY `categories_brands_brands_FK` (`brand_no`),
  CONSTRAINT `categories_brands_brands_FK` FOREIGN KEY (`brand_no`) REFERENCES `brands` (`brand_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `categories_brands_categories_FK` FOREIGN KEY (`category_no`) REFERENCES `categories` (`category_no`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categories_materials`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories_materials` (
  `category_no` int NOT NULL,
  `material_no` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`category_no`,`material_no`),
  KEY `categories_materials_materials_FK` (`material_no`),
  CONSTRAINT `categories_materials_categories_FK` FOREIGN KEY (`category_no`) REFERENCES `categories` (`category_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `categories_materials_materials_FK` FOREIGN KEY (`material_no`) REFERENCES `materials` (`material_no`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category_appraisal_points`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category_appraisal_points` (
  `category_point_no` bigint NOT NULL AUTO_INCREMENT,
  `category_no` int NOT NULL,
  `list_order` int DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `point_no` bigint NOT NULL,
  PRIMARY KEY (`category_point_no`),
  KEY `categories_appraisal_points_users_FK` (`updated_by`),
  KEY `categories_appraisal_points_users_FK_1` (`created_by`),
  KEY `categories_appraisal_points_categories_FK` (`category_no`),
  KEY `category_appraisal_points_appraisal_points_FK` (`point_no`),
  CONSTRAINT `categories_appraisal_points_categories_FK` FOREIGN KEY (`category_no`) REFERENCES `categories` (`category_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `categories_appraisal_points_users_FK` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `categories_appraisal_points_users_FK_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `category_appraisal_points_appraisal_points_FK` FOREIGN KEY (`point_no`) REFERENCES `appraisal_points` (`point_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `color_chips`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `color_chips` (
  `color_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `color_code` varchar(12) NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`color_no`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `goods_no` bigint NOT NULL AUTO_INCREMENT,
  `legacy_goods_no` int DEFAULT NULL,
  `appraisal_history_no` bigint DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `model_no` bigint DEFAULT NULL,
  `material_no` int DEFAULT NULL,
  `color_no` int DEFAULT NULL,
  `origin_no` int DEFAULT NULL,
  `goods_image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `serial_no` varchar(100) DEFAULT NULL,
  `brand_model_no` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`goods_no`),
  KEY `goods_models_FK` (`model_no`),
  KEY `goods_materials_FK` (`material_no`),
  KEY `goods_color_chips_FK` (`color_no`),
  KEY `goods_origin_countries_FK` (`origin_no`),
  KEY `goods_users_FK` (`created_by`),
  KEY `goods_users_FK_1` (`updated_by`),
  CONSTRAINT `goods_color_chips_FK` FOREIGN KEY (`color_no`) REFERENCES `color_chips` (`color_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `goods_materials_FK` FOREIGN KEY (`material_no`) REFERENCES `materials` (`material_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `goods_models_FK` FOREIGN KEY (`model_no`) REFERENCES `models` (`model_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `goods_origin_countries_FK` FOREIGN KEY (`origin_no`) REFERENCES `origin_countries` (`origin_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `goods_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `goods_users_FK_1` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `goods_property`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_property` (
  `property_no` bigint NOT NULL AUTO_INCREMENT,
  `goods_no` bigint NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`property_no`),
  KEY `goods_property_goods_FK` (`goods_no`),
  KEY `goods_property_users_FK` (`created_by`),
  KEY `goods_property_users_FK_1` (`updated_by`),
  CONSTRAINT `goods_property_goods_FK` FOREIGN KEY (`goods_no`) REFERENCES `goods` (`goods_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `goods_property_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `goods_property_users_FK_1` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grade_codes`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grade_codes` (
  `grade_code_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `code` varchar(9) NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `usable` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`grade_code_no`),
  KEY `grade_codes_list_order_IDX` (`list_order`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groups`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups` (
  `group_no` int NOT NULL AUTO_INCREMENT,
  `parent_group_no` int DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`group_no`),
  UNIQUE KEY `user_groups_name_key` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groups_menus`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups_menus` (
  `menu_no` int NOT NULL,
  `group_no` int NOT NULL,
  `permission` varchar(10) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`menu_no`,`group_no`),
  KEY `menu_user_groups_user_groups_fk` (`group_no`),
  CONSTRAINT `menu_user_groups_menus_fk` FOREIGN KEY (`menu_no`) REFERENCES `menus` (`menu_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `menu_user_groups_user_groups_fk` FOREIGN KEY (`group_no`) REFERENCES `groups` (`group_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `groups_users`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `groups_users` (
  `user_no` bigint NOT NULL,
  `group_no` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_no`,`group_no`),
  KEY `user_group_members_user_groups_fk` (`group_no`),
  CONSTRAINT `user_group_members_user_groups_fk` FOREIGN KEY (`group_no`) REFERENCES `groups` (`group_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_group_members_users_fk` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `materials`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materials` (
  `material_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`material_no`),
  KEY `materials_list_order_IDX` (`list_order`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `menus`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `menu_no` int NOT NULL AUTO_INCREMENT,
  `parent_menu_no` int DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `menu_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `url_pattern` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `menu_order` int NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `level` smallint NOT NULL DEFAULT '0',
  `type` varchar(20) DEFAULT 'FUNCTIONAL' COMMENT 'FUNCTIONAL: 기능있는 메뉴, GROUP: 그룹핑 메뉴',
  `icon` varchar(255) DEFAULT NULL,
  `visible` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`menu_no`),
  UNIQUE KEY `menus_name_key` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mfas`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mfas` (
  `mfa_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` varchar(16) NOT NULL,
  PRIMARY KEY (`mfa_no`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `model_appraisal_points`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `model_appraisal_points` (
  `model_point_no` bigint NOT NULL AUTO_INCREMENT,
  `point_no` bigint NOT NULL,
  `model_no` bigint NOT NULL,
  `brand_point_no` bigint DEFAULT NULL,
  `required` tinyint(1) NOT NULL DEFAULT '1',
  `image_count` int NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `guide_image_url` text,
  `usable` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  PRIMARY KEY (`model_point_no`),
  KEY `model_appraisal_points_appraisal_points_FK` (`point_no`),
  KEY `model_appraisal_points_models_FK` (`model_no`),
  KEY `model_appraisal_points_brand_appraisal_points_FK` (`brand_point_no`),
  KEY `model_appraisal_points_users_FK` (`created_by`),
  KEY `model_appraisal_points_users_FK_1` (`updated_by`),
  CONSTRAINT `model_appraisal_points_appraisal_points_FK` FOREIGN KEY (`point_no`) REFERENCES `appraisal_points` (`point_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `model_appraisal_points_brand_appraisal_points_FK` FOREIGN KEY (`brand_point_no`) REFERENCES `brand_appraisal_points` (`brand_point_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `model_appraisal_points_models_FK` FOREIGN KEY (`model_no`) REFERENCES `models` (`model_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `model_appraisal_points_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `model_appraisal_points_users_FK_1` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `models`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `models` (
  `model_no` bigint NOT NULL AUTO_INCREMENT,
  `main_goods_no` bigint DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `code` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `category_no` int NOT NULL,
  `brand_no` int NOT NULL,
  `main_image_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `memo` varchar(1000) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`model_no`),
  KEY `models_categories_FK` (`category_no`),
  KEY `models_brands_FK` (`brand_no`),
  KEY `models_users_FK` (`updated_by`),
  KEY `models_users_FK_1` (`created_by`),
  CONSTRAINT `models_brands_FK` FOREIGN KEY (`brand_no`) REFERENCES `brands` (`brand_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `models_categories_FK` FOREIGN KEY (`category_no`) REFERENCES `categories` (`category_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `models_users_FK` FOREIGN KEY (`updated_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `models_users_FK_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `origin_countries`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `origin_countries` (
  `origin_no` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_english` varchar(100) NOT NULL,
  `list_order` int NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  PRIMARY KEY (`origin_no`),
  KEY `origin_countries_list_order_IDX` (`list_order`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `system_language_master`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_language_master` (
  `language_code` varchar(11) NOT NULL,
  `language_type` varchar(7) NOT NULL,
  `type` varchar(21) NOT NULL,
  `content` varchar(200) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`language_code`,`language_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_sessions`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_sessions` (
  `user_no` bigint NOT NULL,
  `session_type` varchar(16) NOT NULL,
  `refresh_token` varchar(500) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_no`,`session_type`),
  CONSTRAINT `user_sessions_users_fk` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_no` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `salt` varchar(255) NOT NULL,
  `mfa_no` int DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `email` varchar(128) NOT NULL,
  `creator_no` bigint DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `firebase_uid` varchar(32) DEFAULT NULL COMMENT 'firbase uid',
  `totp_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`user_no`),
  UNIQUE KEY `users_unique` (`user_id`),
  UNIQUE KEY `users_unique_1` (`email`),
  KEY `users_mfas_fk` (`mfa_no`),
  KEY `users_users_fk` (`creator_no`),
  CONSTRAINT `users_mfas_fk` FOREIGN KEY (`mfa_no`) REFERENCES `mfas` (`mfa_no`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `users_users_fk` FOREIGN KEY (`creator_no`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_menus`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_menus` (
  `user_no` bigint NOT NULL,
  `menu_no` int NOT NULL,
  `permission` varchar(10) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_no`,`menu_no`),
  KEY `users_menus_menus_fk` (`menu_no`),
  CONSTRAINT `users_menus_menus_fk` FOREIGN KEY (`menu_no`) REFERENCES `menus` (`menu_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `users_menus_users_fk` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_mfas`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_mfas` (
  `user_no` bigint NOT NULL,
  `mfa_no` int NOT NULL,
  `activated` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_no`,`mfa_no`),
  KEY `users_mfas_mfas_fk` (`mfa_no`),
  CONSTRAINT `users_mfas_mfas_fk` FOREIGN KEY (`mfa_no`) REFERENCES `mfas` (`mfa_no`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `users_mfas_users_fk` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'gugus'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-21 13:28:18
