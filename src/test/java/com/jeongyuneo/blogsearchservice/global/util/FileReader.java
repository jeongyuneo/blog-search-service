package com.jeongyuneo.blogsearchservice.global.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {

    private FileReader() {
        throw new UnsupportedOperationException();
    }

    public static String readJson(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
