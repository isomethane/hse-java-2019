package ru.hse.mnmalysheva.myjunit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents simpler version of JUnit.
 * It runs tests and returns results and time of tests execution.
 **/
public class MyJUnit {
    /**
     * Runs public methods annotated as tests in specified class.
     * @param testClass class to test
     * @return tests summary
     */
    public static TestsSummary runTests(Class<?> testClass) {
        var testsSummary = new TestsSummary();

        var beforeClassMethods = new ArrayList<Method>();
        var afterClassMethods = new ArrayList<Method>();
        var beforeMethods = new ArrayList<Method>();
        var afterMethods = new ArrayList<Method>();
        var tests = new ArrayList<Method>();

        var methods = new ArrayList<>(Arrays.asList(testClass.getMethods()));
        methods.sort(Comparator.comparing(Method::getName));

        for (var method : testClass.getMethods()) {
            if (method.getAnnotation(BeforeClass.class) != null) {
                beforeClassMethods.add(method);
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                afterClassMethods.add(method);
            }
            if (method.getAnnotation(Before.class) != null) {
                beforeMethods.add(method);
            }
            if (method.getAnnotation(After.class) != null) {
                afterMethods.add(method);
            }
            if (method.getAnnotation(Test.class) != null) {
                tests.add(method);
            }
        }

        testsSummary.setBeforeClassMethodsResult(runMethods(beforeClassMethods, null));
        if (testsSummary.getBeforeClassMethodsResult().status != TestStatus.PASSED) {
            return testsSummary;
        }
        for (var test : tests) {
            testsSummary.addTest(test.getName(), runTest(beforeMethods, test, afterMethods));
        }
        testsSummary.setAfterClassMethodsResult(runMethods(afterClassMethods, null));
        return testsSummary;
    }

    private static TestResult runMethods(@NotNull List<Method> methods, @Nullable Object instance) {
        for (var method : methods) {
            try {
                method.invoke(instance);
            } catch (InvocationTargetException e) {
                return new TestResult(TestStatus.FAILED, exceptionThrownMessage(e, method));
            } catch (IllegalAccessException ignored) {
                throw new RuntimeException();
            }
        }
        return new TestResult(TestStatus.PASSED);
    }

    private static TestResult runTest(Method test, Object instance) {
        Test testAnnotation = test.getAnnotation(Test.class);
        Class<? extends Throwable> expectedException = testAnnotation.expected();
        Class<? extends Throwable> actualException = NoException.class;

        long startTime = System.currentTimeMillis();

        try {
            test.invoke(instance);
        } catch (InvocationTargetException e) {
            actualException = e.getCause().getClass();
        } catch (IllegalAccessException ignored) {
            throw new RuntimeException();
        }

        long testTime = System.currentTimeMillis() - startTime;

        if (actualException != expectedException) {
            if (actualException == NoException.class) {
                return new TestResult(
                        TestStatus.FAILED,
                        notFoundExceptionMessage(expectedException),
                        testTime
                );
            }
            if (expectedException == NoException.class) {
                return new TestResult(
                        TestStatus.FAILED,
                        exceptionThrownMessage(actualException, test),
                        testTime
                );
            }
            return new TestResult(
                    TestStatus.FAILED,
                    unexpectedExceptionMessage(expectedException, actualException),
                    testTime
            );
        }

        return new TestResult(TestStatus.PASSED, testTime);
    }

    private static TestResult runTest(List<Method> beforeMethods, Method test, List<Method> afterMethods) {
        Test testAnnotation = test.getAnnotation(Test.class);
        String ignore = testAnnotation.ignore();

        if (!ignore.isEmpty()) {
            return new TestResult(TestStatus.IGNORED, ignore);
        }

        var testClass = test.getDeclaringClass();
        Constructor<?> constructor = null;
        Object instance;
        try {
            constructor = testClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (NoSuchMethodException e) {
            return new TestResult(TestStatus.FAILED, noConstructorMessage(testClass));
        } catch (InstantiationException e) {
            return new TestResult(TestStatus.FAILED, abstractClassMessage(testClass));
        } catch (InvocationTargetException e) {
            return new TestResult(TestStatus.FAILED, exceptionThrownMessage(e, constructor));
        } catch (IllegalAccessException ignored) {
            throw new RuntimeException();
        }

        TestResult beforeResult = runMethods(beforeMethods, instance);
        if (beforeResult.status != TestStatus.PASSED) {
            return beforeResult;
        }
        TestResult testResult = runTest(test, instance);
        if (testResult.status != TestStatus.PASSED) {
            return testResult;
        }
        TestResult afterResult = runMethods(afterMethods, instance);
        if (afterResult.status != TestStatus.PASSED) {
            return new TestResult(afterResult.status, afterResult.message, testResult.time);
        }
        return testResult;
    }

    private static String noConstructorMessage(Class<?> testClass) {
        return testClass.getName() + " has no constructor without arguments";
    }

    private static String abstractClassMessage(Class<?> testClass) {
        return testClass.getName() + " is abstract";
    }

    private static String exceptionThrownMessage(InvocationTargetException exception, Executable executable) {
        return exception.getCause().getClass().getName() + " at " + executable.getName();
    }

    private static String exceptionThrownMessage(Class<? extends Throwable> exception, Executable executable) {
        return exception.getName() + " at " + executable.getName();
    }

    private static String unexpectedExceptionMessage(Class<? extends Throwable> expected,
                                                     Class<? extends Throwable> actual) {
        return "Unexpected exception type thrown. Expected: " + expected.getName()
                + ", actual: " + actual.getName();
    }

    private static String notFoundExceptionMessage(Class<? extends Throwable> expectedClass) {
        return "Expected " + expectedClass.getName() + " to be thrown, but nothing was thrown";
    }
}