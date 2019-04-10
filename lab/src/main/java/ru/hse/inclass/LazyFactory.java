package ru.hse.inclass;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
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

    private static class SingleThreadedLazy<T> implements Lazy<T> {
        private T value;
        private Supplier<T> supplier;

        private SingleThreadedLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            if (supplier != null) {
                value = supplier.get();
                supplier = null;
            }
            return value;
        }
    }

    private static class LockedLazy<T> implements Lazy<T> {
        private volatile Box<T> value = null;
        private Supplier<T> supplier;

        private LockedLazy(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public synchronized T get() {
            var local = value;
            if (local == null) {
                synchronized (this) {
                    local = value;
                    if (local == null) {
                        value = local = new Box<>(supplier.get());
                    }
                }
            }
            return local.value;
        }
    }

    private static class LockFreeLazy<T> implements Lazy<T> {
        private AtomicReference<Box<T>> value = new AtomicReference<>(null);
        private Supplier<T> supplier;

        private LockFreeLazy(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            var local = value.get();
            if (local == null) {
                value.compareAndSet(null, new Box<>(supplier.get()));
            }
            local = value.get();
            return local.value;
        }
    }

    private static class Box<T> {
        private T value;

        private Box(T t) {
            value = t;
        }
    }
}
