package ru.hse.mnmalysheva.ftpgui;

import org.jetbrains.annotations.NotNull;

public class FileDescription {
    public final String name;
    public final boolean isDirectory;

    public FileDescription(@NotNull String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    @Override
    public @NotNull String toString() {
        return name;
    }
}
