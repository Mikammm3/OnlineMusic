package com.mika.music.utils;

import lombok.Data;

@Data
public class ResponseBodyMessage<T> {
    private Integer code; // 200成功 -1失败 -2未登录
    private String errMsg;// 错误信息
    private T data;

    public ResponseBodyMessage(Integer code, String errMsg, T data) {
        this.code = code;
        this.errMsg = errMsg;
        this.data = data;
    }
}
