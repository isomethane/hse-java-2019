package ru.hse.mnmalysheva.myjunit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import ru.hse.mnmalysheva.myjunit.testclasses.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MyJUnitTest {
    private TestsSummary expectedTestSummary;

    private void checkTestsSummary(TestsSummary expected, TestsSummary actual) {
        assertEquals(expected.getTotalNumberOfTests(), actual.getTotalNumberOfTests());
        assertEquals(expected.getNumberOfPassedTests(), actual.getNumberOfPassedTests());
        assertEquals(expected.getNumberOfFailedTests(), actual.getNumberOfFailedTests());
        assertEquals(expected.getNumberOfIgnoredTests(), actual.getNumberOfIgnoredTests());

        checkTestResult(expected.getBeforeClassMethodsResult(), actual.getBeforeClassMethodsResult());
        checkTestResult(expected.getAfterClassMethodsResult(), actual.getAfterClassMethodsResult());

        Map<String, TestResult> expectedResults = expected.getResults();
        Map<String, TestResult> actualResults = actual.getResults();

        assertEquals(expectedResults.size(), actualResults.size());
        for (String testName : expected.getResults().keySet()) {
            TestResult expectedResult = expectedResults.get(testName);
            TestResult actualResult = actualResults.get(testName);
            assertNotNull(actualResult);
            checkTestResult(expectedResult, actualResult);
        }
    }

    private void checkTestResult(TestResult expected, TestResult actual) {
        if (expected == null && actual == null) {
            return;
        }
        assertEquals(expected.status, actual.status);
        assertEquals(expected.message, actual.message);
        assertTrue(actual.time >= expected.time);
    }

    @BeforeEach
    void init() {
        expectedTestSummary = new TestsSummary();
    }

    @Test
    void correctClassTest() {
        expectedTestSummary.setBeforeClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.setAfterClassMethodsResult(new TestResult(TestStatus.PASSED));

        expectedTestSummary.addTest(
                "okTest",
                new TestResult(TestStatus.PASSED, 500)
        );
        expectedTestSummary.addTest(
                "ignoredTest",
                new TestResult(TestStatus.IGNORED, "Don't want to test...")
        );
        expectedTestSummary.addTest(
                "exceptionTest",
                new TestResult(TestStatus.PASSED)
        );
        expectedTestSummary.addTest(
                "failedTest",
                new TestResult(TestStatus.FAILED, "java.lang.IllegalArgumentException at failedTest")
        );
        expectedTestSummary.addTest(
                "unexpectedExceptionTest",
                new TestResult(
                        TestStatus.FAILED,
                        "Unexpected exception type thrown. " +
                                "Expected: java.lang.IllegalStateException, " +
                                "actual: java.io.IOException"
                )
        );
        expectedTestSummary.addTest(
                "notFoundExceptionTest",
                new TestResult(
                        TestStatus.FAILED,
                        "Expected java.lang.IllegalStateException to be thrown, but nothing was thrown"
                )
        );

        TestsSummary actualTestSummary = MyJUnit.runTests(CorrectTestClass.class);
        checkTestsSummary(expectedTestSummary, actualTestSummary);

        assertEquals(
                "before class\n" +
                        "before\n" +
                        "exceptionTest\n" +
                        "after\n" +
                        "before\n" +
                        "failedTest\n" +
                        "before\n" +
                        "notFoundExceptionTest\n" +
                        "before\n" +
                        "okTest\n" +
                        "after\n" +
                        "before\n" +
                        "unexpectedExceptionTest\n" +
                        "after class\n",
                CorrectTestClass.stringBuffer.toString()
        );
    }

    @Test
    void noConstructorClassTest() {
        expectedTestSummary.setBeforeClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.setAfterClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.addTest(
                "test",
                new TestResult(
                        TestStatus.FAILED,
                        "ru.hse.mnmalysheva.myjunit.testclasses.NoConstructorTestClass " +
                                "has no constructor without arguments"
                )
        );

        TestsSummary actualTestSummary = MyJUnit.runTests(NoConstructorTestClass.class);
        checkTestsSummary(expectedTestSummary, actualTestSummary);
    }

    @Test
    void abstractClassTest() {
        expectedTestSummary.setBeforeClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.setAfterClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.addTest(
                "test",
                new TestResult(
                        TestStatus.FAILED,
                        "ru.hse.mnmalysheva.myjunit.testclasses.AbstractTestClass is abstract"
                )
        );

        TestsSummary actualTestSummary = MyJUnit.runTests(AbstractTestClass.class);
        checkTestsSummary(expectedTestSummary, actualTestSummary);
    }

    @Test
    void constructorFailedClassTest() {
        expectedTestSummary.setBeforeClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.setAfterClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.addTest(
                "test",
                new TestResult(
                        TestStatus.FAILED,
                        "java.lang.RuntimeException at " +
                                "ru.hse.mnmalysheva.myjunit.testclasses.ConstructorFailedTestClass"
                )
        );

        TestsSummary actualTestSummary = MyJUnit.runTests(ConstructorFailedTestClass.class);
        checkTestsSummary(expectedTestSummary, actualTestSummary);
    }

    @Test
    void beforeClassFailedTest() {
        expectedTestSummary.setBeforeClassMethodsResult(
                new TestResult(TestStatus.FAILED, "java.lang.RuntimeException at fail")
        );

        TestsSummary actualTestSummary = MyJUnit.runTests(BeforeClassFailedTestClass.class);
        checkTestsSummary(expectedTestSummary, actualTestSummary);
    }

    @Test
    void afterClassFailedTest() {
        expectedTestSummary.setBeforeClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.setAfterClassMethodsResult(
                new TestResult(TestStatus.FAILED, "java.lang.RuntimeException at fail")
        );
        expectedTestSummary.addTest("test", new TestResult(TestStatus.PASSED));

        TestsSummary actualTestSummary = MyJUnit.runTests(AfterClassFailedTestClass.class);
        checkTestsSummary(expectedTestSummary, actualTestSummary);
    }

    @Test
    void beforeFailedTest() {
        expectedTestSummary.setBeforeClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.setAfterClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.addTest(
                "test", new TestResult(TestStatus.FAILED, "java.lang.RuntimeException at fail")
        );

        TestsSummary actualTestSummary = MyJUnit.runTests(BeforeFailedTestClass.class);
        checkTestsSummary(expectedTestSummary, actualTestSummary);
    }

    @Test
    void afterFailedTest() {
        expectedTestSummary.setBeforeClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.setAfterClassMethodsResult(new TestResult(TestStatus.PASSED));
        expectedTestSummary.addTest(
                "test", new TestResult(TestStatus.FAILED, "java.lang.RuntimeException at fail")
        );

        TestsSummary actualTestSummary = MyJUnit.runTests(AfterFailedTestClass.class);
        checkTestsSummary(expectedTestSummary, actualTestSummary);
    }
}