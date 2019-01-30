package ru.hse.mnmalysheva.hashtable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Singly linked list. */
public class List {
    /** Singly linked list node. */
    private static class Node {
        /** Node content. */
        private Object data;
        /** Link to next node. */
        private Node next;

        private Node(Object data) {
            this.data = data;
        }

        private Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    /** Fictive head. */
    private Node head = new Node(null);

    /** Add element at list beginning. */
    public void add(@NotNull Object data) {
        head.next = new Node(data, head.next);
    }

    /** Remove the first occurrence of the specified element in list.
     * @return Null if no element was found, removed element otherwise.
     */
    public @Nullable Object remove(@NotNull Object o) {
        for (Node prev = head, cur = prev.next; cur != null; prev = cur, cur = cur.next) {
            var data = cur.data;
            if (data.equals(o)) {
                prev.next = cur.next;
                return data;
            }
        }
        return null;
    }

    /** Remove first element.
     * @return Removed element.
     */
    public Object removeFirst() {
        var first = head.next;
        if (first == null) {
            throw new IllegalStateException("Trying to remove first element of empty list.");
        }
        head.next = first.next;
        return first.data;
    }

    /** Find the first occurrence of the specified element in list.
     * @return Null if no element was found, founded element otherwise.
     */
    public @Nullable Object find(@NotNull Object o) {
        for (var cur = head.next; cur != null; cur = cur.next) {
            var data = cur.data;
            if (data.equals(o)) {
                return data;
            }
        }
        return null;
    }

    /** Check if list is empty. */
    public boolean isEmpty() {
        return head.next == null;
    }

    /** Remove all elements from list. */
    public void clear() {
        head.next = null;
    }
}
