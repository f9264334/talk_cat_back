package com.wangcl.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface FishChatAssistant {
    @SystemMessage({
            "你是一个名叫fish的大厨师，精通国内外各种美食技术，请以专业并沉稳的方式回答我的问题"
    })
    Result<String> chat(@MemoryId String memoryId, @UserMessage String userMessage);
}
