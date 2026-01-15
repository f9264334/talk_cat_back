package com.wangcl.controller;

import com.wangcl.dao.entity.User;
import com.wangcl.dao.store.InMemoryKVStore;
import com.wangcl.dto.AuthResponse;
import com.wangcl.dto.LoginRequest;
import com.wangcl.dto.RegisterRequest;
import com.wangcl.dto.Result;
import com.wangcl.service.UserService;
import com.wangcl.util.JSONTool;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private InMemoryKVStore kvStore;

    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        if (token != null) {
            // 这里需要构建用户信息
            String userinfoString = (String) kvStore.get("token");
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
            User user;
            if (userinfoString != null) {
                user = JSONTool.parse(userinfoString, User.class);
            } else {
                user = userService.findByUsername(request.getUserName());
            }
            userInfo.setUsername(user.getUsername());
            userInfo.setId(user.getId());
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setUser(userInfo);

            return Result.success(authResponse);
        } else {
            return Result.builder()
                    .code(401)
                    .message("账号或密码错误")
                    .build();
        }
    }

    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterRequest request) {
        String token = userService.register(request);
        if (token != null) {
            // 这里需要构建用户信息
            String userinfoString = (String) kvStore.get("token");
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
            User user = JSONTool.parse(userinfoString, User.class);
            userInfo.setUsername(user.getUsername());
            userInfo.setId(user.getId());
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setUser(userInfo);

            return Result.success(authResponse);
        } else {
            return Result.builder()
                    .code(400)
                    .message("账号已存在")
                    .build();
        }
    }
}

