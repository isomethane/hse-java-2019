package ru.hse.mnmalysheva.hashtable;

import java.util.Iterator;

/** Singly linked list. */
public class List implements Iterable {
    /** Singly linked list node. */
    private class Node {
        private Object data;
        private Node next = null;

        public Node(Object data) {
            this.data = data;
        }
    }

    /** Singly linked list iterator. */
    private class ListIterator implements Iterator, ForwardIterator {
        /** Node located before cursor.
         * Invariant: must never become null.
         * If cursor locates before the first element, prevNode points to fictive head.
         */
        private Node prevNode;

        public ListIterator(Node prevNode) {
            this.prevNode = prevNode;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            return prevNode.next != null;
        }

        /** {@inheritDoc} */
        @Override
        public Object getNext() {
            return prevNode.next.data;
        }

        /** {@inheritDoc} */
        @Override
        public void goNext() {
            prevNode = prevNode.next;
        }

        /** {@inheritDoc} */
        @Override
        public Object next() {
            Object result = getNext();
            goNext();
            return result;
        }

        /** {@inheritDoc} */
        @Override
        public void remove() {
            prevNode.next = prevNode.next.next;
        }

        /** {@inheritDoc} */
        @Override
        public void set(Object data) {
            prevNode.next.data = data;
        }

        /** {@inheritDoc} */
        @Override
        public void add(Object data) {
            Node node = new Node(data);
            node.next = prevNode.next;
            prevNode.next = node;
            prevNode = node;
        }
    }

    /** Fictive head. */
    private Node head = new Node(null);

    /** {@inheritDoc} */
    public Iterator iterator() {
        return new ListIterator(head);
    }

    /** Return list iterator over the elements in list. */
    public ForwardIterator forwardIterator() {
        return new ListIterator(head);
    }

    /** Add element after fictive head. */
    public void add(Object data) {
        ForwardIterator it = forwardIterator();
        it.add(data);
    }

    /** Remove all elements from list. */
    public void clear() {
        head.next = null;
    }

    /** Check if list contains specified object. */
    public boolean contains(Object o) {
        for (Object i : this) {
            if (i.equals(o)) {
                return true;
            }
        }
        return false;
    }

    /** Find the first occurrence of the specified element in list. */
    public Object find(Object o) {
        for (Object i : this) {
            if (i.equals(o)) {
                return i;
            }
        }
        return null;
    }

    /** Remove the first occurrence of the specified element in list.
     * @return Removed element.
     */
    public Object remove(Object o) {
        for (ForwardIterator it = forwardIterator(); it.hasNext(); it.goNext()) {
            if (it.getNext().equals(o)) {
                Object removed = it.getNext();
                it.remove();
                return removed;
            }
        }
        return null;
    }
}
