package com.mika.music.controller;

import com.mika.music.constants.Constant;
import com.mika.music.model.User;
import com.mika.music.service.UserService;
import com.mika.music.utils.BCryptUtil;
import com.mika.music.utils.ResponseBodyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ResponseBodyMessage<Boolean> login(String userName, String password, HttpServletRequest request) {
        // 1. 进行参数校验
        // 2. 查询数据库, 对密码进行校验
        // 3. 密码校验成功，设置 session, 返回结果
        log.info("login，接收参数 userName: " + userName + ", password: " + password);
        if (!StringUtils.hasLength(userName) || !StringUtils.hasLength(password)) {
            return new ResponseBodyMessage<>(-2, "用户或密码不能为空", false);
        }
        User user = userService.getUserByName(userName);
        log.info("login，user: " + user);
        if (user == null || !BCryptUtil.verify(password, user.getPassword())) {
            return new ResponseBodyMessage<>(-1, "用户名或者密码错误", false);
        }
        user.setPassword("");
        request.getSession().setAttribute(Constant.USERINFO_SESSION_KEY, user);
        return new ResponseBodyMessage<>(200, null, true);
    }
}
