package ru.hse.mnmalysheva.ftpgui;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Scanner;

/** FTPServer command-line interface. **/
public class FTPServerCLI {
    /** Starts program. **/
    public static void main(@NotNull String[] args) {
        var server = new FTPServer();

        printUsage();

        var scanner = new Scanner(System.in);
        boolean toExit = false;
        while (!toExit) {
            String[] line = scanner.nextLine().split(" ");
            if (line.length == 0) {
                continue;
            }
            var command = line[0];
            switch (command) {
                case "start":
                    if (line.length < 2) {
                        System.out.println("Please specify port number");
                    } else if (line.length > 2) {
                        System.out.println("Incorrect number of arguments");
                    } else {
                        try {
                            int port = Integer.parseInt(line[1]);
                            server.start(port);
                            System.out.println("Server started");
                        } catch (NumberFormatException e) {
                            System.out.println("Port is not a correct number");
                        } catch (IllegalStateException e) {
                            System.out.println("Server is already running");
                        } catch (IOException e) {
                            System.out.println("Failed to start server :(");
                        }
                    }
                    break;
                case "stop":
                    try {
                        server.stop();
                        System.out.println("Server stopped");
                    } catch (IOException e) {
                        System.out.println("Failed to stop server :(");
                    }
                    break;
                case "exit":
                    try {
                        server.stop();
                    } catch (IOException ignored) {}
                    toExit = true;
                    break;
                default:
                    System.out.println("Incorrect command");
                    break;
            }
        }
    }

    private static void printUsage() {
        System.out.println("Commands:");
        System.out.println("* start <port>");
        System.out.println("* stop");
        System.out.println("* exit");
    }
}
