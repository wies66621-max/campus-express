package com.school.campusexpress.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.annotation.RequireAuth;
import com.school.campusexpress.common.R;
import com.school.campusexpress.entity.ShippingAppointment;
import com.school.campusexpress.service.ShippingAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shipping-appointment")
@RequiredArgsConstructor
@Tag(name = "寄件预约管理", description = "寄件预约相关接口")
public class ShippingAppointmentController {

    private final ShippingAppointmentService shippingAppointmentService;

    @GetMapping("/page")
    @Operation(summary = "分页查询寄件预约")
    @RequireAuth(roles = {"admin", "courier"})
    public R<IPage<ShippingAppointment>> getAppointmentPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long stationId) {
        Page<ShippingAppointment> page = new Page<>(current, size);
        IPage<ShippingAppointment> result = shippingAppointmentService.getAppointmentPage(page, userId, status, stationId);
        return R.success(result);
    }

    @GetMapping("/my-page")
    @Operation(summary = "查询我的寄件预约")
    @RequireAuth(roles = {"user"})
    public R<IPage<ShippingAppointment>> getMyAppointmentPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Page<ShippingAppointment> page = new Page<>(current, size);
        IPage<ShippingAppointment> result = shippingAppointmentService.getAppointmentPage(page, userId, status, null);
        return R.success(result);
    }

    @PostMapping
    @Operation(summary = "创建寄件预约")
    @RequireAuth(roles = {"user"})
    public R<ShippingAppointment> createAppointment(@RequestBody ShippingAppointment appointment,
                                                          HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("创建寄件预约 - userId: {}, appointment: {}", userId, appointment);
        appointment.setUserId(userId);
        ShippingAppointment result = shippingAppointmentService.createAppointment(appointment);
        return R.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询预约详情")
    @RequireAuth(roles = {"admin", "courier", "user"})
    public R<ShippingAppointment> getAppointmentDetail(@PathVariable Long id) {
        ShippingAppointment appointment = shippingAppointmentService.getAppointmentDetail(id);
        return R.success(appointment);
    }

    @PutMapping("/audit/{id}")
    @Operation(summary = "审核寄件预约")
    @RequireAuth(roles = {"admin", "courier"})
    public R<Boolean> auditAppointment(@PathVariable Long id,
                                           @RequestParam Integer status,
                                           @RequestParam(required = false) String auditRemark,
                                           HttpServletRequest request) {
        Long auditorId = (Long) request.getAttribute("userId");
        boolean result = shippingAppointmentService.auditAppointment(id, status, auditRemark, auditorId);
        return R.success(result);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消寄件预约")
    @RequireAuth(roles = {"user"})
    public R<Boolean> cancelAppointment(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        boolean result = shippingAppointmentService.cancelAppointment(id, userId);
        return R.success(result);
    }
}
