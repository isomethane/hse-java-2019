package ru.hse.mnmalysheva.ftpgui;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class FTPUtils {
    private static int INCORRECT_INPUT_CODE = -1;
    private static int BUFFER_SIZE = 1024;

    static FTPQuery readQuery(DataInputStream in) throws IOException {
        int queryCode = in.readInt();
        String path = in.readUTF();
        return new FTPQuery(FTPQueryType.fromCode(queryCode), path);
    }

    static void writeQuery(FTPQuery query, DataOutputStream out) throws IOException {
        out.writeInt(query.type.getCode());
        out.writeUTF(query.path);
    }

    static void executeQuery(FTPQuery query, DataOutputStream out) throws IOException {
        if (query.type == FTPQueryType.LIST) {
            FTPUtils.writeDirectory(Path.of(query.path), out);
        } else {
            FTPUtils.writeFile(Path.of(query.path), out);
        }
    }

    static void readFile(DataInputStream in, OutputStream destination) throws IOException {
        long size = in.readLong();
        if (size == INCORRECT_INPUT_CODE) {
            throw new FileNotFoundException();
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        long bytesLeft = size;
        while (bytesLeft >= 0) {
            int toRead = (int) Math.min(BUFFER_SIZE, bytesLeft);
            int bytesRead = in.read(buffer, 0, toRead);
            if (bytesRead != toRead) {
                throw new RuntimeException();
            }
            destination.write(buffer, 0, bytesRead);
            bytesLeft -= BUFFER_SIZE;
        }
    }

    static @Nullable List<FileDescription> readDirectory(DataInputStream in) throws IOException {
        int size = in.readInt();
        if (size == INCORRECT_INPUT_CODE) {
            return null;
        }
        var result = new ArrayList<FileDescription>();
        for (int i = 0; i < size; i++) {
            String path = in.readUTF();
            boolean isDirectory = in.readBoolean();
            result.add(new FileDescription(path, isDirectory));
        }
        return result;
    }

    static void writeFile(Path path, DataOutputStream out) throws IOException {
        var file = path.toFile();
        if (!file.isFile()) {
            out.writeLong(INCORRECT_INPUT_CODE);
            return;
        }
        out.writeLong(file.length());

        var in = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
    }

    static void writeDirectory(Path path, DataOutputStream out) throws IOException {
        var directory = path.toFile();
        if (!directory.isDirectory()) {
            out.writeLong(INCORRECT_INPUT_CODE);
            return;
        }

        File[] content = directory.listFiles();
        if (content == null) {
            out.writeLong(INCORRECT_INPUT_CODE);
            return;
        }

        out.writeInt(content.length);
        for (var file : content) {
            out.writeUTF(file.getName());
            out.writeBoolean(file.isDirectory());
        }
    }
}
