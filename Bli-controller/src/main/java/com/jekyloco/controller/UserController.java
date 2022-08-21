package com.jekyloco.controller;

import com.alibaba.fastjson.JSONObject;
import com.jekyloco.controller.support.UserSupport;
import com.jekyloco.domain.JsonResponse;
import com.jekyloco.domain.PageResult;
import com.jekyloco.domain.User;
import com.jekyloco.domain.UserInfo;
import com.jekyloco.service.UserFollowingService;
import com.jekyloco.service.UserService;
import com.jekyloco.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * HTTP中的POST方法不具有幂等性
 * Restful接口URL命名规则
 * 1. HTTP方法后跟的URL必须是名词且统一成名词复数形式
 * 2. URL中不采用大小写混合的驼峰命名，尽量采用小写单词，使用‘-’连接多个单词
 */
@CrossOrigin(value = {"*"})
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserFollowingService userFollowingService;

    /**
     * 获取当前登录用户的信息
     *
     * @return
     */
    @GetMapping("/users")
    public JsonResponse<User> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserById(userId);
        return new JsonResponse<>(user);
    }


    /**
     * 公钥接口 提供给前端对密码进行RSA加密
     *
     * @return
     */
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey() {
        String publicKey = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(publicKey);
    }

    /**
     * 注册
     * 接受用户信息
     * 账号（手机号）
     * 密码（经由前端RSA加密传递给后端、后端解密后使用MD5加密存入数据库）
     *
     * @param user
     * @return
     */
    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    /**
     * 登录
     * 接受用户账号密码
     * 验证后返回token
     * 用户进行操作行为时通过token来验证用户并获取用户登录信息
     *
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/users-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    /**
     * 修改账号密码
     *
     * @param user
     * @return
     * @throws Exception
     */
    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return JsonResponse.success();
    }

    /**
     * 修改用户基本信息
     *
     * @param userInfo
     * @return
     */
    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) {
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }

    /**
     * 分页查询用户信息
     */
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(Integer no, Integer size, String nick) {
        Long userId = userSupport.getCurrentUserId();
        //public class JSONObject extends JSON implements Map<String,Object>
        JSONObject params = new JSONObject();
        params.put("no", no);
        params.put("size", size);
        params.put("nick", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.pageListUserInfos(params);
        if (result.getTotal() > 0) {
            List<UserInfo> checkUserInfoList = userFollowingService.checkFollowingStatus(result.getList(), userId);
            result.setList(checkUserInfoList);
        }
        return new JsonResponse<>(result);
    }

}
