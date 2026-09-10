package com.dockmind.api.rest;

import com.dockmind.api.dto.request.RagAskRequest;
import com.dockmind.api.dto.response.RagAskResponse;
import com.dockmind.api.dto.response.RagSourceResponse;
import com.dockmind.application.dto.RagAnswer;
import com.dockmind.application.service.SimpleRagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rag")
public class SimpleRagController {
    private final SimpleRagService simpleRagService;

    @PostMapping("/seed")
    public ResponseEntity<Map<String,Object>> seed() {
        int chunksStored = simpleRagService.seed();
        return ResponseEntity.ok(Map.of(
                "message", "Seed data stored",
                "chunksStored", chunksStored
        ));
    }

    @PostMapping("/ask")
    public ResponseEntity<RagAskResponse> ask(@Valid @RequestBody RagAskRequest ragAskRequest) {
        RagAnswer result = simpleRagService.ask(ragAskRequest.question());
        List<RagSourceResponse> sources = result.sources() == null ? List.of() :
                result.sources().stream().map(source -> new RagSourceResponse(source.content(),
                source.metadata())).toList();

        RagAskResponse response = new RagAskResponse(result.answer(), sources);

        return ResponseEntity.ok(response);
    }
}
