package com.multi;

import com.multi.executor.JarExecutor;
import com.multi.model.JarInfo;
import com.multi.monitoring.io.OutputReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JarRunner {
    private final List<JarInfo> jarInfos;
    private final String workingDirectory;

    public JarRunner(List<JarInfo> jarInfos, String workingDirectory) {
        this.jarInfos = jarInfos;
        this.workingDirectory = workingDirectory;
    }

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(jarInfos.size());
        List<Process> processes = new ArrayList<>();

        // 로그 폴더 생성
        String logDir = workingDirectory + "\\logs";
        File logDirectory = new File(logDir);
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }

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
                    File subLogDirectory = new File(subLogDir);
                    if (!subLogDirectory.exists()) {
                        subLogDirectory.mkdirs();
                    }

                    // 로그 파일 경로 설정
                    String logFileName = "application.log";
                    String logFilePath = subLogDir + "\\" + logFileName;

                    Thread outputReaderThread = new Thread(new OutputReader(process.getInputStream(), "[" + jarInfo.getPath() + "]", logFilePath));
                    outputReaderThread.start();

                    int exitCode = process.waitFor();
                    System.out.println("Spring Boot 애플리케이션 [" + jarInfo.getPath() + "] 실행이 완료되었습니다. 종료 코드: " + exitCode);

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