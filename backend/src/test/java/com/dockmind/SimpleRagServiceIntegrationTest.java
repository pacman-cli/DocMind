package com.dockmind;

import com.dockmind.application.service.SimpleRagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class SimpleRagServiceIntegrationTest {

    @Autowired
    private SimpleRagService simpleRagService;

    @Test
    void testSeedAndAsk() {
        int chunks = simpleRagService.seed();
        System.out.println("Seeded chunks: " + chunks);
        var answer = simpleRagService.ask("What is DockMind?");
        System.out.println("Answer: " + answer.answer());
        assertNotNull(answer.answer());
    }
}
