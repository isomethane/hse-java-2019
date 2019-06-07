package ru.hse.mnmalysheva.test6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ParkingManagerTest {
    private static final int NUMBER_OF_PARKING_PLACES = 1_000_000;
    private ParkingManager parkingManager;

    @BeforeEach
    void init() {
        parkingManager = new ParkingManager(NUMBER_OF_PARKING_PLACES);
    }

    @Test
    void basicTestSingleThreaded() {
        var parkingManager = new ParkingManager(3);
        assertTrue(parkingManager.tryEnter());  // 1
        assertTrue(parkingManager.tryEnter());  // 2
        assertTrue(parkingManager.leave());     // 1
        assertTrue(parkingManager.tryEnter());  // 2
        assertTrue(parkingManager.tryEnter());  // 3
        assertFalse(parkingManager.tryEnter()); // 3!
        assertFalse(parkingManager.tryEnter()); // 3!
        assertTrue(parkingManager.leave());     // 2
        assertTrue(parkingManager.leave());     // 1
        assertTrue(parkingManager.leave());     // 0
        assertFalse(parkingManager.leave());    // 0!
        assertFalse(parkingManager.leave());    // 0!
        assertTrue(parkingManager.tryEnter());  // 1
        assertTrue(parkingManager.tryEnter());  // 2
        assertTrue(parkingManager.tryEnter());  // 3
        assertFalse(parkingManager.tryEnter()); // 3!
    }

    @RepeatedTest(20)
    void testEnter() throws InterruptedException {
        var numberOfSuccessfulEnters = new AtomicInteger(0);

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 100; i++) {
            var thread = new Thread(() -> {
                boolean isFull = false;
                for (int j = 0; j < 100_000; j++) {
                    boolean isSuccessful = parkingManager.tryEnter();
                    if (isSuccessful) {
                        assertFalse(isFull);
                        numberOfSuccessfulEnters.incrementAndGet();
                    } else {
                        isFull = true;
                    }
                }
            });
            threads.add(thread);
        }

        for (var thread : threads) {
            thread.start();
        }
        for (var thread : threads) {
            thread.join();
        }
        assertEquals(NUMBER_OF_PARKING_PLACES, numberOfSuccessfulEnters.get());
    }

    @RepeatedTest(20)
    void testEnterExactlyNumberOfParkingPlaces() throws InterruptedException {
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 100; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j < 10_000; j++) {
                    assertTrue(parkingManager.tryEnter());
                }
            });
            threads.add(thread);
        }

        for (var thread : threads) {
            thread.start();
        }
        for (var thread : threads) {
            thread.join();
        }
        assertFalse(parkingManager.tryEnter());
    }

    @RepeatedTest(20)
    void testLeave() throws InterruptedException {
        fillParking();

        var numberOfSuccessfulLeaves = new AtomicInteger(0);

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 100; i++) {
            var thread = new Thread(() -> {
                boolean isEmpty = false;
                for (int j = 0; j < 100_000; j++) {
                    boolean isSuccessful = parkingManager.leave();
                    if (isSuccessful) {
                        assertFalse(isEmpty);
                        numberOfSuccessfulLeaves.incrementAndGet();
                    } else {
                        isEmpty = true;
                    }
                }
            });
            threads.add(thread);
        }

        for (var thread : threads) {
            thread.start();
        }
        for (var thread : threads) {
            thread.join();
        }
        assertEquals(NUMBER_OF_PARKING_PLACES, numberOfSuccessfulLeaves.get());
    }

    @RepeatedTest(20)
    void testLeaveExactlyNumberOfParkingPlaces() throws InterruptedException {
        fillParking();

        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 100; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j < 10_000; j++) {
                    assertTrue(parkingManager.leave());
                }
            });
            threads.add(thread);
        }

        for (var thread : threads) {
            thread.start();
        }
        for (var thread : threads) {
            thread.join();
        }
        assertFalse(parkingManager.leave());
    }

    // This test does not check all guarantees, but repeated many times it may detect race conditions.
    @RepeatedTest(20)
    void testEnterAndLeave() throws InterruptedException {
        int numberOfPlaces = 5;
        parkingManager = new ParkingManager(numberOfPlaces);

        var threads = new ArrayList<Thread>();
        var numberOfCars = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            var thread = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    if (parkingManager.tryEnter()) {
                        numberOfCars.incrementAndGet();
                    }
                }
            });
            threads.add(thread);
            thread = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    if (parkingManager.leave()) {
                        numberOfCars.decrementAndGet();
                    }
                }
            });
            threads.add(thread);
        }
        for (var thread : threads) {
            thread.start();
        }
        for (var thread : threads) {
            thread.join();
        }
        assertTrue(numberOfCars.get() >= 0);
        assertTrue(numberOfCars.get() <= numberOfPlaces);
    }

    private void fillParking() {
        for (int i = 0; i < NUMBER_OF_PARKING_PLACES; i++) {
            assertTrue(parkingManager.tryEnter());
        }
        assertFalse(parkingManager.tryEnter());
    }

    private static class ActionResult {
        int delta;
        boolean isSuccessful;
    }
}