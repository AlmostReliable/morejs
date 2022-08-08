package com.almostreliable.morejs.util;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class Utils {
    public static <T> Optional<T> cast(Object o, Class<T> type) {
        if (type.isInstance(o)) {
            return Optional.of(type.cast(o));
        }

        return Optional.empty();
    }

    public static String format(String string) {
        int index = string.indexOf(":");
        String sanitized = string.substring(index + 1).replaceAll("[#:._]", " ").trim();
        return Arrays
                .stream(sanitized.split(" "))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));
    }
}
