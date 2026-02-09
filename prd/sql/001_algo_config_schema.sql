-- 算法配置管理系统数据库脚本
-- MySQL 8.0+

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS `algo_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `recipe_id` VARCHAR(64) NOT NULL,
  `eqp_type` VARCHAR(64) NOT NULL,
  `eqp_id` VARCHAR(64) DEFAULT NULL,
  `product_id` VARCHAR(64) DEFAULT NULL,
  `case_name` VARCHAR(128) DEFAULT NULL,
  `python_version` VARCHAR(32) DEFAULT NULL,
  `algorithm_name` VARCHAR(128) NOT NULL,
  `algorithm_version` VARCHAR(64) NOT NULL,
  `algorithm_desc` TEXT,
  `algorithm_file_path` VARCHAR(512) DEFAULT NULL,
  `algorithm_config_json` JSON DEFAULT NULL,
  `aggregate_enabled` TINYINT(1) NOT NULL DEFAULT 0,
  `dc_config_json` JSON DEFAULT NULL,
  `send_kov` TINYINT(1) NOT NULL DEFAULT 0,
  `send_lis` TINYINT(1) NOT NULL DEFAULT 0,
  `send_lithops` TINYINT(1) NOT NULL DEFAULT 0,
  `external_system_json` JSON DEFAULT NULL,
  `enabled` TINYINT(1) NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` VARCHAR(64) DEFAULT NULL,
  `updated_by` VARCHAR(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_recipe_id` (`recipe_id`),
  KEY `idx_algorithm_name` (`algorithm_name`),
  KEY `idx_updated_at` (`updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='算法配置表';

CREATE TABLE IF NOT EXISTS `image_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `image_code` VARCHAR(64) DEFAULT NULL,
  `image_name` VARCHAR(128) NOT NULL,
  `image_tag` VARCHAR(64) NOT NULL,
  `registry_url` VARCHAR(256) NOT NULL,
  `image_size` VARCHAR(32) DEFAULT NULL,
  `image_desc` TEXT,
  `status` VARCHAR(16) NOT NULL DEFAULT 'ENABLED',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_image_code` (`image_code`),
  UNIQUE KEY `uk_image_name_tag` (`image_name`,`image_tag`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='镜像配置表';

CREATE TABLE IF NOT EXISTS `image_algo_rel` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `image_id` BIGINT NOT NULL,
  `algorithm_name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_image_algo` (`image_id`,`algorithm_name`),
  KEY `idx_algorithm_name` (`algorithm_name`),
  CONSTRAINT `fk_image_algo_rel_image_id`
    FOREIGN KEY (`image_id`) REFERENCES `image_config` (`id`)
    ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='镜像与算法关联关系表';

CREATE TABLE IF NOT EXISTS `resource_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `algorithm_name` VARCHAR(128) NOT NULL,
  `image_id` BIGINT NOT NULL,
  `image_version` VARCHAR(64) DEFAULT NULL,
  `current_version` VARCHAR(64) DEFAULT NULL,
  `nas_id` VARCHAR(64) DEFAULT NULL,
  `cpu_limit` VARCHAR(32) DEFAULT NULL,
  `mem_limit` VARCHAR(32) DEFAULT NULL,
  `cpu_request` VARCHAR(32) DEFAULT NULL,
  `mem_request` VARCHAR(32) DEFAULT NULL,
  `status` VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_algorithm_name` (`algorithm_name`),
  KEY `idx_status` (`status`),
  KEY `idx_updated_at` (`updated_at`),
  CONSTRAINT `fk_resource_config_image_id`
    FOREIGN KEY (`image_id`) REFERENCES `image_config` (`id`)
    ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源配置表';

CREATE TABLE IF NOT EXISTS `operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `module` VARCHAR(32) NOT NULL,
  `operation` VARCHAR(32) NOT NULL,
  `biz_id` VARCHAR(64) DEFAULT NULL,
  `operator` VARCHAR(64) DEFAULT NULL,
  `content` JSON DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_module_operation` (`module`,`operation`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';

