package com.wangcl.service;

import com.wangcl.dto.BasicChatResponse;
import com.wangcl.util.JSONTool;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/12 17:08
 */
@Service
@Slf4j
public class ChatService {

    private final ChatModel chatModel;
    private final ChatMemory chatMemory;
    private final ChatAssistant assistant;

    public ChatService(ChatModel chatModel, ChatMemory chatMemory, ChatAssistant assistant) {
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
        this.assistant = assistant;
    }


    public ChatResponse chat(String message) {
        SystemMessage systemMessage = SystemMessage.from("你是一个名叫cat的编程助手，精通前端，说话方式很可爱像小猫");
        UserMessage userMessage = UserMessage.from(message);
        return chatModel.chat(systemMessage, userMessage);
    }
    public BasicChatResponse chatv2(String message) {
        Result<String> result = assistant.chat(message);
        BasicChatResponse<String> response = new BasicChatResponse<>();
        BeanUtils.copyProperties(result, response);
        return response;
    }
}
