package ru.hse.inclass;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CountdownLatch {
    private final Lock lock = new ReentrantLock();
    private final Condition reachedNull = lock.newCondition();
    private final Condition becameNotNUll = lock.newCondition();
    private int count;

    public CountdownLatch(int count) {
        this.count = count;
    }

    public void await() throws InterruptedException {
        lock.lock();
        while (count > 0) {
            reachedNull.await();
        }
        lock.unlock();
    }

    public void countDown() throws InterruptedException {
        lock.lock();
        while (count == 0) {
            becameNotNUll.await();
        }
        count--;
        if (count == 0) {
            reachedNull.signalAll();
        }
        lock.unlock();
    }

    public void countUp() {
        lock.lock();
        count++;
        becameNotNUll.signalAll();
        lock.unlock();
    }
}
