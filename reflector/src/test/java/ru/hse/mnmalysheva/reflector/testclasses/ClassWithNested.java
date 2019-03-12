package ru.hse.mnmalysheva.reflector.testclasses;

import java.util.Set;

public class ClassWithNested<E> {
    private Inner inner = new Inner();
    private Nested<String> nested = new Nested<>();

    public void foo(Set<Integer> set) {
        inner.set = set;
    }

    public Set<Integer> bar() {
        return inner.set;
    }

    private class Inner {
        Set<Integer> set;
        E foo(Inner argument) {
            return null;
        }
    }

    private static class Nested<E> {
        private String string = "nested";
        <T extends E> T foo(ClassWithNested<E> argument) {
            return null;
        }
        int bar(String string) {
            return 5;
        }
    }

    private interface NestedInterface<E> {
        <T extends E> T foo();
        int bar(String string);
    }
}
