package com.multi;

import com.multi.config.JarFilePathsConfig;
import com.multi.model.JarInfo;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        JarFilePathsConfig yamlConfig = new JarFilePathsConfig("application.yml");
        List<JarInfo> jarInfos = yamlConfig.getJarInfos();
        String workingDirectory = ".\\";

        JarRunner jarRunner = new JarRunner(jarInfos, workingDirectory);
        jarRunner.run();
    }
}