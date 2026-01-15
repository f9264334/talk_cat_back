package com.wangcl.config;

import com.wangcl.dao.entity.User;
import com.wangcl.dao.store.InMemoryKVStore;
import com.wangcl.util.JSONTool;
import com.wangcl.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtUtil;

    @Autowired
    private InMemoryKVStore kvStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 1. 获取Header中的Token
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. 解析Token（格式：Bearer {token}）
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // 去掉Bearer前缀
            try {
                username = jwtUtil.getUsernameFromJWT(jwt); // 从Token获取用户名
            } catch (Exception e) {
                logger.error("解析JWT Token失败", e);
            }
        }

        // 3. 验证Token并设置认证对象
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = JSONTool.parse((String) kvStore.get(jwt), User.class);
            // 验证Token有效
            if (userDetails != null) {
                // 生成认证对象（用户名+权限+已认证）
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, null);
                // 设置请求详情
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 存入SecurityContextHolder，后续authenticated()会读取这个对象
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // 4. 继续执行过滤器链
        chain.doFilter(request, response);
    }
}