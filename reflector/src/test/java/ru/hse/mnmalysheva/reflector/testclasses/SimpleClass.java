package ru.hse.mnmalysheva.reflector.testclasses;

import java.util.Collection;
import java.util.Set;

public class SimpleClass {
    public static final int a = 30;
    private double b = 40;
    protected final String c = "abc";
    final boolean d = true;

    void foo(Set<Integer> set) {
        System.out.println(set);
    }

    Collection<String> bar() {
        return null;
    }

    int getA() {
        return a;
    }

    double getB() {
        return b;
    }

    String getC() {
        return c;
    }

    boolean getD() {
        return d;
    }
}
