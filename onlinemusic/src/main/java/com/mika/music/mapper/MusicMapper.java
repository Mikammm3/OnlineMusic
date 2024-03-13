package com.mika.music.mapper;

import com.mika.music.model.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface MusicMapper {

    Integer insert(Music music);

    @Select("select * from music where delete_flag = 0 and title = #{title}")
    Music getSongByName1(String title);

    @Select("select * from music where title = #{title}")
    Music getSongByName2(String title);

    @Update("update music set delete_flag = 0 , user_id = #{userId} where id = #{id}")
    Integer updateDeleteFlag(Integer id, Integer userId);

    @Update("update music set delete_flag = 1 where id = #{id}")
    Integer deleteById(Integer id);

    Integer deleteByIds(List<Integer> ids);

    @Select("select * from music where delete_flag = 0")
    List<Music> getMusics();

    @Select("select * from music where title like concat('%', #{title}, '%') and delete_flag = 0")
    List<Music> getMusicsByName(String title);


//    List<Music> getMusicByIds(@RequestParam List<Integer> ids);

//    @Select("select * from music where delete_flag = 0 and id = #{id}")
//    Music getMusicById(Integer id);

//    @Select("select * from music where title like concat('%', #{title}, '%') and delete_flag = 0 and id = #{id}")
//    List<Music> getMusicsByNameAndId(String title, Integer id);
}
