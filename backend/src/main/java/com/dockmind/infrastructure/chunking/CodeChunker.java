package com.dockmind.infrastructure.chunking;

import com.dockmind.infrastructure.parser.SourceFile;

import java.util.List;
import java.util.UUID;

public interface CodeChunker {
    List<CodeChunk> chunk(SourceFile file, UUID repositoryId, String branch);
}
