package ru.hse.mnmalysheva.myjunit;

import org.jetbrains.annotations.NotNull;

import static ru.hse.mnmalysheva.myjunit.MyJUnit.runTests;

/** This class represents command-line interface for {@link MyJUnit} **/
public class MyJUnitCLI {
    /**
     * Prints results of tests in given class.
     * @param args the only argument should be class name
     */
    public static void main(@NotNull String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect number of arguments. The only argument should be class name.");
            return;
        }
        String className = args[0];
        Class<?> testClass;
        try {
            testClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class '" + className + "' not found.");
            return;
        }

        TestsSummary testsSummary = runTests(testClass);
        printBeforeClassResult(testsSummary.getBeforeClassMethodsResult());
        for (var entry : testsSummary.getResults().entrySet()) {
            printTestResult(entry.getKey(), entry.getValue());
        }
        printAfterClassResult(testsSummary.getAfterClassMethodsResult());
        printStatistics(testsSummary);
    }

    private static void printTestResult(String testName, TestResult testResult) {
        System.out.print(testName + " ");
        switch (testResult.status) {
            case PASSED:
                System.out.println("PASSED");
                break;
            case FAILED:
                System.out.println("FAILED");
                System.out.println(testResult.message);
                break;
            case IGNORED:
                System.out.println("IGNORED");
                System.out.println("Cause: " + testResult.message);
                break;
            default:
                break;
        }
        System.out.println("Time: " + testResult.time + " ms");
        System.out.println();
    }

    private static void printBeforeClassResult(TestResult result) {
        if (result.status == TestStatus.FAILED) {
            System.out.println("Exception thrown while executing @BeforeClass methods:");
            System.out.println(result.message);
            System.out.println();
        }
    }

    private static void printAfterClassResult(TestResult result) {
        if (result.status == TestStatus.FAILED) {
            System.out.println("Exception thrown while executing @AfterClass methods:");
            System.out.println(result.message);
            System.out.println();
        }
    }

    private static void printStatistics(TestsSummary testsSummary) {
        System.out.println("Total number of tests: " + testsSummary.getTotalNumberOfTests());
        System.out.println("Passed: " + testsSummary.getNumberOfPassedTests());
        System.out.println("Failed: " + testsSummary.getNumberOfFailedTests());
        System.out.println("Ignored: " + testsSummary.getNumberOfIgnoredTests());
    }
}
