package com.school.campusexpress.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.annotation.RequireAuth;
import com.school.campusexpress.common.R;
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
    @RequireAuth
    @GetMapping("/list")
    public R<Page<Express>> getExpressList(
            @Parameter(description = "快递站ID") @RequestParam(required = false) Long stationId,
            @Parameter(description = "快递状态") @RequestParam(required = false) String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<Express> page = expressService.getExpressList(stationId, status, pageNum, pageSize);
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
    @RequireAuth
    @GetMapping("/statistics")
    public R<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = expressService.getStatistics();
            return R.success(statistics);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
