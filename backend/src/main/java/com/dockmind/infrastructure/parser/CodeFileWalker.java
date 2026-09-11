package com.dockmind.infrastructure.parser;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CodeFileWalker {
    private static final Set<String> IGNORED_DIRECTORIES = Set.of(
            ".git",
            "node_modules",
            "target",
            "build",
            "dist",
            ".gradle",
            ".idea",
            ".vscode",
            "__pycache__",
            "vendor",
            ".next");
    private static final Set<String> IGNORED_FILES = Set.of(
            "package-lock.json",
            "yarn.lock",
            "pnpm-lock.yaml",
            "go.sum",
            ".DS_Store");

    private static final Set<String> IGNORED_EXTENSIONS = Set.of(
            ".class",
            ".jar",
            ".war",
            ".png",
            ".jpg",
            ".jpeg",
            ".gif",
            ".ico",
            ".pdf",
            ".zip",
            ".tar",
            ".gz",
            ".mp4",
            ".woff",
            ".woff2",
            ".ttf",
            ".map",
            ".min.js");

    public List<SourceFile> walk(Path root) throws IOException {
        List<SourceFile> sourceFiles = new ArrayList<>();

        // Walk the file tree and collect source files
        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (dir.getFileName() != null && IGNORED_DIRECTORIES.contains(dir.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE; // Skip ignored directories
                }
                return FileVisitResult.CONTINUE; // Continue visiting directories
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    // Check if the file should be skipped
                    if (shouldSkip(file, attrs)) {
                        return FileVisitResult.CONTINUE;
                    }

                    // Read the file contents
                    byte[] bytes = Files.readAllBytes(file);

                    // Check if the file is binary
                    if (isBinary(bytes)) {
                        return FileVisitResult.CONTINUE;
                    }

                    // Convert the file contents to a string
                    String content = new String(bytes, StandardCharsets.UTF_8);
                    // Get the relative path of the file
                    String relativePath = root.relativize(file).toString().replace("\\", "/");
                    // Add the file to the source files list
                    sourceFiles.add(new SourceFile(file, relativePath, content));
                } catch (IOException e) {
                    // skip unreadable file
                }

                return FileVisitResult.CONTINUE;
            }
        });
        return sourceFiles;
    }

    private boolean shouldSkip(Path file, BasicFileAttributes attrs) {
        // Check if the file should be skipped based on its name or size
        String fileName = file.getFileName().toString();

        // Check if the file is in the ignored files list
        if (IGNORED_FILES.contains(fileName)) {
            return true;
        }

        // Check if the file size exceeds the limit
        if (attrs.size() > 1_000_000) {
            return true;
        }

        // Check if the file has an ignored extension
        String lower = fileName.toLowerCase();
        return IGNORED_EXTENSIONS.stream().anyMatch(lower::endsWith); // Return true if the file has an ignored
                                                                      // extension
    }

    // Check if the file is binary (contains null bytes)
    private boolean isBinary(byte[] bytes) {
        int limit = Math.min(bytes.length, 1024); // Check first 1024 bytes
        for (int i = 0; i < limit; i++) { // Check each byte
            if (bytes[i] == 0) { // Found a null byte
                return true;
            }
        }
        return false;
    }

}
