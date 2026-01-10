-- 修复station表的id字段，添加AUTO_INCREMENT
USE campus_express;

-- 删除主键约束
ALTER TABLE station DROP PRIMARY KEY;

-- 添加AUTO_INCREMENT属性
ALTER TABLE station MODIFY COLUMN id BIGINT NOT NULL AUTO_INCREMENT COMMENT '站点ID';

-- 重新添加主键约束
ALTER TABLE station ADD PRIMARY KEY (id);
