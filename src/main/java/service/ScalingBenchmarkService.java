package service;

import java.util.*;

public class ScalingBenchmarkService {

    public Map<Integer, Double> runScalingTest(
            int maxThreads,
            java.util.function.IntFunction<Runnable> taskFactory
    ) {
        System.out.println("Прогрів JVM (Warm-up).");
        Runnable warmupTask = taskFactory.apply(maxThreads);
        for (int i = 0; i < 2; i++) {
            warmupTask.run();
        }
        System.out.println("Прогрів завершено.\n");

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

        int runs = 10;
        long total = 0;

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();
            total += (end - start);
        }

        double ms = (total / runs) / 1_000_000.0;

        results.put(threads, ms);
        System.out.println("Відпрацьовано для потоків: " + threads + " (Середній час: " + String.format("%.2f", ms) + " ms)");
    }

    public void printSpeedup(Map<Integer, Double> times) {
        double t1 = times.get(1);

        System.out.println("Результати масштабування багатопотоковості (Scaling Benchmark)");
        System.out.println("!!!===================================================================!!!");
        System.out.printf("%-10s | %-12s | %-20s | %-15s%n", "Потоки", "Час (ms)", "Прискорення (S)", "Ефективність (E)");
        System.out.println("-------------------------------------------------------------------");

        for (var e : times.entrySet()) {
            int p = e.getKey();
            double tp = e.getValue();

            double speedup = t1 / tp;
            double efficiency = speedup / p;

            System.out.printf(
                    "%-10d | %-12.2f | %-20.2fx | %.2f%%%n",
                    p, tp, speedup, efficiency * 100
            );
        }
        System.out.println("!!!===================================================================!!!");
    }
}