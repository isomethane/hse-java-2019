package ru.hse.inclass;

import org.junit.jupiter.api.Test;

class CountdownLatchTest {
    class Worker implements Runnable {
        private final CountdownLatch start;
        private final CountdownLatch done;
        private final int number;

        Worker(CountdownLatch start, CountdownLatch done, int number) {
            this.start = start;
            this.done = done;
            this.number = number;
        }

        @Override
        public void run() {
            try {
                start.await();
                System.out.println("Worker " + number + " is working");
                done.countDown();
            } catch (InterruptedException ignored) {}
        }
    }

    class Doer {

    }

    @Test
    void testAwait() {

    }


}