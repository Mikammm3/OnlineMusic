package com.mika.music.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;


@Slf4j
public class BCryptUtil {

    // 加密
    public static String encrypt(String inputPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String newPassword = bCryptPasswordEncoder.encode(inputPassword);
        log.info("BCrypt加密后密码: " + newPassword);
        return newPassword;
    }

    // 解密
    public static Boolean verify(String inputPassword, String sqlPassword) {
        if (!StringUtils.hasLength(sqlPassword)) {
            log.info("数据库密码为空");
            return false;
        }
        return new BCryptPasswordEncoder().matches(inputPassword, sqlPassword);
    }
}
