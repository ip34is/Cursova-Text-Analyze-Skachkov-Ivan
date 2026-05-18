package service;

import util.FileLoader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ForkJoinComparisonService {
    private final PlagiarismService service;
    private final int threads;

    public record PlagiarismResult(String file1, String file2, double similarity) {}

    public ForkJoinComparisonService(PlagiarismService service, int threads) {
        this.service = service;
        this.threads = threads;
    }

    public List<PlagiarismResult> compareFolder(String folderPath, double threshold) {
        List<Path> files = load(folderPath);
        ForkJoinPool pool = new ForkJoinPool(threads);

        CompareTask mainTask = new CompareTask(files, 0, files.size(), service, threshold);
        List<PlagiarismResult> results = pool.invoke(mainTask);

        pool.shutdown();
        return results;
    }

    private List<Path> load(String path) {
        try {
            return Files.list(Path.of(path)).filter(p -> p.toString().endsWith(".txt")).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class CompareTask extends RecursiveTask<List<PlagiarismResult>> {
        private static final int THRESHOLD_SIZE = 2; // Базовий випадок
        private final List<Path> files;
        private final int start;
        private final int end;
        private final PlagiarismService service;
        private final double threshold;

        public CompareTask(List<Path> files, int start, int end, PlagiarismService service, double threshold) {
            this.files = files;
            this.start = start;
            this.end = end;
            this.service = service;
            this.threshold = threshold;
        }

        @Override
        protected List<PlagiarismResult> compute() {
            List<PlagiarismResult> localResults = new ArrayList<>();

            if (end - start <= THRESHOLD_SIZE) {
                for (int i = start; i < end; i++) {
                    for (int j = i + 1; j < files.size(); j++) {
                        Path f1 = files.get(i);
                        Path f2 = files.get(j);
                        String t1 = FileLoader.readFile(f1);
                        String t2 = FileLoader.readFile(f2);
                        double similarity = service.checkSimilarity(t1, t2);
                        if (similarity >= threshold) {
                            localResults.add(new PlagiarismResult(f1.getFileName().toString(), f2.getFileName().toString(), similarity));
                        }
                    }
                }
                return localResults;
            } else {
                int mid = start + (end - start) / 2;
                CompareTask leftTask = new CompareTask(files, start, mid, service, threshold);
                CompareTask rightTask = new CompareTask(files, mid, end, service, threshold);

                leftTask.fork();
                List<PlagiarismResult> rightResult = rightTask.compute();
                List<PlagiarismResult> leftResult = leftTask.join();

                localResults.addAll(leftResult);
                localResults.addAll(rightResult);
                return localResults;
            }
        }
    }
}