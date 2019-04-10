package ru.hse.inclass;

import java.util.function.Supplier;

public class LazyFactory {
    public static <T> Lazy<T> createSingleThreadedLazy(Supplier<T> supplier) {
        throw new UnsupportedOperationException();
    }

    public static <T> Lazy<T> createLockedLazy(Supplier<T> supplier) {
        throw new UnsupportedOperationException();
    }

    public static <T> Lazy<T> createLockFreeLazy(Supplier<T> supplier) {
        throw new UnsupportedOperationException();
    }
}
