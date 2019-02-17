package ru.hse.mnmalysheva.treeset;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TreapSetTest {
    private MyTreeSet<Integer> intSet;
    private MyTreeSet<Integer> reverseIntSet;

    @BeforeEach
    void init() {
        intSet = new TreapSet<>();
        reverseIntSet = intSet.descendingSet();
    }

    // comparator tests

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

    @Test
    void nullsCanBeStoredWithOkComparator() {
        intSet = new TreapSet<>((a, b) ->
                a == null ?
                        b == null ?
                                0 :
                                Integer.compare(0, b) :
                        b == null ?
                                a.compareTo(0) :
                                a.compareTo(b));
        intSet.add(1);
        intSet.add(3);
        assertTrue(intSet.add(null));
        assertFalse(intSet.add(0));
        intSet.add(-1);
        intSet.add(2);
        var it = intSet.iterator();
        assertEquals(-1, it.next().intValue());
        assertNull(it.next());
        assertEquals(1, it.next().intValue());
        assertEquals(2, it.next().intValue());
        assertEquals(3, it.next().intValue());
        assertFalse(it.hasNext());

        assertTrue(intSet.remove(0));
        assertTrue(intSet.add(0));
        assertFalse(intSet.add(null));
        it = intSet.iterator();
        assertEquals(-1, it.next().intValue());
        assertEquals(0, it.next().intValue());
        assertEquals(1, it.next().intValue());
        assertEquals(2, it.next().intValue());
        assertEquals(3, it.next().intValue());
        assertFalse(it.hasNext());
    }

    @Test
    void canRemoveOtherTypeThatCanCompareToWithNoComparator() {
        class StrangeInt implements Comparable<StrangeInt> {
            int value;
            StrangeInt(int value) { this.value = value; }
            @Override
            public boolean equals(Object o) {
                if (o instanceof StrangeInt) {
                    return value == ((StrangeInt) o).value;
                }
                return false;
            }

            @Override
            public int compareTo(@NotNull StrangeInt o) {
                return Integer.compare(value, o.value);
            }
        }
        class StrangeIntFoo extends StrangeInt {
            String foo = "foo";
            StrangeIntFoo(int value) { super(value); }
        }
        class StrangeIntBar extends StrangeInt {
            String bar = "bar";
            StrangeIntBar(int value) { super(value); }
        }
        var strangeIntSet = new TreapSet<StrangeIntFoo>();
        strangeIntSet.add(new StrangeIntFoo(1));
        strangeIntSet.add(new StrangeIntFoo(3));
        strangeIntSet.add(new StrangeIntFoo(2));
        strangeIntSet.add(new StrangeIntFoo(4));
        assertTrue(strangeIntSet.remove(new StrangeIntBar(2)));
        var it = strangeIntSet.iterator();
        assertEquals(new StrangeInt(1), it.next());
        assertEquals(new StrangeInt(3), it.next());
        assertEquals(new StrangeInt(4), it.next());
        assertFalse(it.hasNext());
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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        intSet.remove(0);
        assertEquals(5, intSet.size());
        intSet.remove(4);
        assertEquals(4, intSet.size());
        intSet.remove(4);
        assertEquals(4, intSet.size());
        intSet.remove(10);
        assertEquals(4, intSet.size());
        intSet.remove(9);
        assertEquals(3, intSet.size());
        intSet.remove(1);
        assertEquals(2, intSet.size());
        intSet.remove(1);
        assertEquals(2, intSet.size());
        intSet.remove(2);
        assertEquals(1, intSet.size());
        intSet.remove(6);
        assertEquals(0, intSet.size());
    }

    // add tests

    @Test
    void addNotExistingReturnsTrue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9
    }

    @Test
    void addExistingReturnsFalse() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        assertFalse(intSet.add(1));
        assertFalse(intSet.add(2));
        assertFalse(intSet.add(4));
        assertFalse(intSet.add(6));
        assertFalse(intSet.add(9));
    }

    // remove tests

    @Test
    void removeNotExistingReturnsFalse() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        assertFalse(intSet.remove(0));
        assertFalse(intSet.remove(3));
        assertFalse(intSet.remove(10));

        intSet.remove(4);
        assertFalse(intSet.remove(4));
    }

    @Test
    void removeExistingReturnsTrue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        assertTrue(intSet.remove(2));
        assertTrue(intSet.remove(9));
        assertTrue(intSet.remove(1));
        assertTrue(intSet.remove(4));
        assertTrue(intSet.remove(6));
    }

    // iterator tests

    @Test
    void iteratorNextReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = intSet.iterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertEquals(Integer.valueOf(9), it.next());
    }

    @Test
    void iteratorHasNextReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = intSet.iterator();
        it.next();
        it.next();
        intSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = intSet.iterator();
        it.next();
        it.next();
        intSet.remove(8);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void iteratorNextDoesNotThrowAfterNoModification() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = intSet.iterator();
        it.next();
        it.next();
        intSet.add(4);
        it.next();

        it = intSet.iterator();
        it.next();
        it.next();
        intSet.remove(3);
        it.next();
    }

    @Test
    void iteratorRemoveWorksCorrect() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = intSet.iterator();
        it.next();
        it.remove();
        it.next();
        it.next();
        it.remove();
        it.next();
        it.next();
        it.remove();
        it = intSet.iterator();
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void iteratorRemoveBeforeNextThrowsIllegalStateException() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = intSet.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void iteratorRemoveAfterRemoveThrowsIllegalStateException() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = intSet.iterator();
        it.next();
        it.remove();
        assertThrows(IllegalStateException.class, it::remove);
    }

    // descending iterator tests

    @Test
    void descendingIteratorNextReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = intSet.descendingIterator();
        assertEquals(Integer.valueOf(9), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test
    void descendingIteratorHasNextReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = intSet.descendingIterator();
        it.next();
        it.next();
        intSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = intSet.descendingIterator();
        it.next();
        it.next();
        intSet.remove(8);
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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        assertNull(intSet.lower(0));
        assertNull(intSet.lower(1));
        assertEquals(Integer.valueOf(1), intSet.lower(2));
        assertEquals(Integer.valueOf(2), intSet.lower(3));
        assertEquals(Integer.valueOf(2), intSet.lower(4));
        assertEquals(Integer.valueOf(4), intSet.lower(5));
        assertEquals(Integer.valueOf(4), intSet.lower(6));
        assertEquals(Integer.valueOf(6), intSet.lower(7));
        assertEquals(Integer.valueOf(6), intSet.lower(8));
        assertEquals(Integer.valueOf(6), intSet.lower(9));
        assertEquals(Integer.valueOf(9), intSet.lower(10));
    }

    // higher tests

    @Test
    void higherReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        assertEquals(Integer.valueOf(1), intSet.higher(0));
        assertEquals(Integer.valueOf(2), intSet.higher(1));
        assertEquals(Integer.valueOf(4), intSet.higher(2));
        assertEquals(Integer.valueOf(4), intSet.higher(3));
        assertEquals(Integer.valueOf(6), intSet.higher(4));
        assertEquals(Integer.valueOf(6), intSet.higher(5));
        assertEquals(Integer.valueOf(9), intSet.higher(6));
        assertEquals(Integer.valueOf(9), intSet.higher(7));
        assertEquals(Integer.valueOf(9), intSet.higher(8));
        assertNull(intSet.higher(9));
        assertNull(intSet.higher(10));
    }

    // floor tests

    @Test
    void floorReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        assertNull(intSet.floor(0));
        assertEquals(Integer.valueOf(1), intSet.floor(1));
        assertEquals(Integer.valueOf(2), intSet.floor(2));
        assertEquals(Integer.valueOf(2), intSet.floor(3));
        assertEquals(Integer.valueOf(4), intSet.floor(4));
        assertEquals(Integer.valueOf(4), intSet.floor(5));
        assertEquals(Integer.valueOf(6), intSet.floor(6));
        assertEquals(Integer.valueOf(6), intSet.floor(7));
        assertEquals(Integer.valueOf(6), intSet.floor(8));
        assertEquals(Integer.valueOf(9), intSet.floor(9));
        assertEquals(Integer.valueOf(9), intSet.floor(10));
    }

    // ceiling tests

    @Test
    void ceilingReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        assertEquals(Integer.valueOf(1), intSet.ceiling(0));
        assertEquals(Integer.valueOf(1), intSet.ceiling(1));
        assertEquals(Integer.valueOf(2), intSet.ceiling(2));
        assertEquals(Integer.valueOf(4), intSet.ceiling(3));
        assertEquals(Integer.valueOf(4), intSet.ceiling(4));
        assertEquals(Integer.valueOf(6), intSet.ceiling(5));
        assertEquals(Integer.valueOf(6), intSet.ceiling(6));
        assertEquals(Integer.valueOf(9), intSet.ceiling(7));
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
        assert(intSet.size() == 2 && descendingIntSet.size() == intSet.size());
        descendingIntSet.add(1);
        assert(intSet.size() == 3 && descendingIntSet.size() == intSet.size());
        descendingIntSet.add(3);
        assert(intSet.size() == 4 && descendingIntSet.size() == intSet.size());
        intSet.add(0);
        assert(intSet.size() == 5 && descendingIntSet.size() == intSet.size());
        intSet.add(5);
        assert(intSet.size() == 6 && descendingIntSet.size() == intSet.size());
        assertTrue(intSet.contains(1));
        assertTrue(intSet.contains(3));
        assertTrue(descendingIntSet.contains(0));
        assertTrue(descendingIntSet.contains(2));
        assertTrue(descendingIntSet.contains(4));
        assertTrue(descendingIntSet.contains(5));

        intSet.remove(3);
        assert(intSet.size() == 5 && descendingIntSet.size() == intSet.size());
        descendingIntSet.remove(4);
        assert(intSet.size() == 4 && descendingIntSet.size() == intSet.size());
        assertFalse(descendingIntSet.contains(3));
        assertFalse(intSet.contains(4));

        intSet.clear();
        assertTrue(descendingIntSet.isEmpty());
    }

    // descending set iterator tests

    @Test
    void descendingSetIteratorNextReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = reverseIntSet.iterator();
        assertEquals(Integer.valueOf(9), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test
    void descendingSetIteratorHasNextReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = reverseIntSet.iterator();
        it.next();
        it.next();
        reverseIntSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        reverseIntSet.remove(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        intSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        intSet.remove(8);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void descendingSetIteratorNextDoesNotThrowAfterNoModification() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = reverseIntSet.iterator();
        it.next();
        it.next();
        reverseIntSet.add(4);
        it.next();

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        reverseIntSet.remove(8);
        it.next();

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        intSet.add(4);
        it.next();

        it = reverseIntSet.iterator();
        it.next();
        it.next();
        intSet.remove(8);
        it.next();
    }

    // descending set descending iterator tests

    @Test
    void descendingSetDescendingIteratorNextReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

        var it = reverseIntSet.descendingIterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertEquals(Integer.valueOf(9), it.next());
    }

    @Test
    void descendingSetDescendingIteratorHasNextReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9

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
        reverseIntSet.addAll(Arrays.asList(4, 1, 3));
        var it = reverseIntSet.descendingIterator();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void descendingSetDescendingIteratorNextThrowsConcurrentModificationExceptionAfterModification() {
        reverseIntSet.addAll(Arrays.asList(4, 1, 3));

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

        it = reverseIntSet.descendingIterator();
        it.next();
        it.next();
        reverseIntSet.clear();
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
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9
        assertNull(reverseIntSet.higher(0));
        assertNull(reverseIntSet.higher(1));
        assertEquals(Integer.valueOf(1), reverseIntSet.higher(2));
        assertEquals(Integer.valueOf(2), reverseIntSet.higher(3));
        assertEquals(Integer.valueOf(2), reverseIntSet.higher(4));
        assertEquals(Integer.valueOf(4), reverseIntSet.higher(5));
        assertEquals(Integer.valueOf(4), reverseIntSet.higher(6));
        assertEquals(Integer.valueOf(6), reverseIntSet.higher(7));
        assertEquals(Integer.valueOf(6), reverseIntSet.higher(8));
        assertEquals(Integer.valueOf(6), reverseIntSet.higher(9));
        assertEquals(Integer.valueOf(9), reverseIntSet.higher(10));
    }

    // descending set lower tests

    @Test
    void descendingSetLowerReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9
        assertEquals(Integer.valueOf(1), reverseIntSet.lower(0));
        assertEquals(Integer.valueOf(2), reverseIntSet.lower(1));
        assertEquals(Integer.valueOf(4), reverseIntSet.lower(2));
        assertEquals(Integer.valueOf(4), reverseIntSet.lower(3));
        assertEquals(Integer.valueOf(6), reverseIntSet.lower(4));
        assertEquals(Integer.valueOf(6), reverseIntSet.lower(5));
        assertEquals(Integer.valueOf(9), reverseIntSet.lower(6));
        assertEquals(Integer.valueOf(9), reverseIntSet.lower(7));
        assertEquals(Integer.valueOf(9), reverseIntSet.lower(8));
        assertNull(reverseIntSet.lower(9));
        assertNull(reverseIntSet.lower(10));
    }

    // descending set ceiling tests

    @Test
    void descendingSetCeilingReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9
        assertNull(reverseIntSet.ceiling(0));
        assertEquals(Integer.valueOf(1), reverseIntSet.ceiling(1));
        assertEquals(Integer.valueOf(2), reverseIntSet.ceiling(2));
        assertEquals(Integer.valueOf(2), reverseIntSet.ceiling(3));
        assertEquals(Integer.valueOf(4), reverseIntSet.ceiling(4));
        assertEquals(Integer.valueOf(4), reverseIntSet.ceiling(5));
        assertEquals(Integer.valueOf(6), reverseIntSet.ceiling(6));
        assertEquals(Integer.valueOf(6), reverseIntSet.ceiling(7));
        assertEquals(Integer.valueOf(6), reverseIntSet.ceiling(8));
        assertEquals(Integer.valueOf(9), reverseIntSet.ceiling(9));
        assertEquals(Integer.valueOf(9), reverseIntSet.ceiling(10));
    }

    // descending set floor tests

    @Test
    void descendingSetFloorReturnsCorrectValue() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9
        assertEquals(Integer.valueOf(1), reverseIntSet.floor(0));
        assertEquals(Integer.valueOf(1), reverseIntSet.floor(1));
        assertEquals(Integer.valueOf(2), reverseIntSet.floor(2));
        assertEquals(Integer.valueOf(4), reverseIntSet.floor(3));
        assertEquals(Integer.valueOf(4), reverseIntSet.floor(4));
        assertEquals(Integer.valueOf(6), reverseIntSet.floor(5));
        assertEquals(Integer.valueOf(6), reverseIntSet.floor(6));
        assertEquals(Integer.valueOf(9), reverseIntSet.floor(7));
        assertEquals(Integer.valueOf(9), reverseIntSet.floor(8));
        assertEquals(Integer.valueOf(9), reverseIntSet.floor(9));
        assertNull(reverseIntSet.floor(10));
    }

    // descending set descending set tests

    @Test
    void descendingSetDescendingSetReturnsOriginalSet() {
        intSet.addAll(Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1)); // 1 2 4 6 9
        var it = reverseIntSet.descendingSet().iterator();
        assertArrayEquals(intSet.toArray(), reverseIntSet.descendingSet().toArray());
    }
}