package ru.hse.inclass.lab5;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {

    @Test
    void deserialize() throws NoSuchMethodException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, IOException {
        class TestClass {
            private int testValue = 30;
            private String testString = "aaa";

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TestClass testClass = (TestClass) o;
                return testValue == testClass.testValue &&
                        Objects.equals(testString, testClass.testString);
            }

            @Override
            public int hashCode() {
                return Objects.hash(testValue, testString);
            }
        }

        var testClass = new TestClass();
        var out = new ByteArrayOutputStream();
        Serialization.serialize(testClass, out);
        var another = Serialization.deserialize(new ByteArrayInputStream(out.toByteArray()), TestClass.class);
        assertEquals(testClass, another);
    }
}