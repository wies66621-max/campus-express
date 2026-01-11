package com.school.campusexpress.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.campusexpress.entity.Express;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExpressMapper extends BaseMapper<Express> {

    @Select("SELECT COUNT(*) FROM express WHERE tracking_number = #{trackingNumber}")
    int countByTrackingNumber(String trackingNumber);
}
