package ru.hse.mnmalysheva.ftpgui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class FTPServer {
    private int port;
    private ServerSocket server;
    private boolean isRunning;

    /** Returns {@code true} if server is running. **/
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Starts server at specified port.
     * @param port port number
     * @throws IllegalStateException if server is already running.
     * @throws IOException if I/O error occurred when creating server socket.
     */
    public void start(int port) throws IOException {
        if (isRunning) {
            throw new IllegalStateException("Server is already running");
        }
        this.port = port;
        server = new ServerSocket(port);
        new Thread(new ServerSocketWorker(server)).start();
        isRunning = true;
    }

    /**
     * Stops server.
     * @throws IOException if an I/O error occurred when closing server socket.
     */
    public void stop() throws IOException {
        if (isRunning) {
            isRunning = false;
            server.close();
            server = null;
        }
    }

    /** Returns server port. **/
    public int getPort() {
        return port;
    }

    private static class ServerSocketWorker implements Runnable {
        private ServerSocket server;

        private ServerSocketWorker(ServerSocket server) {
            this.server = server;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = server.accept();
                    var queryHandler = new QueryHandler(socket.getInputStream(), socket.getOutputStream());
                    new Thread(queryHandler).start();
                } catch (SocketException e) {
                    return;
                } catch (IOException ignored) {}
            }
        }
    }

    private static class QueryHandler implements Runnable {
        private DataInputStream in;
        private DataOutputStream out;

        private QueryHandler(InputStream in, OutputStream out) {
            this.in = new DataInputStream(in);
            this.out = new DataOutputStream(out);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    FTPQuery query = FTPUtils.readQuery(in);
                    FTPUtils.executeQuery(query, out);
                } catch (IOException e) {
                    return;
                }
            }
        }
    }
}
