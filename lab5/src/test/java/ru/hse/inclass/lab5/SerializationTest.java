package ru.hse.inclass.lab5;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {
    private class Test1 {
        int a = 3;
        int b = 4;
        char k = 'a';

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Test1 test1 = (Test1) o;
            return a == test1.a &&
                    b == test1.b &&
                    k == test1.k;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, k);
        }
    }

    @Test
    void test1() throws IOException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        var temp = new ByteArrayOutputStream();
        var obj1 = new Test1();

        Serialization.serialize(obj1, temp);

        var obj2 = Serialization.deserialize(new ByteArrayInputStream(temp.toByteArray()), Test1.class);

        assertEquals(obj1, obj2);

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
}
