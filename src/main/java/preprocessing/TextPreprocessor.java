package preprocessing;

public class TextPreprocessor {

    public static String normalize(String text) {
        return text
                .toLowerCase()
                .replaceAll("<[^>]*>", " ")
                .replaceAll("[-—–]", " ")
                .replaceAll("[^a-zа-яіїєґ0-9\\s']", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}