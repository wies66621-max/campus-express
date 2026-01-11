package com.school.campusexpress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.campusexpress.entity.ShippingAppointment;
import com.school.campusexpress.mapper.ShippingAppointmentMapper;
import com.school.campusexpress.service.ShippingAppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ShippingAppointmentServiceImpl extends ServiceImpl<ShippingAppointmentMapper, ShippingAppointment> implements ShippingAppointmentService {

    @Override
    public IPage<ShippingAppointment> getAppointmentPage(Page<ShippingAppointment> page,
                                                           Long userId,
                                                           Integer status,
                                                           Long stationId) {
        return baseMapper.selectAppointmentPage(page, userId, status, stationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShippingAppointment createAppointment(ShippingAppointment appointment) {
        appointment.setStatus(0);
        appointment.setCreateTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());
        save(appointment);
        return appointment;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditAppointment(Long id, Integer status, String auditRemark, Long auditorId) {
        ShippingAppointment appointment = getById(id);
        if (appointment == null) {
            throw new RuntimeException("预约记录不存在");
        }
        if (appointment.getStatus() != 0) {
            throw new RuntimeException("该预约已审核，无需重复操作");
        }
        appointment.setStatus(status);
        appointment.setAuditRemark(auditRemark);
        appointment.setAuditorId(auditorId);
        appointment.setAuditTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());
        return updateById(appointment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelAppointment(Long id, Long userId) {
        ShippingAppointment appointment = getById(id);
        if (appointment == null) {
            throw new RuntimeException("预约记录不存在");
        }
        if (!appointment.getUserId().equals(userId)) {
            throw new RuntimeException("无权取消他人的预约");
        }
        if (appointment.getStatus() != 0) {
            throw new RuntimeException("只能取消待审核的预约");
        }
        appointment.setStatus(3);
        appointment.setUpdateTime(LocalDateTime.now());
        return updateById(appointment);
    }

    @Override
    public ShippingAppointment getAppointmentDetail(Long id) {
        return getById(id);
    }
}
