package ru.hse.mnmalysheva.trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * Trie class.
 * Trie is a data structure used to store set of strings.
 * Allows to add, find and remove strings in O(length).
 */
public class Trie implements Serializable {
    /** Trie node class. */
    private class Node {
        /** Flag is true when node represents complete string. */
        boolean isTerminal = false;
        /** Number of terminal nodes in subtree. */
        int size = 0;
        /** Character-node map of children. */
        Hashtable<Character, Node> children = new Hashtable<>();

        /** Set isTerminal to new state. Recounts subtree size when state changed.
         * @param state New terminal state.
         * @return true if state changed, false otherwise.
         */
        boolean switchTerminal(boolean state) {
            if (isTerminal == state) {
                return false;
            }
            isTerminal = state;
            size += isTerminal ? 1 : -1;
            return true;
        }

        /** Add string to trie.
         * @param element String to add.
         * @param pos Next char position.
         * @return true if trie did not contain this string, false otherwise.
         */
        boolean add(String element, int pos) {
            if (pos == element.length()) {
                return switchTerminal(true);
            }
            var nextChar = element.charAt(pos);
            var child = children.get(nextChar);
            if (child == null) {
                child = new Node();
                children.put(nextChar, child);
            }
            boolean added = child.add(element, pos + 1);
            if (added) {
                size++;
            }
            return added;
        }

        /** Check if trie contains specified string.
         * @param pos Next char position.
         */
        boolean contains(String element, int pos) {
            if (pos == element.length()) {
                return isTerminal;
            }
            var nextChar = element.charAt(pos);
            var child = children.get(nextChar);
            if (child == null) {
                return false;
            }
            return child.contains(element, pos + 1);
        }

        /** Remove string from trie.
         * @param element String to remove.
         * @param pos Next char position.
         * @return true if trie contained this string, false otherwise.
         */
        boolean remove(String element, int pos) {
            if (pos == element.length()) {
                return switchTerminal(false);
            }
            var nextChar = element.charAt(pos);
            var child = children.get(nextChar);
            if (child == null) {
                return false;
            }
            boolean removed = child.remove(element, pos + 1);
            if (removed) {
                size--;
                if (child.size == 0) {
                    children.remove(nextChar);
                }
            }
            return removed;
        }

        /** Number of strings starting with specified prefix.
         * @param pos Next char position.
         */
        int howManyStartWithPrefix(String prefix, int pos) {
            if (pos == prefix.length()) {
                return size;
            }
            var nextChar = prefix.charAt(pos);
            var child = children.get(nextChar);
            if (child == null) {
                return 0;
            }
            return child.howManyStartWithPrefix(prefix, pos + 1);
        }
    }

    /** Tree root represents empty string. */
    private Node root = new Node();

    /** Throw exception if string is null. */
    void checkString(String element) {
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
    public void serialize(OutputStream out) throws IOException {}

    /** {@inheritDoc} */
    @Override
    public void deserialize(InputStream in) throws IOException {}
}
