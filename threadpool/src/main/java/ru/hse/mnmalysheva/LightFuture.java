package ru.hse.mnmalysheva;

import java.util.function.Function;

// TODO javadoc
public interface LightFuture<T> {
    boolean isReady();
    T get() throws LightExecutionException;
    <R> LightFuture<R> thenApply(Function<T, R> function);
}
