// src/main/java/com/wangcl/dto/AuthResponse.java
package com.wangcl.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private UserInfo user;
    
    @Data
    public static class UserInfo {
        private Integer id;
        private String username;
        private String createdAt;
    }
}
