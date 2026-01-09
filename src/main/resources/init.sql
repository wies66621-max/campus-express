-- 校园快递管理系统数据库初始化脚本
-- 创建时间: 2026-01-08
-- 数据库版本: MySQL 8.0+

-- 创建数据库
CREATE DATABASE IF NOT EXISTS campus_express DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE campus_express;

-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码(加密)',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `role` VARCHAR(20) DEFAULT 'user' COMMENT '角色(admin/user)',
  `status` INT DEFAULT 1 COMMENT '状态(0=禁用,1=启用)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 快递表
DROP TABLE IF EXISTS `express`;
CREATE TABLE `express` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '快递ID',
  `express_no` VARCHAR(50) NOT NULL COMMENT '快递单号',
  `recipient_name` VARCHAR(50) NOT NULL COMMENT '收件人姓名',
  `recipient_phone` VARCHAR(20) NOT NULL COMMENT '收件人手机号',
  `pickup_code` VARCHAR(10) NOT NULL COMMENT '取件码',
  `status` VARCHAR(20) DEFAULT 'pending' COMMENT '状态(pending=待取件,picked=已取件,expired=超期)',
  `storage_time` DATETIME NOT NULL COMMENT '入库时间',
  `pickup_time` DATETIME DEFAULT NULL COMMENT '取件时间',
  `station_id` BIGINT DEFAULT NULL COMMENT '快递站ID',
  `admin_id` BIGINT DEFAULT NULL COMMENT '操作管理员ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_express_no` (`express_no`),
  UNIQUE KEY `uk_pickup_code` (`pickup_code`),
  KEY `idx_recipient_phone` (`recipient_phone`),
  KEY `idx_status` (`status`),
  KEY `idx_station_id` (`station_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='快递表';

-- 3. 取件记录表
DROP TABLE IF EXISTS `pickup_record`;
CREATE TABLE `pickup_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `express_id` BIGINT NOT NULL COMMENT '快递ID',
  `user_id` BIGINT NOT NULL COMMENT '取件用户ID',
  `pickup_time` DATETIME NOT NULL COMMENT '取件时间',
  `pickup_code` VARCHAR(10) NOT NULL COMMENT '取件码',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_express_id` (`express_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_pickup_time` (`pickup_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='取件记录表';

-- 4. 快递站表
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '快递站ID',
  `station_name` VARCHAR(100) NOT NULL COMMENT '快递站名称',
  `location` VARCHAR(200) DEFAULT NULL COMMENT '位置',
  `admin_id` BIGINT DEFAULT NULL COMMENT '负责人ID',
  `status` INT DEFAULT 1 COMMENT '状态(0=禁用,1=启用)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_admin_id` (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='快递站表';

-- 插入初始数据

-- 插入默认管理员账号 (用户名: admin, 密码: admin123)
-- 密码使用BCrypt加密，原始密码为 admin123
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `role`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', '13800138000', 'admin', 1);

-- 插入测试快递站
INSERT INTO `station` (`station_name`, `location`, `admin_id`, `status`) VALUES
('北校区快递站', '北校区学生公寓楼下', 1, 1),
('南校区快递站', '南校区食堂旁边', 1, 1),
('东校区快递站', '东校区图书馆一楼', 1, 1);

-- 查看表结构
SHOW TABLES;

-- 查看用户表数据
SELECT * FROM `user`;

-- 查看快递站数据
SELECT * FROM `station`;
