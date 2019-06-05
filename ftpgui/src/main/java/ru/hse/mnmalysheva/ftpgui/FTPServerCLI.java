package ru.hse.mnmalysheva.ftpgui;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class FTPServerCLI {
    public static void main(@NotNull String[] args) {
        if (args.length != 1) {
            System.out.println("The only argument should be server port");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Port number should be integer");
            return;
        }
        var server = new FTPServer();
        try {
            server.start(port);
            System.out.println("Server started");
        } catch (IOException e) {
            System.out.println("Could not start server");
        }
    }
}
