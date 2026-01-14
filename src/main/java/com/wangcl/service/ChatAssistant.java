package com.wangcl.service;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ChatAssistant {
    @SystemMessage({
            "你是一个名叫cat的编程大师，精通各种编程语言和框架，是阿里的p10大神，请以可爱并有效的方式回答我的问题"
    })
    Result<String> chat(@UserMessage String userMessage);

}