package ru.hse.mnmalysheva.test4;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractChecksum implements Checksum {
    private static final int BUFFER_SIZE = 1024;

    @Override
    public byte[] calculateChecksum(@NotNull Path path) throws IOException {
        var file = path.toFile();
        if (file.isFile()) {
            return fileChecksum(new FileInputStream(file));
        }
        if (file.isDirectory()) {
            return directoryChecksum(path);
        }
        throw new IOException("Path does not correspond neither to normal file not to directory");
    }

    protected static @NotNull byte[] fileChecksum(@NotNull InputStream in) throws IOException {
        try {
            var messageDigest = MessageDigest.getInstance("MD5");

            var buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, bytesRead);
            }
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract @NotNull byte[] directoryChecksum(@NotNull Path path) throws IOException;

    protected File[] getFiles(@NotNull File file) {
        var files = Objects.requireNonNull(file.listFiles());
        Arrays.sort(files);
        return files;
    }
}
