package com.dockmind.infrastructure.chunking;

import com.dockmind.infrastructure.parser.SourceFile;
import org.springframework.stereotype.Service;

/**
 * CodeChunkerFactory
 */
@Service
public class CodeChunkerFactory {

    public CodeChunker forFile(SourceFile file) {
        return new DefaultTextCodeChunker();
    }

    public CodeChunker toFile(SourceFile file) {
        return forFile(file);
    }
}
