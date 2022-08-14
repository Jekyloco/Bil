package com.jekyloco.domain;

import lombok.Data;

import java.util.Date;

@Data
//用户关注信息
public class UserFollowing {

    private Long id;
    //用户id
    private Long userId;
    //关注人的id
    private Long followingId;
    //关注的分组
    private Long groupId;

    private Date createTime;

    private UserInfo userInfo;
}
