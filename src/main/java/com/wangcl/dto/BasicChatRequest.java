package com.wangcl.dto;

import lombok.Data;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/12 17:15
 */
@Data
public class BasicChatRequest {
    private String message;
    private String agent;
}
