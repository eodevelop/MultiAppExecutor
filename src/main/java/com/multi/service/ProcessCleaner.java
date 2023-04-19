package com.multi.service;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class ProcessCleaner {
    private final List<Process> processes;
    private final ExecutorService executorService;

    public ProcessCleaner(List<Process> processes, ExecutorService executorService) {
        this.processes = processes;
        this.executorService = executorService;
    }

    public void cleanupOnShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            synchronized (processes) {
                for (Process process : processes) {
                    process.destroyForcibly();
                }
            }
            executorService.shutdownNow();
        }));
    }
}
