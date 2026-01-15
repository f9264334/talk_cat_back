package com.wangcl.service;

import com.wangcl.dao.entity.User;
import com.wangcl.dao.mapper.UserMapper;
import com.wangcl.dao.store.InMemoryKVStore;
import com.wangcl.dto.LoginRequest;
import com.wangcl.dto.RegisterRequest;
import com.wangcl.dto.UserRequest;
import com.wangcl.util.JSONTool;
import com.wangcl.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/15 14:01
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private InMemoryKVStore kvStore;
    @Transactional
    public String register(RegisterRequest request){
        if (existsByUsername(request.getUserName())) {
            return null; // 用户名已存在
        }

        User user = new User();
        user.setUsername(request.getUserName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());



        userMapper.insert(user);
        String token = jwtTokenProvider.generateToken(user);
        if (token != null){
            kvStore.set(token, JSONTool.writeToString(user));
        }
        return token;

    }

    public String login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUserName());
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            String token = jwtTokenProvider.generateToken(user);
            if (token != null){
                kvStore.set(token, JSONTool.writeToString(user));
            }
            return token;
        }
        return null;
    }
    public User findByUsername(String username){
        return userMapper.findByUsername(username);
    }
    boolean existsByUsername(String username){
        return userMapper.existsByUsername(username);
    }
}
