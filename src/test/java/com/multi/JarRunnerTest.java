package com.multi;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class JarRunnerTest {
    @Test
    void runJars() {
        List<String> jarFilePaths = Arrays.asList(
                "C:\\workspace\\tx-auth-api\\target\\tx-auth-api-0.0.1-SNAPSHOT.jar",
                "C:\\workspace\\User\\tx-user-api\\target\\tx-user-api-0.0.1-SNAPSHOT.jar"
        );
        String workingDirectory = "C:\\workspace\\temp";
        String profile = "test";

        JarRunner jarRunner = new JarRunner(jarFilePaths, workingDirectory, profile);
        jarRunner.run();
    }
}
