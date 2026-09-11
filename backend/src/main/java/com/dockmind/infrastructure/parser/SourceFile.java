package com.dockmind.infrastructure.parser;

import java.nio.file.Path;

public record SourceFile(
        Path fullPath,
        String relativePath,
        String content
) {
}
