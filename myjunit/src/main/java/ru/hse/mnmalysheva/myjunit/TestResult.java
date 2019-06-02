package ru.hse.mnmalysheva.myjunit;

import org.jetbrains.annotations.NotNull;

/** This class represents single test result. **/
public class TestResult {
    public final @NotNull TestStatus status;
    public final @NotNull String message;
    public final long time;

    public TestResult(@NotNull TestStatus status, @NotNull String message, long timeMillis) {
        this.status = status;
        this.message = message;
        this.time = timeMillis;
    }

    public TestResult(@NotNull TestStatus status, long timeMillis) {
        this.status = status;
        this.message = "";
        this.time = timeMillis;
    }

    public TestResult(@NotNull TestStatus status, @NotNull String message) {
        this.status = status;
        this.message = message;
        this.time = 0;
    }

    public TestResult(@NotNull TestStatus status) {
        this.status = status;
        this.message = "";
        this.time = 0;
    }
}
