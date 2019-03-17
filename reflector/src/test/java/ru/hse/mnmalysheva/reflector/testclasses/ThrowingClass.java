package ru.hse.mnmalysheva.reflector.testclasses;

import java.io.IOException;

public class ThrowingClass<T extends Exception> {
    public void foo() throws IOException, T {}
}
