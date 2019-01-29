package ru.hse.mnmalysheva.trie;

import java.io.*;
import java.util.Hashtable;

/**
 * Trie is a data structure used to store set of strings.
 * Allows to add, find and remove strings in O(length).
 */
public class Trie implements Serializable {
    /** Trie node class. */
    private static class Node implements Serializable {
        /** Flag is true when node represents complete string. */
        private boolean isTerminal = false;
        /** Number of terminal nodes in subtree. */
        private int size = 0;
        /** Character-node map of children. */
        private Hashtable<Character, Node> children = new Hashtable<>();

        /** Set isTerminal to new state. Recounts subtree size when state changed.
         * @param state New terminal state.
         * @return true if state changed, false otherwise.
         */
        private boolean switchTerminal(boolean state) {
            if (isTerminal == state) {
                return false;
            }
            isTerminal = state;
            size += isTerminal ? 1 : -1;
            return true;
        }

        /** Write N last bytes (little-endian) of integer to OutputStream.
         * @param size Number of bytes to write.
         * @param number Integer to write.
         */
        private static void writeInt(OutputStream out, int size, int number) throws IOException {
            for (int i = 0; i < size; i++) {
                out.write(number >> (i * Byte.SIZE));
            }
        }

        /** Read N last bytes (little-endian) of integer from InputStream.
         * @param size Number of bytes to write.
         */
        private static int readInt(InputStream in, int size) throws IOException {
            int number = 0;
            for (int i = 0; i < size; i++) {
                number += in.read() << (i * Byte.SIZE);
            }
            return number;
        }

        /** Add string to trie.
         * @param element String to add.
         * @param position Next char position.
         * @return true if trie did not contain this string, false otherwise.
         */
        public boolean add(String element, int position) {
            if (position == element.length()) {
                return switchTerminal(true);
            }
            var nextChar = element.charAt(position);
            var child = children.get(nextChar);
            if (child == null) {
                child = new Node();
                children.put(nextChar, child);
            }
            boolean added = child.add(element, position + 1);
            if (added) {
                size++;
            }
            return added;
        }

        /** Check if trie contains specified string.
         * @param position Next char position.
         */
        public boolean contains(String element, int position) {
            if (position == element.length()) {
                return isTerminal;
            }
            var nextChar = element.charAt(position);
            var child = children.get(nextChar);
            if (child == null) {
                return false;
            }
            return child.contains(element, position + 1);
        }

        /** Remove string from trie.
         * @param element String to remove.
         * @param position Next char position.
         * @return true if trie contained this string, false otherwise.
         */
        public boolean remove(String element, int position) {
            if (position == element.length()) {
                return switchTerminal(false);
            }
            var nextChar = element.charAt(position);
            var child = children.get(nextChar);
            if (child == null) {
                return false;
            }
            boolean removed = child.remove(element, position + 1);
            if (removed) {
                size--;
                if (child.size == 0) {
                    children.remove(nextChar);
                }
            }
            return removed;
        }

        /** Number of strings starting with specified prefix.
         * @param prefix String prefix.
         * @param position Next char position.
         */
        public int howManyStartWithPrefix(String prefix, int position) {
            if (position == prefix.length()) {
                return size;
            }
            var nextChar = prefix.charAt(position);
            var child = children.get(nextChar);
            if (child == null) {
                return 0;
            }
            return child.howManyStartWithPrefix(prefix, position + 1);
        }

        /** {@inheritDoc} */
        @Override
        public void serialize(OutputStream out) throws IOException {
            writeInt(out, Character.BYTES + 1, children.size());
            out.write(isTerminal ? 1 : 0);
            for (var k : children.keySet()) {
                writeInt(out, Character.BYTES, k);
                children.get(k).serialize(out);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void deserialize(InputStream in) throws IOException {
            var n = readInt(in, Character.BYTES + 1);
            isTerminal = in.read() != 0;
            for (int i = 0; i < n; i++) {
                var nextChar = (char) readInt(in, Character.BYTES);
                var child = new Node();
                children.put(nextChar, child);
                child.deserialize(in);
            }
        }
    }

    /** Tree root represents empty string. */
    private Node root = new Node();

    /** Throw IllegalArgumentException if string is null. */
    private static void checkString(String element) {
        if (element == null) {
            throw new IllegalArgumentException("null strings are not allowed");
        }
    }

    /** Add string to trie.
     * @param element String to add.
     * @return true if trie did not contain this string, false otherwise.
     */
    public boolean add(String element) {
        checkString(element);
        return root.add(element, 0);
    }

    /** Check if trie contains specified string. */
    public boolean contains(String element) {
        checkString(element);
        return root.contains(element, 0);
    }

    /** Remove string from trie.
     * @param element String to remove.
     * @return true if trie contained this string, false otherwise.
     */
    public boolean remove(String element) {
        checkString(element);
        return root.remove(element, 0);
    }

    /** Number of strings in trie. */
    public int size() {
        return root.size;
    }

    /** Number of strings starting with specified prefix. */
    public int howManyStartWithPrefix(String prefix) {
        checkString(prefix);
        return root.howManyStartWithPrefix(prefix, 0);
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(OutputStream out) throws IOException {
        root.serialize(out);
    }

    /** {@inheritDoc} */
    @Override
    public void deserialize(InputStream in) throws IOException {
        root = new Node();
        root.deserialize(in);
    }
}
