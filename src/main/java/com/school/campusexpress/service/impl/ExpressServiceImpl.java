package com.school.campusexpress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.campusexpress.entity.Express;
import com.school.campusexpress.mapper.ExpressMapper;
import com.school.campusexpress.service.ExpressService;
import com.school.campusexpress.util.PickupCodeUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExpressServiceImpl extends ServiceImpl<ExpressMapper, Express> implements ExpressService {

    @Override
    public Express addExpress(Express express) {
        if (express.getTrackingNumber() == null || express.getTrackingNumber().trim().isEmpty()) {
            throw new RuntimeException("快递单号不能为空");
        }
        if (express.getReceiverName() == null || express.getReceiverName().trim().isEmpty()) {
            throw new RuntimeException("收件人姓名不能为空");
        }
        if (express.getReceiverPhone() == null || express.getReceiverPhone().trim().isEmpty()) {
            throw new RuntimeException("收件人电话不能为空");
        }

        LambdaQueryWrapper<Express> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Express::getTrackingNumber, express.getTrackingNumber());
        Express existExpress = getOne(wrapper);
        if (existExpress != null) {
            throw new RuntimeException("快递单号已存在");
        }

        String pickupCode;
        int maxRetries = 10;
        int retryCount = 0;
        LambdaQueryWrapper<Express> codeWrapper;
        do {
            pickupCode = PickupCodeUtil.generate();
            codeWrapper = new LambdaQueryWrapper<>();
            codeWrapper.eq(Express::getPickupCode, pickupCode);
            retryCount++;
        } while (getOne(codeWrapper) != null && retryCount < maxRetries);

        if (retryCount >= maxRetries) {
            throw new RuntimeException("生成取件码失败，请重试");
        }

        express.setPickupCode(pickupCode);
        express.setStatus(0);
        express.setCreateTime(LocalDateTime.now());
        express.setUpdateTime(LocalDateTime.now());

        save(express);
        return express;
    }

    @Override
    public Page<Express> getExpressList(Long stationId, String status, Integer pageNum, Integer pageSize) {
        Page<Express> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Express> wrapper = new LambdaQueryWrapper<>();

        if (stationId != null) {
            wrapper.eq(Express::getStationId, stationId);
        }
        if (status != null && !status.trim().isEmpty()) {
            wrapper.eq(Express::getStatus, status);
        }

        wrapper.orderByDesc(Express::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Express getExpressById(Long id) {
        if (id == null) {
            throw new RuntimeException("快递ID不能为空");
        }
        return getById(id);
    }

    @Override
    public Express updateExpress(Express express) {
        if (express.getId() == null) {
            throw new RuntimeException("快递ID不能为空");
        }

        Express existExpress = getById(express.getId());
        if (existExpress == null) {
            throw new RuntimeException("快递不存在");
        }

        express.setUpdateTime(LocalDateTime.now());
        updateById(express);
        return getById(express.getId());
    }

    @Override
    public boolean deleteExpress(Long id) {
        if (id == null) {
            throw new RuntimeException("快递ID不能为空");
        }

        Express express = getById(id);
        if (express == null) {
            throw new RuntimeException("快递不存在");
        }

        return removeById(id);
    }

    @Override
    public Express pickupByCode(String pickupCode, Long userId) {
        if (pickupCode == null || pickupCode.trim().isEmpty()) {
            throw new RuntimeException("取件码不能为空");
        }

        LambdaQueryWrapper<Express> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Express::getPickupCode, pickupCode);
        Express express = getOne(wrapper);

        if (express == null) {
            throw new RuntimeException("取件码不存在");
        }

        if (express.getStatus() != 0) {
            throw new RuntimeException("快递已被取件");
        }

        express.setStatus(1);
        express.setUpdateTime(LocalDateTime.now());

        updateById(express);
        return express;
    }

    @Override
    public Page<Express> searchByExpressNo(String expressNo, Integer pageNum, Integer pageSize) {
        if (expressNo == null || expressNo.trim().isEmpty()) {
            throw new RuntimeException("快递单号不能为空");
        }

        Page<Express> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Express> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Express::getTrackingNumber, expressNo);
        wrapper.orderByDesc(Express::getCreateTime);

        return page(page, wrapper);
    }

    @Override
    public Page<Express> searchByPhone(String phone, Integer pageNum, Integer pageSize) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("手机号不能为空");
        }

        Page<Express> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Express> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Express::getReceiverPhone, phone);
        wrapper.orderByDesc(Express::getCreateTime);

        return page(page, wrapper);
    }

    @Override
    public Page<Express> getMyExpress(Long userId, Integer pageNum, Integer pageSize) {
        Page<Express> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Express> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Express::getReceiverPhone, userId.toString());
        wrapper.orderByDesc(Express::getCreateTime);

        return page(page, wrapper);
    }
}
