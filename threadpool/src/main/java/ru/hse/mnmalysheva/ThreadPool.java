package ru.hse.mnmalysheva;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

/** Thread pool with fixed number of threads. **/
public class ThreadPool {
    private Thread[] threads;
    private final TaskQueue<Task<?>> tasks = new TaskQueue<>();
    private volatile boolean toFinish;

    /**
     * Constructs a new {@code ThreadPool} of specified size.
     * @param numberOfThreads thread pool size
     */
    public ThreadPool(int numberOfThreads) {
        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException("Thread pool should have at least one thread.");
        }

        threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(new ThreadLifecycle());
            threads[i].start();
        }
    }

    /**
     * Add task for further execution.
     * @param supplier computation is invocation of {@link Supplier#get()}.
     * @param <R> result type.
     * @return {@link LightFuture} that represents the result of computation.
     * @throws IllegalStateException if thread pool was shut down.
     */
    public <R> LightFuture<R> submit(Supplier<R> supplier) {
        checkState();
        var task = new Task<>(supplier);
        submitTask(task);
        return task;
    }

    /**
     * Shuts down thread pool.
     * All previously submitted tasks are executed, but no new tasks are accepted.
     */
    public void shutdown() {
        toFinish = true;
        for (var thread : threads) {
            boolean isSuccessful = false;
            while (!isSuccessful) {
                try {
                    thread.interrupt();
                    thread.join();
                    isSuccessful = true;
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void checkState() {
        if (toFinish) {
            throw new IllegalStateException("Thread pool was shut down.");
        }
    }

    private void submitTask(Task<?> task) {
        tasks.put(task);
    }

    private class ThreadLifecycle implements Runnable {
        @Override
        public void run() {
            while (true) {
                Task<?> task;
                try {
                    task = tasks.get();
                } catch (InterruptedException e) {
                    break;
                }
                task.execute();
            }
        }
    }

    private static class TaskQueue<T> {
        private final Queue<T> queue = new LinkedList<>();

        private synchronized void put(T element) {
            queue.add(element);
            notify();
        }

        private synchronized T get() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            return queue.remove();
        }
    }

    private class Task<T> implements LightFuture<T> {
        Supplier<T> supplier;
        final List<Task<?>> futureTasks = new LinkedList<>();
        final Object readinessLock = new Object();
        volatile boolean isReady;
        boolean isSuccessful;
        T result;
        LightExecutionException executionException;

        private Task(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public T get() throws LightExecutionException, InterruptedException {
            if (!isReady) {
                synchronized (readinessLock) {
                    while (!isReady) {
                        readinessLock.wait();
                    }
                }
            }
            if (isSuccessful) {
                return result;
            }
            throw executionException;
        }

        @Override
        public <R> LightFuture<R> thenApply(Function<? super T, ? extends R> function) {
            checkState();
            var futureTask = new Task<R>(() -> function.apply(result));
            if (!isReady) {
                synchronized (readinessLock) {
                    if (!isReady) {
                        futureTasks.add(futureTask);
                        return futureTask;
                    }
                }
            }
            if (isSuccessful) {
                submitTask(futureTask);
                return futureTask;
            }
            futureTask.setExecutionException(executionException);
            return futureTask;
        }

        private void execute() {
            try {
                result = supplier.get();
                isSuccessful = true;
            } catch (Throwable t) {
                setExecutionException(new LightExecutionException(t));
                return;
            }
            setReady();
            for (var futureTask : futureTasks) {
                submitTask(futureTask);
            }
            futureTasks.clear();
        }

        private void setReady() {
            synchronized (readinessLock) {
                isReady = true;
                readinessLock.notifyAll();
            }
        }

        private void setExecutionException(LightExecutionException e) {
            executionException = e;
            setReady();
            for (var futureTask : futureTasks) {
                futureTask.setExecutionException(new LightExecutionException(
                        "Parental task executed with an exception", e
                ));
            }
            futureTasks.clear();
        }
    }
}
