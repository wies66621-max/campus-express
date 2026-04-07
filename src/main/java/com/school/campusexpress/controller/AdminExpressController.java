package com.school.campusexpress.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.annotation.RequireAuth;
import com.school.campusexpress.common.R;
import com.school.campusexpress.dto.ExpressInboundDTO;
import com.school.campusexpress.entity.Express;
import com.school.campusexpress.service.ExpressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "管理员快递管理", description = "管理员快递录入、查询、更新、删除接口")
@RestController
@RequestMapping("/admin/express")
public class AdminExpressController {

    @Autowired
    private ExpressService expressService;

    @Operation(summary = "快递入库（扫码）")
    @RequireAuth(roles = {"admin", "courier"})
    @PostMapping("/inbound")
    public R<String> inbound(@Valid @RequestBody ExpressInboundDTO dto) {
        try {
            expressService.inbound(dto);
            return R.success("入库成功");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "录入快递")
    @RequireAuth
    @PostMapping("/add")
    public R<Express> addExpress(@Valid @RequestBody Express express) {
        try {
            Express result = expressService.addExpress(express);
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "快递列表查询")
    @RequireAuth(roles = {"admin", "courier"})
    @GetMapping("/list")
    public R<Page<Express>> getExpressList(
            @Parameter(description = "快递站ID") @RequestParam(required = false) Long stationId,
            @Parameter(description = "快递状态") @RequestParam(required = false) String status,
            @Parameter(description = "快递单号") @RequestParam(required = false) String trackingNumber,
            @Parameter(description = "收件人姓名") @RequestParam(required = false) String receiverName,
            @Parameter(description = "收件人电话") @RequestParam(required = false) String receiverPhone,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<Express> page;
            if (trackingNumber != null && !trackingNumber.trim().isEmpty() ||
                receiverName != null && !receiverName.trim().isEmpty() ||
                receiverPhone != null && !receiverPhone.trim().isEmpty()) {
                page = expressService.getExpressListWithSearch(stationId, status, trackingNumber, receiverName, receiverPhone, pageNum, pageSize);
            } else {
                page = expressService.getExpressList(stationId, status, pageNum, pageSize);
            }
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "快递详情查询")
    @RequireAuth
    @GetMapping("/{id}")
    public R<Express> getExpressById(@Parameter(description = "快递ID") @PathVariable Long id) {
        try {
            Express express = expressService.getExpressById(id);
            if (express != null) {
                return R.success(express);
            }
            return R.error("快递不存在");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "更新快递信息")
    @RequireAuth
    @PutMapping("/update")
    public R<Express> updateExpress(@RequestBody Express express) {
        try {
            Express result = expressService.updateExpress(express);
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "删除快递")
    @RequireAuth
    @DeleteMapping("/delete/{id}")
    public R<String> deleteExpress(@Parameter(description = "快递ID") @PathVariable Long id) {
        try {
            boolean success = expressService.deleteExpress(id);
            if (success) {
                return R.success("删除成功");
            }
            return R.error("删除失败");
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "快递统计")
    @RequireAuth(roles = {"admin", "courier"})
    @GetMapping("/statistics")
    public R<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = expressService.getStatistics();
            return R.success(statistics);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "快递快速查询（支持多条件）")
    @RequireAuth(roles = {"admin", "courier"})
    @GetMapping("/search")
    public R<Page<Express>> quickSearch(
            @Parameter(description = "快递单号") @RequestParam(required = false) String trackingNumber,
            @Parameter(description = "取件码") @RequestParam(required = false) String pickupCode,
            @Parameter(description = "收件人手机号") @RequestParam(required = false) String receiverPhone,
            @Parameter(description = "状态(0=待取件,1=已取件)") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<Express> page = expressService.quickSearch(trackingNumber, pickupCode, receiverPhone, status, pageNum, pageSize);
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
