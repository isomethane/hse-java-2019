package ru.hse.mnmalysheva.hashtable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void size() {
        HashTable t = new HashTable(3);
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
    void resize() {
        HashTable t = new HashTable(10);
        for (Integer i = 1; i <= 7; i++) {
            t.put(i.toString(), "...");
        }
        t.resize(3);
        assertEquals(7, t.size());
        for (Integer i = 1; i <= 7; i++) {
            assertTrue(t.contains(i.toString()));
        }
        t.resize(100);
        assertEquals(7, t.size());
        for (Integer i = 1; i <= 7; i++) {
            assertTrue(t.contains(i.toString()));
        }
        t.resize(1);
        assertEquals(7, t.size());
        for (Integer i = 1; i <= 7; i++) {
            assertTrue(t.contains(i.toString()));
        }
    }

    @Test
    void contains() {
        HashTable t = new HashTable();
        for (Integer i = 1; i <= 7; i++) {
            t.put(i.toString(), "...");
        }
        for (Integer i = 5; i <= 15; i++) {
            t.put(i.toString(), "!!!");
        }
        for (Integer i = 1; i <= 15; i++) {
            assertTrue(t.contains(i.toString()));
        }
        for (Integer i = 1; i <= 10; i++) {
            t.remove(i.toString());
        }
        for (Integer i = 1; i <= 10; i++) {
            assertFalse(t.contains(i.toString()));
        }
        for (Integer i = 11; i <= 15; i++) {
            assertTrue(t.contains(i.toString()));
        }
    }

    @Test
    void get() {
        HashTable t = new HashTable();
        for (Integer i = 1; i <= 15; i++) {
            t.put(i.toString(), "...");
        }
        for (Integer i = 5; i <= 10; i++) {
            t.put(i.toString(), "value" + i.toString());
        }
        assertNull(t.get("0"));
        for (Integer i = 1; i < 5; i++) {
            assertEquals("...", t.get(i.toString()));
        }
        for (Integer i = 5; i <= 10; i++) {
            assertEquals("value" + i.toString(), t.get(i.toString()));
        }
        for (Integer i = 11; i <= 15; i++) {
            assertEquals("...", t.get(i.toString()));
        }
    }

    @Test
    void put() {
        HashTable t = new HashTable();
        for (Integer i = 1; i <= 15; i++) {
            assertNull(t.put(i.toString(), "value" + i.toString()));
        }
        for (Integer i = 5; i <= 10; i++) {
            assertEquals("value" + i.toString(), t.put(i.toString(), "..."));
        }
    }

    @Test
    void remove() {
        HashTable t = new HashTable();
        for (Integer i = 1; i <= 15; i++) {
            t.put(i.toString(), "value" + i.toString());
        }
        assertNull(t.remove("0"));
        for (Integer i = 5; i <= 10; i++) {
            assertEquals("value" + i.toString(), t.remove(i.toString()));
        }
        assertEquals(9, t.size());
        for (Integer i = 1; i < 5; i++) {
            assertTrue(t.contains(i.toString()));
        }
        for (Integer i = 5; i <= 10; i++) {
            assertFalse(t.contains(i.toString()));
        }
        for (Integer i = 11; i <= 15; i++) {
            assertTrue(t.contains(i.toString()));
        }
    }

    @Test
    void clear() {
        HashTable t = new HashTable();
        for (Integer i = 1; i <= 15; i++) {
            t.put(i.toString(), "value" + i.toString());
        }
        t.clear();
        assertEquals(0, t.size());
        for (Integer i = 1; i <= 15; i++) {
            assertFalse(t.contains(i.toString()));
        }
        t.put("a", "b");
        assertEquals(1, t.size());
    }
}