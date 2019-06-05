package ru.hse.mnmalysheva.ftpgui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class FTPClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public void connect(@NotNull String host, int port) throws IOException {
        connect(InetAddress.getByName(host), port);
    }

    public void connect(@NotNull InetAddress address, int port) throws IOException {
        disconnect();
        socket = new Socket(address, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
            socket = null;
            in = null;
            out = null;
        }
    }

    public @Nullable List<FileDescription> executeList(@NotNull String path) throws IOException {
        FTPUtils.writeQuery(new Query(QueryType.LIST, path), out);
        return FTPUtils.readDirectory(in);
    }

    public void executeGet(@NotNull String path, OutputStream destination) throws IOException {
        FTPUtils.writeQuery(new Query(QueryType.GET, path), out);
        FTPUtils.readFile(in, destination);
    }
}
