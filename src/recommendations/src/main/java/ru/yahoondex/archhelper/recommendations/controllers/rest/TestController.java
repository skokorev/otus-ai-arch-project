package ru.yahoondex.archhelper.recommendations.controllers.rest;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TestController {
    private final ChatClient chatClient;
    @Autowired
    public TestController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public String dummyResponse() {
        return chatClient.prompt("Группа 111").call().content();
    }
}