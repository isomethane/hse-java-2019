package ru.hse.mnmalysheva.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {
    private Trie testTrie;

    @BeforeEach
    void init() {
        testTrie = new Trie();
    }

    // add tests

    @Test
    void addNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testTrie.add(null));
    }

    @Test
    void addNewStringReturnsTrue() {
        assertTrue(testTrie.add("Test1String"));
        assertTrue(testTrie.add("Test2String"));
        assertTrue(testTrie.add("Test3String"));
        assertTrue(testTrie.add("Test"));
        assertTrue(testTrie.add("test"));
        assertTrue(testTrie.add("String"));
        assertTrue(testTrie.add("РусскийТекст"));
        assertTrue(testTrie.add("РусскийТекстПодлиннее"));
    }

    @Test
    void addExistingStringReturnsFalse() {
        testTrie.add("Test1String");
        testTrie.add("Test2String");
        testTrie.add("Test3String");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        assertFalse(testTrie.add("Test1String"));
        assertFalse(testTrie.add("Test2String"));
        assertFalse(testTrie.add("Test3String"));
        assertFalse(testTrie.add("Test"));
        assertFalse(testTrie.add("test"));
        assertFalse(testTrie.add("String"));
        assertFalse(testTrie.add("РусскийТекст"));
        assertFalse(testTrie.add("РусскийТекстПодлиннее"));
    }

    // contains tests

    @Test
    void containsNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testTrie.contains(null));
    }

    @Test
    void doesNotContainNotAdded() {
        testTrie.add("TestString");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        assertFalse(testTrie.contains(""));
        assertFalse(testTrie.contains("TestString..."));
        assertFalse(testTrie.contains("Tes"));
        assertFalse(testTrie.contains("t"));
        assertFalse(testTrie.contains("StrinG"));
        assertFalse(testTrie.contains("РусскийТ"));
        assertFalse(testTrie.contains("РусскийТекстПокороче"));
    }

    @Test
    void containsAdded() {
        testTrie.add("TestString");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        assertTrue(testTrie.contains("TestString"));
        assertTrue(testTrie.contains("Test"));
        assertTrue(testTrie.contains("test"));
        assertTrue(testTrie.contains("String"));
        assertTrue(testTrie.contains("РусскийТекст"));
        assertTrue(testTrie.contains("РусскийТекстПодлиннее"));
    }

    @Test
    void containsWorksRightAfterRemove() {
        testTrie.add("Русский");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        testTrie.remove("Русский");
        testTrie.remove("РусскийТекстПодлиннее");

        assertFalse(testTrie.contains("Русский"));
        assertTrue(testTrie.contains("РусскийТекст"));
        assertFalse(testTrie.contains("РусскийТекстПодлиннее"));
    }

    @Test
    void containsCheckDifferentPathVariants() {
        testTrie.add("Русский");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");
        assertFalse(testTrie.contains(""));
        assertTrue(testTrie.contains("Русский"));
        assertFalse(testTrie.contains("РусскийТ"));
        assertTrue(testTrie.contains("РусскийТекст"));
        assertFalse(testTrie.contains("РусскийТЕКСТ"));
        assertTrue(testTrie.contains("РусскийТекстПодлиннее"));
    }

    // remove tests

    @Test
    void removeNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> testTrie.remove(null));
    }

    @Test
    void removeNewStringReturnsFalse() {
        testTrie.add("TestString");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        assertFalse(testTrie.remove(""));
        assertFalse(testTrie.remove("TestString..."));
        assertFalse(testTrie.remove("Tes"));
        assertFalse(testTrie.remove("t"));
        assertFalse(testTrie.remove("StrinG"));
        assertFalse(testTrie.remove("РусскийТ"));
        assertFalse(testTrie.remove("РусскийТекстПокороче"));
    }

    @Test
    void removeExistingStringReturnsTrue() {
        testTrie.add("Test1String");
        testTrie.add("Test2String");
        testTrie.add("Test3String");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        assertTrue(testTrie.remove("Test1String"));
        assertTrue(testTrie.remove("Test2String"));
        assertTrue(testTrie.remove("Test3String"));
        assertTrue(testTrie.remove("Test"));
        assertTrue(testTrie.remove("test"));
        assertTrue(testTrie.remove("String"));
        assertTrue(testTrie.remove("РусскийТекст"));
        assertTrue(testTrie.remove("РусскийТекстПодлиннее"));
    }

    @Test
    void removeDoesNotAffectOtherStrings() {
        testTrie.add("Test1String");
        testTrie.add("Test2String");
        testTrie.add("Test3String");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        testTrie.remove("Test1String");
        testTrie.remove("Test2String");
        testTrie.remove("Test");
        testTrie.remove("Русский");
        testTrie.remove("РусскийТекстПодлиннее");

        assertTrue(testTrie.contains("Test3String"));
        assertTrue(testTrie.contains("test"));
        assertTrue(testTrie.contains("String"));
        assertTrue(testTrie.contains("РусскийТекст"));
    }

    @Test
    void doesNotContainRemovedStrings() {
        testTrie.add("Test1String");
        testTrie.add("Test2String");
        testTrie.add("Test3String");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        testTrie.remove("Test1String");
        testTrie.remove("Test2String");
        testTrie.remove("Test");
        testTrie.remove("Русский");
        testTrie.remove("РусскийТекстПодлиннее");

        assertFalse(testTrie.contains("Test1String"));
        assertFalse(testTrie.contains("Test2String"));
        assertFalse(testTrie.contains("Test"));
        assertFalse(testTrie.contains("Русский"));
        assertFalse(testTrie.contains("РусскийТекстПодлиннее"));
    }

    // size tests

    @Test
    void emptyTrieSize() {
        assertEquals(0, testTrie.size());
    }

    @Test
    void sizeChangesCorrectlyAfterAdd() {
        testTrie.add("Test1String");
        assertEquals(1, testTrie.size());
        testTrie.add("Test2String");
        assertEquals(2, testTrie.size());
        testTrie.add("Test3String");
        assertEquals(3, testTrie.size());
        testTrie.add("Test");
        assertEquals(4, testTrie.size());
        testTrie.add("test");
        assertEquals(5, testTrie.size());
        testTrie.add("String");
        assertEquals(6, testTrie.size());
        testTrie.add("Test2String");
        assertEquals(6, testTrie.size());
        testTrie.add("Test");
        assertEquals(6, testTrie.size());
        testTrie.add("РусскийТекст");
        assertEquals(7, testTrie.size());
        testTrie.add("String");
        assertEquals(7, testTrie.size());
        testTrie.add("РусскийТекстПодлиннее");
        assertEquals(8, testTrie.size());
    }

    @Test
    void sizeChangesCorrectlyAfterRemove() {
        testTrie.add("Test1String");
        testTrie.add("Test2String");
        testTrie.add("Test3String");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        testTrie.remove("Test1String");
        assertEquals(7, testTrie.size());
        testTrie.remove("Test2String");
        assertEquals(6, testTrie.size());
        testTrie.remove("Test3String...");
        assertEquals(6, testTrie.size());
        testTrie.remove("Test");
        assertEquals(5, testTrie.size());
        testTrie.remove("Русский");
        assertEquals(5, testTrie.size());
        testTrie.remove("РусскийТекстПодлиннее");
        assertEquals(4, testTrie.size());
    }

    // howManyStartWithPrefix tests

    @Test
    void sizeEqualsHowManyStartWithEmptyPrefix() {
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("Test1String");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("Test2String");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("Test3String");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("Test");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("test");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("String");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("Test2String");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("Test");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("РусскийТекст");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("String");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.add("РусскийТекстПодлиннее");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));

        testTrie.remove("Test1String");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.remove("Test2String");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.remove("Test3String...");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.remove("Test");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.remove("Русский");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
        testTrie.remove("РусскийТекстПодлиннее");
        assertEquals(testTrie.size(), testTrie.howManyStartWithPrefix(""));
    }

    @Test
    void howManyStartWithNotExistingPrefix() {
        testTrie.add("Test1String");
        testTrie.add("Test2String");
        testTrie.add("Test3String");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        assertEquals(0, testTrie.howManyStartWithPrefix("А"));
        assertEquals(0, testTrie.howManyStartWithPrefix("Test0"));
        assertEquals(0, testTrie.howManyStartWithPrefix("StrinG"));
        assertEquals(0, testTrie.howManyStartWithPrefix("String2"));
        assertEquals(0, testTrie.howManyStartWithPrefix("РусскийТекст1"));
    }

    @Test
    void howManyStartWithExistingPrefix() {
        testTrie.add("Test1String");
        testTrie.add("Test2String");
        testTrie.add("Test3String");
        testTrie.add("Test");
        testTrie.add("test");
        testTrie.add("String");
        testTrie.add("Русский");
        testTrie.add("РусскийТекст");
        testTrie.add("РусскийТекстПодлиннее");

        assertEquals(4, testTrie.howManyStartWithPrefix("Test"));
        assertEquals(1, testTrie.howManyStartWithPrefix("Test1"));
        assertEquals(1, testTrie.howManyStartWithPrefix("S"));
        assertEquals(3, testTrie.howManyStartWithPrefix("Рус"));
        assertEquals(3, testTrie.howManyStartWithPrefix("Русский"));
        assertEquals(2, testTrie.howManyStartWithPrefix("РусскийТ"));
        assertEquals(2, testTrie.howManyStartWithPrefix("РусскийТекст"));
        assertEquals(1, testTrie.howManyStartWithPrefix("РусскийТекстПодлиннее"));
    }

    @Test
    void resultOfSerializeDeserializeEqualsSource() {
        Trie from = new Trie(), to = new Trie();
        from.add("Test1String");
        from.add("Test2String");
        from.add("Test3String");
        from.add("Test");
        from.add("test");
        from.add("String");
        from.add("Русский");
        from.add("РусскийТекст");
        from.add("РусскийТекстПодлиннее");

        to.add("ABC");
        to.add("def");
        to.add("123");

        var out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> from.serialize(out));
        var in = new ByteArrayInputStream(out.toByteArray());
        assertDoesNotThrow(() -> to.deserialize(in));

        assertTrue(to.contains("Test1String"));
        assertTrue(to.contains("Test2String"));
        assertTrue(to.contains("Test3String"));
        assertTrue(to.contains("Test"));
        assertTrue(to.contains("test"));
        assertTrue(to.contains("String"));
        assertTrue(to.contains("Русский"));
        assertTrue(to.contains("РусскийТекст"));
        assertTrue(to.contains("РусскийТекстПодлиннее"));

        assertFalse(to.contains(""));
        assertFalse(to.contains("T"));
        assertFalse(to.contains("Test1"));
        assertFalse(to.contains("StrinG"));
        assertFalse(to.contains("String2"));

        assertFalse(to.contains("ABC"));
        assertFalse(to.contains("def"));
        assertFalse(to.contains("123"));
    }

    @Test
    void serializeDeserializeEmptyTrie() {
        Trie from = new Trie(), to = new Trie();
        to.add("ABC");
        to.add("def");
        to.add("123");

        var out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> from.serialize(out));
        var in = new ByteArrayInputStream(out.toByteArray());
        assertDoesNotThrow(() -> to.deserialize(in));

        assertFalse(to.contains(""));
        assertFalse(to.contains("T"));
        assertFalse(to.contains("ABC"));
        assertFalse(to.contains("def"));
        assertFalse(to.contains("123"));
    }

    @Test
    void canChangeTrieAfterDeserialize() {
        Trie from = new Trie(), to = new Trie();
        from.add("Test1String");
        from.add("Test2String");
        from.add("Test3String");
        from.add("Test");
        from.add("test");
        from.add("String");
        from.add("Русский");
        from.add("РусскийТекст");
        from.add("РусскийТекстПодлиннее");

        to.add("ABC");
        to.add("def");
        to.add("123");

        var out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> from.serialize(out));
        var in = new ByteArrayInputStream(out.toByteArray());
        assertDoesNotThrow(() -> to.deserialize(in));

        to.add("NewString");
        to.add("НоваяСтрока");
        to.remove("t");
        to.remove("Test1String");
        to.remove("Test");
        to.remove("РусскийТекст");

        assertTrue(to.contains("NewString"));
        assertTrue(to.contains("НоваяСтрока"));
        assertFalse(to.contains("Test1String"));
        assertTrue(to.contains("Test2String"));
        assertTrue(to.contains("Test3String"));
        assertFalse(to.contains("Test"));
        assertTrue(to.contains("test"));
        assertTrue(to.contains("String"));
        assertTrue(to.contains("Русский"));
        assertFalse(to.contains("РусскийТекст"));
        assertTrue(to.contains("РусскийТекстПодлиннее"));

        assertFalse(to.contains(""));
        assertFalse(to.contains("T"));
        assertFalse(to.contains("Test2"));
        assertFalse(to.contains("StrinG"));
        assertFalse(to.contains("String2"));

        assertFalse(to.contains("ABC"));
        assertFalse(to.contains("def"));
        assertFalse(to.contains("123"));
    }

    @Test
    void serializeAllCharacters() {
        Trie from = new Trie(), to = new Trie();
        for (int c = 0; c <= Character.MAX_VALUE; c++) {
            from.add(Character.toString(c));
        }

        var out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> from.serialize(out));
        var in = new ByteArrayInputStream(out.toByteArray());
        assertDoesNotThrow(() -> to.deserialize(in));

        for (char c = 0; c < Character.MAX_VALUE; c++) {
            assertTrue(to.contains(Character.toString(c)));
        }
    }
}
