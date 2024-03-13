package com.mika.music.model;

import lombok.Data;

import java.util.Date;

@Data
public class LoveMusic {
    private Integer id;
    private Integer userId;
    private Integer musicId;
    private Integer deleteFlag;
    private Date createTime;
    private Date updateTime;
}
