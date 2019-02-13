package ru.hse.mnmalysheva;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Set;

/** Search tree set interface. */
public interface MyTreeSet<E> extends Set<E> {

    /** Returns an iterator over the elements in this set in descending order. */
    @NotNull Iterator<E> descendingIterator();

    /**
     * Returns a reverse order view of the elements contained in this set.
     * Changes to the set are reflected in the descending set, and vice-versa.
     * The returned set has an ordering equivalent to Collections.reverseOrder(comparator()).
     */
    @NotNull MyTreeSet<E> descendingSet();

    /** Returns the first (lowest) element currently in this set. */
    E first();

    /** Returns the last (highest) element currently in this set. */
    E last();

    /** Returns the greatest element in set less than the given element, or null if there is no such element. */
    E lower(E element);

    /** Returns the least element in set greater than the given element, or null if there is no such element. */
    E higher(E element);

    /** Returns the greatest element in set not greater than the given element, or null if there is no such element. */
    E floor(E element);

    /** Returns the least element in set not less than the given element, or null if there is no such element. */
    E ceiling(E element);
}
