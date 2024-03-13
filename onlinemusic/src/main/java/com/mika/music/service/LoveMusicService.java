package com.mika.music.service;

import com.mika.music.mapper.LoveMusicMapper;
import com.mika.music.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoveMusicService {

    @Autowired
    private LoveMusicMapper loveMusicMapper;

    public Integer insert(Integer userId, Integer musicId) {
        return loveMusicMapper.insert(userId, musicId);
    }

    public Music checkLoved(Integer musicId, Integer userId) {
        return loveMusicMapper.checkLoved(musicId, userId);
    }

    public Integer deleteLoved(Integer musicId, Integer userId) {
        return loveMusicMapper.deleteLoved(musicId, userId);
    }

    public List<Music> getLovedMusics(Integer userId) {
        return loveMusicMapper.getLovedMusics(userId);
    }

    public List<Music> getLovedMusicsByName(String name, Integer userId) {
        return loveMusicMapper.getLovedMusicsByName(name, userId);
    }
}
