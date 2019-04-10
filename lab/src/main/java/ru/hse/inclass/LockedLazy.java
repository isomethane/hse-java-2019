package ru.hse.inclass;

public class LockedLazy<T> implements Lazy<T> {
    @Override
    public T get() {
        return null;
    }
}
