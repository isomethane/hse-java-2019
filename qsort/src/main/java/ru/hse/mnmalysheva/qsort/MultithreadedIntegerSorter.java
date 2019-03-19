package ru.hse.mnmalysheva.qsort;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;

/** Sorts {@link List} of {@link Integer}s with quick sort algorithm using multiple threads. **/
public class MultithreadedIntegerSorter {
    private static final int STANDARD_SORTING_THRESHOLD = 100;
    private final Random random = new Random();
    private final ForkJoinPool threadPool;

    /** Constructs sorter with number of threads equal to {@link Runtime#availableProcessors()}. **/
    public MultithreadedIntegerSorter() {
        threadPool = new ForkJoinPool();
    }

    /** Constructs sorter with specified number of threads. **/
    public MultithreadedIntegerSorter(int numberOfThreads) {
        threadPool = new ForkJoinPool(numberOfThreads);
    }

    /** Returns number of threads. **/
    public int getNumberOfThreads() {
        return threadPool.getParallelism();
    }

    /** Shuts down sorter thread pool. **/
    public void shutdown() {
        threadPool.shutdown();
    }

    /**
     * Submits sorting task.
     * @param list list to sort
     * @return a {@code Future} representing pending completion of sorting
     */
    public Future<Void> sort(@NotNull List<Integer> list) {
        return threadPool.submit(new QuickSortTask(list));
    }

    private class QuickSortTask extends RecursiveAction {
        List<Integer> list;
        List<Integer> lessPart;
        List<Integer> greaterPart;

        private QuickSortTask(@NotNull List<Integer> list) {
            this.list = list;
        }

        @Override
        protected void compute() {
            if (list.size() < STANDARD_SORTING_THRESHOLD) {
                Collections.sort(list);
            } else {
                partition(random.nextInt(list.size()));
                invokeAll(new QuickSortTask(lessPart), new QuickSortTask(greaterPart));
            }
        }

        private void partition(int pivotIndex) {
            int pivot = list.get(pivotIndex);
            int i = 0;
            int j = list.size() - 1;
            while (i <= j) {
                while (list.get(i) < pivot) {
                    i++;
                }
                while (list.get(j) > pivot) {
                    j--;
                }
                if (i <= j) {
                    Collections.swap(list, i++, j--);
                }
            }
            lessPart = list.subList(0, j + 1);
            greaterPart = list.subList(i, list.size());
        }
    }
}
