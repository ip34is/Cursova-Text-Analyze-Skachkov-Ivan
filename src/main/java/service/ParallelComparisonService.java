package service;

import util.FileLoader;

import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ParallelComparisonService {

    private final PlagiarismService service;
    private final int threads;

    public ParallelComparisonService(PlagiarismService service, int threads) {
        this.service = service;
        this.threads = threads;
    }

    public void compareFolder(String folderPath, boolean printResults) {
        List<Path> files = load(folderPath);

        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < files.size(); i++) {
            for (int j = i + 1; j < files.size(); j++) {

                Path f1 = files.get(i);
                Path f2 = files.get(j);

                pool.submit(() -> {
                    String t1 = FileLoader.readFile(f1);
                    String t2 = FileLoader.readFile(f2);

                    double similarity = service.checkSimilarity(t1, t2);

                    if (printResults && similarity >= 0.6) {
                        System.out.printf("Плагіат: [%s] - [%s] \nСхожість: %.1f%%%n",
                                f1.getFileName(), f2.getFileName(), similarity * 100);
                    }
                });
            }
        }

        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Path> load(String path) {
        try {
            return Files.list(Path.of(path))
                    .filter(p -> p.toString().endsWith(".txt"))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}