package com.jeongyuneo.blogsearchservice.global.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {

    private static final String JSON_FILE_PATH = "src/test/java/resources/kakaoapi/";

    public FileReader() {
        throw new UnsupportedOperationException();
    }

    public static String readJson(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH + fileName)));
    }
}
