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
        return threadPool.submit(new RecursiveAction() {
            @Override
            protected void compute() {
                var arrayList = new ArrayList<>(list);
                new QuickSortTask(arrayList, 0, arrayList.size()).invoke();

                var iterator = list.listIterator();
                for (var element : arrayList) {
                    iterator.next();
                    iterator.set(element);
                }
            }
        });
    }

    private class QuickSortTask extends RecursiveAction {
        private final ArrayList<Integer> list;
        private final int fromIndex;
        private final int toIndex;

        private QuickSortTask(@NotNull ArrayList<Integer> list, int fromIndex, int toIndex) {
            assert fromIndex >= 0 && toIndex <= list.size();
            this.list = list;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }

        @Override
        protected void compute() {
            if (toIndex - fromIndex < STANDARD_SORTING_THRESHOLD) {
                Collections.sort(list.subList(fromIndex, toIndex));
            } else {
                int bound = partition();
                invokeAll(
                        new QuickSortTask(list, fromIndex, bound),
                        new QuickSortTask(list, bound, toIndex)
                );
            }
        }

        private int partition() {
            int pivot = list.get(fromIndex + random.nextInt(toIndex - fromIndex));
            int i = fromIndex;
            int j = toIndex - 1;
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
            return i;
        }
    }
}
