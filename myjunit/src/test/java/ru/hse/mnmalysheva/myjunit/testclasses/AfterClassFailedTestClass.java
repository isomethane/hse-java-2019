package ru.hse.mnmalysheva.myjunit.testclasses;

import ru.hse.mnmalysheva.myjunit.AfterClass;
import ru.hse.mnmalysheva.myjunit.Test;

public class AfterClassFailedTestClass {
    @AfterClass
    public static void fail() {
        throw new RuntimeException();
    }

    @Test
    public void test() {
    }
}
