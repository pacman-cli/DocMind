package com.dockmind.api.dto.response;

import java.util.UUID;

public record IngestionStatusResponse(
        UUID jobId,
        String status,
        Integer chunksProcessed,
        String errorMessage
) {
}
