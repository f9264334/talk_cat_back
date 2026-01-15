package com.wangcl.service;

import com.wangcl.config.UserContext;
import com.wangcl.dao.store.InMemoryKVStore;
import com.wangcl.dto.BasicChatRequest;
import com.wangcl.dto.BasicChatResponse;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.wangcl.common.AgentEnum.*;

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
    private final CatChatAssistant catChatAssistant;
    private final DogChatAssistant dogChatAssistant;
    private final FishChatAssistant fishChatAssistant;
    private final InMemoryKVStore kvStore;

    public ChatService(ChatModel chatModel, CatChatAssistant catChatAssistant, DogChatAssistant dogChatAssistant, FishChatAssistant fishChatAssistant, InMemoryKVStore kvStore) {
        this.chatModel = chatModel;
        this.catChatAssistant = catChatAssistant;
        this.dogChatAssistant = dogChatAssistant;
        this.fishChatAssistant = fishChatAssistant;
        this.kvStore = kvStore;
    }


    public ChatResponse chat(String message) {
        SystemMessage systemMessage = SystemMessage.from("你是一个名叫cat的编程助手，精通前端，说话方式很可爱像小猫");
        UserMessage userMessage = UserMessage.from(message);
        return chatModel.chat(systemMessage, userMessage);
    }
    public BasicChatResponse chatv2(BasicChatRequest request) {
        int id = UserContext.getCurrentUser().getId();
        Result<String> result;
        switch (request.getAgent()) {
            case CAT:
                result= catChatAssistant.chat(String.valueOf(id),request.getMessage());
                break;
            case DOG:
                result = dogChatAssistant.chat(String.valueOf(id),request.getMessage());
                break;
            case FISH:
                result = fishChatAssistant.chat(String.valueOf(id),request.getMessage());
                break;
            default:
                throw new IllegalArgumentException("Invalid pet type");
        }
        BasicChatResponse<String> response = new BasicChatResponse<>();
        BeanUtils.copyProperties(result, response);
        return response;
    }
}
