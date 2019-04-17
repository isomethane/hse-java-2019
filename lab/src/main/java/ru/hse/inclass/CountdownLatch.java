package ru.hse.inclass;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountdownLatch {
    private final Lock lock = new ReentrantLock();
    private final Condition waitCondition = lock.newCondition();
    private final int maximumCount;
    private int currentCount;

    public CountdownLatch(int count) {
        maximumCount = count;
    }

    public void await() {
        lock.lock();
        if (currentCount > 0) {
            try {
                waitCondition.await();
            } catch (InterruptedException ignored) {}
        }
        lock.unlock();
    }

    public void countDown() {}

    public void countUp() {}
}
