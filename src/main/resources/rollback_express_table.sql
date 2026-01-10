-- 回滚express表结构，恢复使用原来的字段名
USE campus_express;

-- 1. 删除新增的字段
ALTER TABLE express DROP COLUMN express_no;
ALTER TABLE express DROP COLUMN recipient_name;
ALTER TABLE express DROP COLUMN recipient_phone;
ALTER TABLE express DROP COLUMN storage_time;
ALTER TABLE express DROP COLUMN pickup_time;

-- 2. 恢复status字段为整型
ALTER TABLE express MODIFY COLUMN status INT COMMENT '状态：0-待取件，1-已取件';

-- 3. 恢复status值
UPDATE express SET status = 0 WHERE status = 'pending';
UPDATE express SET status = 1 WHERE status = 'picked';

-- 4. 确保tracking_number有唯一约束
ALTER TABLE express ADD UNIQUE KEY uk_tracking_number (tracking_number);