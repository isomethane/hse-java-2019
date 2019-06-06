package ru.hse.mnmalysheva;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolTest {
    private static final int REPEATED_TEST_COUNT = 20;
    private static final int NUMBER_OF_THREADS = 8;
    private static ThreadPool threadPool;

    @BeforeEach
    void initThreadPool() {
        threadPool = new ThreadPool(NUMBER_OF_THREADS);
    }

    @AfterEach
    void shutdown() {
        threadPool.shutdown();
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void testQueue() throws LightExecutionException, InterruptedException {
        var pool = new ThreadPool(1);
        final int numberOfTasks = 1000;
        var futures = new ArrayList<LightFuture<Integer>>();
        var results = Collections.synchronizedList(new ArrayList<Integer>());
        for (int i = 0; i < numberOfTasks; i++) {
            final int finalI = i;
            futures.add(pool.submit(() -> {
                results.add(finalI);
                return 0;
            }));
        }
        for (var future : futures) {
            assertEquals(0, future.get().intValue());
        }
        var expected = IntStream.range(0, numberOfTasks).boxed().collect(Collectors.toList());
        assertEquals(expected, results);
    }

    @Test
    void cannotCreatePoolWithLessThanOneThread() {
        assertThrows(IllegalArgumentException.class, () -> new ThreadPool(0));
        assertThrows(IllegalArgumentException.class, () -> new ThreadPool(-1));
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void testThreadPoolSize() throws LightExecutionException, InterruptedException {
        final int numberOfTasks = 16;

        for (int numberOfThreads = 1; numberOfThreads <= 8; numberOfThreads++) {
            ThreadPool pool = new ThreadPool(numberOfThreads);

            var futures = new ArrayList<LightFuture<Long>>();
            var results = new HashSet<Long>();
            for (int i = 0; i < numberOfTasks; i++) {
                futures.add(pool.submit(() -> {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ignored) {}
                    return Thread.currentThread().getId();
                }));
            }
            for (var future : futures) {
                results.add(future.get());
            }
            assertEquals(numberOfThreads, results.size());

            pool.shutdown();
        }
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void testIsReady() throws LightExecutionException, InterruptedException {
        var future = threadPool.submit(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
            return "finished";
        });
        assertFalse(future.isReady());
        assertFalse(future.isReady());
        assertFalse(future.isReady());
        assertEquals("finished", future.get());
        assertTrue(future.isReady());
        assertTrue(future.isReady());
        assertTrue(future.isReady());

        future = threadPool.submit(() -> "finished");
        Thread.sleep(100);
        assertTrue(future.isReady());
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void everyTaskExecutesOnce() throws LightExecutionException, InterruptedException {
        StrictSupplier.refresh();
        final int numberOfTasks = 1000;
        var futures = new ArrayList<LightFuture<Integer>>();
        var results = new ArrayList<Integer>();
        for (int i = 0; i < numberOfTasks; i++) {
            futures.add(threadPool.submit(new StrictSupplier()));
        }
        for (var future : futures) {
            int result = future.get();
            results.add(result);
            assertEquals(result, future.get().intValue());
            assertEquals(result, future.get().intValue());
        }
        Collections.sort(results);

        var expected = IntStream.range(0, numberOfTasks).boxed().collect(Collectors.toList());
        assertEquals(expected, results);
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void testSubmitTasksFromDifferentThreads() throws LightExecutionException, InterruptedException {
        StrictSupplier.refresh();
        final int numberOfThreads = 100;
        final int numberOfTasks = 1000;
        var threads = new ArrayList<Thread>();
        var futures = Collections.synchronizedList(new ArrayList<LightFuture<Integer>>());
        var results = new ArrayList<Integer>();
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < numberOfTasks; j++) {
                    futures.add(threadPool.submit(new StrictSupplier()));
                }
            }));
        }
        for (var thread : threads) {
            thread.start();
        }
        for (var thread : threads) {
            thread.join();
        }
        for (var future : futures) {
            int result = future.get();
            results.add(result);
            assertEquals(result, future.get().intValue());
            assertEquals(result, future.get().intValue());
        }
        Collections.sort(results);

        var expected = IntStream.range(0, numberOfThreads * numberOfTasks).boxed().collect(Collectors.toList());
        assertEquals(expected, results);
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void thenApplyBasic() throws LightExecutionException, InterruptedException {
        var future1 = threadPool.submit(() -> 5);
        var future2 = future1.thenApply(a -> a * 11);
        var future3 = future2.thenApply(a -> a + 100);
        var future4 = future3.thenApply(a -> "AAAAA" + a);
        assertEquals(5, future1.get().intValue());
        assertEquals(55, future2.get().intValue());
        assertEquals(155, future3.get().intValue());
        assertEquals("AAAAA155", future4.get());
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void thenApplyBeforeExecutionWorksOk() throws LightExecutionException, InterruptedException {
        var future1 = threadPool.submit(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            return 5;
        });
        var future2 = future1.thenApply(a -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            return a * 11;
        });
        var future3 = future2.thenApply(a -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            return a + 100;
        });
        var future4 = future3.thenApply(a -> "AAAAA" + a);
        assertEquals(5, future1.get().intValue());
        assertEquals(55, future2.get().intValue());
        assertEquals(155, future3.get().intValue());
        assertEquals("AAAAA155", future4.get());
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void thenApplyAfterExecutionWorksOk() throws LightExecutionException, InterruptedException {
        var future1 = threadPool.submit(() -> 5);
        assertEquals(5, future1.get().intValue());
        var future2 = future1.thenApply(a -> a * 11);
        assertEquals(55, future2.get().intValue());
        var future3 = future2.thenApply(a -> a + 100);
        assertEquals(155, future3.get().intValue());
        var future4 = future3.thenApply(a -> "AAAAA" + a);
        assertEquals("AAAAA155", future4.get());
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void testExceptions() {
        var future1 = threadPool.submit((Supplier<Integer>) () -> {
            throw new RuntimeException();
        });
        var future2 = future1.thenApply(a -> a * 11);
        var future3 = future2.thenApply(a -> a + 100);
        var future4 = future3.thenApply(a -> "AAAAA" + a);
        assertThrows(LightExecutionException.class, future1::get);
        assertThrows(LightExecutionException.class, future2::get);
        assertThrows(LightExecutionException.class, future3::get);
        assertThrows(LightExecutionException.class, future4::get);
    }

    @RepeatedTest(REPEATED_TEST_COUNT)
    void thenApplyDoesNotBlockCurrentTread() throws LightExecutionException, InterruptedException {
        var future = threadPool.submit(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
            return 100;
        });
        var futures = new ArrayList<LightFuture<Integer>>();
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            futures.add(future.thenApply(a -> (a + finalI) * 100));
        }
        assertFalse(future.isReady());
        for (int i = 0; i < 10; i++) {
            assertEquals((100 + i) * 100, futures.get(i).get().intValue());
        }
    }

    @Test
    void cannotSubmitAfterShutdown() {
        threadPool.shutdown();
        assertThrows(IllegalStateException.class, () -> threadPool.submit(() -> 1));
    }

    @Test
    void cannotApplyAfterShutdown() {
        var future1 = threadPool.submit(() -> {
            long result = 0;
            for (int i = 0; i < 100000; i++) {
                result += i;
            }
            return result;
        });
        var future2 = threadPool.submit(() -> 5);
        threadPool.shutdown();
        assertThrows(IllegalStateException.class, () -> future1.thenApply(a -> a * 11));
        assertThrows(IllegalStateException.class, () -> future2.thenApply(a -> a * 11));
    }

    private static class StrictSupplier implements Supplier<Integer> {
        private static AtomicInteger count = new AtomicInteger();
        private AtomicBoolean isUsed = new AtomicBoolean();

        private static void refresh() {
            count.set(0);
        }

        @Override
        public Integer get() {
            if (isUsed.getAndSet(true)) {
                throw new RuntimeException();
            }
            return count.getAndIncrement();
        }
    }
}