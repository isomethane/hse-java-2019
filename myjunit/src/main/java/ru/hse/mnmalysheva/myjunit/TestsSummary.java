package ru.hse.mnmalysheva.myjunit;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/** This class represents summary of all tests in class. **/
public class TestsSummary {
    private final Map<String, TestResult> results = new HashMap<>();
    private TestResult beforeClassMethodsResult;
    private TestResult afterClassMethodsResult;
    private int numberOfPassedTests;
    private int numberOfFailedTests;
    private int numberOfIgnoredTests;
    private int totalNumberOfTests;

    public Map<String, TestResult> getResults() {
        return results;
    }

    public TestResult getBeforeClassMethodsResult() {
        return beforeClassMethodsResult;
    }

    public TestResult getAfterClassMethodsResult() {
        return afterClassMethodsResult;
    }

    public int getNumberOfPassedTests() {
        return numberOfPassedTests;
    }

    public int getNumberOfFailedTests() {
        return numberOfFailedTests;
    }

    public int getNumberOfIgnoredTests() {
        return numberOfIgnoredTests;
    }

    public int getTotalNumberOfTests() {
        return totalNumberOfTests;
    }

    void addTest(@NotNull String name, @NotNull TestResult result) {
        results.put(name, result);
        switch (result.status) {
            case PASSED:
                numberOfPassedTests++;
                break;
            case FAILED:
                numberOfFailedTests++;
                break;
            case IGNORED:
                numberOfIgnoredTests++;
                break;
            default:
                break;
        }
        totalNumberOfTests++;
    }

    void setBeforeClassMethodsResult(TestResult beforeClassMethodsResult) {
        this.beforeClassMethodsResult = beforeClassMethodsResult;
    }

    void setAfterClassMethodsResult(TestResult afterClassMethodsResult) {
        this.afterClassMethodsResult = afterClassMethodsResult;
    }
}
