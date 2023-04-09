package com.multi.monitoring.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OutputReader implements Runnable {
    private final InputStream inputStream;
    private final String prefix;

    public OutputReader(InputStream inputStream, String prefix) {
        this.inputStream = inputStream;
        this.prefix = prefix;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(prefix + " " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}