package com.wangcl.controller;

import com.wangcl.dto.LoginRequest;
import com.wangcl.dto.RegisterRequest;
import com.wangcl.dto.Result;
import com.wangcl.dto.UserRequest;
import com.wangcl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/15 13:36
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
}
