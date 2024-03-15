-- 数据库
drop database if exists `online_music`;
create database if not exists `online_music` character set utf8; -- 使用数据库
use `online_music`;

-- ⽤户表
DROP TABLE IF EXISTS online_music.user;
CREATE TABLE online_music.user(
         `id` INT NOT NULL AUTO_INCREMENT,
         `user_name` VARCHAR ( 128 ) NOT NULL,
         `password` VARCHAR ( 128 ) NOT NULL,
         `delete_flag` TINYINT ( 4 ) NULL DEFAULT 0,
         `create_time` DATETIME DEFAULT now(),
         `update_time` DATETIME DEFAULT now(),
         PRIMARY KEY ( id ),
UNIQUE INDEX user_name_UNIQUE ( user_name ASC )) ENGINE = INNODB DEFAULT
CHARACTER SET = utf8mb4 COMMENT = '用户表';

-- 音乐表
drop table if exists online_music.music;
CREATE TABLE online_music.music (
          `id` int NOT NULL AUTO_INCREMENT,
          `title` varchar(50) NOT NULL,
          `singer` varchar(30) NOT NULL,
          `create_time` DATETIME DEFAULT now(),
--          varchar(13) NOT NULL,
          `update_time` DATETIME DEFAULT now(),
          `delete_flag` TINYINT ( 4 ) NULL DEFAULT 0,
          `url` varchar(1000) NOT NULL,
          `user_id` int(11) NOT NULL,
          PRIMARY KEY (id))
 ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '音乐表';

-- 收藏表
DROP TABLE IF EXISTS online_music.love_music;
CREATE TABLE online_music.love_music (
           `id` int PRIMARY KEY AUTO_INCREMENT,
           `user_id` int(11) NOT NULL,
           `music_id` int(11) NOT NULL,
           `delete_flag` TINYINT ( 4 ) NOT NULL DEFAULT 0,
           `create_time` DATETIME DEFAULT now(),
           `update_time` DATETIME DEFAULT now());

-- 123 是密码
INSERT INTO user(user_name,password) VALUES("mika","$2a$10$hSQvbzhZdwvq4dm/uVHYQe8eh74CEWz7tHqjYDdlzxLS51UB1E1Qu");
INSERT INTO user(user_name,password) VALUES("zhangsan","$2a$10$hSQvbzhZdwvq4dm/uVHYQe8eh74CEWz7tHqjYDdlzxLS51UB1E1Qu");
INSERT INTO user(user_name,password) VALUES("lisi","$2a$10$hSQvbzhZdwvq4dm/uVHYQe8eh74CEWz7tHqjYDdlzxLS51UB1E1Qu");
INSERT INTO user(user_name,password) VALUES("wangwu","$2a$10$hSQvbzhZdwvq4dm/uVHYQe8eh74CEWz7tHqjYDdlzxLS51UB1E1Qu");
INSERT INTO user(user_name,password) VALUES("zhaoliu","$2a$10$hSQvbzhZdwvq4dm/uVHYQe8eh74CEWz7tHqjYDdlzxLS51UB1E1Qu");