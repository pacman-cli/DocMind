package com.dockmind.api.dto.response;

import java.util.List;

public record RagAskResponse(
        String answer,
        List<RagSourceResponse> sources
) {
}
