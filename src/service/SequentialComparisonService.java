package service;

import util.FileLoader;
import java.nio.file.*;
import java.util.*;

public class SequentialComparisonService {

    private final PlagiarismService service;

    public SequentialComparisonService(PlagiarismService service) {
        this.service = service;
    }

    public void compareFolder(String folderPath) {
        List<Path> files = load(folderPath);

        for (int i = 0; i < files.size(); i++) {
            for (int j = i + 1; j < files.size(); j++) {

                String t1 = FileLoader.readFile(files.get(i));
                String t2 = FileLoader.readFile(files.get(j));

                service.checkSimilarity(t1, t2);
            }
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