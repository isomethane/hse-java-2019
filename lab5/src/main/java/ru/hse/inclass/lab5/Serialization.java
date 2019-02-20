package ru.hse.inclass.lab5;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.io.*;

import java.lang.Class;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public class Serialization {
    static void serialize(Object o, OutputStream out) throws IOException {
        var clazz = o.getClass();

        serialize_by_class(o, o.getClass(), new ObjectOutputStream(out));
    }

    static private <T> void serialize_by_class(Object o, Class<T> clazz, ObjectOutputStream out)
                                                                            throws IOException {
        if (clazz == null) {
            return;
        }
        serialize_by_class(o, clazz.getSuperclass(), out);

        Field[] fields = clazz.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        for (var field : fields) {
            try {
                out.writeObject(field.get(o));
            } catch (IllegalAccessException ex) {
                System.out.println("!!!!");
            }
        }
    }

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
