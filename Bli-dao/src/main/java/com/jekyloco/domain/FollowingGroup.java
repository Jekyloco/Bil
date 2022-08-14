package com.jekyloco.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户关注分组表实体类
 */
@Data
public class FollowingGroup {
    private Long id;

    private Long userId;
    //关注分组名称
    private String name;
    //关注分组类型
    private String type;

    private Date createTime;

    private Date updateTime;

    private List<UserInfo> followingUserInfoList;
}
