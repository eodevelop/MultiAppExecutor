package com.multi.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DirectoryManager {
    public static void createIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void list() throws IOException {
        Path currentPath = Paths.get("./logs").toAbsolutePath();

        try (Stream<Path> paths = Files.list(currentPath)) {
            paths.filter(Files::isDirectory)
                    .forEach(path -> System.out.println("  - " + path.getFileName()));
        }
    }
}
