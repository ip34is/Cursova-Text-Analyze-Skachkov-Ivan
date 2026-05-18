import service.PlagiarismService;
import service.ForkJoinComparisonService;
import service.FixedPoolComparisonService;
import service.ScalingBenchmarkService;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PlagiarismService plagiarismService = new PlagiarismService(10, 0.6);
        ScalingBenchmarkService benchmarkService = new ScalingBenchmarkService();
        int maxThreads = Runtime.getRuntime().availableProcessors();

        String[] datasets = {
                "datasets/dataset_small",
                "datasets/dataset_medium",
                "datasets/dataset_large"
        };

        for (String datasetPath : datasets) {
            System.out.println(" АНАЛІЗ ДАТАСЕТУ: " + datasetPath.toUpperCase());

            System.out.println("Пошук плагіату (Демонстрація ForkJoin)");
            ForkJoinComparisonService demoService = new ForkJoinComparisonService(plagiarismService, maxThreads);

            try {
                List<ForkJoinComparisonService.PlagiarismResult> searchResults = demoService.compareFolder(datasetPath, 0.6);
                if (searchResults.isEmpty()) {
                    System.out.println("Плагіату не знайдено.");
                } else {
                    for (ForkJoinComparisonService.PlagiarismResult res : searchResults) {
                        System.out.printf("Плагіат: [%s] - [%s] | Схожість: %.1f%%%n",
                                res.file1(), res.file2(), res.similarity() * 100);
                    }
                }
            } catch (Exception e) {
                System.out.println("Папка " + datasetPath + " не знайдена.");
                continue;
            }

            System.out.println("\n[1] Запуск бенчмарку: FixedThreadPool");
            var fixedPoolResults = benchmarkService.runScalingTest(maxThreads, threads -> () -> {
                FixedPoolComparisonService fixedService = new FixedPoolComparisonService(plagiarismService, threads);
                fixedService.compareFolder(datasetPath, 0.6);
            });
            benchmarkService.printSpeedup(fixedPoolResults);

            System.out.println("\n[2] Запуск бенчмарку: ForkJoinPool (RecursiveTask)");
            var forkJoinResults = benchmarkService.runScalingTest(maxThreads, threads -> () -> {
                ForkJoinComparisonService forkJoinService = new ForkJoinComparisonService(plagiarismService, threads);
                forkJoinService.compareFolder(datasetPath, 0.6);
            });
            benchmarkService.printSpeedup(forkJoinResults);
            System.out.println();
        }
    }
}