package preprocessing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextPreprocessorTest {

    @Test
    void testNormalizeRemovesPunctuation() {
        String input = "Привіт, світ! Як справи? Це: тест.";
        String expected = "привіт світ як справи це тест";
        assertEquals(expected, TextPreprocessor.normalize(input), "Пунктуація видаляється неправильно!");
    }

    @Test
    void testNormalizeConvertsToLowerCase() {
        String input = "ВЕЛИКІ та МаЛі ЛіТеРи";
        String expected = "великі та малі літери";
        assertEquals(expected, TextPreprocessor.normalize(input), "Регістр не зводиться до нижнього!");
    }

    @Test
    void testNormalizeHandlesMultipleSpacesAndNewlines() {
        String input = "Тут   багато \n\n пробілів \t і \r табів";
        String expected = "тут багато пробілів і табів";
        assertEquals(expected, TextPreprocessor.normalize(input), "Зайві пробіли або переноси рядків не нормалізуються!");
    }

    @Test
    void testNormalizeEmptyString() {
        String input = "   , , , !!!   ";
        String expected = "";
        assertEquals(expected, TextPreprocessor.normalize(input), "Рядок, що складається лише з пунктуації, має ставати порожнім!");
    }

    @Test
    void testNormalizeHandlesHtmlApostrophesAndHyphens() {
        String input = "Тільки шматок м'яса...<br /> Квітень-вересень 1846!";
        String expected = "тільки шматок м'яса квітень вересень 1846";

        assertEquals(expected, TextPreprocessor.normalize(input),
                "HTML-теги, апострофи або дефіси обробляються неправильно!");
    }
}