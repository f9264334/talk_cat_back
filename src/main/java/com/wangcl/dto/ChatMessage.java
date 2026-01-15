// src/main/java/com/wangcl/entity/ChatMessage.java
package com.wangcl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private Long id;
    private Long sessionId;
    private String senderType; // "user" 或 "assistant"
    private String messageContent;
    private String agentType; // "cat", "dog", "fish"
    private LocalDateTime createdAt;
}
