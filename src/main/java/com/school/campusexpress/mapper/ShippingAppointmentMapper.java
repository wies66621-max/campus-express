package com.school.campusexpress.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.entity.ShippingAppointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShippingAppointmentMapper extends BaseMapper<ShippingAppointment> {

    IPage<ShippingAppointment> selectAppointmentPage(Page<ShippingAppointment> page,
                                                       @Param("userId") Long userId,
                                                       @Param("status") Integer status,
                                                       @Param("stationId") Long stationId);
}
