import service.*;

public class Main {

    public static void main(String[] args) {

        String folder = "dataset";

        PlagiarismService core =
                new PlagiarismService(4, 0.6);

        int maxThreads =
                Runtime.getRuntime().availableProcessors();

        ScalingBenchmarkService scaling =
                new ScalingBenchmarkService();

        var results = scaling.runScalingTest(
                maxThreads,
                (threads) -> {

                    if (threads == 1) {
                        var seq = new SequentialComparisonService(core);
                        return () -> seq.compareFolder(folder);
                    } else {
                        var par = new ParallelComparisonService(core, threads);
                        return () -> par.compareFolder(folder);
                    }
                }
        );

        scaling.printSpeedup(results);
    }
}