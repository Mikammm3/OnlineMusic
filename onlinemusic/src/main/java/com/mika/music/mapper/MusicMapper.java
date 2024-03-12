package com.mika.music.mapper;

import com.mika.music.model.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MusicMapper {
    Integer insert(Music music);

    @Select("select * from music where delete_flag = 0 and title = #{title}")
    Music getSongByName(String title);
}
