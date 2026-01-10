package com.school.campusexpress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.school.campusexpress.entity.Station;
import com.school.campusexpress.mapper.StationMapper;
import com.school.campusexpress.service.StationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

    @Override
    public Station addStation(Station station) {
        if (station.getStationName() == null || station.getStationName().trim().isEmpty()) {
            throw new RuntimeException("快递站名称不能为空");
        }

        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Station::getStationName, station.getStationName());
        Station existStation = getOne(wrapper);
        if (existStation != null) {
            throw new RuntimeException("快递站名称已存在");
        }

        station.setStatus(station.getStatus() == null ? 1 : station.getStatus());
        station.setCreateTime(LocalDateTime.now());
        station.setUpdateTime(LocalDateTime.now());

        save(station);
        return station;
    }

    @Override
    public Page<Station> getStationList(Integer status, Integer pageNum, Integer pageSize) {
        Page<Station> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Station> wrapper = new LambdaQueryWrapper<>();

        if (status != null) {
            wrapper.eq(Station::getStatus, status);
        }

        wrapper.orderByDesc(Station::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public Station getStationById(Long id) {
        if (id == null) {
            throw new RuntimeException("快递站ID不能为空");
        }
        return getById(id);
    }

    @Override
    public Station updateStation(Station station) {
        if (station.getId() == null) {
            throw new RuntimeException("快递站ID不能为空");
        }

        Station existStation = getById(station.getId());
        if (existStation == null) {
            throw new RuntimeException("快递站不存在");
        }

        station.setUpdateTime(LocalDateTime.now());
        updateById(station);
        return getById(station.getId());
    }

    @Override
    public boolean deleteStation(Long id) {
        if (id == null) {
            throw new RuntimeException("快递站ID不能为空");
        }

        Station station = getById(id);
        if (station == null) {
            throw new RuntimeException("快递站不存在");
        }

        return removeById(id);
    }
}
