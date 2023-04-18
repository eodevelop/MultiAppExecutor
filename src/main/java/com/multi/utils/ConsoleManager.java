package com.multi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class ConsoleManager {
    private volatile boolean stopTail = false;

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter command (list, clear, tail, stop or exit): ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("list")) {
                try {
                    listDirectories();
                } catch (IOException e) {
                    System.err.println("Error listing directories: " + e.getMessage());
                }
            } else if (command.equalsIgnoreCase("clear")) {
                clearConsole();
            } else if (command.equalsIgnoreCase("tail")) {
                System.out.print("Enter the folder name: ");
                String folderName = scanner.nextLine();
                tailLogFile(folderName);
            } else if (command.equalsIgnoreCase("stop")) {
                stopTail = true;
            } else if (command.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("Invalid command. Type 'list', 'clear', 'tail', 'stop' or 'exit'.");
            }
        }

        scanner.close();
    }

    private void listDirectories() throws IOException {
        Path currentPath = Paths.get("./logs").toAbsolutePath();
        System.out.println("logs directory: " + currentPath);

        try (Stream<Path> paths = Files.list(currentPath)) {
            paths.filter(Files::isDirectory)
                    .forEach(path -> System.out.println("  - " + path.getFileName()));
        }
    }

    private void tailLogFile(String folderName) {
        Path logFile = Paths.get("./logs/" + folderName + "/application.log").toAbsolutePath();
        if (Files.exists(logFile)) {
            stopTail = false;
            Thread tailLogThread = new Thread(new TailLogThread(logFile));
            tailLogThread.start();
        } else {
            System.out.println("The specified folder or log file does not exist.");
        }
    }

    private static void clearConsole() {
        String osName = System.getProperty("os.name");
        try {
            if (osName.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error clearing console: " + e.getMessage());
        }
    }

    private class TailLogThread implements Runnable {
        private final Path logFile;

        public TailLogThread(Path logFile) {
            this.logFile = logFile;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(logFile)))) {
                String line;
                while (!stopTail) {
                    line = reader.readLine();
                    if (line != null) {
                        System.out.println(line);
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error tailing log file: " + e.getMessage());
            }
        }
    }
}