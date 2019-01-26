package ru.hse.mnmalysheva.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {
    List testList;

    @BeforeEach
    void init() {
        testList = new List();
    }

    // add tests

    @Test
    void addNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> new List().add(null));
    }

    @Test
    void containsAddedElements() {
        testList.add(1);
        assertEquals(1, testList.find(1));
        testList.add("1234567");
        assertEquals(1, testList.find(1));
        assertEquals("1234567", testList.find("1234567"));
    }

    @Test
    void canRemoveAddedElements() {
        testList.add(".");
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        testList.add(".");
        assertEquals("4", testList.remove("4"));
        assertEquals(".", testList.remove("."));
        assertEquals(1, testList.remove(1));
        assertEquals(3, testList.remove(3));
        assertEquals(".", testList.remove("."));
        assertEquals("2", testList.remove("2"));
    }

    @Test
    void addedElementsRemoveInRightOrder() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        assertEquals("4", testList.removeFirst());
        assertEquals(3, testList.removeFirst());
        assertEquals("2", testList.removeFirst());
        assertEquals(1, testList.removeFirst());
    }

    @Test
    void notEmptyAfterAdd() {
        testList.add(1);
        assertFalse(testList.isEmpty());
    }

    // remove tests

    @Test
    void removeReturnsRightObject() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        assertEquals(null, testList.remove(0));
        assertEquals("4", testList.remove("4"));
        assertEquals(1, testList.remove(1));
        assertEquals(3, testList.remove(3));
        assertEquals("2", testList.remove("2"));
    }

    @Test
    void removedObjectsCannotBeFound() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        assertEquals(3, testList.remove(3));
        assertEquals("2", testList.remove("2"));
        assertEquals(null, testList.find("2"));
        assertEquals(null, testList.find(3));
    }

    @Test
    void notRemovedObjectsCanBeFound() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");

        assertEquals(3, testList.remove(3));
        assertEquals("2", testList.remove("2"));

        assertEquals(1, testList.find(1));
        assertEquals("4", testList.find("4"));
    }

    // removeFirst test

    @Test
    void removeFirst() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");

        assertEquals("4", testList.removeFirst());
        assertEquals(3, testList.removeFirst());
        assertEquals("2", testList.removeFirst());
        assertEquals(1, testList.removeFirst());
        assertNull(testList.removeFirst());
    }

    // find tests

    @Test
    void nullIsNotFound() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        assertNull(testList.find(null));
    }

    @Test
    void findDoesNotBreakList() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");

        testList.find(1);
        testList.find(3);
        testList.find("2");
        testList.find("4");
        testList.find(4);
        testList.find(1);
        testList.find("4");
        testList.find("0");

        assertEquals("4", testList.removeFirst());
        assertEquals(3, testList.removeFirst());
        assertEquals("2", testList.removeFirst());
        assertEquals(1, testList.removeFirst());
        assertNull(testList.removeFirst());
    }

    @Test
    void findWorksRightWithExistingElements() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        testList.add(true);

        assertEquals(3, testList.find(3));
        assertEquals("2", testList.find("2"));
        assertEquals("4", testList.find("4"));
        assertEquals(1, testList.find(1));
        assertEquals(true, testList.find(true));
    }

    @Test
    void findWorksRightAfterRemove() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        testList.add(true);

        assertEquals(3, testList.remove(3));
        assertEquals("2", testList.remove("2"));

        assertNull(testList.find(3));
        assertNull(testList.find("2"));
        assertEquals("4", testList.find("4"));
        assertEquals(1, testList.find(1));
        assertEquals(true, testList.find(true));
    }

    @Test
    void justCreatedListIsEmpty() {
        assertTrue(testList.isEmpty());
    }

    @Test
    void listWithElementsIsNotEmpty() {
        testList.add(1);
        assertFalse(testList.isEmpty());
        testList.add("2");
        assertFalse(testList.isEmpty());
        testList.add(3);
        assertFalse(testList.isEmpty());
        testList.add("4");
        assertFalse(testList.isEmpty());
        testList.removeFirst();
        assertFalse(testList.isEmpty());
    }

    @Test
    void listIsEmptyAfterRemovingAllElements() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        testList.removeFirst();
        testList.removeFirst();
        testList.remove(1);
        assertFalse(testList.isEmpty());
        testList.remove("2");
        assertTrue(testList.isEmpty());
    }

    @Test
    void listIsEmptyAfterClear() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");
        testList.clear();
        assertTrue(testList.isEmpty());
    }

    // clear tests

    @Test
    void nothingCanBeFoundAfterClear() {
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");

        testList.clear();

        assertNull(testList.find(3));
        assertNull(testList.find("2"));
        assertNull(testList.find("4"));
        assertNull(testList.find(1));
        assertNull(testList.find(true));
    }

    @Test
    void canClearEmptyList() {
        assertDoesNotThrow(() -> new List().clear());
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");

        testList.clear();
        assertDoesNotThrow(() -> testList.clear());
    }

    @Test
    void clearDoesNotBreakList() {
        assertDoesNotThrow(() -> new List().clear());
        testList.add(1);
        testList.add("2");
        testList.add(3);
        testList.add("4");

        testList.clear();
        assertDoesNotThrow(() -> testList.add("123"));
        assertEquals("123", testList.find("123"));
    }
}