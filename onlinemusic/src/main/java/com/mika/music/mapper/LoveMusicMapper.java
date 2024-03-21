package com.mika.music.mapper;


import com.mika.music.model.Music;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Mapper
public interface LoveMusicMapper {

    @Insert("insert into love_music(user_id,music_id) values(#{userId},#{musicId})")
    Integer insert(Integer userId, Integer musicId);

    @Select("select * from love_music where music_id = #{musicId} and user_id = #{userId}")
    Music checkLoved(Integer musicId, Integer userId);

    @Delete("delete from love_music where music_id = #{musicId} and user_id = #{userId}")
    Integer deleteLoved(Integer musicId, Integer userId);

    @Select("select m.* from music m, love_music ml where ml.music_id = m.id and ml.user_id = #{userId} and m.delete_flag = 0")
    List<Music> getLovedMusics(Integer userId);

    @Select("select m.* from music m, love_music ml where ml.music_id = m.id and ml.user_id = #{userId} and m.title like CONCAT('%',#{name},'%') and m.delete_flag = 0")
    List<Music> getLovedMusicsByName(String name, Integer userId);


    Integer deleteSelLoved(@RequestParam List<Integer> musicIds);

    @Delete("delete from love_music where music_id = #{musicId}")
    Integer deleteLovedByMusicId(Integer musicId);
    
}
