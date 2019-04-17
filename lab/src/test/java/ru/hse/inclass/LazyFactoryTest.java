package ru.hse.inclass;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LazyFactoryTest {
    private static final String AAA = "AAAAAAAAAAAAAAAAA";

    @RepeatedTest(20)
    void createSingleThreadedLazy() {
        var lazy = LazyFactory.createSingleThreadedLazy(new DummySupplier());
        assertEquals(AAA, lazy.get());
        assertEquals(AAA, lazy.get()); // throws if not ok
        assertEquals(AAA, lazy.get());
    }

    @RepeatedTest(20)
    void createLockedLazy() throws InterruptedException {
        // tmp
        ThreadSafeDummySupplier.currentIndex = 0;

        Thread[] threads = new Thread[300];
        var supplier0 = new ThreadSafeDummySupplier(100);
        var supplier1 = new ThreadSafeDummySupplier(200);
        var supplier2 = new ThreadSafeDummySupplier(300);
        var lazy0 = LazyFactory.createLockedLazy(supplier0);
        var lazy1 = LazyFactory.createLockedLazy(supplier1);
        var lazy2 = LazyFactory.createLockedLazy(supplier2);
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 5000; j++) {
                    assertEquals(Integer.valueOf(0), lazy0.get());
                }
            });
        }
        for (int i = 100; i < 200; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 5000; j++) {
                    assertEquals(Integer.valueOf(1), lazy1.get());
                }
            });
        }
        for (int i = 200; i < 300; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 5000; j++) {
                    assertEquals(Integer.valueOf(2), lazy2.get());
                }
            });
        }
        for (int i = 0; i < 300; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 300; i++) {
            threads[i].join();
        }
    }

    @RepeatedTest(20)
    void createLockFreeLazy() throws InterruptedException {
        for (int k = 0; k < 10; k++) {
            var concurrentMap = new ConcurrentHashMap<Integer, Integer>();

            Thread[] threads = new Thread[300];
            var supplier = new LockFreeDummySupplier(k * 10);
            var lazy = LazyFactory.createLockFreeLazy(supplier);
            for (int i = 0; i < 300; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 500; j++) {
                        concurrentMap.put(lazy.get(), 42);
                    }
                });
            }
            for (int i = 0; i < 300; i++) {
                threads[i].start();
            }
            for (int i = 0; i < 300; i++) {
                threads[i].join();
            }
            assertEquals(1, concurrentMap.size());
            System.out.println((k * 10) + ": " + supplier.value);
        }
    }

    private class DummySupplier implements Supplier<String> {
        private boolean isCreated;

        @Override
        public String get() {
            if (isCreated) {
                throw new IllegalStateException();
            }
            isCreated = true;
            return AAA;
        }
    }

    private static class ThreadSafeDummySupplier implements Supplier<Integer> {
        private static int currentIndex;
        boolean isCreated;
        int value;
        int sleepTime;

        ThreadSafeDummySupplier(int sleepTime) {
            this.sleepTime = sleepTime;
            synchronized (ThreadSafeDummySupplier.class) {
                value = currentIndex++;
            }
        }

        @Override
        public Integer get() {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            if (isCreated) {
                throw new IllegalStateException();
            }
            isCreated = true;
            return value;
        }
    }

    private static class LockFreeDummySupplier implements Supplier<Integer> {
        int value;
        int sleepTime;

        LockFreeDummySupplier(int sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public Integer get() {
            int local;
            synchronized (this) {
                local = value++;
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            return local;
        }
    }
}