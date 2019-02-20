package ru.hse.inclass.lab5;

import java.io.*;

import java.lang.Class;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public class Serialization {
    public static void serialize(Object o, OutputStream out) {}

    private static  <T> void deserialize(DataInputStream in, Class<T> c, T instance)
            throws IOException, ClassNotFoundException, IllegalAccessException {
        if (c == null) {
            return;
        }
        deserialize(in, c.getSuperclass(), instance);
        var fields = c.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        for (var f : fields) {
            if (f.getType().equals(byte.class)) {
                f.setByte(instance, in.readByte());
            } else if (f.getType().equals(short.class)) {
                f.setShort(instance, in.readShort());
            } else if (f.getType().equals(int.class)) {
                f.setInt(instance, in.readInt());
            } else if (f.getType().equals(long.class)) {
                f.setLong(instance, in.readLong());
            } else if (f.getType().equals(float.class)) {
                f.setFloat(instance, in.readFloat());
            } else if (f.getType().equals(double.class)) {
                f.setDouble(instance, in.readDouble());
            } else if (f.getType().equals(boolean.class)) {
                f.setBoolean(instance, in.readBoolean());
            } else if (f.getType().equals(char.class)) {
                f.setChar(instance, in.readChar());
            } else if (f.getType().equals(String.class)) {
                f.set(instance, in.readUTF());
            }
        }
    }

    public static <T> T deserialize(InputStream in, Class<T> c)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException, ClassNotFoundException {
        T instance = c.getDeclaredConstructor().newInstance();
        deserialize(new DataInputStream(in), c, instance);
        return instance;
    }
}
