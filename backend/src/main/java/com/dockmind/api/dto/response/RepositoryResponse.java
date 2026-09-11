package com.dockmind.api.dto.response;

import java.util.UUID;

public record RepositoryResponse(
        UUID id,
        String name,
        String gitUrl,
        String branch,
        String status) {
}
