package com.almostreliable.morejs.util;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static <T> Optional<T> cast(Object o, Class<T> type) {
        if (type.isInstance(o)) {
            return Optional.of(type.cast(o));
        }

        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T nullableCast(@Nullable Object o) {
        if (o == null) {
            return null;
        }
        return (T) o;
    }

    public static String format(String string) {
        int index = string.indexOf(":");
        String sanitized = string.substring(index + 1).replaceAll("[#:._]", " ").trim();
        return Arrays
                .stream(sanitized.split(" "))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static List<Object> asList(Object o) {
        if (o instanceof List) {
            return Utils.cast(o);
        }

        if (o instanceof Object[]) {
            return new ArrayList<>(Arrays.asList((Object[]) o));
        }

        return new ArrayList<>(Collections.singletonList(o));
    }
}
