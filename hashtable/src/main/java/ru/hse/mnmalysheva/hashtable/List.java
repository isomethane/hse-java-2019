package ru.hse.mnmalysheva.hashtable;

/** Singly linked list. */
public class List {
    /** Singly linked list node. */
    private class Node {
        /** Node content. */
        Object data;
        /** Link to next node. */
        Node next = null;

        Node(Object data) {
            this.data = data;
        }

        Node(Object data, Node next) {
            this.data = data;
            this.next = next;
        }

        Object getData() {
            return this.data;
        }

        Node getNext() {
            return this.next;
        }

        void setNext(Node next) {
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
        var newNode = new Node(data, head.getNext());
        head.setNext(newNode);
    }

    /** Remove the first occurrence of the specified element in list.
     * @return Removed element.
     */
    public Object remove(Object o) {
        for (Node prev = head, cur = prev.getNext(); cur != null; prev = cur, cur = cur.getNext()) {
            Object data = cur.getData();
            if (data.equals(o)) {
                prev.setNext(cur.getNext());
                return data;
            }
        }
        return null;
    }

    /** Remove first element.
     * @return Removed element.
     */
    public Object removeFirst() {
        var first = head.getNext();
        if (first != null) {
            head.setNext(first.getNext());
            return first.getData();
        }
        return null;
    }

    /** Find the first occurrence of the specified element in list.
     * @return Link to element if found, false otherwise.
     */
    public Object find(Object o) {
        for (var cur = head.getNext(); cur != null; cur = cur.getNext()) {
            var data = cur.getData();
            if (data.equals(o)) {
                return data;
            }
        }
        return null;
    }

    /** Check if list is empty. */
    public boolean isEmpty() {
        return head.getNext() == null;
    }

    /** Remove all elements from list. */
    public void clear() {
        head.setNext(null);
    }
}
