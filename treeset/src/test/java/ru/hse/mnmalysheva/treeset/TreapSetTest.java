package ru.hse.mnmalysheva.treeset;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TreapSetTest {
    private MyTreeSet<Integer> freshSet;
    private MyTreeSet<Integer> filledSet;
    private MyTreeSet<Integer> reverseFreshSet;
    private MyTreeSet<Integer> reverseSet;
    private List<Integer> testList = Arrays.asList(4, 6, 2, 2, 9, 1, 6, 1);
    private Integer[] testArray = {1, 2, 4, 6, 9};

    @BeforeEach
    void init() {
        freshSet = new TreapSet<>();
        filledSet = new TreapSet<>();
        filledSet.addAll(testList);

        reverseFreshSet = freshSet.descendingSet();
        reverseSet = filledSet.descendingSet();
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
        var testSet = new TreapSet<Integer>((a, b) ->
                a == null ?
                        b == null ?
                                0 :
                                Integer.compare(0, b) :
                        b == null ?
                                a.compareTo(0) :
                                a.compareTo(b)
        );
        testSet.add(1);
        testSet.add(3);
        assertTrue(testSet.add(null));
        assertFalse(testSet.add(0));
        testSet.add(-1);
        testSet.add(2);
        var it = testSet.iterator();
        assertEquals(-1, it.next().intValue());
        assertNull(it.next());
        assertEquals(1, it.next().intValue());
        assertEquals(2, it.next().intValue());
        assertEquals(3, it.next().intValue());
        assertFalse(it.hasNext());

        assertTrue(testSet.remove(0));
        assertTrue(testSet.add(0));
        assertFalse(testSet.add(null));
        it = testSet.iterator();
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
            private int value;
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
            private String foo = "foo";
            private StrangeIntFoo(int value) { super(value); }
        }
        class StrangeIntBar extends StrangeInt {
            private String bar = "bar";
            private StrangeIntBar(int value) { super(value); }
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
        assertEquals(0, freshSet.size());
    }

    @Test
    void sizeChangesCorrectlyAfterAdd() {
        freshSet.add(2);
        assertEquals(1, freshSet.size());
        freshSet.add(4);
        assertEquals(2, freshSet.size());
        freshSet.add(4);
        assertEquals(2, freshSet.size());
        freshSet.add(1);
        assertEquals(3, freshSet.size());
        freshSet.add(3);
        assertEquals(4, freshSet.size());
        freshSet.add(5);
        assertEquals(5, freshSet.size());
        freshSet.add(1);
        assertEquals(5, freshSet.size());
    }

    @Test
    void sizeChangesCorrectlyAfterRemove() {
        filledSet.remove(0);
        assertEquals(5, filledSet.size());
        filledSet.remove(4);
        assertEquals(4, filledSet.size());
        filledSet.remove(4);
        assertEquals(4, filledSet.size());
        filledSet.remove(10);
        assertEquals(4, filledSet.size());
        filledSet.remove(9);
        assertEquals(3, filledSet.size());
        filledSet.remove(1);
        assertEquals(2, filledSet.size());
        filledSet.remove(1);
        assertEquals(2, filledSet.size());
        filledSet.remove(2);
        assertEquals(1, filledSet.size());
        filledSet.remove(6);
        assertEquals(0, filledSet.size());
    }

    // clear tests

    @Test
    void clearWorksCorrectly() {
        filledSet.clear();
        assertEquals(0, filledSet.size());
        assertFalse(filledSet.iterator().hasNext());
        assertFalse(filledSet.contains(1));
        assertFalse(filledSet.contains(2));
        assertFalse(filledSet.contains(4));
        assertFalse(filledSet.contains(6));
        assertFalse(filledSet.contains(9));
    }

    @Test
    void canUseSetAfterClear() {
        filledSet.clear();
        assertDoesNotThrow(() -> filledSet.addAll(testList));
        assertArrayEquals(testArray, filledSet.toArray());
    }

    // contains test

    @Test
    void containsWorksCorrectly() {
        assertTrue(filledSet.contains(1));
        assertTrue(filledSet.contains(2));
        assertTrue(filledSet.contains(4));
        assertTrue(filledSet.contains(6));
        assertTrue(filledSet.contains(9));
        assertFalse(filledSet.contains(0));
        assertFalse(filledSet.contains(3));
        assertFalse(filledSet.contains(10));
        filledSet.remove(2);
        assertFalse(filledSet.contains(2));
    }

    // add tests

    @Test
    void addNotExistingReturnsTrue() {
        assertTrue(filledSet.add(0));
        assertTrue(filledSet.add(3));
        assertTrue(filledSet.add(10));

        filledSet.remove(4);
        assertTrue(filledSet.add(4));
    }

    @Test
    void addExistingReturnsFalse() {
        assertFalse(filledSet.add(1));
        assertFalse(filledSet.add(2));
        assertFalse(filledSet.add(4));
        assertFalse(filledSet.add(6));
        assertFalse(filledSet.add(9));
    }

    // remove tests

    @Test
    void removeNotExistingReturnsFalse() {
        assertFalse(filledSet.remove(0));
        assertFalse(filledSet.remove(3));
        assertFalse(filledSet.remove(10));

        filledSet.remove(4);
        assertFalse(filledSet.remove(4));
    }

    @Test
    void removeExistingReturnsTrue() {
        assertTrue(filledSet.remove(2));
        assertTrue(filledSet.remove(9));
        assertTrue(filledSet.remove(1));
        assertTrue(filledSet.remove(4));
        assertTrue(filledSet.remove(6));
    }

    // iterator tests

    @Test
    void iteratorNextReturnsCorrectValue() {
        var it = filledSet.iterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertEquals(Integer.valueOf(9), it.next());
    }

    @Test
    void iteratorHasNextReturnsCorrectValue() {
        var it = filledSet.iterator();
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
        var it = filledSet.iterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorMethodsThrowConcurrentModificationExceptionAfterModification() {
        var it = filledSet.iterator();
        it.next();
        it.next();
        filledSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::hasNext);
        assertThrows(ConcurrentModificationException.class, it::next);
        assertThrows(ConcurrentModificationException.class, it::remove);

        it = filledSet.iterator();
        it.next();
        it.next();
        filledSet.remove(8);
        assertThrows(ConcurrentModificationException.class, it::hasNext);
        assertThrows(ConcurrentModificationException.class, it::next);
        assertThrows(ConcurrentModificationException.class, it::remove);

        it = filledSet.iterator();
        filledSet.clear();
        assertThrows(ConcurrentModificationException.class, it::hasNext);
    }

    @Test
    void iteratorMethodsDoNotThrowAfterNoModification() {
        var it = filledSet.iterator();
        it.next();
        it.next();
        filledSet.add(4);
        assertDoesNotThrow(it::hasNext);
        assertDoesNotThrow(it::next);

        it = filledSet.iterator();
        it.next();
        it.next();
        filledSet.remove(3);
        assertDoesNotThrow(it::hasNext);
        assertDoesNotThrow(it::next);

        it = freshSet.iterator();
        freshSet.clear();
        assertDoesNotThrow(it::hasNext);
    }

    @Test
    void iteratorRemoveWorksCorrectly() {
        var it = filledSet.iterator();
        it.next();
        it.remove();
        it.next();
        it.next();
        it.remove();
        it.next();
        it.next();
        it.remove();
        it = filledSet.iterator();
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void iteratorRemoveBeforeNextThrowsIllegalStateException() {
        var it = filledSet.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void iteratorRemoveAfterRemoveThrowsIllegalStateException() {
        var it = filledSet.iterator();
        it.next();
        it.remove();
        assertThrows(IllegalStateException.class, it::remove);
    }

    // descending iterator tests

    @Test
    void descendingIteratorNextReturnsCorrectValue() {
        var it = filledSet.descendingIterator();
        assertEquals(Integer.valueOf(9), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test
    void descendingIteratorHasNextReturnsCorrectValue() {
        var it = filledSet.descendingIterator();
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
        var it = filledSet.descendingIterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void descendingIteratorNextThrowsConcurrentModificationExceptionAfterModification() {
        var it = filledSet.descendingIterator();
        it.next();
        it.next();
        filledSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = filledSet.descendingIterator();
        it.next();
        it.next();
        filledSet.remove(8);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    // first tests

    @Test
    void firstInEmptySetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, freshSet::first);
    }

    @Test
    void firstReturnsCorrectValue() {
        freshSet.add(4);
        assertEquals(Integer.valueOf(4), freshSet.first());
        freshSet.add(6);
        assertEquals(Integer.valueOf(4), freshSet.first());
        freshSet.add(2);
        assertEquals(Integer.valueOf(2), freshSet.first());
        freshSet.add(9);
        assertEquals(Integer.valueOf(2), freshSet.first());
        freshSet.add(1);
        assertEquals(Integer.valueOf(1), freshSet.first());
        freshSet.add(3);
        assertEquals(Integer.valueOf(1), freshSet.first());
        freshSet.add(7);
        assertEquals(Integer.valueOf(1), freshSet.first());
    }

    // last tests

    @Test
    void lastInEmptySetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, freshSet::last);
    }

    @Test
    void lastReturnsCorrectValue() {
        freshSet.add(4);
        assertEquals(Integer.valueOf(4), freshSet.last());
        freshSet.add(6);
        assertEquals(Integer.valueOf(6), freshSet.last());
        freshSet.add(2);
        assertEquals(Integer.valueOf(6), freshSet.last());
        freshSet.add(9);
        assertEquals(Integer.valueOf(9), freshSet.last());
        freshSet.add(1);
        assertEquals(Integer.valueOf(9), freshSet.last());
        freshSet.add(3);
        assertEquals(Integer.valueOf(9), freshSet.last());
        freshSet.add(7);
        assertEquals(Integer.valueOf(9), freshSet.last());
    }

    // lower tests

    @Test
    void lowerReturnsCorrectValue() {
        assertNull(filledSet.lower(0));
        assertNull(filledSet.lower(1));
        assertEquals(Integer.valueOf(1), filledSet.lower(2));
        assertEquals(Integer.valueOf(2), filledSet.lower(3));
        assertEquals(Integer.valueOf(2), filledSet.lower(4));
        assertEquals(Integer.valueOf(4), filledSet.lower(5));
        assertEquals(Integer.valueOf(4), filledSet.lower(6));
        assertEquals(Integer.valueOf(6), filledSet.lower(7));
        assertEquals(Integer.valueOf(6), filledSet.lower(8));
        assertEquals(Integer.valueOf(6), filledSet.lower(9));
        assertEquals(Integer.valueOf(9), filledSet.lower(10));
    }

    // higher tests

    @Test
    void higherReturnsCorrectValue() {
        assertEquals(Integer.valueOf(1), filledSet.higher(0));
        assertEquals(Integer.valueOf(2), filledSet.higher(1));
        assertEquals(Integer.valueOf(4), filledSet.higher(2));
        assertEquals(Integer.valueOf(4), filledSet.higher(3));
        assertEquals(Integer.valueOf(6), filledSet.higher(4));
        assertEquals(Integer.valueOf(6), filledSet.higher(5));
        assertEquals(Integer.valueOf(9), filledSet.higher(6));
        assertEquals(Integer.valueOf(9), filledSet.higher(7));
        assertEquals(Integer.valueOf(9), filledSet.higher(8));
        assertNull(filledSet.higher(9));
        assertNull(filledSet.higher(10));
    }

    // floor tests

    @Test
    void floorReturnsCorrectValue() {
        assertNull(filledSet.floor(0));
        assertEquals(Integer.valueOf(1), filledSet.floor(1));
        assertEquals(Integer.valueOf(2), filledSet.floor(2));
        assertEquals(Integer.valueOf(2), filledSet.floor(3));
        assertEquals(Integer.valueOf(4), filledSet.floor(4));
        assertEquals(Integer.valueOf(4), filledSet.floor(5));
        assertEquals(Integer.valueOf(6), filledSet.floor(6));
        assertEquals(Integer.valueOf(6), filledSet.floor(7));
        assertEquals(Integer.valueOf(6), filledSet.floor(8));
        assertEquals(Integer.valueOf(9), filledSet.floor(9));
        assertEquals(Integer.valueOf(9), filledSet.floor(10));
    }

    // ceiling tests

    @Test
    void ceilingReturnsCorrectValue() {
        assertEquals(Integer.valueOf(1), filledSet.ceiling(0));
        assertEquals(Integer.valueOf(1), filledSet.ceiling(1));
        assertEquals(Integer.valueOf(2), filledSet.ceiling(2));
        assertEquals(Integer.valueOf(4), filledSet.ceiling(3));
        assertEquals(Integer.valueOf(4), filledSet.ceiling(4));
        assertEquals(Integer.valueOf(6), filledSet.ceiling(5));
        assertEquals(Integer.valueOf(6), filledSet.ceiling(6));
        assertEquals(Integer.valueOf(9), filledSet.ceiling(7));
        assertEquals(Integer.valueOf(9), filledSet.ceiling(8));
        assertEquals(Integer.valueOf(9), filledSet.ceiling(9));
        assertNull(filledSet.ceiling(10));
    }

    // descending set tests

    @Test
    void setAndDescendingSetAreBackedUpByEachOther() {
        freshSet.add(4);
        freshSet.add(2);
        reverseFreshSet = freshSet.descendingSet();
        assertTrue(freshSet.size() == 2 && reverseFreshSet.size() == freshSet.size());
        reverseFreshSet.add(1);
        assertTrue(freshSet.size() == 3 && reverseFreshSet.size() == freshSet.size());
        reverseFreshSet.add(3);
        assertTrue(freshSet.size() == 4 && reverseFreshSet.size() == freshSet.size());
        freshSet.add(0);
        assertTrue(freshSet.size() == 5 && reverseFreshSet.size() == freshSet.size());
        freshSet.add(5);
        assertTrue(freshSet.size() == 6 && reverseFreshSet.size() == freshSet.size());
        assertTrue(freshSet.contains(1));
        assertTrue(freshSet.contains(3));
        assertTrue(reverseFreshSet.contains(0));
        assertTrue(reverseFreshSet.contains(2));
        assertTrue(reverseFreshSet.contains(4));
        assertTrue(reverseFreshSet.contains(5));

        freshSet.remove(3);
        assertTrue(freshSet.size() == 5 && reverseFreshSet.size() == freshSet.size());
        reverseFreshSet.remove(4);
        assertTrue(freshSet.size() == 4 && reverseFreshSet.size() == freshSet.size());
        assertFalse(reverseFreshSet.contains(3));
        assertFalse(freshSet.contains(4));

        freshSet.clear();
        assertTrue(reverseFreshSet.isEmpty());
    }

    // descending set iterator tests

    @Test
    void descendingSetIteratorNextReturnsCorrectValue() {
        var it = reverseSet.iterator();
        assertEquals(Integer.valueOf(9), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test
    void descendingSetIteratorHasNextReturnsCorrectValue() {
        var it = reverseSet.iterator();
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
        var it = reverseSet.iterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void descendingSetIteratorNextThrowsConcurrentModificationExceptionAfterModification() {
        var it = reverseSet.iterator();
        it.next();
        it.next();
        reverseSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseSet.iterator();
        it.next();
        it.next();
        reverseSet.remove(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseSet.iterator();
        it.next();
        it.next();
        filledSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseSet.iterator();
        it.next();
        it.next();
        filledSet.remove(8);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void descendingSetIteratorNextDoesNotThrowAfterNoModification() {
        var it = reverseSet.iterator();
        it.next();
        it.next();
        reverseSet.add(4);
        assertDoesNotThrow(it::next);

        it = reverseSet.iterator();
        it.next();
        it.next();
        reverseSet.remove(3);
        assertDoesNotThrow(it::next);

        it = reverseSet.iterator();
        it.next();
        it.next();
        filledSet.add(4);
        assertDoesNotThrow(it::next);

        it = reverseSet.iterator();
        it.next();
        it.next();
        filledSet.remove(3);
        assertDoesNotThrow(it::next);
    }

    // descending set descending iterator tests

    @Test
    void descendingSetDescendingIteratorNextReturnsCorrectValue() {
        var it = reverseSet.descendingIterator();
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());
        assertEquals(Integer.valueOf(4), it.next());
        assertEquals(Integer.valueOf(6), it.next());
        assertEquals(Integer.valueOf(9), it.next());
    }

    @Test
    void descendingSetDescendingIteratorHasNextReturnsCorrectValue() {
        var it = reverseSet.descendingIterator();
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
        var it = reverseSet.descendingIterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void descendingSetDescendingIteratorNextThrowsConcurrentModificationExceptionAfterModification() {
        var it = reverseSet.descendingIterator();
        it.next();
        it.next();
        reverseSet.add(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseSet.descendingIterator();
        it.next();
        it.next();
        reverseSet.remove(8);
        assertThrows(ConcurrentModificationException.class, it::next);

        it = reverseSet.descendingIterator();
        it.next();
        it.next();
        reverseSet.clear();
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    // descending set first tests

    @Test
    void descendingSetFirstInEmptySetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, reverseFreshSet::first);
    }

    @Test
    void descendingSetFirstReturnsCorrectValue() {
        reverseFreshSet.add(4);
        assertEquals(Integer.valueOf(4), reverseFreshSet.first());
        reverseFreshSet.add(6);
        assertEquals(Integer.valueOf(6), reverseFreshSet.first());
        reverseFreshSet.add(2);
        assertEquals(Integer.valueOf(6), reverseFreshSet.first());
        reverseFreshSet.add(9);
        assertEquals(Integer.valueOf(9), reverseFreshSet.first());
        reverseFreshSet.add(1);
        assertEquals(Integer.valueOf(9), reverseFreshSet.first());
        reverseFreshSet.add(3);
        assertEquals(Integer.valueOf(9), reverseFreshSet.first());
        reverseFreshSet.add(7);
        assertEquals(Integer.valueOf(9), reverseFreshSet.first());
    }

    // descending set last tests

    @Test
    void descendingSetLastInEmptySetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, reverseFreshSet::last);
    }

    @Test
    void descendingSetLastReturnsCorrectValue() {
        reverseFreshSet.add(4);
        assertEquals(Integer.valueOf(4), reverseFreshSet.last());
        reverseFreshSet.add(6);
        assertEquals(Integer.valueOf(4), reverseFreshSet.last());
        reverseFreshSet.add(2);
        assertEquals(Integer.valueOf(2), reverseFreshSet.last());
        reverseFreshSet.add(9);
        assertEquals(Integer.valueOf(2), reverseFreshSet.last());
        reverseFreshSet.add(1);
        assertEquals(Integer.valueOf(1), reverseFreshSet.last());
        reverseFreshSet.add(3);
        assertEquals(Integer.valueOf(1), reverseFreshSet.last());
        reverseFreshSet.add(7);
        assertEquals(Integer.valueOf(1), reverseFreshSet.last());
    }

    // descending set higher tests

    @Test
    void descendingSetHigherReturnsCorrectValue() {
        assertNull(reverseSet.higher(0));
        assertNull(reverseSet.higher(1));
        assertEquals(Integer.valueOf(1), reverseSet.higher(2));
        assertEquals(Integer.valueOf(2), reverseSet.higher(3));
        assertEquals(Integer.valueOf(2), reverseSet.higher(4));
        assertEquals(Integer.valueOf(4), reverseSet.higher(5));
        assertEquals(Integer.valueOf(4), reverseSet.higher(6));
        assertEquals(Integer.valueOf(6), reverseSet.higher(7));
        assertEquals(Integer.valueOf(6), reverseSet.higher(8));
        assertEquals(Integer.valueOf(6), reverseSet.higher(9));
        assertEquals(Integer.valueOf(9), reverseSet.higher(10));
    }

    // descending set lower tests

    @Test
    void descendingSetLowerReturnsCorrectValue() {
        assertEquals(Integer.valueOf(1), reverseSet.lower(0));
        assertEquals(Integer.valueOf(2), reverseSet.lower(1));
        assertEquals(Integer.valueOf(4), reverseSet.lower(2));
        assertEquals(Integer.valueOf(4), reverseSet.lower(3));
        assertEquals(Integer.valueOf(6), reverseSet.lower(4));
        assertEquals(Integer.valueOf(6), reverseSet.lower(5));
        assertEquals(Integer.valueOf(9), reverseSet.lower(6));
        assertEquals(Integer.valueOf(9), reverseSet.lower(7));
        assertEquals(Integer.valueOf(9), reverseSet.lower(8));
        assertNull(reverseSet.lower(9));
        assertNull(reverseSet.lower(10));
    }

    // descending set ceiling tests

    @Test
    void descendingSetCeilingReturnsCorrectValue() {
        assertNull(reverseSet.ceiling(0));
        assertEquals(Integer.valueOf(1), reverseSet.ceiling(1));
        assertEquals(Integer.valueOf(2), reverseSet.ceiling(2));
        assertEquals(Integer.valueOf(2), reverseSet.ceiling(3));
        assertEquals(Integer.valueOf(4), reverseSet.ceiling(4));
        assertEquals(Integer.valueOf(4), reverseSet.ceiling(5));
        assertEquals(Integer.valueOf(6), reverseSet.ceiling(6));
        assertEquals(Integer.valueOf(6), reverseSet.ceiling(7));
        assertEquals(Integer.valueOf(6), reverseSet.ceiling(8));
        assertEquals(Integer.valueOf(9), reverseSet.ceiling(9));
        assertEquals(Integer.valueOf(9), reverseSet.ceiling(10));
    }

    // descending set floor tests

    @Test
    void descendingSetFloorReturnsCorrectValue() {
        assertEquals(Integer.valueOf(1), reverseSet.floor(0));
        assertEquals(Integer.valueOf(1), reverseSet.floor(1));
        assertEquals(Integer.valueOf(2), reverseSet.floor(2));
        assertEquals(Integer.valueOf(4), reverseSet.floor(3));
        assertEquals(Integer.valueOf(4), reverseSet.floor(4));
        assertEquals(Integer.valueOf(6), reverseSet.floor(5));
        assertEquals(Integer.valueOf(6), reverseSet.floor(6));
        assertEquals(Integer.valueOf(9), reverseSet.floor(7));
        assertEquals(Integer.valueOf(9), reverseSet.floor(8));
        assertEquals(Integer.valueOf(9), reverseSet.floor(9));
        assertNull(reverseSet.floor(10));
    }

    // descending set descending set tests

    @Test
    void descendingSetDescendingSetReturnsOriginalSet() {
        assertArrayEquals(filledSet.toArray(), reverseSet.descendingSet().toArray());
    }
}
