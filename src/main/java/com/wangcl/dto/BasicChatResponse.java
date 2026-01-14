package com.wangcl.dto;

import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.tool.ToolExecution;
import lombok.Data;

import java.util.List;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/14 17:36
 */
@Data
public class BasicChatResponse<T> {
    private  T content;
    private  TokenUsage tokenUsage;
    private  List<Content> sources;
    private  FinishReason finishReason;
    private  List<ToolExecution> toolExecutions;
    private  List<ChatResponse> intermediateResponses;
    private  ChatResponse Response;
}
