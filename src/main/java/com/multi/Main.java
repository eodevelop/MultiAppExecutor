package com.multi;

import com.multi.executor.JarExecutor;
import com.multi.monitoring.io.OutputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        List<String> jarFilePaths =
                Arrays.asList(
                        "C:\\workspace\\tx-auth-api\\target\\tx-auth-api-0.0.1-SNAPSHOT.jar",
                        "C:\\workspace\\User\\tx-user-api\\target\\tx-user-api-0.0.1-SNAPSHOT.jar"
                );
        String workingDirectory = "C:\\workspace\\temp";
        String profile = "test";

        JarRunner jarRunner = new JarRunner(jarFilePaths, workingDirectory, profile);
        jarRunner.run();
    }
}