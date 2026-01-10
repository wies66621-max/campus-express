package com.school.campusexpress.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.campusexpress.entity.PickupRecord;
import com.school.campusexpress.mapper.PickupRecordMapper;
import com.school.campusexpress.service.PickupRecordService;
import org.springframework.stereotype.Service;

@Service
public class PickupRecordServiceImpl extends ServiceImpl<PickupRecordMapper, PickupRecord> implements PickupRecordService {

    @Override
    public PickupRecord saveRecord(PickupRecord record) {
        super.save(record);
        return record;
    }
}
