package ru.hse.mnmalysheva.test4;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public interface Checksum {
    byte[] calculateChecksum(@NotNull Path path) throws IOException;
}
