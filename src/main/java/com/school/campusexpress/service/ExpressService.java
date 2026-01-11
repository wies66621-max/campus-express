package com.school.campusexpress.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.dto.ExpressInboundDTO;
import com.school.campusexpress.entity.Express;

import java.util.Map;

public interface ExpressService {

    Express addExpress(Express express);

    void inbound(ExpressInboundDTO dto);

    Page<Express> getExpressList(Long stationId, String status, Integer pageNum, Integer pageSize);

    Page<Express> getExpressListWithSearch(Long stationId, String status, String trackingNumber, String receiverName, String receiverPhone, Integer pageNum, Integer pageSize);

    Express getExpressById(Long id);

    Express updateExpress(Express express);

    boolean deleteExpress(Long id);

    Express pickupByCode(String pickupCode, Long userId);

    Page<Express> searchByExpressNo(String expressNo, Integer pageNum, Integer pageSize);

    Page<Express> searchByPhone(String phone, Integer pageNum, Integer pageSize);

    Page<Express> getMyExpress(Long userId, Integer pageNum, Integer pageSize);

    Map<String, Object> getStatistics();

    Page<Express> quickSearch(String trackingNumber, String pickupCode, String receiverPhone, Integer status, Integer pageNum, Integer pageSize);
}
