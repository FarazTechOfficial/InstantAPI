package com.instantapi.generator;

public class NameUtil {
    public static String className(String name) {
        String[] parts = name.trim().replace("-", " ").replace("_", " ").split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) result.append(part.substring(1));
            }
        }
        return result.toString();
    }

    public static String fieldName(String name) {
        String value = className(name);
        if (value.isEmpty()) return "api";
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }
}
