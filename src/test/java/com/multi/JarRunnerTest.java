package com.multi;

import com.multi.config.JarFilePathsConfig;
import com.multi.model.JarInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JarRunnerTest {
    @Test
    void runJars() {
        JarFilePathsConfig yamlConfig = new JarFilePathsConfig("application.yml");
        List<JarInfo> jarInfos = yamlConfig.getJarInfos();
        String workingDirectory = ".\\";

        JarRunner jarRunner = new JarRunner(jarInfos, workingDirectory);
        jarRunner.run();
    }
}
