package com.jekyloco.service;

import com.jekyloco.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public String getName(Long id) {
        return userMapper.getName(id);
    }
}
