package ru.hse.mnmalysheva.ftpgui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * This class represents FTP client.
 * Can download and list files from server.
 **/
public class FTPClient {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isConnected;

    /** Returns {@code true} if successfully connected to {@link FTPServer}. **/
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Connects to server at specified named host and port.
     * @throws IllegalStateException if already connected.
     * @throws UnknownHostException if IP address of the host could not be determined.
     * @throws IOException if failed to create socket or get its I/O streams.
     */
    public void connect(@NotNull String host, int port)
            throws IOException {
        connect(InetAddress.getByName(host), port);
    }

    /**
     * Connect to server at specified address and port.
     * @throws IllegalStateException if already connected.
     * @throws IOException if failed to create socket or get its I/O streams.
     */
    public void connect(@NotNull InetAddress address, int port) throws IOException {
        if (isConnected) {
            throw new IllegalStateException("Already connected");
        }
        socket = new Socket(address, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        isConnected = true;
    }

    /**
     * Disconnects from server if it was previously connected.
     * @throws IOException if an I/O error occurred when closing the socket.
     */
    public void disconnect() throws IOException {
        if (isConnected) {
            isConnected = false;
            socket.close();
        }
    }

    /**
     * Gets list of files in directory from server.
     * @param path directory path
     * @return list of files in directory ({@code null} if directory with specified name does not exist)
     * @throws IOException if I/O error occurred when communicating with server.
     */
    public @Nullable List<FileDescription> executeList(@NotNull String path) throws IOException {
        FTPUtils.writeQuery(new Query(QueryType.LIST, path), out);
        return FTPUtils.readDirectory(in);
    }

    /**
     * Writes content of file from server to specified {@link OutputStream}.
     * @param path file path
     * @param destination stream to write
     * @throws FileNotFoundException if normal file with specified name does not exist.
     * @throws IOException if I/O error occurred when communicating with server.
     */
    public void executeGet(@NotNull String path, OutputStream destination) throws IOException {
        FTPUtils.writeQuery(new Query(QueryType.GET, path), out);
        FTPUtils.readFile(in, destination);
    }
}
