package ru.hse.mnmalysheva.treeset;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/** Search tree set interface. */
public interface MyTreeSet<E> extends Set<E> {

    /** {@link TreeSet#descendingIterator()} **/
    @NotNull Iterator<E> descendingIterator();

    /** {@link TreeSet#descendingSet()} **/
    @NotNull MyTreeSet<E> descendingSet();

    /** {@link TreeSet#first()} **/
    E first();

    /** {@link TreeSet#last()} **/
    E last();

    /** {@link TreeSet#lower(Object)} **/
    E lower(E element);

    /** {@link TreeSet#higher(Object)} **/
    E higher(E element);

    /** {@link TreeSet#floor(Object)} **/
    E floor(E element);

    /** {@link TreeSet#ceiling(Object)} **/
    E ceiling(E element);
}
