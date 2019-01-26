package ru.hse.mnmalysheva.trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Trie class.
 * Trie is a data structure used to store set of strings.
 * Allows to add, find and remove strings in O(length).
 */
public class Trie implements Serializable {
    /** Add string to trie.
     * @param element String to add.
     * @return true if trie did not contain this string, false otherwise.
     */
    public boolean add(String element) {
        return false;
    }

    /** Check if trie contains specified string. */
    public boolean contains(String element) {
        return false;
    }

    /** Remove string from trie.
     * @param element String to remove.
     * @return true if trie contained this string, false otherwise.
     */
    public boolean remove(String element) {
        return false;
    }

    /** Number of strings in trie. */
    public int size() {
        return 0;
    }

    /** Number of strings starting with specified prefix. */
    public int howManyStartWithPrefix(String prefix) {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(OutputStream out) throws IOException {}

    /** {@inheritDoc} */
    @Override
    public void deserialize(InputStream in) throws IOException {}
}
