package ru.hse.mnmalysheva.test4;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MultithreadedChecksum extends AbstractChecksum {
    private ForkJoinPool pool = new ForkJoinPool();

    @Override
    public @NotNull byte[] directoryChecksum(@NotNull Path path) throws IOException {
        try {
            return pool.submit(new ChecksumTask(path)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class ChecksumTask extends RecursiveTask<byte[]> {
        private Path path;

        private ChecksumTask(@NotNull Path path) {
            this.path = path;
        }

        @Override
        protected byte[] compute() {
            try {
                var file = path.toFile();
                var messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(file.getName().getBytes());
                var forkedTasks = new ArrayList<ChecksumTask>();

                var files = Objects.requireNonNull(file.listFiles());
                Arrays.sort(files);

                for (var f : files) {
                    var task = new ChecksumTask(f.toPath());
                    task.fork();
                    forkedTasks.add(task);
                }
                for (var task : forkedTasks) {
                    task.join();
                    messageDigest.update(task.get());
                }
                return messageDigest.digest();
            } catch (Exception e) { // !!!!
                throw new RuntimeException(e);
            }
        }
    }
}
