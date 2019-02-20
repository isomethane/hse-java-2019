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
    }
}