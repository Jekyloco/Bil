package com.jekyloco.service;

import com.jekyloco.domain.User;

public interface UserService {

    String getName(Long id);

//    User getUserByPhone(String phone);

    void addUser(User user);

    String login(User user) throws Exception;

    User getUserById(Long userId);


}
