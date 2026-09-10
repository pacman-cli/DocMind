package com.dockmind.application.dto;

import java.util.List;

public record RagAnswer(
        String answer,
        List<RagSource> sources
) {
}
