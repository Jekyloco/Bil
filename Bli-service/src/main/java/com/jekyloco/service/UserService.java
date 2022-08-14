package com.jekyloco.service;

import com.jekyloco.domain.User;
import com.jekyloco.domain.UserInfo;

import java.util.List;
import java.util.Set;

public interface UserService {

    String getName(Long id);

//    User getUserByPhone(String phone);

    void addUser(User user);

    String login(User user) throws Exception;

    User getUserById(Long userId);

    void updateUsers(User user) throws Exception;

    void updateUserInfos(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserId(Set<Long> userIdList);

}
