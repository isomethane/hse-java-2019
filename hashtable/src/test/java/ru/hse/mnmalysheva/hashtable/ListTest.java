package ru.hse.mnmalysheva.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {
    private List<String> testList;

    @BeforeEach
    void init() {
        testList = new List<>();
    }

    // add tests

    @Test
    void addRightOrder() {
        testList.add("3");
        testList.add("2");
        testList.add("1");
        var it = testList.iterator();
        assertTrue(it.hasNext());
        assertEquals("1", it.next());
        assertTrue(it.hasNext());
        assertEquals("2", it.next());
        assertTrue(it.hasNext());
        assertEquals("3", it.next());
        assertFalse(it.hasNext());
    }

    // clear tests

    @Test
    void listIsEmptyAfterClear() {
        testList.add("a");
        testList.add("b");
        testList.add("c");
        testList.clear();
        var it = testList.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void canAddElementsAfterClear() {
        testList.add("a");
        testList.add("b");
        testList.add("c");
        testList.clear();

        testList.add("3");
        testList.add("2");
        testList.add("1");
        var it = testList.iterator();
        assertTrue(it.hasNext());
        assertEquals("1", it.next());
        assertTrue(it.hasNext());
        assertEquals("2", it.next());
        assertTrue(it.hasNext());
        assertEquals("3", it.next());
        assertFalse(it.hasNext());
    }

    // iterator tests

    @Test
    void hasNextInEmptyList() {
        var it = testList.iterator();
        assertFalse(it.hasNext());

        testList.add("a");
        testList.add("b");
        testList.add("c");
        testList.clear();
        it = testList.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void hasNextInListWithOneElement() {
        testList.add("a");
        var it = testList.iterator();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void hasNextInListWithManyElements() {
        testList.add("a");
        testList.add("b");
        testList.add("c");
        var it = testList.iterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    void removeDoesNotChangeHasNext() {
        testList.add("a");
        testList.add("b");
        testList.add("c");
        var it = testList.iterator();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.remove();
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
        it.remove();
        assertFalse(it.hasNext());
    }

    // next tests

    @Test
    void nextInEmptyListThrowsNoSuchElementException() {
        var it = testList.iterator();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void nextAfterEndOfListThrowsNoSuchElementException() {
        testList.add("a");
        testList.add("b");
        testList.add("c");
        var it = testList.iterator();
        it.next();
        it.next();
        it.next();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void nextReturnsCorrectValue() {
        testList.add("3");
        testList.add("2");
        testList.add("1");

        var it = testList.iterator();
        assertEquals("1", it.next());
        assertEquals("2", it.next());
        assertEquals("3", it.next());

        testList.add("0");
        testList.add("-1");
        testList.add("-2");
        testList.add("-3");

        it = testList.iterator();
        assertEquals("-3", it.next());
        assertEquals("-2", it.next());
        assertEquals("-1", it.next());
        assertEquals("0", it.next());
        assertEquals("1", it.next());
        assertEquals("2", it.next());
        assertEquals("3", it.next());
    }

    // remove tests

    @Test
    void removeBeforeNextThrowsIllegalStateException() {
        testList.add("a");
        testList.add("b");
        testList.add("c");
        var it = testList.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void removeAfterRemoveThrowsIllegalStateException() {
        testList.add("a");
        testList.add("b");
        testList.add("c");
        var it = testList.iterator();
        it.next();
        it.next();
        it.remove();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void removeWorksCorrect() {
        testList.add("A");
        testList.add("2");
        testList.add("B");
        testList.add("1");
        testList.add("C");
        var it = testList.iterator();
        assertEquals("C", it.next());
        it.remove();
        assertEquals("1", it.next());
        assertEquals("B", it.next());
        it.remove();
        assertEquals("2", it.next());
        assertEquals("A", it.next());
        it.remove();
        assertFalse(it.hasNext());

        it = testList.iterator();
        assertEquals("1", it.next());
        assertEquals("2", it.next());
    }
}