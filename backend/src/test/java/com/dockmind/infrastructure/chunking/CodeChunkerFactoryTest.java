package com.dockmind.infrastructure.chunking;

import com.dockmind.infrastructure.parser.SourceFile;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CodeChunkerFactoryTest {

    @Test
    void shouldCreateChunkerAndProduceChunks() {
        CodeChunkerFactory factory = new CodeChunkerFactory();
        SourceFile sourceFile = new SourceFile(
                Path.of("/test/Sample.java"),
                "Sample.java",
                "line 1\nline 2\nline 3");

        CodeChunker chunkerFor = factory.forFile(sourceFile);
        assertNotNull(chunkerFor);

        CodeChunker chunkerTo = factory.toFile(sourceFile);
        assertNotNull(chunkerTo);

        UUID repoId = UUID.randomUUID();
        List<CodeChunk> chunks = chunkerFor.chunk(sourceFile, repoId, "main");
        assertEquals(1, chunks.size());
        assertEquals("line 1\nline 2\nline 3", chunks.get(0).content());
        assertEquals("java", chunks.get(0).metadata().get("language"));
        assertEquals("Sample.java", chunks.get(0).metadata().get("filePath"));
    }
}
