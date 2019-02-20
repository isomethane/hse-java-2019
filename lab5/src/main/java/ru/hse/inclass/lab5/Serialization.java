package ru.hse.inclass.lab5;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;

public class Serialization {
    static void serialize(Object o, OutputStream out) throws IOException, IllegalAccessException {
        var clazz = o.getClass();

        serializeByClass(o, o.getClass(), new DataOutputStream(out));
    }

    static private <T> void serializeByClass(Object o, Class<T> clazz, DataOutputStream out) throws IllegalAccessException, IOException {
        if (clazz == null) {
            return;
        }
        serializeByClass(o, clazz.getSuperclass(), out);

        Field[] fields = clazz.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        for (var field : fields) {
            var fieldClass = field.getType();
            field.setAccessible(true);

            if (fieldClass.equals(boolean.class)) {
                out.writeBoolean(field.getBoolean(o));
            } else if (fieldClass.equals(byte.class)) {
                out.writeByte(field.getByte(o));
            } else if (fieldClass.equals(char.class)) {
                out.writeChar(field.getChar(o));
            } else if (fieldClass.equals(double.class)) {
                out.writeDouble(field.getDouble(o));
            } else if (fieldClass.equals(float.class)) {
                out.writeFloat(field.getFloat(o));
            } else if (fieldClass.equals(int.class)) {
                out.writeInt(field.getInt(o));
            } else if (fieldClass.equals(long.class)) {
                out.writeLong(field.getLong(o));
            } else if (fieldClass.equals(short.class)) {
                out.writeShort(field.getShort(o));
            } else if (fieldClass.equals(String.class)) {
                out.writeUTF((String)field.get(o));
            }
        }
    }

    private static  <T> void deserialize(DataInputStream in, Class<T> c, T instance)
            throws IOException, ClassNotFoundException, IllegalAccessException {
        if (c == null) {
            return;
        }
        deserialize(in, c.getSuperclass(), instance);
        var fields = c.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));
        for (var f : fields) {
            f.setAccessible(true);

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
