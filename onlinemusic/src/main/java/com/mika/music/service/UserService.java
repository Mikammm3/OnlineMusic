package com.mika.music.service;

import com.mika.music.mapper.UserMapper;
import com.mika.music.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }
}
