package hash;

import java.util.List;

public class WordRollingHash {

    private final long base = 1_000_003;
    private final long mod = 1_000_000_007;

    public long computeHash(List<String> words) {
        long hash = 0;
        for (String word : words) {
            hash = (hash * base + word.hashCode()) % mod;
        }
        return hash;
    }

    public long recalculateHash(long oldHash,
                                String leftWord,
                                String rightWord,
                                long power) {

        oldHash = (oldHash - (leftWord.hashCode() * power % mod) + mod) % mod;
        oldHash = (oldHash * base + rightWord.hashCode()) % mod;

        return oldHash;
    }

    public long getPower(int windowSize) {
        long power = 1;
        for (int i = 0; i < windowSize - 1; i++) {
            power = (power * base) % mod;
        }
        return power;
    }
}