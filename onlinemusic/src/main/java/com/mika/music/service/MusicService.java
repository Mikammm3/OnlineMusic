package com.mika.music.service;

import com.mika.music.mapper.MusicMapper;
import com.mika.music.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicService {
    @Autowired
    private MusicMapper musicMapper;

    public Integer addMusic(Music music) {
        return musicMapper.insert(music);
    }

    public Music getSongByName(String title) {
        return musicMapper.getSongByName(title);
    }
}
