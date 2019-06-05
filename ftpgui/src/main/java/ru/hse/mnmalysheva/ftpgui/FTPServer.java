package ru.hse.mnmalysheva.ftpgui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class FTPServer {
    private int port;
    private ServerSocket server;

    public void start(int port) throws IOException {
        stop();

        this.port = port;
        server = new ServerSocket(port);
        new Thread(new ServerSocketWorker(server)).start();
    }

    public void stop() throws IOException {
        if (server != null) {
            server.close();
            server = null;
        }
    }

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
                    Query query = FTPUtils.readQuery(in);
                    FTPUtils.executeQuery(query, out);
                } catch (IOException e) {
                    return;
                }
            }
        }
    }
}
