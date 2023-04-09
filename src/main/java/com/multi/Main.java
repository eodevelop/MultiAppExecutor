package com.multi;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // 원하는 Spring Boot JAR 파일 경로를 리스트로 추가합니다.
        List<String> jarFilePaths =
                Arrays.asList(
                        "C:\\workspace\\tx-auth-api\\target\\tx-auth-api-0.0.1-SNAPSHOT.jar",
                        "C:\\workspace\\User\\tx-user-api\\target\\tx-user-api-0.0.1-SNAPSHOT.jar"
                        );

        // 병렬 작업을 위해 ExecutorService를 생성합니다.
        ExecutorService executorService = Executors.newFixedThreadPool(jarFilePaths.size());

        // 실행 중인 프로세스를 저장할 리스트를 생성합니다.
        List<Process> processes = new ArrayList<>();

        // 각 JAR 파일에 대해 실행할 작업을 생성하고, 작업을 ExecutorService에 제출합니다.
        for (String jarFilePath : jarFilePaths) {
            executorService.submit(() -> {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarFilePath);
                    processBuilder.redirectErrorStream(true); // 에러 스트림을 출력 스트림으로 리다이렉트
                    processBuilder.directory(new File("C:\\workspace\\temp")); // 원하는 작업 디렉토리 설정

                    // 원하는 환경 변수에 값을 설정합니다.
                    processBuilder.environment().put("SPRING_PROFILES_ACTIVE", "test");

                    Process process = processBuilder.start(); // 프로세스 시작

                    // 실행 중인 프로세스를 리스트에 추가합니다.
                    synchronized (processes) {
                        processes.add(process);
                    }

                    // 프로세스의 출력 스트림을 읽기 위한 스레드를 생성하고 시작합니다.
                    Thread outputReaderThread = new Thread(() -> {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                System.out.println("[" + jarFilePath + "] " + line);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    outputReaderThread.start();

                    // 프로세스가 완료될 때까지 대기합니다.
                    int exitCode = process.waitFor();
                    System.out.println("Spring Boot 애플리케이션 [" + jarFilePath + "] 실행이 완료되었습니다. 종료 코드: " + exitCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // 프로그램 종료 시 실행 중인 프로세스를 종료하는 작업을 등록합니다.
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