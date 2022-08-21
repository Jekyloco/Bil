package com.jekyloco.dao;

import com.alibaba.fastjson.JSONObject;
import com.jekyloco.domain.User;
import com.jekyloco.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserDao {
    String getName(Long id);

    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoById(Long userId);

    Integer upDateUsers(User user);

    Integer updateUserInfos(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

    Integer pageCountUserInfo(Map<String, Object> params);

    List<UserInfo> pageListUserInfos(Map<String, Object> params);
}
