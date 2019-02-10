package ru.hse.mnmalysheva.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {
    private Trie testTrie;

    @BeforeEach
    void init() {
        testTrie = new Trie();
    }

    // add tests

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

    // equals tests

    @Test
    void emptyTriesAreEqual() {
        var first = new Trie();
        var second = new Trie();
        assertEquals(first, second);
    }

    @Test
    void emptyTrieIsNotEqualToTrieWithEmptyString() {
        var first = new Trie();
        var second = new Trie();
        first.add("");
        assertNotEquals(first, second);
    }

    @Test
    void equalsSimpleTest() {
        var first = new Trie();
        var second = new Trie();

        first.add("ab");
        second.add("ac");

        assertNotEquals(first, second);
    }

    @Test
    void addOrderDoesNotMatter() {
        var first = new Trie();
        var second = new Trie();

        first.add("Test1String");
        first.add("Test2String");
        first.add("Test3String");
        first.add("Test");
        first.add("test");
        first.add("String");
        first.add("Test3String");
        first.add("Русский");
        first.add("РусскийТекст");
        first.add("РусскийТекст");
        first.add("РусскийТекстПодлиннее");

        second.add("РусскийТекстПодлиннее");
        second.add("Test2String");
        second.add("Test1String");
        second.add("String");
        second.add("Test2String");
        second.add("Test");
        second.add("Русский");
        second.add("РусскийТекст");
        second.add("test");
        second.add("Test3String");

        assertEquals(first, second);
    }

    @Test
    void removeStringEqualsNotAdd() {
        var first = new Trie();
        var second = new Trie();

        first.add("Test1String");
        first.add("Test2String");
        first.add("Test3String");
        first.add("test");
        first.add("String");
        first.add("Test3String");
        first.add("Русский");
        first.add("РусскийТекст");
        first.add("РусскийТекст");

        second.add("РусскийТекстПодлиннее");
        second.add("Test2String");
        second.add("Test1String");
        second.add("String");
        second.add("Test2String");
        second.add("Test");
        second.add("Русский");
        second.add("РусскийТекст");
        second.add("test");
        second.add("Test3String");

        second.remove("Test");
        second.remove("РусскийТекстПодлиннее");
        second.remove("abacaba");

        assertEquals(first, second);
    }

    @Test
    void removeStringMakesNotEqual() {
        var first = new Trie();
        var second = new Trie();

        first.add("Test1String");
        first.add("Test2String");
        first.add("Test3String");
        first.add("Test");
        first.add("test");
        first.add("String");
        first.add("Test3String");
        first.add("Русский");
        first.add("РусскийТекст");
        first.add("РусскийТекст");
        first.add("РусскийТекстПодлиннее");

        second.add("РусскийТекстПодлиннее");
        second.add("Test2String");
        second.add("Test1String");
        second.add("String");
        second.add("Test2String");
        second.add("Test");
        second.add("Русский");
        second.add("РусскийТекст");
        second.add("test");
        second.add("Test3String");

        second.remove("String");

        assertNotEquals(first, second);
    }

    @Test
    void removeAllEqualsEmpty() {
        var first = new Trie();
        var second = new Trie();

        first.add("Test1String");
        first.add("Test2String");
        first.add("Test3String");
        first.add("Test");
        first.add("test");
        first.add("String");
        first.add("Test3String");
        first.add("Русский");
        first.add("РусскийТекст");
        first.add("РусскийТекст");
        first.add("РусскийТекстПодлиннее");

        first.remove("РусскийТекстПодлиннее");
        first.remove("Test2String");
        first.remove("Test1String");
        first.remove("String");
        first.remove("Test2String");
        first.remove("Test");
        first.remove("Русский");
        first.remove("РусскийТекст");
        first.remove("test");
        first.remove("Test3String");

        assertEquals(first, second);
    }

    // hashcode test

    @Test
    void emptyTriesHaveEqualHashCodes() {
        var first = new Trie();
        var second = new Trie();
        assertEquals(first.hashCode(), second.hashCode());
    }
    @Test
    void addOrderDoesNotMatterForHashCode() {
        var first = new Trie();
        var second = new Trie();

        first.add("Test1String");
        first.add("Test2String");
        first.add("Test3String");
        first.add("Test");
        first.add("test");
        first.add("String");
        first.add("Test3String");
        first.add("Русский");
        first.add("РусскийТекст");
        first.add("РусскийТекст");
        first.add("РусскийТекстПодлиннее");

        second.add("РусскийТекстПодлиннее");
        second.add("Test2String");
        second.add("Test1String");
        second.add("String");
        second.add("Test2String");
        second.add("Test");
        second.add("Русский");
        second.add("РусскийТекст");
        second.add("test");
        second.add("Test3String");

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void removeStringEqualsNotAddForHashCode() {
        var first = new Trie();
        var second = new Trie();

        first.add("Test1String");
        first.add("Test2String");
        first.add("Test3String");
        first.add("test");
        first.add("String");
        first.add("Test3String");
        first.add("Русский");
        first.add("РусскийТекст");
        first.add("РусскийТекст");

        second.add("РусскийТекстПодлиннее");
        second.add("Test2String");
        second.add("Test1String");
        second.add("String");
        second.add("Test2String");
        second.add("Test");
        second.add("Русский");
        second.add("РусскийТекст");
        second.add("test");
        second.add("Test3String");

        second.remove("Test");
        second.remove("РусскийТекстПодлиннее");
        second.remove("abacaba");

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void removeAllEqualsEmptyForHashCode() {
        var first = new Trie();
        var second = new Trie();

        first.add("Test1String");
        first.add("Test2String");
        first.add("Test3String");
        first.add("Test");
        first.add("test");
        first.add("String");
        first.add("Test3String");
        first.add("Русский");
        first.add("РусскийТекст");
        first.add("РусскийТекст");
        first.add("РусскийТекстПодлиннее");

        first.remove("РусскийТекстПодлиннее");
        first.remove("Test2String");
        first.remove("Test1String");
        first.remove("String");
        first.remove("Test2String");
        first.remove("Test");
        first.remove("Русский");
        first.remove("РусскийТекст");
        first.remove("test");
        first.remove("Test3String");

        assertEquals(first.hashCode(), second.hashCode());
    }

    // serialize/deserialize tests

    @Test
    void serializeEmpty() {
        var actual = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> testTrie.serialize(actual));

        var expected = new ByteArrayOutputStream();
        var out = new DataOutputStream(expected);
        assertDoesNotThrow((

        ) -> out.writeInt(0));
        assertDoesNotThrow(() -> out.writeBoolean(false));

        assertArrayEquals(expected.toByteArray(), actual.toByteArray());
    }

    @Test
    void serializeOneString() {
        testTrie.add("abc");
        var actual = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> testTrie.serialize(actual));

        var expected = new ByteArrayOutputStream();
        var expectedData = new DataOutputStream(expected);
        assertDoesNotThrow(() -> expectedData.writeInt(1));
        assertDoesNotThrow(() -> expectedData.writeBoolean(false));
        assertDoesNotThrow(() -> expectedData.writeChar('a'));
        assertDoesNotThrow(() -> expectedData.writeInt(1));
        assertDoesNotThrow(() -> expectedData.writeBoolean(false));
        assertDoesNotThrow(() -> expectedData.writeChar('b'));
        assertDoesNotThrow(() -> expectedData.writeInt(1));
        assertDoesNotThrow(() -> expectedData.writeBoolean(false));
        assertDoesNotThrow(() -> expectedData.writeChar('c'));
        assertDoesNotThrow(() -> expectedData.writeInt(0));
        assertDoesNotThrow(() -> expectedData.writeBoolean(true));

        assertArrayEquals(expected.toByteArray(), actual.toByteArray());
    }

    @Test
    void deserializeEmpty() {
        var in = new ByteArrayOutputStream();
        var inData = new DataOutputStream(in);
        assertDoesNotThrow(() -> inData.writeInt(0));
        assertDoesNotThrow(() -> inData.writeBoolean(false));

        testTrie.add("abc");
        assertDoesNotThrow(() -> testTrie.deserialize(new ByteArrayInputStream(in.toByteArray())));

        assertEquals(new Trie(), testTrie);
    }

    @Test
    void deserializeOneString() {
        var in = new ByteArrayOutputStream();
        var inData = new DataOutputStream(in);
        assertDoesNotThrow(() -> inData.writeInt(1));
        assertDoesNotThrow(() -> inData.writeBoolean(false));
        assertDoesNotThrow(() -> inData.writeChar('a'));
        assertDoesNotThrow(() -> inData.writeInt(1));
        assertDoesNotThrow(() -> inData.writeBoolean(false));
        assertDoesNotThrow(() -> inData.writeChar('b'));
        assertDoesNotThrow(() -> inData.writeInt(1));
        assertDoesNotThrow(() -> inData.writeBoolean(false));
        assertDoesNotThrow(() -> inData.writeChar('c'));
        assertDoesNotThrow(() -> inData.writeInt(0));
        assertDoesNotThrow(() -> inData.writeBoolean(true));

        testTrie.add("def");
        assertDoesNotThrow(() -> testTrie.deserialize(new ByteArrayInputStream(in.toByteArray())));

        var expected = new Trie();
        expected.add("abc");
        assertEquals(expected, testTrie);
    }

    @Test
    void deserializeTwoStrings() {
        var in = new ByteArrayOutputStream();
        var inData = new DataOutputStream(in);
        assertDoesNotThrow(() -> inData.writeInt(1));
        assertDoesNotThrow(() -> inData.writeBoolean(false));
        assertDoesNotThrow(() -> inData.writeChar('a'));
        assertDoesNotThrow(() -> inData.writeInt(2));
        assertDoesNotThrow(() -> inData.writeBoolean(false));
        assertDoesNotThrow(() -> inData.writeChar('b'));
        assertDoesNotThrow(() -> inData.writeInt(0));
        assertDoesNotThrow(() -> inData.writeBoolean(true));
        assertDoesNotThrow(() -> inData.writeChar('c'));
        assertDoesNotThrow(() -> inData.writeInt(0));
        assertDoesNotThrow(() -> inData.writeBoolean(true));

        testTrie.add("def");
        assertDoesNotThrow(() -> testTrie.deserialize(new ByteArrayInputStream(in.toByteArray())));

        var expected = new Trie();
        expected.add("ab");
        expected.add("ac");
        assertEquals(expected, testTrie);
    }

    @Test
    void resultOfSerializeDeserializeEqualsSource() {
        var from = new Trie();
        var to = new Trie();
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

        assertEquals(from, to);
    }

    @Test
    void serializeDeserializeEmptyTrie() {
        var from = new Trie();
        var to = new Trie();

        to.add("ABC");
        to.add("def");
        to.add("123");

        var out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> from.serialize(out));
        var in = new ByteArrayInputStream(out.toByteArray());
        assertDoesNotThrow(() -> to.deserialize(in));

        assertEquals(from, to);
    }

    @Test
    void canChangeTrieAfterDeserialize() {
        var from = new Trie();
        var to = new Trie();

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
        var from = new Trie();
        var to = new Trie();

        for (int c = 0; c <= Character.MAX_VALUE; c++) {
            from.add(Character.toString(c));
        }

        var out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> from.serialize(out));
        var in = new ByteArrayInputStream(out.toByteArray());
        assertDoesNotThrow(() -> to.deserialize(in));

        assertEquals(from, to);
    }
}
