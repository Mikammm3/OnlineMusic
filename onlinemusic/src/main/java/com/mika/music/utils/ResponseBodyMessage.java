package com.mika.music.utils;

import lombok.Data;

@Data
public class ResponseBodyMessage<T> {
    private Integer code; // 200成功 -1失败 -2未登录
    private String message;
    private T data;

    public ResponseBodyMessage(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
