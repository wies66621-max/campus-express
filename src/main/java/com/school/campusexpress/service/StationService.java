package com.school.campusexpress.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.school.campusexpress.entity.Station;

public interface StationService extends IService<Station> {

    Station addStation(Station station);

    Page<Station> getStationList(Integer status, Integer pageNum, Integer pageSize);

    Station getStationById(Long id);

    Station updateStation(Station station);

    boolean deleteStation(Long id);
}
