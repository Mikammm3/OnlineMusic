package com.mika.music.mapper;

import com.mika.music.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where user_name = #{userName} and delete_flag = 0")
    User getUserByName(String userName);
}
