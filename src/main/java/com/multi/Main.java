package com.multi;

import com.multi.config.JarFilePathsConfig;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        JarFilePathsConfig yamlConfig = new JarFilePathsConfig("application.yml");
        List<String> jarFilePaths = yamlConfig.getJarFilePaths();
        String workingDirectory = "C:\\workspace\\temp";
        String profile = "test";

        JarRunner jarRunner = new JarRunner(jarFilePaths, workingDirectory, profile);
        jarRunner.run();
    }
}