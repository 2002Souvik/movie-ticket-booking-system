package util;

import java.util.UUID;

public class IdGenerator {
    public static String generateId(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}