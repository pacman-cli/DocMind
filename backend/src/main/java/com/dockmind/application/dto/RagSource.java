package com.dockmind.application.dto;

import java.util.Map;

public record RagSource(
        String content,
        Map<String,Object> metadata
) {
}
