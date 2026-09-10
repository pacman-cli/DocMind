package com.dockmind.api.dto.response;

import java.util.Map;

public record RagSourceResponse(
        String content,
        Map<String, Object> metadata
) {
}
