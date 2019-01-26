package ru.hse.mnmalysheva.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {
    private HashTable testTable;

    @BeforeEach
    void init() {
        testTable = new HashTable();
    }

    // size tests

    @Test
    void emptyTableSize() {
        assertEquals(0, testTable.size());
        testTable.put("1", "a");
        testTable.remove("1");
        assertEquals(0, testTable.size());
        testTable.put("2", "b");
        testTable.clear();
        assertEquals(0, testTable.size());
    }

    @Test
    void sizeIncreasesAfterAddingNewKey() {
        testTable.put("1", "a");
        assertEquals(1, testTable.size());
        testTable.put("2", "b");
        assertEquals(2, testTable.size());
        testTable.put("3", "c");
        assertEquals(3, testTable.size());
        testTable.put("4", "d");
        assertEquals(4, testTable.size());
    }

    @Test
    void sizeDoesNotIncreaseAfterAddingExistingKey() {
        testTable.put("1", "a");
        assertEquals(1, testTable.size());
        testTable.put("1", "b");
        assertEquals(1, testTable.size());
    }

    @Test
    void sizeIncreasesAfterAddingKeyWithSameHashCode() {
        testTable.put("AaAa", "1");
        assertEquals(1, testTable.size());
        testTable.put("BBBB", "2");
        assertEquals(2, testTable.size());
        testTable.put("AaBB", "3");
        assertEquals(3, testTable.size());
        testTable.put("BBAa", "4");
        assertEquals(4, testTable.size());
    }

    @Test
    void sizeDecreasesAfterRemovingKey() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        assertEquals(4, testTable.size());
        testTable.remove("2");
        assertEquals(3, testTable.size());
        testTable.remove("3");
        assertEquals(2, testTable.size());
        testTable.remove("1");
        assertEquals(1, testTable.size());
        testTable.remove("4");
        assertEquals(0, testTable.size());
    }

    @Test
    void sizeDoesNotDecreasesWhenNothingRemoved() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        assertEquals(4, testTable.size());
        testTable.remove("5");
        assertEquals(4, testTable.size());
    }

    // contains tests

    @Test
    void containsAdded() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        assertTrue(testTable.contains("2"));
        assertTrue(testTable.contains("3"));
        assertTrue(testTable.contains("1"));
        assertTrue(testTable.contains("4"));
    }

    @Test
    void doesNotContainRemoved() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        testTable.remove("3");
        testTable.remove("2");
        assertFalse(testTable.contains("2"));
        assertFalse(testTable.contains("3"));
        assertTrue(testTable.contains("1"));
        assertTrue(testTable.contains("4"));
    }

    @Test
    void doesNotContainAnythingAfterClear() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        testTable.clear();
        assertFalse(testTable.contains("2"));
        assertFalse(testTable.contains("3"));
        assertFalse(testTable.contains("1"));
        assertFalse(testTable.contains("4"));
    }

    @Test
    void containsKeyIfKeyWithSameHashCodeWasRemoved() {
        testTable.put("AaAa", "1");
        testTable.put("BBBB", "2");
        testTable.put("AaBB", "3");
        testTable.put("BBAa", "4");
        testTable.remove("BBBB");
        testTable.remove("BBAa");
        assertTrue(testTable.contains("AaAa"));
        assertFalse(testTable.contains("BBBB"));
        assertTrue(testTable.contains("AaBB"));
        assertFalse(testTable.contains("BBAa"));
    }

    // get tests

    @Test
    void getNullThrows() {
        testTable.put("1", "a");
        assertThrows(Throwable.class, () -> testTable.get(null));
    }

    @Test
    void getReturnsLastAdded() {
        testTable.put("1", "a4");
        testTable.put("2", "b4");
        testTable.put("3", "c4");
        testTable.put("4", "d4");
        testTable.put("1", "a3");
        testTable.put("2", "b3");
        testTable.put("3", "c3");
        testTable.put("1", "a2");
        testTable.put("2", "b2");
        testTable.put("1", "a1");
        assertEquals("a1", testTable.get("1"));
        assertEquals("b2", testTable.get("2"));
        assertEquals("c3", testTable.get("3"));
        assertEquals("d4", testTable.get("4"));
    }

    @Test
    void getReturnsNullIfKeyWasNotAdded() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        assertNull(testTable.get("5"));
    }

    @Test
    void getReturnsNullIfKeyWasRemoved() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        testTable.remove("3");
        testTable.remove("2");
        assertEquals("a", testTable.get("1"));
        assertNull(testTable.get("2"));
        assertNull(testTable.get("3"));
        assertEquals("d", testTable.get("4"));
    }

    @Test
    void getReturnsRightValueForKeysWithSameHashCode() {
        testTable.put("AaAa", "1");
        testTable.put("BBBB", "2");
        testTable.put("AaBB", "3");
        testTable.put("BBAa", "4");
        assertEquals("2", testTable.get("BBBB"));
        assertEquals("3", testTable.get("AaBB"));
        assertEquals("1", testTable.get("AaAa"));
        assertEquals("4", testTable.get("BBAa"));
    }

    // put tests

    @Test
    void putNullThrows() {
        testTable.put("1", "a");
        assertThrows(Throwable.class, () -> testTable.put(null, "123"));
        init();
        testTable.put("1", "a");
        assertThrows(Throwable.class, () -> testTable.put("123", null));
    }

    @Test
    void putReturnsRightValue() {
        assertNull(testTable.put("1", "a4"));
        assertNull(testTable.put("2", "b4"));
        assertNull(testTable.put("3", "c4"));
        assertNull(testTable.put("4", "d4"));
        assertEquals("a4", testTable.put("1", "a3"));
        assertEquals("b4", testTable.put("2", "b3"));
        assertEquals("c4", testTable.put("3", "c3"));
        assertEquals("a3", testTable.put("1", "a2"));
        assertEquals("b3", testTable.put("2", "b2"));
        assertEquals("a2", testTable.put("1", "a1"));
    }

    @Test
    void putAfterReturnReturnsNull() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        testTable.remove("3");
        testTable.remove("2");
        assertNull(testTable.put("2", "B"));
        assertNull(testTable.put("3", "C"));
    }

    @Test
    void putReturnsRightValueForKeysWithSameHashCode() {
        testTable.put("AaAa", "1");
        testTable.put("BBBB", "2");
        testTable.put("AaBB", "3");
        testTable.put("BBAa", "4");
        assertEquals("2", testTable.put("BBBB", "j"));
        assertEquals("3", testTable.put("AaBB", "a"));
        assertEquals("1", testTable.put("AaAa", "v"));
        assertEquals("4", testTable.put("BBAa", "a"));
    }

    @Test
    void addKeyWithNegativeHashCodeDoesNotThrow() {
        assert("StringWithNegativeHashCode239".hashCode() < 0);
        assertDoesNotThrow(() -> testTable.put("StringWithNegativeHashCode239", "Is it ok?"));
    }

    // remove tests

    @Test
    void removeNullThrows() {
        testTable.put("1", "a");
        assertThrows(Throwable.class, () -> testTable.remove(null));
    }

    @Test
    void removeReturnsLastAdded() {
        testTable.put("1", "a4");
        testTable.put("2", "b4");
        testTable.put("3", "c4");
        testTable.put("4", "d4");
        testTable.put("1", "a3");
        testTable.put("2", "b3");
        testTable.put("3", "c3");
        testTable.put("1", "a2");
        testTable.put("2", "b2");
        testTable.put("1", "a1");
        assertEquals("a1", testTable.remove("1"));
        assertEquals("b2", testTable.remove("2"));
        assertEquals("c3", testTable.remove("3"));
        assertEquals("d4", testTable.remove("4"));
    }

    @Test
    void removeReturnsNullIfKeyWasNotAdded() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        assertNull(testTable.remove("5"));
    }

    @Test
    void removeReturnsNullIfKeyWasRemoved() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        testTable.remove("3");
        testTable.remove("2");
        assertNull(testTable.remove("2"));
        assertNull(testTable.remove("3"));
    }

    @Test
    void removeDoesNotAffectOtherElements() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        testTable.remove("3");
        testTable.remove("2");
        assertEquals("a", testTable.get("1"));
        assertEquals("d", testTable.get("4"));
    }

    @Test
    void removeReturnsRightValueForKeysWithSameHashCode() {
        testTable.put("AaAa", "1");
        testTable.put("BBBB", "2");
        testTable.put("AaBB", "3");
        testTable.put("BBAa", "4");
        assertEquals("2", testTable.remove("BBBB"));
        assertEquals("3", testTable.remove("AaBB"));
        assertEquals("1", testTable.remove("AaAa"));
        assertEquals("4", testTable.remove("BBAa"));
    }

    // remove tests

    @Test
    void clearRemovesAllKeys() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        testTable.clear();
        assertNull(testTable.get("2"));
        assertNull(testTable.get("3"));
        assertNull(testTable.get("1"));
        assertNull(testTable.get("4"));
    }

    @Test
    void clearDoesNotBreakTable() {
        testTable.put("1", "a");
        testTable.put("2", "b");
        testTable.put("3", "c");
        testTable.put("4", "d");
        testTable.clear();
        assertDoesNotThrow(() -> testTable.put("1", "a"));
        assertEquals("a", testTable.remove("1"));
    }
}