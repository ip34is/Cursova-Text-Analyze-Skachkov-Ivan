package preprocessing;

public class TextPreprocessor {

    public static String normalize(String text) {
        return text
                .toLowerCase()
                .replaceAll("[^a-zа-я0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}