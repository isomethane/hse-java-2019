package ru.hse.mnmalysheva;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TODO general description
public class LightExecutionException extends Exception {
    /** Constructs a {@code LightExecutionException} with {@code null} as its detail message. **/
    public LightExecutionException() {}

    /**
     * Constructs a {@code LightExecutionException} with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public LightExecutionException(@NotNull String message) {
        super(message);
    }

    /**
     * Constructs a {@code LightExecutionException} with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A {@code null} value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public LightExecutionException(@NotNull String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@code LightExecutionException} with the specified cause and a detail
     * message of {@code (cause==null ? null : cause.toString())} (which
     * typically contains the class and detail message of {@code cause}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
    public LightExecutionException(@Nullable Throwable cause) {
        super(cause);
    }
}
