package com.multi.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class ConsoleManager {
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter command (list, clear or exit): ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("list")) {
                try {
                    listDirectories();
                } catch (IOException e) {
                    System.err.println("Error listing directories: " + e.getMessage());
                }
            } else if (command.equalsIgnoreCase("clear")) {
                clearConsole();
            } else if (command.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("Invalid command. Type 'list', 'clear' or 'exit'.");
            }
        }

        scanner.close();
    }

    private void listDirectories() throws IOException {
        Path currentPath = Paths.get("").toAbsolutePath();
        System.out.println("Current directory: " + currentPath);

        try (Stream<Path> paths = Files.list(currentPath)) {
            paths.filter(Files::isDirectory)
                    .forEach(path -> System.out.println("  - " + path.getFileName()));
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
}
