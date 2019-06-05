package ru.hse.mnmalysheva.test6;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class represents parking manager machine.
 * It registers entering and leaving cars and tells whether desired action is possible.
 **/
public class ParkingManager {
    private int numberOfParkingPlaces;
    private AtomicInteger numberOfOccupiedPlaces = new AtomicInteger(0);

    /** Constructs new parking manager with specified number of places. **/
    public ParkingManager(int numberOfParkingPlaces) {
        this.numberOfParkingPlaces = numberOfParkingPlaces;
    }

    /**
     * Returns {@code true} if there are free parking places and registers car.
     * @return {@code true} if there are free parking places.
     */
    public boolean tryEnter() {
        int previousValue = numberOfOccupiedPlaces.getAndUpdate(value ->
                value == numberOfParkingPlaces ? numberOfParkingPlaces : value + 1
        );
        return previousValue != numberOfParkingPlaces;
    }

    /**
     * Registers leaving car.
     * @return {@code false} if all parking places are already free.
     */
    public boolean leave() {
        int previousValue = numberOfOccupiedPlaces.getAndUpdate(value ->
                value == 0 ? 0 : value - 1
        );
        return previousValue != 0;
    }
}
