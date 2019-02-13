package ru.hse.mnmalysheva.trie;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Hashtable;

/**
 * Trie is a data structure used to store set of strings.
 * Allows to add, find and remove strings in O(length).
 */
public class Trie implements Serializable {
    private static class Node {
        private boolean isTerminal = false;
        private int numberOfTerminalsInSubtree;
        private final Hashtable<Character, Node> children = new Hashtable<>();

        @Override
        public int hashCode() {
            int hash = isTerminal ? 1 : 0;
            for (var entry : children.entrySet()) {
                hash += entry.getKey() * entry.getValue().hashCode();
            }
            return hash;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Node) {
                var node = (Node) object;
                return isTerminal == node.isTerminal &&
                        numberOfTerminalsInSubtree == node.numberOfTerminalsInSubtree &&
                        children.equals(node.children);
            }
            return false;
        }

        private boolean switchTerminal(boolean state) {
            if (isTerminal == state) {
                return false;
            }
            isTerminal = state;
            numberOfTerminalsInSubtree += isTerminal ? 1 : -1;
            return true;
        }

        private boolean add(@NotNull String element, int position) {
            if (position == element.length()) {
                return switchTerminal(true);
            }
            var nextCharacter = element.charAt(position);
            var child = children.get(nextCharacter);
            if (child == null) {
                child = new Node();
                children.put(nextCharacter, child);
            }
            boolean added = child.add(element, position + 1);
            if (added) {
                numberOfTerminalsInSubtree++;
            }
            return added;
        }

        private boolean contains(@NotNull String element, int position) {
            if (position == element.length()) {
                return isTerminal;
            }
            var nextCharacter = element.charAt(position);
            var child = children.get(nextCharacter);
            if (child == null) {
                return false;
            }
            return child.contains(element, position + 1);
        }

        private boolean remove(@NotNull String element, int position) {
            if (position == element.length()) {
                return switchTerminal(false);
            }
            var nextCharacter = element.charAt(position);
            var child = children.get(nextCharacter);
            if (child == null) {
                return false;
            }
            boolean removed = child.remove(element, position + 1);
            if (removed) {
                numberOfTerminalsInSubtree--;
                if (child.numberOfTerminalsInSubtree == 0) {
                    children.remove(nextCharacter);
                }
            }
            return removed;
        }

        private int howManyStartWithPrefix(@NotNull String prefix, int position) {
            if (position == prefix.length()) {
                return numberOfTerminalsInSubtree;
            }
            var nextCharacter = prefix.charAt(position);
            var child = children.get(nextCharacter);
            if (child == null) {
                return 0;
            }
            return child.howManyStartWithPrefix(prefix, position + 1);
        }

        private void serialize(@NotNull DataOutputStream out) throws IOException {
            out.writeInt(children.size());
            out.writeBoolean(isTerminal);
            for (var entry : children.entrySet()) {
                out.writeChar(entry.getKey());
                entry.getValue().serialize(out);
            }
        }

        private void deserialize(@NotNull DataInputStream in) throws IOException {
            var numberOfChildren = in.readInt();
            isTerminal = in.readBoolean();
            numberOfTerminalsInSubtree = isTerminal ? 1 : 0;
            for (int i = 0; i < numberOfChildren; i++) {
                var nextCharacter = in.readChar();
                var child = new Node();
                children.put(nextCharacter, child);
                child.deserialize(in);
                numberOfTerminalsInSubtree += child.numberOfTerminalsInSubtree;
            }
        }
    }

    private Node root = new Node();

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return root.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Trie) {
            var trie = (Trie) object;
            return root.equals(trie.root);
        }
        return false;
    }

    /** Add string to trie.
     * @param element String to add.
     * @return true if trie did not contain this string, false otherwise.
     */
    public boolean add(@NotNull String element) {
        return root.add(element, 0);
    }

    /** Check if trie contains specified string. */
    public boolean contains(@NotNull String element) {
        return root.contains(element, 0);
    }

    /** Remove string from trie.
     * @param element String to remove.
     * @return true if trie contained this string, false otherwise.
     */
    public boolean remove(@NotNull String element) {
        return root.remove(element, 0);
    }

    /** Number of strings in trie. */
    public int size() {
        return root.numberOfTerminalsInSubtree;
    }

    /** Number of strings starting with specified prefix. */
    public int howManyStartWithPrefix(@NotNull String prefix) {
        return root.howManyStartWithPrefix(prefix, 0);
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(@NotNull OutputStream out) throws IOException {
        root.serialize(new DataOutputStream(out));
    }

    /** {@inheritDoc} */
    @Override
    public void deserialize(@NotNull InputStream in) throws IOException {
        root = new Node();
        root.deserialize(new DataInputStream(in));
    }
}
