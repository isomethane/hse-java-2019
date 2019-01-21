package ru.hse.mnmalysheva.hashtable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    void add() {
        List l = new List();
        l.add("1");
        l.add("2");
        l.add("3");

        ForwardIterator it = l.forwardIterator();
        assertTrue(it.hasNext());
        assertEquals("3", it.getNext());
        it.goNext();
        assertTrue(it.hasNext());
        assertEquals("2", it.getNext());
        it.goNext();
        assertTrue(it.hasNext());
        assertEquals("1", it.getNext());
        it.goNext();
        assertFalse(it.hasNext());
    }

    @Test
    void clear() {
        List l = new List();
        l.add("...");
        assertTrue(l.forwardIterator().hasNext());
        l.clear();
        assertFalse(l.forwardIterator().hasNext());
        l.add("...");
        assertTrue(l.forwardIterator().hasNext());
    }

    @Test
    void contains() {
        List l = new List();
        l.add("1");
        l.add("2");
        l.add("3");
        assertTrue(l.contains("1"));
        assertTrue(l.contains("2"));
        assertTrue(l.contains("3"));
        assertFalse(l.contains("4"));
        l.clear();
        assertFalse(l.contains("1"));
        assertFalse(l.contains("2"));
        assertFalse(l.contains("3"));
        assertFalse(l.contains("4"));
        l.add("1");
        l.add("2");
        l.add("3");
        l.remove("2");
        assertTrue(l.contains("1"));
        assertFalse(l.contains("2"));
        assertTrue(l.contains("3"));
        assertFalse(l.contains("4"));
        l.remove("1");
        assertFalse(l.contains("1"));
        assertFalse(l.contains("2"));
        assertTrue(l.contains("3"));
        assertFalse(l.contains("4"));
    }

    @Test
    void find() {
        List l = new List();
        l.add("1");
        l.add(2);
        l.add(true);
        assertEquals("1", l.find("1"));
        assertEquals(2, l.find(2));
        assertEquals(true, l.find(true));
        assertNull(l.find(1));
        assertNull(l.find("2"));
        assertNull(l.find(false));
    }

    @Test
    void remove() {
        List l = new List();
        l.add("1");
        l.add("2");
        l.add("3");
        l.add("4");
        l.add("5");
        l.add("6");

        assertNull(l.remove("-1"));
        assertEquals("5", l.remove("5"));
        assertEquals("1", l.remove("1"));
        assertEquals("3", l.remove("3"));

        ForwardIterator it = l.forwardIterator();
        assertTrue(it.hasNext());
        assertEquals("6", it.getNext());
        it.goNext();
        assertTrue(it.hasNext());
        assertEquals("4", it.getNext());
        it.goNext();
        assertTrue(it.hasNext());
        assertEquals("2", it.getNext());
        it.goNext();
        assertFalse(it.hasNext());
    }
}