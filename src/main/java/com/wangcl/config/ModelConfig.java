package com.wangcl.config;

import com.wangcl.service.ChatAssistant;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/12 17:09
 */
@Configuration
public class ModelConfig{

    @Value("${ali.apikey}")
    private String AliApiKey;
    @Bean
    public ChatModel chatModel() {
        QwenChatModel qwenChatModel = QwenChatModel
                .builder()
                .modelName("qwen-plus")
                .apiKey(AliApiKey)
                .maxTokens(1000)
                .temperature(1.0f)
                .build();
        return qwenChatModel;
    }
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .id("assistant-chat-memory-001")
                .build();
    }

    @Bean(name = "assistant")
    public ChatAssistant assistant(ChatMemory chatMemory, QwenChatModel qwenChatModel) {
        return AiServices.builder(ChatAssistant.class)
                .chatModel(qwenChatModel)
                .chatMemory(chatMemory) // 绑定记忆组件
                .build();
    }
}
