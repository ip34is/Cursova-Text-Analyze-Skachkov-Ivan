package service;

public class BenchmarkService {

    public double measure(Runnable task) {
        int runs = 5;
        long total = 0;

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            total += (end - start);
        }

        return (total / runs) / 1_000_000.0;
    }
}