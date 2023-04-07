package com.multi;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        List<String> jarFilePaths = Arrays.asList("C:\\workspace\\Api\\tx-auth-api\\target\\tx-auth-api-0.0.1-SNAPSHOT.jar");

        ExecutorService executorService = Executors.newFixedThreadPool(jarFilePaths.size());

        for (String jarFilePath : jarFilePaths) {
            executorService.submit(() -> {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarFilePath);
                    processBuilder.redirectErrorStream(true); // 에러 스트림을 출력 스트림으로 리다이렉트
                    processBuilder.directory(new File("C:\\workspace\\temp")); // 작업 디렉토리 설정
                    Process process = processBuilder.start(); // 프로세스 시작

                    int exitCode = process.waitFor();
                    System.out.println("Spring Boot 애플리케이션 [" + jarFilePath + "] 실행이 완료되었습니다. 종료 코드: " + exitCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}