package com.mika.music.controller;

import com.mika.music.constants.Constant;
import com.mika.music.model.Music;
import com.mika.music.model.User;
import com.mika.music.service.LoveMusicService;
import com.mika.music.utils.ResponseBodyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lovemusic")
public class LoveMusicController {

    @Autowired
    private LoveMusicService loveMusicService;


    @RequestMapping("/like")
    public ResponseBodyMessage<Boolean> like(Integer musicId, HttpServletRequest request) {
        // 检查是否登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            return new ResponseBodyMessage<>(-2, "请登录后再操作", false);
        }
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        Integer userId = user.getId();
        log.info("userId: " + userId + ", musicId: " + musicId);
        if (userId == null || musicId == null || userId < 1 || musicId < 1) {
            return new ResponseBodyMessage<>(-1, "userId 或 musicId 不合法", false);
        }
        // 1. 检查是否已经收藏过该音乐
        Music ret = loveMusicService.checkLoved(musicId, userId);
        if (ret != null) {
            Integer result = loveMusicService.deleteLoved(musicId, userId);
            return new ResponseBodyMessage<>(-1, "取消收藏成功", false);
        }
        // 2. 添加音乐至收藏列表
        Integer result = loveMusicService.insert(userId, musicId);
        if (result > 0) {
            return new ResponseBodyMessage<>(200, null, true);
        }
        return new ResponseBodyMessage<>(-1, "收藏失败", false);
    }

    // 查找指定用户的收藏列表中的指定歌曲
    @RequestMapping("/get")
    public ResponseBodyMessage<List<Music>> getMusic(String musicName, HttpServletRequest request) {
        // 检查是否登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            return new ResponseBodyMessage<>(-2, "请登录后再操作", null);
        }

        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        Integer userId = user.getId();
        log.info("userId: " + userId);
        if (userId == null || userId < 1) {
            return new ResponseBodyMessage<>(-1, "userId 或 musicId 不合法", null);
        }

        // 如果歌名为空，则查询所有列表
        if (!StringUtils.hasLength(musicName)) {
            return new ResponseBodyMessage<>(200, null, loveMusicService.getLovedMusics(userId));
        }

        List<Music> ret = loveMusicService.getLovedMusicsByName(musicName, userId);
        if (ret == null || ret.size() <= 0) {
            return new ResponseBodyMessage<>(-1, "没有你要找的歌曲", ret);
        }
        return new ResponseBodyMessage<>(200, null, ret);

    }

}
