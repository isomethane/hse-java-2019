package ru.hse.mnmalysheva.myjunit.testclasses;

import ru.hse.mnmalysheva.myjunit.Test;

public class ConstructorFailedTestClass {
    public ConstructorFailedTestClass() {
        throw new RuntimeException();
    }

    @Test
    public void test() {
    }
}
