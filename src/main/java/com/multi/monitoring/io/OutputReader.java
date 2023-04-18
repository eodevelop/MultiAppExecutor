package com.multi.monitoring.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OutputReader implements Runnable {
    private final InputStream inputStream;
    private final String logFilePath;

    public OutputReader(InputStream inputStream, String logFilePath) {
        this.inputStream = inputStream;
        this.logFilePath = logFilePath;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(logFilePath))) {

            String logLine;
            while ((logLine = reader.readLine()) != null) {
                writer.write(logLine);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}