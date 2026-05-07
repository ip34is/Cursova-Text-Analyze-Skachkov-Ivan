package hash;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WordRollingHashTest {

    @Test
    void testRecalculateHashMatchesComputeHash() {
        WordRollingHash hasher = new WordRollingHash();
        int windowSize = 3;

        List<String> window1 = Arrays.asList("це", "дуже", "круто");
        List<String> window2 = Arrays.asList("дуже", "круто", "працює");

        long hash1 = hasher.computeHash(window1);

        long expectedHash2 = hasher.computeHash(window2);

        long power = hasher.getPower(windowSize);
        long actualHash2 = hasher.recalculateHash(
                hash1,
                window1.get(0),
                window2.get(2),
                power
        );

        assertEquals(expectedHash2, actualHash2,
                "Помилка в математиці Rolling Hash! Зсув вікна дає інший результат, ніж підрахунок з нуля.");
    }
}