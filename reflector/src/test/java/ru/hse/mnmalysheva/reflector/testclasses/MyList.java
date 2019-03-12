package ru.hse.mnmalysheva.reflector.testclasses;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Singly linked list. */
public class MyList<E> implements Iterable<E> {
    /** Fictive head. */
    private Node<E> head = new Node<>(null);

    /** Add element at list beginning. */
    public void add(@NotNull E element) {
        head.next = new Node<>(element, head.next);
    }

    /** Remove all elements from list. */
    public void clear() {
        head.next = null;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return new ListForwardIterator<>(head);
    }

    private static class Node<E> {
        private E data;
        private Node<E> next;

        private Node(E data) {
            this.data = data;
        }

        private Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private static class ListForwardIterator<E> implements Iterator<E> {
        private Node<E> previous;
        private Node<E> current;
        private boolean canRemove;

        private ListForwardIterator(Node<E> head) {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current.next != null;
        }

        @Override
        public @NotNull E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("The iteration has no more elements.");
            }
            previous = current;
            current = current.next;
            canRemove = true;
            return current.data;
        }

        @Override
        public void remove() {
            if (!canRemove) {
                if (previous == null) {
                    throw new IllegalStateException("Cannot remove element. " +
                            "The next method has not yet been called.");
                }
                throw new IllegalStateException("Cannot remove element. " +
                        "The remove method has already been called after the last call to the next method.");
            }
            previous.next = current.next;
            current = previous;
            canRemove = false;
        }
    }
}
