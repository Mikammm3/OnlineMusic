package com.mika.music.service;

import com.mika.music.mapper.MusicMapper;
import com.mika.music.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {
    @Autowired
    private MusicMapper musicMapper;

    public Integer addMusic(Music music) {
        return musicMapper.insert(music);
    }

    // 查询没被删除的音乐
    public Music getSongByName1(String title) {
        return musicMapper.getSongByName1(title);
    }

    // 能查询所有音乐（包括被删除的）
    public Music getSongByName2(String title) {
        return musicMapper.getSongByName2(title);
    }

    // 增加曲库中已有的歌曲
    public Integer updateDeleteFlag(Integer musicId, Integer userId) {
        return musicMapper.updateDeleteFlag(musicId, userId);
    }

    public Integer deleteById(Integer id) {
        return musicMapper.deleteById(id);
    }

    public Integer deleteByIds(List<Integer> ids) {
        return musicMapper.deleteByIds(ids);
    }

    public List<Music> getMusics() {
        return musicMapper.getMusics();
    }

    public List<Music> getMusicsByName(String musicName) {
        return musicMapper.getMusicsByName(musicName);
    }

//    public Music getMusicById(Integer id) {
//        return musicMapper.getMusicById(id);
//    }

//    public List<Music> getMusicsByNameAndId(String title, Integer id) {
//        return musicMapper.getMusicsByNameAndId(title, id);
//    }
}
