package com.jekyloco.controller;

import com.jekyloco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("getName")
    public String getName(Long id) {
        return userService.getName(id);
    }

}
