package ru.hse.mnmalysheva.trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Implementing this interface allows an object to be transformed into/from byte sequence. */
public interface Serializable {
    /** Transform object into byte sequence. */
    void serialize(OutputStream out) throws IOException;
    /** Replace object with one got from byte sequence. */
    void deserialize(InputStream in) throws IOException;
}
