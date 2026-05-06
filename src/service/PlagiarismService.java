package service;

import algorithm.SimilarityAlgorithm;
import preprocessing.TextPreprocessor;

public class PlagiarismService {

    private final SimilarityAlgorithm algorithm;
    private final double threshold;

    public PlagiarismService(int windowSize, double threshold) {
        this.algorithm = new algorithm.RabinKarpSimilarity(windowSize);
        this.threshold = threshold;
    }

    public double checkSimilarity(String text1, String text2) {
        String t1 = TextPreprocessor.normalize(text1);
        String t2 = TextPreprocessor.normalize(text2);

        return algorithm.compare(t1, t2);
    }

    public boolean isPlagiarism(String text1, String text2) {
        return checkSimilarity(text1, text2) >= threshold;
    }
}