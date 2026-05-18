package service;

import util.FileLoader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class FixedPoolComparisonService {
    private final PlagiarismService service;
    private final int threads;

    public record PlagiarismResult(String file1, String file2, double similarity) {}

    public FixedPoolComparisonService(PlagiarismService service, int threads) {
        this.service = service;
        this.threads = threads;
    }

    public List<PlagiarismResult> compareFolder(String folderPath, double threshold) {
        List<Path> files = load(folderPath);
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        List<PlagiarismResult> results = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < files.size(); i++) {
            for (int j = i + 1; j < files.size(); j++) {
                Path f1 = files.get(i);
                Path f2 = files.get(j);
                pool.submit(() -> {
                    String t1 = FileLoader.readFile(f1);
                    String t2 = FileLoader.readFile(f2);
                    double similarity = service.checkSimilarity(t1, t2);
                    if (similarity >= threshold) {
                        results.add(new PlagiarismResult(f1.getFileName().toString(), f2.getFileName().toString(), similarity));
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
        return results;
    }

    private List<Path> load(String path) {
        try {
            return Files.list(Path.of(path)).filter(p -> p.toString().endsWith(".txt")).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}