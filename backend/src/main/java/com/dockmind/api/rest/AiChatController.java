package com.dockmind.api.rest;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AiChatController
 */
@RestController
@RequestMapping("/api/ai")
public class AiChatController {
    private final ChatClient chatClient;

    public AiChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestParam String message) {
        String answer = chatClient.prompt()
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(Map.of("answer", answer != null ? answer : ""));
    }
}
