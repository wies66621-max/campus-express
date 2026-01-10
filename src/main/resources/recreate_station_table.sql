-- 重新创建station表，添加AUTO_INCREMENT
USE campus_express;

-- 删除旧表
DROP TABLE IF EXISTS station;

-- 创建新表，使用AUTO_INCREMENT
CREATE TABLE `station` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '站点ID',
  `station_name` varchar(100) NOT NULL COMMENT '站点名称',
  `location` varchar(255) DEFAULT NULL COMMENT '站点位置',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `status` tinyint DEFAULT '1' COMMENT '站点状态(1-启用,0-停用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='快递站点表';
