package ru.hse.mnmalysheva.myjunit.testclasses;

import ru.hse.mnmalysheva.myjunit.Before;
import ru.hse.mnmalysheva.myjunit.Test;

public class BeforeFailedTestClass {
    @Before
    public void fail() {
        throw new RuntimeException();
    }

    @Test
    public void test() {
    }
}
