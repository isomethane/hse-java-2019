package ru.hse.mnmalysheva.ftpgui;

import org.jetbrains.annotations.NotNull;

/** This class represents simple description of file. **/
public class FileDescription {
    /** File name. **/
    public final String name;
    /** Directory flag. **/
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
