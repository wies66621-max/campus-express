package com.school.campusexpress.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.common.R;
import com.school.campusexpress.entity.PickupRecord;
import com.school.campusexpress.service.PickupRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "取件记录管理", description = "管理员查询取件记录接口")
@RestController
@RequestMapping("/admin/pickup-record")
public class AdminPickupRecordController {

    @Autowired
    private PickupRecordService pickupRecordService;

    @Operation(summary = "取件记录列表")
    @GetMapping("/list")
    public R<Page<PickupRecord>> getPickupRecordList(
            @Parameter(description = "快递ID") @RequestParam(required = false) Long expressId,
            @Parameter(description = "操作员ID") @RequestParam(required = false) Long operatorId,
            @Parameter(description = "开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<PickupRecord> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<PickupRecord> wrapper = new LambdaQueryWrapper<>();

            if (expressId != null) {
                wrapper.eq(PickupRecord::getExpressId, expressId);
            }
            if (operatorId != null) {
                wrapper.eq(PickupRecord::getOperatorId, operatorId);
            }
            if (startTime != null) {
                wrapper.ge(PickupRecord::getPickupTime, startTime);
            }
            if (endTime != null) {
                wrapper.le(PickupRecord::getPickupTime, endTime);
            }

            wrapper.orderByDesc(PickupRecord::getPickupTime);
            Page<PickupRecord> result = pickupRecordService.page(page, wrapper);
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "取件记录详情")
    @GetMapping("/{id}")
    public R<PickupRecord> getPickupRecordById(@Parameter(description = "记录ID") @PathVariable Long id) {
        try {
            PickupRecord record = pickupRecordService.getById(id);
            if (record != null) {
                return R.success(record);
            }
            return R.error("取件记录不存在");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
