package com.school.campusexpress.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school.campusexpress.entity.ShippingAppointment;

public interface ShippingAppointmentService extends IService<ShippingAppointment> {

    IPage<ShippingAppointment> getAppointmentPage(Page<ShippingAppointment> page,
                                                   Long userId,
                                                   Integer status,
                                                   Long stationId);

    ShippingAppointment createAppointment(ShippingAppointment appointment);

    boolean auditAppointment(Long id, Integer status, String auditRemark, Long auditorId);

    boolean cancelAppointment(Long id, Long userId);

    ShippingAppointment getAppointmentDetail(Long id);
}
