import service.PlagiarismService;
import service.ParallelComparisonService;
import service.ScalingBenchmarkService;

public class Main {
    public static void main(String[] args) {
        PlagiarismService plagiarismService = new PlagiarismService(5, 0.6);
        ScalingBenchmarkService benchmarkService = new ScalingBenchmarkService();
        int maxThreads = Runtime.getRuntime().availableProcessors();

        String[] datasets = {
                "datasets/dataset_small",
                "datasets/dataset_medium",
                "datasets/dataset_large"
        };

        for (String datasetPath : datasets) {
            System.out.println(" АНАЛІЗ ДАТАСЕТУ: " + datasetPath.toUpperCase());

            System.out.println("Пошук плагіату (Демонстрація)");
            ParallelComparisonService demoService = new ParallelComparisonService(plagiarismService, maxThreads);

            try {
                demoService.compareFolder(datasetPath, true);
            } catch (Exception e) {
                System.out.println("Папка " + datasetPath + " не знайдена.");
                continue;
            }

            System.out.println("\nЗапуск бенчмарку продуктивності");
            var results = benchmarkService.runScalingTest(maxThreads, threads -> () -> {
                ParallelComparisonService taskService = new ParallelComparisonService(plagiarismService, threads);
                taskService.compareFolder(datasetPath, false);
            });

            benchmarkService.printSpeedup(results);
        }
    }
}