package ru.hse.mnmalysheva.myjunit.testclasses;

import ru.hse.mnmalysheva.myjunit.After;
import ru.hse.mnmalysheva.myjunit.Test;

public class AfterFailedTestClass {
    @After
    public void fail() {
        throw new RuntimeException();
    }

    @Test
    public void test() {
    }
}
