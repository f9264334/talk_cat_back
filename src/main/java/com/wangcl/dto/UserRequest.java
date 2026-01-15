package com.wangcl.dto;

import lombok.Data;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/15 14:03
 */
@Data
public class UserRequest {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private String phone;
}
