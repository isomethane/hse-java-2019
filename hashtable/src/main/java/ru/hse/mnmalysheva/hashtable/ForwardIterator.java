package ru.hse.mnmalysheva.hashtable;

/** One-directional iterator interface. */
public interface ForwardIterator {
    /** Check if there is element after cursor. */
    boolean hasNext();
    /** Get next element. */
    Object getNext();
    /** Move cursor forward. */
    void goNext();
    /** Remove element after cursor. */
    void remove();
    /** Set element after cursor. */
    void set(Object data);
    /** Add element before cursor. */
    void add(Object data);
}
