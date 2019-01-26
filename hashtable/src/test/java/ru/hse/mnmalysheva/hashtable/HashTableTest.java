package ru.hse.mnmalysheva.hashtable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void size() {
        var t = new HashTable();
        assertEquals(0, t.size());
        t.put("1", "a");
        assertEquals(1, t.size());
        t.put("1", "b");
        assertEquals(1, t.size());
        t.put("2", "c");
        assertEquals(2, t.size());
        t.put("3", "d");
        assertEquals(3, t.size());
        t.put("4", "e");
        assertEquals(4, t.size());
        t.remove("0");
        assertEquals(4, t.size());
        t.remove("1");
        assertEquals(3, t.size());
        t.clear();
        assertEquals(0, t.size());
        t.put("1", "a");
        assertEquals(1, t.size());
    }

    @Test
    void contains() {
        var t = new HashTable();
        for (int i = 1; i <= 7; i++) {
            t.put(Integer.toString(i), "...");
        }
        for (int i = 5; i <= 15; i++) {
            t.put(Integer.toString(i), "!!!");
        }
        for (int i = 1; i <= 15; i++) {
            assertTrue(t.contains(Integer.toString(i)));
        }
        for (int i = 1; i <= 10; i++) {
            t.remove(Integer.toString(i));
        }
        for (int i = 1; i <= 10; i++) {
            assertFalse(t.contains(Integer.toString(i)));
        }
        for (int i = 11; i <= 15; i++) {
            assertTrue(t.contains(Integer.toString(i)));
        }
    }

    @Test
    void get() {
        var t = new HashTable();
        for (int i = 1; i <= 15; i++) {
            t.put(Integer.toString(i), "...");
        }
        for (int i = 5; i <= 10; i++) {
            t.put(Integer.toString(i), "value" + i);
        }
        assertNull(t.get("0"));
        for (int i = 1; i < 5; i++) {
            assertEquals("...", t.get(Integer.toString(i)));
        }
        for (int i = 5; i <= 10; i++) {
            assertEquals("value" + i, t.get(Integer.toString(i)));
        }
        for (int i = 11; i <= 15; i++) {
            assertEquals("...", t.get(Integer.toString(i)));
        }
    }

    @Test
    void put() {
        var t = new HashTable();
        for (int i = 1; i <= 15; i++) {
            assertNull(t.put(Integer.toString(i), "value" + i));
        }
        for (int i = 5; i <= 10; i++) {
            assertEquals("value" + i, t.put(Integer.toString(i), "..."));
        }
    }

    @Test
    void remove() {
        var t = new HashTable();
        for (int i = 1; i <= 15; i++) {
            t.put(Integer.toString(i), "value" + i);
        }
        assertNull(t.remove("0"));
        for (int i = 5; i <= 10; i++) {
            assertEquals("value" + i, t.remove(Integer.toString(i)));
        }
        assertEquals(9, t.size());
        for (int i = 1; i < 5; i++) {
            assertTrue(t.contains(Integer.toString(i)));
        }
        for (int i = 5; i <= 10; i++) {
            assertFalse(t.contains(Integer.toString(i)));
        }
        for (int i = 11; i <= 15; i++) {
            assertTrue(t.contains(Integer.toString(i)));
        }
    }

    @Test
    void clear() {
        var t = new HashTable();
        for (int i = 1; i <= 15; i++) {
            t.put(Integer.toString(i), "value" + i);
        }
        t.clear();
        assertEquals(0, t.size());
        for (int i = 1; i <= 15; i++) {
            assertFalse(t.contains(Integer.toString(i)));
        }
        t.put("a", "b");
        assertEquals(1, t.size());
    }
}