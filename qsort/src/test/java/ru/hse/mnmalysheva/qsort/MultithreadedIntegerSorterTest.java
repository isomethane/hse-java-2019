package ru.hse.mnmalysheva.qsort;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class MultithreadedIntegerSorterTest {
    private MultithreadedIntegerSorter sorter;
    private static List<Integer> expected = new ArrayList<>();
    private static List<Integer> shuffled;
    private static List<Integer> testList;
    private static final int LIST_SIZE = 1000000;

    @BeforeAll
    static void init() {
        for (int i = 0; i < LIST_SIZE; i++) {
            expected.add(i);
        }
        shuffled = new ArrayList<>(expected);
        Collections.shuffle(shuffled, new Random(30));
    }

    @BeforeEach
    void initList() {
        testList = new ArrayList<>(shuffled);
    }

    @AfterEach
    void shutdownSorter() {
        sorter.shutdown();
    }

    @Test
    void sortWithOneThread() throws ExecutionException, InterruptedException {
        sorter = new MultithreadedIntegerSorter(1);
        sorter.sort(testList).get();
        assertEquals(expected, testList);
    }

    @Test
    void sortWithMultipleThreads() throws ExecutionException, InterruptedException {
        sorter = new MultithreadedIntegerSorter(5);
        sorter.sort(testList).get();
        assertEquals(expected, testList);
    }

    @Test
    void sortWithDefaultNumberOfThreads() throws ExecutionException, InterruptedException {
        sorter = new MultithreadedIntegerSorter();
        assertEquals(Runtime.getRuntime().availableProcessors(), sorter.getNumberOfThreads());
        sorter.sort(testList).get();
        assertEquals(expected, testList);
    }

    @Test
    void sortManyTasks() throws ExecutionException, InterruptedException {
        sorter = new MultithreadedIntegerSorter();

        var secondTestList = new ArrayList<>(shuffled);
        Collections.shuffle(secondTestList, new Random(50));

        var completedFirst = sorter.sort(testList);
        var completedSecond = sorter.sort(secondTestList);

        completedSecond.get();
        completedFirst.get();

        assertEquals(expected, testList);
        assertEquals(expected, secondTestList);
    }

    @Test
    void sortLinkedList() throws ExecutionException, InterruptedException {
        sorter = new MultithreadedIntegerSorter();
        testList = new LinkedList<>(shuffled);
        sorter.sort(testList).get();
        assertEquals(expected, testList);
    }

    @Test
    void sortSubLists() throws ExecutionException, InterruptedException {
        sorter = new MultithreadedIntegerSorter();
        testList = new LinkedList<>();
        for (int i = 999; i >= 0; i--) {
            testList.add(i);
        }
        var completedFirst = sorter.sort(testList.subList(0, 500));
        var completedSecond = sorter.sort(testList.subList(500, 750));
        var completedThird = sorter.sort(testList.subList(750, 1000));

        completedFirst.get();
        completedSecond.get();
        completedThird.get();

        var expected = new LinkedList<>();
        for (int i = 500; i < 1000; i++) {
            expected.add(i);
        }
        for (int i = 250; i < 500; i++) {
            expected.add(i);
        }
        for (int i = 0; i < 250; i++) {
            expected.add(i);
        }
        assertEquals(expected, testList);
    }
}