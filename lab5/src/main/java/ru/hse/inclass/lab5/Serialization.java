package ru.hse.inclass.lab5;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
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


}
