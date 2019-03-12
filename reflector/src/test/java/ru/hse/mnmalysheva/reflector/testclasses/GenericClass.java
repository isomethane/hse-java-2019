package ru.hse.mnmalysheva.reflector.testclasses;

import java.util.Collection;
import java.util.Set;

public class GenericClass<K extends Number, V> {
    K key;
    V value;

    private static <K, V> V foo(K key) {
        return null;
    }

    public <M extends K> void bar(M m) {}

    public void fooBar(Collection<? super K> c, Set<? extends V> s) {}
}
