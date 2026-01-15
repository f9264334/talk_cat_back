// src/main/java/com/wangcl/entity/ChatSession.java
package com.wangcl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSession {
    private Long id;
    private Long userId;
    private String sessionName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
