package io.muzoo.ssc;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MyFileVisitor implements FileVisitor<Path> {
    private int totalFiles = 0;
    private int totalDirs = 0;
    private Set<String> uniqueExtensions = new HashSet<>();
    private Map<String, Integer> extensionCount = new HashMap<>();
    private int totalLetterAInHtmlFiles = 0;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        totalDirs++;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        totalFiles++;

        if (Files.isRegularFile(file) && file.toString().toLowerCase().endsWith(".html")) {
            try {
                String content = new String(Files.readAllBytes(file));
                totalLetterAInHtmlFiles += content.chars().filter(ch -> ch == 'a').count();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String extension = getFileExtension(file);
        uniqueExtensions.add(extension);
        extensionCount.put(extension, extensionCount.getOrDefault(extension, 0) + 1);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    private String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public int getTotalFiles() {
        return totalFiles;
    }

    public int getTotalDirs() {
        return totalDirs;
    }

    public Set<String> getUniqueExtensions() {
        return uniqueExtensions;
    }

    public Map<String, Integer> getExtensionCount() {
        return extensionCount;
    }

    public int getTotalLetterAInHtmlFiles() {
        return totalLetterAInHtmlFiles;
    }
}

