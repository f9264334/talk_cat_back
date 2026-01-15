package com.wangcl.controller;

import com.wangcl.dto.BasicChatRequest;
import com.wangcl.dto.Result;
import com.wangcl.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述：
 *
 * @author wangchenglong
 * @version 1.0
 * @date 2026/1/12 17:07
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/startChat")
    public Result startChat(@RequestBody BasicChatRequest request) {
        return Result.success(chatService.chatv2(request));
    }

    @PostMapping("/continueChat")
    public String continueChat(@RequestBody BasicChatRequest request) {
        return "abc"+request.getMessage();
    }
}
