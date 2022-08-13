package com.jekyloco.controller;

import com.jekyloco.controller.support.UserSupport;
import com.jekyloco.domain.JsonResponse;
import com.jekyloco.domain.User;
import com.jekyloco.service.UserService;
import com.jekyloco.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * HTTP中的POST方法不具有幂等性
 * Restful接口URL命名规则
 * 1. HTTP方法后跟的URL必须是名词且统一成名词复数形式
 * 2. URL中不采用大小写混合的驼峰命名，尽量采用小写单词，使用‘-’连接多个单词
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;

    @GetMapping("/users")
    public JsonResponse<User> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserById(userId);
        return new JsonResponse<>(user);
    }


    @GetMapping("/ras-pks")
    public JsonResponse<String> getRsaPublicKey() {
        String publicKey = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(publicKey);
    }

    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    @PostMapping("/users-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

}
