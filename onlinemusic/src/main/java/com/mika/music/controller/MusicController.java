package com.mika.music.controller;

import com.mika.music.constants.Constant;
import com.mika.music.model.Music;
import com.mika.music.model.User;
import com.mika.music.service.LoveMusicService;
import com.mika.music.service.MusicService;
import com.mika.music.utils.Mp3Util;
import com.mika.music.utils.ResponseBodyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private MusicService musicService;

    @Autowired
    private LoveMusicService loveMusicService;

    @Value("${music.local.path}")
    private String SAVE_PATH;

    // 音乐 上传到服务器+上传到数据库
    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> addMusic(@RequestParam String singer, @RequestParam("filename") MultipartFile file, HttpServletRequest request, HttpServletResponse resp) throws IOException {
        // 检查是否登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            return new ResponseBodyMessage<>(-2, "请登录后再操作", false);
        }
        if (!StringUtils.hasLength(singer) || file == null) {
            return new ResponseBodyMessage<>(-1, "请输入歌手后再来操作！", false);
        }

        // 上传到服务器
        // 获取文件的绝对路径
        String fileNameAndType = file.getOriginalFilename();
        log.info("获取路径，fileNameAndType: " + fileNameAndType);
        if (!StringUtils.hasLength(fileNameAndType)) {
            return new ResponseBodyMessage<>(-1, "请输入歌手后再来操作！", false);
        }
        String path = SAVE_PATH + fileNameAndType;
        // 获取曲名
        int point = fileNameAndType.lastIndexOf(".");
        String title = fileNameAndType.substring(0, point);
        // 先判断曲库里面是否已经存在这首歌, 如果存在则判断歌手是否相同，不存在直接插入即可
        Music music = musicService.getSongByName2(title);
        User user = (User) request.getSession().getAttribute(Constant.USERINFO_SESSION_KEY);
        if ((music != null && music.getUserId() == user.getId()) && music.getDeleteFlag() == 0) {
            // 相同曲子相同歌手还没删除歌曲
            return new ResponseBodyMessage<>(-1, "该曲子在曲库中已存在", false);
        } else if (music != null && music.getDeleteFlag() != 0) {
            // 曲子存在，但是被逻辑删除了
            // 将 delete_flag 改为 0
            Integer result = musicService.updateDeleteFlag(music.getId(), user.getId());
            if (result > 0) {
                resp.sendRedirect("/list.html");
                return new ResponseBodyMessage<>(200, null, true);
            } else {
                return new ResponseBodyMessage<>(-1, "服务器上传失败，请联系管理员", false);
            }
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
            log.error(e.getMessage());
            return new ResponseBodyMessage<>(-1, "服务器上传失败，请联系管理员", false);
        }
        // 判断该文件是否是歌曲
        try {
            if (!Mp3Util.isMp3File(path)) {
                dest.delete();
                return new ResponseBodyMessage<>(-1, "该文件不是mp3格式", false);
            }
        } catch (Exception e) {
            dest.delete();
            log.error(e.getMessage());
            return new ResponseBodyMessage<>(-1, "该文件不是mp3格式", false);
        }

        // 数据库上传存路径时，title(歌名) 没有加后缀.mp3
        String url = "/music/get?path=" + title;
        Integer result = -1;
        try {
            result = musicService.addMusic(new Music(title, singer, url, user.getId()));
            if (result > 0) {
                resp.sendRedirect("/list.html");
                return new ResponseBodyMessage<>(200, null, true);
            } else {
                dest.delete();
                return new ResponseBodyMessage<>(-1, "数据库上传失败，请联系管理员", false);
            }
        } catch (Exception e) {
            dest.delete();
            log.error(e.getMessage());
            return new ResponseBodyMessage<>(-1, "数据库上传失败，请联系管理员", false);
        }
    }


    // 播放音乐时，路径为 /music/get?path=xxx.mp3
    @RequestMapping("/get")
    public ResponseEntity<byte[]> getMusic(String path) {
        File file = new File(SAVE_PATH + path);
        byte[] arr = null;
        try {
            arr = Files.readAllBytes(file.toPath());
            if (arr == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(arr);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> delete(Integer id) {
        if (id == null || id < 1) {
            return new ResponseBodyMessage<>(-1, "id 不合法", false);
        }
        Integer result = musicService.deleteById(id);

        if (result > 0) {
            Integer ret = loveMusicService.deleteLovedByMusicId(id);
            return new ResponseBodyMessage<>(200, null, true);
        }
        return new ResponseBodyMessage<>(-1, "删除失败", false);
    }


    @RequestMapping("/deleteSel")
    public ResponseBodyMessage<Boolean> deleteSelMusic(@RequestParam("id[]") List<Integer> id) {
        if (id == null || id.size() <= 0) {
            log.error("ids: " + id);
            return new ResponseBodyMessage<>(-1, "请选中歌曲后再来删除", false);
        }
        Integer result = musicService.deleteByIds(id);
        if (result > 0) {
            Integer ret = loveMusicService.deleteSelLoved(id);
            return new ResponseBodyMessage<>(200, null, true);
        }
        return new ResponseBodyMessage<>(-1, "批量删除失败", false);
    }

    @RequestMapping("/getMusics")
    public ResponseBodyMessage<List<Music>> getMusics(String musicName) {
        if (!StringUtils.hasLength(musicName)) {
            // 音乐名为空，查询所有音乐
            List<Music> result = musicService.getMusics();
            return new ResponseBodyMessage<>(200, null, result);
        }
        List<Music> result = musicService.getMusicsByName(musicName);
        return new ResponseBodyMessage<>(200, null, result);
    }


}
