package ru.hse.mnmalysheva.qsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Prints sorting time for different list sizes and different number of threads. **/
public class MultithreadedSortingAnalyzer {
    private static List<MultithreadedIntegerSorter> sorters = new ArrayList<>();
    private static List<Integer> testList;
    private static final int KILO = 1024;
    private static final int MEGA = KILO * KILO;
    private static final int MAXIMUM_SIZE = 4 * MEGA;

    static {
        int maximumNumberOfThreads = Runtime.getRuntime().availableProcessors();
        for (int i = 2; i <= maximumNumberOfThreads; i++) {
            sorters.add(new MultithreadedIntegerSorter(i));
        }
    }

    /**
     * Compares time of sorting using different number of threads up to {@link Runtime#availableProcessors()}.
     * @param size size of list to sort
     */
    public static void compareSorts(int size) {
        var expected = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            expected.add(i);
        }
        var shuffled = new ArrayList<>(expected);
        Collections.shuffle(shuffled);

        System.out.print("List size: ");
        printListSize(size);
        System.out.print(" elements, ");

        testList = new ArrayList<>(shuffled);

        System.out.print("Standard sorting: ");
        printTime(checkTime(() -> Collections.sort(testList)));
        for (var sorter : sorters) {
            testList = new ArrayList<>(shuffled);
            System.out.print(", ");
            System.out.print(sorter.getNumberOfThreads() + " threads: ");
            printTime(checkTime(() -> {
                try {
                    sorter.sort(testList).get();
                } catch (Exception ignored) {}
            }));
            assert(testList.equals(expected));
        }
        System.out.println();
    }

    /** Prints sorting time for different list sizes and different number of threads. **/
    public static void main(String[] args) {
        for (int size = KILO; size <= MAXIMUM_SIZE; size *= 2) {
            compareSorts(size);
        }
    }

    private static long checkTime(Runnable runnable) {
        var startTime = System.nanoTime();
        runnable.run();
        var endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static void printListSize(long size) {
        if (size >= MEGA) {
            System.out.print(size / MEGA);
            System.out.print("M");
        } else if (size >= KILO) {
            System.out.print(size / KILO);
            System.out.print("K");
        } else {
            System.out.print(size);
        }
    }

    private static void printTime(long nanoSeconds) {
        var milliSeconds = TimeUnit.MILLISECONDS.convert(nanoSeconds, TimeUnit.NANOSECONDS);
        if (milliSeconds < 10) {
            var microSeconds = TimeUnit.MICROSECONDS.convert(nanoSeconds, TimeUnit.NANOSECONDS);
            System.out.print(microSeconds + " mcs");
        } else {
            System.out.print(milliSeconds + " ms");
        }
    }
}
