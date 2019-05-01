package ru.hse.mnmalysheva;

import java.util.function.Function;

/** A {@code LightFuture} represents the result of computation inside {@link ThreadPool}. **/
public interface LightFuture<T> {
    /** Returns {@code true} if this task completed. **/
    boolean isReady();

    /**
     * Returns the result of computation.
     * @throws LightExecutionException if computation executed with exception.
     * @throws InterruptedException if current thread was interrupted while waiting.
     */
    T get() throws LightExecutionException, InterruptedException;

    /**
     * Returns new {@code LightFuture} that represents the result of given {@link Function}
     * applied to the result of current computation.
     * @param function function to apply.
     * @param <R> result type.
     * @return result of function application.
     * @throws IllegalStateException if thread pool was shut down.
     */
    <R> LightFuture<R> thenApply(Function<T, R> function);
}
