package com.jekyloco.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Video {
    private Long id;
    private Long userId;
    private String url;
    private String thumbnail;   //封面
    private String title;
    private String type;    //类型 自制or转载
    private String duration;        //时长
    private String area;        //分区 鬼畜 音乐 电影
    private String description;
    private Date createTime;
    private Date updateTime;
    private List<VideoTag> videoTags;   //标签列表
}
