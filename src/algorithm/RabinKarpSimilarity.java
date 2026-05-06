package algorithm;

import hash.WordRollingHash;
import java.util.*;

public class RabinKarpSimilarity implements SimilarityAlgorithm {

    private final WordRollingHash hasher = new WordRollingHash();
    private final int windowSize;

    public RabinKarpSimilarity(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public double compare(String text1, String text2) {
        List<String> words1 = split(text1);
        List<String> words2 = split(text2);

        Set<Long> hashes1 = getHashes(words1);
        Set<Long> hashes2 = getHashes(words2);

        Set<Long> intersection = new HashSet<>(hashes1);
        intersection.retainAll(hashes2);

        Set<Long> union = new HashSet<>(hashes1);
        union.addAll(hashes2);

        return union.isEmpty() ? 0 :
                (double) intersection.size() / union.size();
    }

    private List<String> split(String text) {
        return Arrays.asList(text.split("\\s+"));
    }

    private Set<Long> getHashes(List<String> words) {
        Set<Long> hashes = new HashSet<>();

        if (words.size() < windowSize) return hashes;

        long hash = hasher.computeHash(words.subList(0, windowSize));
        hashes.add(hash);

        long power = hasher.getPower(windowSize);

        for (int i = 1; i <= words.size() - windowSize; i++) {
            hash = hasher.recalculateHash(
                    hash,
                    words.get(i - 1),
                    words.get(i + windowSize - 1),
                    power
            );
            hashes.add(hash);
        }

        return hashes;
    }
}