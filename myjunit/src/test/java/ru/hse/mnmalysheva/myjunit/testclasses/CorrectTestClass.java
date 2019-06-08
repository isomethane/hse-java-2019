package ru.hse.mnmalysheva.myjunit.testclasses;

import ru.hse.mnmalysheva.myjunit.*;

import java.io.IOException;

public class CorrectTestClass {
    public static StringBuffer stringBuffer = new StringBuffer();

    @BeforeClass
    public static void beforeClass() {
        stringBuffer.append("before class\n");
    }

    @AfterClass
    public static void afterClass() {
        stringBuffer.append("after class\n");
    }

    @Before
    public void before() {
        stringBuffer.append("before\n");
    }

    @After
    public void after() {
        stringBuffer.append("after\n");
    }

    @Test
    public void okTest() throws InterruptedException {
        stringBuffer.append("okTest\n");
        Thread.sleep(501);
    }

    @Test(ignore = "Don't want to test...")
    public void ignoredTest() {
        stringBuffer.append("ignoredTest\n");
        throw new RuntimeException();
    }

    @Test(expected = RuntimeException.class)
    public void exceptionTest() {
        stringBuffer.append("exceptionTest\n");
        throw new RuntimeException();
    }

    @Test
    public void failedTest() {
        stringBuffer.append("failedTest\n");
        throw new IllegalArgumentException();
    }

    @Test(expected = IllegalStateException.class)
    public void unexpectedExceptionTest() throws IOException {
        stringBuffer.append("unexpectedExceptionTest\n");
        throw new IOException();
    }

    @Test(expected = IllegalStateException.class)
    public void notFoundExceptionTest() throws IOException {
        stringBuffer.append("notFoundExceptionTest\n");
    }
}
