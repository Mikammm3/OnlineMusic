package com.mika.music.model;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Music {
    private Integer id;
    private String title;
    private String singer;
    private String url;
    private Integer userId;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;

    public Music(String title, String singer, String url, Integer userId) {
        this.title = title;
        this.singer = singer;
        this.url = url;
        this.userId = userId;
    }

    public Music() {
    }

    public String getCreateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(createTime);
    }
}
