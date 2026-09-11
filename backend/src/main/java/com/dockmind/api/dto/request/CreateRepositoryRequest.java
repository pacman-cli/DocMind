package com.dockmind.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateRepositoryRequest(
        @NotBlank String name,
        @NotBlank String gitUrl,
        String branch) {
}
