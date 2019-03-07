package ru.hse.mnmalysheva.trie;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Implementing this interface allows an object to be converted from/into byte sequence. */
public interface Serializable {
    /** Convert object into byte sequence. */
    void serialize(@NotNull OutputStream out) throws IOException;
    /** Replace object with one converted from byte sequence. */
    void deserialize(@NotNull InputStream in) throws IOException;
}
