package com.dockmind.infrastructure.chunking;

import com.dockmind.infrastructure.parser.SourceFile;

import java.util.*;

public class DefaultTextCodeChunker implements CodeChunker {
    private static final int MAX_LINES = 120;
    private static final int OVERLAP = 20;

    @Override
    public List<CodeChunk> chunk(SourceFile file, UUID repositoryId, String branch) {
        String[] lines = file.content().split("\n");
        List<CodeChunk> chunks = new ArrayList<>();

        int step = MAX_LINES - OVERLAP;
        for (int i = 0; i < lines.length; i += step) {
            int end = Math.min(i + MAX_LINES, lines.length);
            if (i >= end) {
                break;
            }
            String chunkContent = String.join(
                    "\n",
                    Arrays.copyOfRange(lines, i, end));

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("repositoryId", repositoryId);
            metadata.put("branch", branch);
            metadata.put("filePath", file.relativePath());
            metadata.put("language", detectLanguage(file.relativePath()));
            metadata.put("startLine", i + 1);
            metadata.put("endLine", end);

            chunks.add(new CodeChunk(chunkContent, metadata));
            if (end == lines.length) {
                break;
            }
        }
        return chunks;
    }

    private String detectLanguage(String s) {
        if (s.endsWith(".java"))
            return "java";
        if (s.endsWith(".kt"))
            return "kotlin";
        if (s.endsWith(".ts"))
            return "typescript";
        if (s.endsWith(".tsx"))
            return "typescript";
        if (s.endsWith(".js"))
            return "javascript";
        if (s.endsWith(".py"))
            return "python";
        if (s.endsWith(".go"))
            return "go";
        if (s.endsWith(".rb"))
            return "ruby";
        if (s.endsWith(".cs"))
            return "csharp";
        return "text";
    }
}
