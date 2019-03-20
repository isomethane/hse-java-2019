package ru.hse.mnmalysheva.phonebook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PhonebookManagerTest {
    private static PhonebookManager phonebook = PhonebookManager.getInstance();
    private static Map<String, Set<String>> content = Map.of(
            "A", Set.of("1"),
            "B", Set.of("2", "3", "4"),
            "C", Set.of("2", "3"),
            "D", Set.of("4")
    );
    private static Map<String, Set<String>> reverseContent = Map.of(
            "1", Set.of("A"),
            "2", Set.of("B", "C"),
            "3", Set.of("B", "C"),
            "4", Set.of("B", "D")
    );
    private static Map<String, Set<String>> addContent = Map.of(
            "A", Set.of("1", "2", "5"),
            "B", Set.of("2", "3", "4"),
            "C", Set.of("2", "3"),
            "D", Set.of("4"),
            "E", Set.of("1"),
            "F", Set.of("6")
    );
    private static Map<String, Set<String>> deleteContent = Map.of(
            "A", Set.of("1"),
            "B", Set.of("3", "4"),
            "C", Set.of("3")
    );
    private static Map<String, Set<String>> changeNameContent = Map.of(
            "A", Set.of("1", "4"),
            "B", Set.of("2", "3", "4"),
            "E", Set.of("3")
    );
    private static Map<String, Set<String>> changePhoneContent = Map.of(
            "A", Set.of("1"),
            "B", Set.of("3", "4"),
            "C", Set.of("2", "5"),
            "D", Set.of("2")
    );


    @BeforeEach
    void init() {
        phonebook.clear();
        content.forEach((name, phones) -> phones.forEach(phone -> phonebook.add(name, phone)));
    }

    @Test
    void getPhonesByName() {
        content.forEach(
                (name, phones) -> assertEquals(phones, phonebook.getPhonesByName(name))
        );
    }

    @Test
    void getNamesByPhone() {
        reverseContent.forEach(
                (phone, names) -> assertEquals(names, phonebook.getNamesByPhone(phone))
        );
    }

    @Test
    void add() {
        assertFalse(phonebook.add("A", "1"));
        assertFalse(phonebook.add("B", "4"));
        assertTrue(phonebook.add("A", "2"));
        assertTrue(phonebook.add("A", "5"));
        assertTrue(phonebook.add("E", "1"));
        assertTrue(phonebook.add("F", "6"));
        assertEquals(addContent, phonebook.getContent());
    }

    @Test
    void delete() {
        assertFalse(phonebook.delete("B", "1"));
        assertTrue(phonebook.delete("B", "2"));
        assertTrue(phonebook.delete("C", "2"));
        assertTrue(phonebook.delete("D", "4"));
        assertEquals(deleteContent, phonebook.getContent());
    }

    @Test
    void changeName() {
        assertFalse(phonebook.changeName("A", "2", "B"));
        assertFalse(phonebook.changeName("A", "5", "B"));
        assertFalse(phonebook.changeName("E", "1", "A"));
        assertTrue(phonebook.changeName("C", "2", "B"));
        assertTrue(phonebook.changeName("D", "4", "A"));
        assertTrue(phonebook.changeName("C", "3", "E"));
        assertEquals(changeNameContent, phonebook.getContent());
    }

    @Test
    void changePhone() {
        assertFalse(phonebook.changePhone("A", "2", "3"));
        assertFalse(phonebook.changePhone("A", "5", "4"));
        assertFalse(phonebook.changePhone("E", "1", "2"));
        assertTrue(phonebook.changePhone("C", "3", "5"));
        assertTrue(phonebook.changePhone("D", "4", "2"));
        assertTrue(phonebook.changePhone("B", "2", "3"));
        assertEquals(changePhoneContent, phonebook.getContent());
    }

    @Test
    void getContent() {
        assertEquals(content, phonebook.getContent());
    }
}