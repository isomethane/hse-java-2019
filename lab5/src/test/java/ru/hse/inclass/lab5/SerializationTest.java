package ru.hse.inclass.lab5;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SerializationTest {
    static public class TestClass1 {
        public int a = 3;
        public int b = 4;
        protected char k = 'a';

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestClass1 test1 = (TestClass1) o;
            return a == test1.a &&
                    b == test1.b &&
                    k == test1.k;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, k);
        }
    }

    static public class TestClass2 {
        private int testValue = 30;
        private String testString = "aaa";

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestClass2 testClass = (TestClass2) o;
            return testValue == testClass.testValue &&
                    Objects.equals(testString, testClass.testString);
        }

        @Override
        public int hashCode() {
            return Objects.hash(testValue, testString);
        }
    }

    static public class TestClassDerived extends TestClass2 {
        private int testValue = 20;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            TestClassDerived that = (TestClassDerived) o;
            return testValue == that.testValue;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), testValue);
        }
    }

    @Test
    void test1() throws IOException, InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        var testClass = new TestClass1();
        var out = new ByteArrayOutputStream();
        Serialization.serialize(testClass, out);
        var another = Serialization.deserialize(new ByteArrayInputStream(out.toByteArray()), TestClass1.class);
        assertEquals(testClass, another);
    }

    @Test
    void test2() throws NoSuchMethodException, IllegalAccessException,
                InstantiationException, ClassNotFoundException, InvocationTargetException, IOException {

        var testClass = new TestClass2();
        var out = new ByteArrayOutputStream();
        Serialization.serialize(testClass, out);
        var another = Serialization.deserialize(new ByteArrayInputStream(out.toByteArray()), TestClass2.class);
        assertEquals(testClass, another);
    }

    @Test
    void test3() throws NoSuchMethodException, IllegalAccessException,
            InstantiationException, ClassNotFoundException, InvocationTargetException, IOException {

        var testClass = new TestClassDerived();
        var out = new ByteArrayOutputStream();
        Serialization.serialize(testClass, out);
        var another = Serialization.deserialize(new ByteArrayInputStream(out.toByteArray()), TestClassDerived.class);
        assertEquals(testClass, another);
    }
}
