package com.multi.utils;

import java.io.File;

public class DirectoryManager {
    public static void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
