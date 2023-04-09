package com.multi;

import com.multi.executor.JarExecutor;
import com.multi.monitoring.io.OutputReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JarRunner {
    private final List<String> jarFilePaths;
    private final String workingDirectory;
    private final String profile;

    public JarRunner(List<String> jarFilePaths, String workingDirectory, String profile) {
        this.jarFilePaths = jarFilePaths;
        this.workingDirectory = workingDirectory;
        this.profile = profile;
    }

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(jarFilePaths.size());
        List<Process> processes = new ArrayList<>();

        for (String jarFilePath : jarFilePaths) {
            executorService.submit(() -> {
                try {
                    JarExecutor jarExecutor = new JarExecutor(jarFilePath, workingDirectory, profile);
                    Process process = jarExecutor.start();

                    synchronized (processes) {
                        processes.add(process);
                    }

                    Thread outputReaderThread = new Thread(new OutputReader(process.getInputStream(), "[" + jarFilePath + "]"));
                    outputReaderThread.start();

                    int exitCode = process.waitFor();
                    System.out.println("Spring Boot 애플리케이션 [" + jarFilePath + "] 실행이 완료되었습니다. 종료 코드: " + exitCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (processes) {
                for (Process process : processes) {
                    process.destroy();
                }
            }
            executorService.shutdown();
        }));
    }
}