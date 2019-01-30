package ru.hse.mnmalysheva.hashtable;

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
    public void add(Object data) {
        if (data == null) {
            throw new IllegalArgumentException("null-s cannot be stored in list.");
        }
        var newNode = new Node(data, head.next);
        head.next = newNode;
    }

    /** Remove the first occurrence of the specified element in list.
     * @return Removed element.
     */
    public Object remove(Object o) {
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
        if (first != null) {
            head.next = first.next;
            return first.data;
        }
        return null;
    }

    /** Find the first occurrence of the specified element in list.
     * @return Link to element if found, false otherwise.
     */
    public Object find(Object o) {
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
