package com.mika.music.utils;


import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class Mp3Util {

    // 判断文件是否是曲子
    public static Boolean isMp3File(String filePath) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        File file = new File(filePath);
        // 将文件转成MP3格式的
        MP3File audioFile = (MP3File) AudioFileIO.read(file);
        // 看看该文件是否有 TAG 标签
        return audioFile.hasID3v1Tag() || audioFile.hasID3v2Tag();
    }

//    // 判断文件是否在数据库中存在
//    public static Boolean isExist(String path, Integer singer, Integer userId) {
//        File file = new File(path);
//        return file.exists();
//    }
}
