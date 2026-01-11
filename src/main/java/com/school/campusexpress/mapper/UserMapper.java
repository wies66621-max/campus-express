package com.school.campusexpress.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.campusexpress.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User findByPhone(String phone);
}
