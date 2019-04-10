package ru.hse.inclass;

import java.util.function.Supplier;

public class LazyFactory {
    public static <T> Lazy<T> createSingleThreadedLazy(Supplier<T> supplier) {
        return new SingleThreadedLazy<>(supplier);
    }

    public static <T> Lazy<T> createLockedLazy(Supplier<T> supplier) {
        return new LockedLazy<>(supplier);
    }

    public static <T> Lazy<T> createLockFreeLazy(Supplier<T> supplier) {
        return new LockFreeLazy<>(supplier);
    }

    private static class LockedLazy<T> implements Lazy<T> {
        private LockedLazy(Supplier<T> supplier) {
        }

        @Override
        public T get() {
            return null;
        }
    }

    private static class LockFreeLazy<T> implements Lazy<T> {
        private LockFreeLazy(Supplier<T> supplier) {
        }

        @Override
        public T get() {
            return null;
        }
    }

    private static class SingleThreadedLazy<T> implements Lazy<T> {
        private SingleThreadedLazy(Supplier<T> supplier) {
        }

        @Override
        public T get() {
            return null;
        }
    }
}
