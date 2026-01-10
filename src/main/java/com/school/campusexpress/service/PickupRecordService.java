package com.school.campusexpress.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.school.campusexpress.entity.PickupRecord;

public interface PickupRecordService extends IService<PickupRecord> {

    PickupRecord saveRecord(PickupRecord record);
}
