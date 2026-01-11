package com.school.campusexpress.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.annotation.RequireAuth;
import com.school.campusexpress.common.R;
import com.school.campusexpress.entity.Express;
import com.school.campusexpress.service.ExpressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户快递查询", description = "用户取件码取件、快递查询接口")
@RestController
@RequestMapping("/user/express")
public class UserExpressController {

    @Autowired
    private ExpressService expressService;

    @Operation(summary = "取件码取件")
    @RequireAuth(roles = {"user", "courier"})
    @PostMapping("/pickup")
    public R<Map<String, Object>> pickupByCode(
            @Parameter(description = "取件码") @RequestParam String pickupCode,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            Express express = expressService.pickupByCode(pickupCode, userId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("message", "取件成功");
            result.put("express", express);
            
            return R.success(result);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "我的快递")
    @RequireAuth(roles = {"user"})
    @GetMapping("/my")
    public R<Page<Express>> getMyExpress(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            Page<Express> page = expressService.getMyExpress(userId, pageNum, pageSize);
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "按单号搜索快递")
    @RequireAuth(roles = {"user", "courier"})
    @GetMapping("/search/no")
    public R<Page<Express>> searchByExpressNo(
            @Parameter(description = "快递单号") @RequestParam String expressNo,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<Express> page = expressService.searchByExpressNo(expressNo, pageNum, pageSize);
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }

    @Operation(summary = "按手机号搜索快递")
    @RequireAuth(roles = {"user", "courier"})
    @GetMapping("/search/phone")
    public R<Page<Express>> searchByPhone(
            @Parameter(description = "手机号") @RequestParam String phone,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Page<Express> page = expressService.searchByPhone(phone, pageNum, pageSize);
            return R.success(page);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
    }
}
