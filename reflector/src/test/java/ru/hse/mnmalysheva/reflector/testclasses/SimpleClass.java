package ru.hse.mnmalysheva.reflector.testclasses;

import java.util.Collection;
import java.util.Set;

public class SimpleClass {
    public static final int a = 30;
    private double b = 40;
    protected String c = "abc";

    void foo(Set<Integer> set) {
        System.out.println(set);
    }

    Collection<String> bar() {
        return null;
    }
}
