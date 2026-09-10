package com.dockmind.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RagAskRequest(
        @NotBlank String question
) {
}
