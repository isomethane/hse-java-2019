package ru.hse.mnmalysheva.test4;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    private static MultithreadedChecksum multi = new MultithreadedChecksum();
    private static SinglethreadedChecksum single = new SinglethreadedChecksum();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("File/directory name should be the only argument");
            return;
        }
        var path = Paths.get(args[0]);

        var startTime = System.nanoTime();
        var result = single.calculateChecksum(path);
        var endTime = System.nanoTime();
        System.out.println("Single thread hash: " + new String(result) + ", Time: " + (endTime - startTime) + " nanos");

        startTime = System.nanoTime();
        result = multi.calculateChecksum(path);
        endTime = System.nanoTime();
        System.out.println("Multithreaded hash: " + new String(result) + ", Time: " + (endTime - startTime) + " nanos");
    }
}
