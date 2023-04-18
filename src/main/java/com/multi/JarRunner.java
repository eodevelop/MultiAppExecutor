package com.multi;

import com.multi.executor.JarExecutor;
import com.multi.model.JarInfo;
import com.multi.monitoring.io.OutputReader;
import com.multi.utils.ConsoleManager;
import com.multi.utils.DirectoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class JarRunner {
    private final List<JarInfo> jarInfos;
    private final String workingDirectory;
    private ConsoleManager consoleManager = new ConsoleManager();

    public JarRunner(List<JarInfo> jarInfos, String workingDirectory) {
        this.jarInfos = jarInfos;
        this.workingDirectory = workingDirectory;
    }

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(jarInfos.size());
        List<Process> processes = new ArrayList<>();

        // 로그 폴더 생성
        String logDir = workingDirectory + "\\logs";
        DirectoryManager.createDirectoryIfNotExists(logDir);

        for (JarInfo jarInfo : jarInfos) {
            executorService.submit(() -> {
                try {
                    JarExecutor jarExecutor = new JarExecutor(jarInfo.getPath(), workingDirectory, jarInfo.getProfile());
                    Process process = jarExecutor.start();

                    synchronized (processes) {
                        processes.add(process);
                    }

                    // 서브 폴더 생성
                    String subLogDir = logDir + "\\" + jarInfo.getPath().substring(jarInfo.getPath().lastIndexOf("\\") + 1);
                    DirectoryManager.createDirectoryIfNotExists(subLogDir);

                    // 로그 파일 경로 설정
                    String logFileName = "application.log";
                    String logFilePath = subLogDir + "\\" + logFileName;

                    Thread outputReaderThread = new Thread(new OutputReader(process.getInputStream(), "[" + jarInfo.getPath() + "]", logFilePath));
                    outputReaderThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Create a new thread for the ConsoleManager
        Thread consoleManagerThread = new Thread(() -> consoleManager.start());
        consoleManagerThread.start();

        cleanupOnShutdown(executorService, processes);
    }

    private static void cleanupOnShutdown(ExecutorService executorService, List<Process> processes) {
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