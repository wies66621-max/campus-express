package com.school.campusexpress.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.school.campusexpress.entity.Express;

public interface ExpressService {

    Express addExpress(Express express);

    Page<Express> getExpressList(Long stationId, String status, Integer pageNum, Integer pageSize);

    Express getExpressById(Long id);

    Express updateExpress(Express express);

    boolean deleteExpress(Long id);

    Express pickupByCode(String pickupCode, Long userId);

    Page<Express> searchByExpressNo(String expressNo, Integer pageNum, Integer pageSize);

    Page<Express> searchByPhone(String phone, Integer pageNum, Integer pageSize);

    Page<Express> getMyExpress(Long userId, Integer pageNum, Integer pageSize);
}
