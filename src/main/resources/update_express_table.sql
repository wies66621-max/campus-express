-- 更新express表结构以匹配新的实体类字段名
USE campus_express;

-- 1. 添加新字段
ALTER TABLE express ADD COLUMN express_no VARCHAR(100) COMMENT '快递单号';
ALTER TABLE express ADD COLUMN recipient_name VARCHAR(50) COMMENT '收件人姓名';
ALTER TABLE express ADD COLUMN recipient_phone VARCHAR(20) COMMENT '收件人电话';
ALTER TABLE express ADD COLUMN storage_time DATETIME COMMENT '入库时间';
ALTER TABLE express ADD COLUMN pickup_time DATETIME COMMENT '取件时间';

-- 2. 修改status字段类型
ALTER TABLE express MODIFY COLUMN status VARCHAR(20) COMMENT '状态：pending-待取件，picked-已取件';

-- 3. 复制数据到新字段
UPDATE express SET express_no = tracking_number WHERE tracking_number IS NOT NULL;
UPDATE express SET recipient_name = receiver_name WHERE receiver_name IS NOT NULL;
UPDATE express SET recipient_phone = receiver_phone WHERE receiver_phone IS NOT NULL;
UPDATE express SET storage_time = create_time WHERE create_time IS NOT NULL;

-- 4. 更新status值
UPDATE express SET status = 'pending' WHERE status = '0';
UPDATE express SET status = 'picked' WHERE status = '1';

-- 5. 删除旧字段
ALTER TABLE express DROP COLUMN tracking_number;
ALTER TABLE express DROP COLUMN receiver_name;
ALTER TABLE express DROP COLUMN receiver_phone;