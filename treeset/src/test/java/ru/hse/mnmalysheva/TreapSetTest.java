package ru.hse.mnmalysheva;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TreapSetTest {
    private MyTreeSet<Integer> intSet;
    private MyTreeSet<Integer> reverseIntSet;

    @BeforeEach
    void init() {
        intSet = new TreapSet<>();
        reverseIntSet = intSet.descendingSet();
    }

    // comparator test

    @Test
    void comparatorConstructorWorksCorrectly() {
        var stringSet = new TreapSet<String>(Comparator.reverseOrder());
        assertTrue(stringSet.add("xyz"));
        assertTrue(stringSet.add("abcd"));
        assertFalse(stringSet.add("abcd"));
        assertTrue(stringSet.add("ijk"));
        var it = stringSet.iterator();
        assertEquals("xyz", it.next());
        assertEquals("ijk", it.next());
        assertEquals("abcd", it.next());
    }

    // size tests

    @Test
    void emptySetSize() {
        assertEquals(0, intSet.size());
    }

    @Test
    void sizeChangesCorrectlyAfterAdd() {
        intSet.add(2);
        assertEquals(1, intSet.size());
        intSet.add(4);
        assertEquals(2, intSet.size());
        intSet.add(4);
        assertEquals(2, intSet.size());
        intSet.add(1);
        assertEquals(3, intSet.size());
        intSet.add(3);
        assertEquals(4, intSet.size());
        intSet.add(5);
        assertEquals(5, intSet.size());
        intSet.add(1);
        assertEquals(5, intSet.size());
    }

    @Test
    void sizeChangesCorrectlyAfterRemove() {
        intSet.add(1);
        intSet.add(2);
        intSet.add(3);
        intSet.add(4);
        intSet.add(5);

        intSet.remove(0);
        assertEquals(5, intSet.size());
        intSet.remove(3);
        assertEquals(4, intSet.size());
        intSet.remove(3);
        assertEquals(4, intSet.size());
        intSet.remove(6);
        assertEquals(4, intSet.size());
        intSet.remove(5);
        assertEquals(3, intSet.size());
        intSet.remove(1);
        assertEquals(2, intSet.size());
        intSet.remove(1);
        assertEquals(2, intSet.size());
        intSet.remove(2);
        assertEquals(1, intSet.size());
        intSet.remove(4);
        assertEquals(0, intSet.size());
    }

    // add tests

    @Test
    void addNotExistingReturnsTrue() {
        assertTrue(intSet.add(2));
        assertTrue(intSet.add(4));
        assertTrue(intSet.add(1));
        assertTrue(intSet.add(3));
        assertTrue(intSet.add(5));
    }

    @Test
    void addExistingReturnsFalse() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        assertFalse(intSet.add(1));
        assertFalse(intSet.add(2));
        assertFalse(intSet.add(3));
        assertFalse(intSet.add(4));
        assertFalse(intSet.add(5));
    }

    // remove tests

    @Test
    void removeNotExistingReturnsFalse() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(5);

        assertFalse(intSet.remove(0));
        assertFalse(intSet.remove(6));
        assertFalse(intSet.remove(3));

        intSet.remove(4);
        assertFalse(intSet.remove(4));
    }

    @Test
    void removeExistingReturnsTrue() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        assertTrue(intSet.remove(3));
        assertTrue(intSet.remove(1));
        assertTrue(intSet.remove(5));
        assertTrue(intSet.remove(4));
        assertTrue(intSet.remove(2));
    }

    // iterator tests

    @Test
    void iteratorNextReturnsCorrectValue() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.iterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(3), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(5), it.next());
    }

    @Test
    void iteratorHasNextReturnsCorrectValue() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.iterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void iteratorNextThrowsNoSuchElementExceptionAfterLastElement() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.iterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorNextThrowsConcurrentModificationExceptionAfterModification() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.iterator();
        it.next();
        it.next();
        intSet.add(6);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = intSet.iterator();
        it.next();
        it.next();
        intSet.remove(6);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void iteratorNextDoesNotThrowAfterNoModification() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.iterator();
        it.next();
        it.next();
        intSet.add(4);
        it.next();

        it = intSet.iterator();
        it.next();
        it.next();
        intSet.remove(6);
        it.next();
    }

    // descending iterator tests

    @Test
    void descendingIteratorNextReturnsCorrectValue() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.descendingIterator();
        assertEquals(Integer.valueOf(5), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(3), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test
    void descendingIteratorHasNextReturnsCorrectValue() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.descendingIterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void descendingIteratorNextThrowsNoSuchElementExceptionAfterLastElement() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.descendingIterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void descendingIteratorNextThrowsConcurrentModificationExceptionAfterModification() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = intSet.descendingIterator();
        it.next();
        it.next();
        intSet.add(6);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = intSet.descendingIterator();
        it.next();
        it.next();
        intSet.remove(6);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    // first tests

    @Test
    void firstInEmptySetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, intSet::first);
    }

    @Test
    void firstReturnsCorrectValue() {
        intSet.add(4);
        assertEquals(Integer.valueOf(4), intSet.first());
        intSet.add(6);
        assertEquals(Integer.valueOf(4), intSet.first());
        intSet.add(2);
        assertEquals(Integer.valueOf(2), intSet.first());
        intSet.add(9);
        assertEquals(Integer.valueOf(2), intSet.first());
        intSet.add(1);
        assertEquals(Integer.valueOf(1), intSet.first());
        intSet.add(3);
        assertEquals(Integer.valueOf(1), intSet.first());
        intSet.add(7);
        assertEquals(Integer.valueOf(1), intSet.first());
    }

    // last tests

    @Test
    void lastInEmptySetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, intSet::last);
    }

    @Test
    void lastReturnsCorrectValue() {
        intSet.add(4);
        assertEquals(Integer.valueOf(4), intSet.last());
        intSet.add(6);
        assertEquals(Integer.valueOf(6), intSet.last());
        intSet.add(2);
        assertEquals(Integer.valueOf(6), intSet.last());
        intSet.add(9);
        assertEquals(Integer.valueOf(9), intSet.last());
        intSet.add(1);
        assertEquals(Integer.valueOf(9), intSet.last());
        intSet.add(3);
        assertEquals(Integer.valueOf(9), intSet.last());
        intSet.add(7);
        assertEquals(Integer.valueOf(9), intSet.last());
    }

    // lower tests

    @Test
    void lowerReturnsCorrectValue() {
        intSet.add(4);
        intSet.add(6);
        intSet.add(2);
        intSet.add(9);
        intSet.add(1);
        intSet.add(3);
        intSet.add(7);
        assertNull(intSet.lower(0));
        assertNull(intSet.lower(1));
        assertEquals(Integer.valueOf(1), intSet.lower(2));
        assertEquals(Integer.valueOf(2), intSet.lower(3));
        assertEquals(Integer.valueOf(3), intSet.lower(4));
        assertEquals(Integer.valueOf(4), intSet.lower(5));
        assertEquals(Integer.valueOf(4), intSet.lower(6));
        assertEquals(Integer.valueOf(6), intSet.lower(7));
        assertEquals(Integer.valueOf(7), intSet.lower(8));
        assertEquals(Integer.valueOf(7), intSet.lower(9));
        assertEquals(Integer.valueOf(9), intSet.lower(10));
    }

    // higher tests

    @Test
    void higherReturnsCorrectValue() {
        intSet.add(4);
        intSet.add(6);
        intSet.add(2);
        intSet.add(9);
        intSet.add(1);
        intSet.add(3);
        intSet.add(7);
        assertEquals(Integer.valueOf(1), intSet.higher(0));
        assertEquals(Integer.valueOf(2), intSet.higher(1));
        assertEquals(Integer.valueOf(3), intSet.higher(2));
        assertEquals(Integer.valueOf(4), intSet.higher(3));
        assertEquals(Integer.valueOf(6), intSet.higher(4));
        assertEquals(Integer.valueOf(6), intSet.higher(5));
        assertEquals(Integer.valueOf(7), intSet.higher(6));
        assertEquals(Integer.valueOf(9), intSet.higher(7));
        assertEquals(Integer.valueOf(9), intSet.higher(8));
        assertNull(intSet.higher(9));
        assertNull(intSet.higher(10));
    }

    // floor tests

    @Test
    void floorReturnsCorrectValue() {
        intSet.add(4);
        intSet.add(6);
        intSet.add(2);
        intSet.add(9);
        intSet.add(1);
        intSet.add(3);
        intSet.add(7);
        assertNull(intSet.floor(0));
        assertEquals(Integer.valueOf(1), intSet.floor(1));
        assertEquals(Integer.valueOf(2), intSet.floor(2));
        assertEquals(Integer.valueOf(3), intSet.floor(3));
        assertEquals(Integer.valueOf(4), intSet.floor(4));
        assertEquals(Integer.valueOf(4), intSet.floor(5));
        assertEquals(Integer.valueOf(6), intSet.floor(6));
        assertEquals(Integer.valueOf(7), intSet.floor(7));
        assertEquals(Integer.valueOf(7), intSet.floor(8));
        assertEquals(Integer.valueOf(9), intSet.floor(9));
        assertEquals(Integer.valueOf(9), intSet.floor(10));
    }

    // ceiling tests

    @Test
    void ceilingReturnsCorrectValue() {
        intSet.add(4);
        intSet.add(6);
        intSet.add(2);
        intSet.add(9);
        intSet.add(1);
        intSet.add(3);
        intSet.add(7);
        assertEquals(Integer.valueOf(1), intSet.ceiling(0));
        assertEquals(Integer.valueOf(1), intSet.ceiling(1));
        assertEquals(Integer.valueOf(2), intSet.ceiling(2));
        assertEquals(Integer.valueOf(3), intSet.ceiling(3));
        assertEquals(Integer.valueOf(4), intSet.ceiling(4));
        assertEquals(Integer.valueOf(6), intSet.ceiling(5));
        assertEquals(Integer.valueOf(6), intSet.ceiling(6));
        assertEquals(Integer.valueOf(7), intSet.ceiling(7));
        assertEquals(Integer.valueOf(9), intSet.ceiling(8));
        assertEquals(Integer.valueOf(9), intSet.ceiling(9));
        assertNull(intSet.ceiling(10));
    }

    // descending set tests

    @Test
    void setAndDescendingSetAreBackedUpByEachOther() {
        intSet.add(4);
        intSet.add(2);
        var descendingIntSet = intSet.descendingSet();
        descendingIntSet.add(1);
        descendingIntSet.add(3);
        intSet.add(0);
        intSet.add(5);
        assertTrue(intSet.contains(1));
        assertTrue(intSet.contains(3));
        assertTrue(descendingIntSet.contains(0));
        assertTrue(descendingIntSet.contains(2));
        assertTrue(descendingIntSet.contains(4));
        assertTrue(descendingIntSet.contains(5));
    }

    // descending set size tests

    @Test
    void descendingSetEmptySetSize() {
        assertEquals(0, reverseIntSet.size());
    }

    @Test
    void descendingSetSizeChangesCorrectlyAfterAdd() {
        reverseIntSet.add(2);
        assertEquals(1, reverseIntSet.size());
        reverseIntSet.add(4);
        assertEquals(2, reverseIntSet.size());
        reverseIntSet.add(4);
        assertEquals(2, reverseIntSet.size());
        reverseIntSet.add(1);
        assertEquals(3, reverseIntSet.size());
        reverseIntSet.add(3);
        assertEquals(4, reverseIntSet.size());
        reverseIntSet.add(5);
        assertEquals(5, reverseIntSet.size());
        reverseIntSet.add(1);
        assertEquals(5, reverseIntSet.size());
    }

    @Test
    void descendingSetSizeChangesCorrectlyAfterRemove() {
        reverseIntSet.add(1);
        reverseIntSet.add(2);
        reverseIntSet.add(3);
        reverseIntSet.add(4);
        reverseIntSet.add(5);

        reverseIntSet.remove(0);
        assertEquals(5, reverseIntSet.size());
        reverseIntSet.remove(3);
        assertEquals(4, reverseIntSet.size());
        reverseIntSet.remove(3);
        assertEquals(4, reverseIntSet.size());
        reverseIntSet.remove(6);
        assertEquals(4, reverseIntSet.size());
        reverseIntSet.remove(5);
        assertEquals(3, reverseIntSet.size());
        reverseIntSet.remove(1);
        assertEquals(2, reverseIntSet.size());
        reverseIntSet.remove(1);
        assertEquals(2, reverseIntSet.size());
        reverseIntSet.remove(2);
        assertEquals(1, reverseIntSet.size());
        reverseIntSet.remove(4);
        assertEquals(0, reverseIntSet.size());
    }

    // descending set add tests

    @Test
    void descendingSetAddNotExistingReturnsTrue() {
        assertTrue(reverseIntSet.add(2));
        assertTrue(reverseIntSet.add(4));
        assertTrue(reverseIntSet.add(1));
        assertTrue(reverseIntSet.add(3));
        assertTrue(reverseIntSet.add(5));
    }

    @Test
    void descendingSetaddExistingReturnsFalse() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        assertFalse(reverseIntSet.add(1));
        assertFalse(reverseIntSet.add(2));
        assertFalse(reverseIntSet.add(3));
        assertFalse(reverseIntSet.add(4));
        assertFalse(reverseIntSet.add(5));
    }

    // descending set remove tests

    @Test
    void descendingSetRemoveNotExistingReturnsFalse() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(5);

        assertFalse(reverseIntSet.remove(0));
        assertFalse(reverseIntSet.remove(6));
        assertFalse(reverseIntSet.remove(3));

        reverseIntSet.remove(4);
        assertFalse(reverseIntSet.remove(4));
    }

    @Test
    void descendingSetRemoveExistingReturnsTrue() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        assertTrue(reverseIntSet.remove(3));
        assertTrue(reverseIntSet.remove(1));
        assertTrue(reverseIntSet.remove(5));
        assertTrue(reverseIntSet.remove(4));
        assertTrue(reverseIntSet.remove(2));
    }

    // descending set iterator tests

    @Test
    void descendingSetIteratorNextReturnsCorrectValue() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.iterator();
        assertEquals(Integer.valueOf(5), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(3), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test
    void descendingSetIteratorHasNextReturnsCorrectValue() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.iterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void descendingSetIteratorNextThrowsNoSuchElementExceptionAfterLastElement() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.iterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void descendingSetIteratorNextThrowsConcurrentModificationExceptionAfterModification() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.iterator();
        it.next();
        it.next();
        reverseIntSet.add(6);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        reverseIntSet.remove(6);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        intSet.add(6);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        intSet.remove(6);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void descendingSetIteratorNextDoesNotThrowAfterNoModification() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.iterator();
        it.next();
        it.next();
        reverseIntSet.add(4);
        it.next();

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        reverseIntSet.remove(6);
        it.next();

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        intSet.add(4);
        it.next();

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        intSet.remove(6);
        it.next();
    }

    // descending set descending iterator tests

    @Test
    void descendingSetDescendingIteratorNextReturnsCorrectValue() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.descendingIterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(3), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(5), it.next());
    }

    @Test
    void descendingSetDescendingIteratorHasNextReturnsCorrectValue() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.descendingIterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void descendingSetDescendingIteratorNextThrowsNoSuchElementExceptionAfterLastElement() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.descendingIterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void descendingSetDescendingIteratorNextThrowsConcurrentModificationExceptionAfterModification() {
        reverseIntSet.add(2);
        reverseIntSet.add(4);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(5);

        var it = reverseIntSet.descendingIterator();
        it.next();
        it.next();
        reverseIntSet.add(6);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseIntSet.descendingIterator();
        it.next();
        it.next();
        reverseIntSet.remove(6);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    // descending set first tests

    @Test
    void descendingSetFirstInEmptySetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, reverseIntSet::first);
    }

    @Test
    void descendingSetFirstReturnsCorrectValue() {
        reverseIntSet.add(4);
        assertEquals(Integer.valueOf(4), reverseIntSet.first());
        reverseIntSet.add(6);
        assertEquals(Integer.valueOf(6), reverseIntSet.first());
        reverseIntSet.add(2);
        assertEquals(Integer.valueOf(6), reverseIntSet.first());
        reverseIntSet.add(9);
        assertEquals(Integer.valueOf(9), reverseIntSet.first());
        reverseIntSet.add(1);
        assertEquals(Integer.valueOf(9), reverseIntSet.first());
        reverseIntSet.add(3);
        assertEquals(Integer.valueOf(9), reverseIntSet.first());
        reverseIntSet.add(7);
        assertEquals(Integer.valueOf(9), reverseIntSet.first());
    }

    // descending set last tests

    @Test
    void descendingSetLastInEmptySetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, reverseIntSet::last);
    }

    @Test
    void descendingSetLastReturnsCorrectValue() {
        reverseIntSet.add(4);
        assertEquals(Integer.valueOf(4), reverseIntSet.last());
        reverseIntSet.add(6);
        assertEquals(Integer.valueOf(4), reverseIntSet.last());
        reverseIntSet.add(2);
        assertEquals(Integer.valueOf(2), reverseIntSet.last());
        reverseIntSet.add(9);
        assertEquals(Integer.valueOf(2), reverseIntSet.last());
        reverseIntSet.add(1);
        assertEquals(Integer.valueOf(1), reverseIntSet.last());
        reverseIntSet.add(3);
        assertEquals(Integer.valueOf(1), reverseIntSet.last());
        reverseIntSet.add(7);
        assertEquals(Integer.valueOf(1), reverseIntSet.last());
    }

    // descending set higher tests

    @Test
    void descendingSetHigherReturnsCorrectValue() {
        reverseIntSet.add(4);
        reverseIntSet.add(6);
        reverseIntSet.add(2);
        reverseIntSet.add(9);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(7);
        assertNull(reverseIntSet.higher(0));
        assertNull(reverseIntSet.higher(1));
        assertEquals(Integer.valueOf(1), reverseIntSet.higher(2));
        assertEquals(Integer.valueOf(2), reverseIntSet.higher(3));
        assertEquals(Integer.valueOf(3), reverseIntSet.higher(4));
        assertEquals(Integer.valueOf(4), reverseIntSet.higher(5));
        assertEquals(Integer.valueOf(4), reverseIntSet.higher(6));
        assertEquals(Integer.valueOf(6), reverseIntSet.higher(7));
        assertEquals(Integer.valueOf(7), reverseIntSet.higher(8));
        assertEquals(Integer.valueOf(7), reverseIntSet.higher(9));
        assertEquals(Integer.valueOf(9), reverseIntSet.higher(10));
    }

    // descending set lower tests

    @Test
    void descendingSetLowerReturnsCorrectValue() {
        reverseIntSet.add(4);
        reverseIntSet.add(6);
        reverseIntSet.add(2);
        reverseIntSet.add(9);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(7);
        assertEquals(Integer.valueOf(1), reverseIntSet.lower(0));
        assertEquals(Integer.valueOf(2), reverseIntSet.lower(1));
        assertEquals(Integer.valueOf(3), reverseIntSet.lower(2));
        assertEquals(Integer.valueOf(4), reverseIntSet.lower(3));
        assertEquals(Integer.valueOf(6), reverseIntSet.lower(4));
        assertEquals(Integer.valueOf(6), reverseIntSet.lower(5));
        assertEquals(Integer.valueOf(7), reverseIntSet.lower(6));
        assertEquals(Integer.valueOf(9), reverseIntSet.lower(7));
        assertEquals(Integer.valueOf(9), reverseIntSet.lower(8));
        assertNull(reverseIntSet.lower(9));
        assertNull(reverseIntSet.lower(10));
    }

    // descending set ceiling tests

    @Test
    void descendingSetCeilingReturnsCorrectValue() {
        reverseIntSet.add(4);
        reverseIntSet.add(6);
        reverseIntSet.add(2);
        reverseIntSet.add(9);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(7);
        assertNull(reverseIntSet.ceiling(0));
        assertEquals(Integer.valueOf(1), reverseIntSet.ceiling(1));
        assertEquals(Integer.valueOf(2), reverseIntSet.ceiling(2));
        assertEquals(Integer.valueOf(3), reverseIntSet.ceiling(3));
        assertEquals(Integer.valueOf(4), reverseIntSet.ceiling(4));
        assertEquals(Integer.valueOf(4), reverseIntSet.ceiling(5));
        assertEquals(Integer.valueOf(6), reverseIntSet.ceiling(6));
        assertEquals(Integer.valueOf(7), reverseIntSet.ceiling(7));
        assertEquals(Integer.valueOf(7), reverseIntSet.ceiling(8));
        assertEquals(Integer.valueOf(9), reverseIntSet.ceiling(9));
        assertEquals(Integer.valueOf(9), reverseIntSet.ceiling(10));
    }

    // descending set floor tests

    @Test
    void descendingSetFloorReturnsCorrectValue() {
        reverseIntSet.add(4);
        reverseIntSet.add(6);
        reverseIntSet.add(2);
        reverseIntSet.add(9);
        reverseIntSet.add(1);
        reverseIntSet.add(3);
        reverseIntSet.add(7);
        assertEquals(Integer.valueOf(1), reverseIntSet.floor(0));
        assertEquals(Integer.valueOf(1), reverseIntSet.floor(1));
        assertEquals(Integer.valueOf(2), reverseIntSet.floor(2));
        assertEquals(Integer.valueOf(3), reverseIntSet.floor(3));
        assertEquals(Integer.valueOf(4), reverseIntSet.floor(4));
        assertEquals(Integer.valueOf(6), reverseIntSet.floor(5));
        assertEquals(Integer.valueOf(6), reverseIntSet.floor(6));
        assertEquals(Integer.valueOf(7), reverseIntSet.floor(7));
        assertEquals(Integer.valueOf(9), reverseIntSet.floor(8));
        assertEquals(Integer.valueOf(9), reverseIntSet.floor(9));
        assertNull(reverseIntSet.floor(10));
    }

    // descending set descending set tests

    @Test
    void descendingSetDescendingSetReturnsOriginalSet() {
        intSet.add(2);
        intSet.add(4);
        intSet.add(1);
        intSet.add(3);
        intSet.add(5);

        var it = reverseIntSet.descendingSet().iterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(3), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(5), it.next());
    }
}