package com.dockmind.infrastructure.chunking;

import java.util.Map;

public record CodeChunk(String content, Map<String, Object> metadata) {}
