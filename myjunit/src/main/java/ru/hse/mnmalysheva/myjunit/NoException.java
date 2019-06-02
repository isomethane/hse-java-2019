package ru.hse.mnmalysheva.myjunit;

/**
 * This class is used for {@code @Test} annotation.
 * If {@code expected} parameter is {@code NoException.class}, then test is not expected to throw any exception.
 **/
final class NoException extends Exception {
    private NoException() {}
}
