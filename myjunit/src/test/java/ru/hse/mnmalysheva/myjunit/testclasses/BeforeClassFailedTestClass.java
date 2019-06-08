package ru.hse.mnmalysheva.myjunit.testclasses;

import ru.hse.mnmalysheva.myjunit.BeforeClass;
import ru.hse.mnmalysheva.myjunit.Test;

public class BeforeClassFailedTestClass {
    @BeforeClass
    public static void fail() {
        throw new RuntimeException();
    }

    @Test
    public void test() {
    }
}
