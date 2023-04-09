package com.multi.executor;

import java.io.File;
import java.io.IOException;

public class JarExecutor {
    private final String jarFilePath;
    private final String workingDirectory;
    private final String profile;

    public JarExecutor(String jarFilePath, String workingDirectory, String profile) {
        this.jarFilePath = jarFilePath;
        this.workingDirectory = workingDirectory;
        this.profile = profile;
    }

    public Process start() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarFilePath);
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(new File(workingDirectory));
        processBuilder.environment().put("SPRING_PROFILES_ACTIVE", profile);

        return processBuilder.start();
    }
}