package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileLoader {

    public static String readFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}