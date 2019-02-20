package ru.hse.inclass.lab5;

import java.io.*;

import java.lang.Class;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public class Serialization {
    private static  <T> void deserialize(ObjectInputStream in, Class<T> c, T instance)
            throws IOException, ClassNotFoundException, IllegalAccessException {
        if (c == null) {
            return;
        }
        deserialize(in, c.getSuperclass(), instance);
        var fields = c.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        for (var f : fields) {
            f.set(instance, in.readObject());
        }
    }

    public static <T> T deserialize(InputStream in, Class<T> c)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException, ClassNotFoundException {
        T instance = c.getDeclaredConstructor().newInstance();
        deserialize(new ObjectInputStream(in), c, instance);
        return instance;
    }
}
