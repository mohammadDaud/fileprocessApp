package com.file.fileprocess.util;

import java.util.UUID;

public class UuidGeneratorUtil {
    private UuidGeneratorUtil() {
        // Prevent instantiation
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}