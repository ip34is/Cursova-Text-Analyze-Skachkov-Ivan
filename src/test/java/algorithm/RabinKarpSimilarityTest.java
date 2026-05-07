package algorithm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RabinKarpSimilarityTest {

    @Test
    void testCompareIdenticalTexts() {
        RabinKarpSimilarity algorithm = new RabinKarpSimilarity(3);
        String text = "це дуже крутий і швидкий алгоритм";

        double similarity = algorithm.compare(text, text);

        assertEquals(1.0, similarity, 0.001, "Однакові тексти повинні мати 100% схожості!");
    }

    @Test
    void testCompareCompletelyDifferentTexts() {
        RabinKarpSimilarity algorithm = new RabinKarpSimilarity(3);
        String text1 = "сьогодні чудова погода для прогулянки";
        String text2 = "зовсім інший текст про програмування";

        double similarity = algorithm.compare(text1, text2);

        assertEquals(0.0, similarity, 0.001, "Абсолютно різні тексти повинні мати 0% схожості!");
    }

    @Test
    void testComparePartialSimilarity() {
        RabinKarpSimilarity algorithm = new RabinKarpSimilarity(3);

        String text1 = "я думаю що це дуже крутий алгоритм для роботи";
        String text2 = "всі знають що це дуже крутий алгоритм для навчання";

        double similarity = algorithm.compare(text1, text2);

        assertTrue(similarity > 0.0 && similarity < 1.0,
                "Частково схожі тексти повинні мати результат між 0.0 та 1.0. Отримано: " + similarity);
    }

    @Test
    void testCompareTextsShorterThanWindowSize() {
        RabinKarpSimilarity algorithm = new RabinKarpSimilarity(4);
        String text1 = "короткий текст";
        String text2 = "короткий текст";

        double similarity = algorithm.compare(text1, text2);

        assertEquals(0.0, similarity, 0.001, "Для текстів, коротших за розмір вікна, схожість має бути 0.0");
    }
}