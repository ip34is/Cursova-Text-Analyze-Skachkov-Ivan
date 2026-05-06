package service;

import java.util.*;

public class ScalingBenchmarkService {

    public Map<Integer, Double> runScalingTest(
            int maxThreads,
            java.util.function.IntFunction<Runnable> taskFactory
    ) {
        Map<Integer, Double> results = new LinkedHashMap<>();

        int threads = 1;
        while (threads <= maxThreads) {
            runSingle(threads, taskFactory, results);
            threads *= 2;
        }

        if (!results.containsKey(maxThreads)) {
            runSingle(maxThreads, taskFactory, results);
        }

        return results;
    }

    private void runSingle(int threads,
                           java.util.function.IntFunction<Runnable> factory,
                           Map<Integer, Double> results) {

        Runnable task = factory.apply(threads);

        int runs = 5;
        long total = 0;

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            total += (end - start);
        }

        double ms = (total / runs) / 1_000_000.0;

        results.put(threads, ms);
        System.out.println("Threads " + threads + ": " + ms + " ms");
    }

    public void printSpeedup(Map<Integer, Double> times) {
        double t1 = times.get(1);

        System.out.println("\nSpeedup:");

        for (var e : times.entrySet()) {
            int p = e.getKey();
            double tp = e.getValue();

            double speedup = t1 / tp;
            double efficiency = speedup / p;

            System.out.printf(
                    "p=%d | S=%.2f | E=%.2f%n",
                    p, speedup, efficiency
            );
        }
    }
}