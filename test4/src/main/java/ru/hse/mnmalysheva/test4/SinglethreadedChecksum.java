package ru.hse.mnmalysheva.test4;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class SinglethreadedChecksum extends AbstractChecksum {
    @Override
    public byte[] directoryChecksum(@NotNull Path path) throws IOException {
        var file = path.toFile();
        try {
            var messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(file.getName().getBytes());
            var files = Objects.requireNonNull(file.listFiles());
            Arrays.sort(files);
            for (var f : files) {
                messageDigest.update(calculateChecksum(f.toPath()));
            }
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
