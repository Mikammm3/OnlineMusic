package com.mika.music.controller;

import com.mika.music.constants.Constant;
import com.mika.music.model.Music;
import com.mika.music.model.User;
import com.mika.music.service.MusicService;
import com.mika.music.utils.ResponseBodyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private MusicService musicService;

    @Value("${music.local.path}")
    private String SAVE_PATH;

    // 上传到服务器+上传到数据库
    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> addMusic(@RequestParam String singer, @RequestParam("filename") MultipartFile file, HttpServletRequest request) {
        // 检查是否登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            return new ResponseBodyMessage<>(-2, "请登录后再操作", false);
        }
        // 上传到服务器
        String fileNameAndType = file.getOriginalFilename();// 获取文件的绝对路径
        log.info("获取路径，fileNameAndType: " + fileNameAndType);
        String path = SAVE_PATH + fileNameAndType;
        // 获取革命
        int point = fileNameAndType.lastIndexOf(".");
        String title = fileNameAndType.substring(0, point);
        // 先判断曲库里面是否已经存在这首歌, 如果存在则判断歌手是否相同，不存在直接插入即可
        Music music = musicService.getSongByName(title);
        User user = (User) request.getSession().getAttribute(Constant.USERINFO_SESSION_KEY);
        if (music == null || music.getUserId() == user.getId()) {
            // 相同曲子相同歌手
            return new ResponseBodyMessage<>(-1, "该曲子在曲库中已存在", false);
        }

        File dest = new File(path);
        if (!dest.exists()) {
            // 如果目录不存在，就创建目录
            dest.mkdir();
        }
        try {
            // 上传文件
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseBodyMessage<>(-1, "服务器上传失败，请联系管理员", false);
        }

        // 数据库上传
        String url = "/music/get/path=" + title;
        Integer result = -1;
        try {
            result = musicService.addMusic(new Music(title, singer, url, user.getId()));
            if (result > 0) return new ResponseBodyMessage<>(200, null, true);
            else return new ResponseBodyMessage<>(-1, "数据库上传失败，请联系管理员", false);
        } catch (Exception e) {
            dest.delete();
            log.error(e.getMessage());
            return new ResponseBodyMessage<>(-1, "数据库上传失败，请联系管理员", false);
        }
    }
}
