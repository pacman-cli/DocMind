package com.dockmind;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ChatClientTest {

    @Autowired
    private ChatClient chatClient;

    @Test
    void testChat() {
        String answer = chatClient.prompt()
                .user("Say hello in one word")
                .call()
                .content();
        System.out.println("Chat response: " + answer);
        assertNotNull(answer);
    }
}
