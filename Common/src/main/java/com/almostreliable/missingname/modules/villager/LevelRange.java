package com.almostreliable.missingname.modules.villager;

import dev.latvian.mods.kubejs.util.UtilsJS;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.IntPredicate;

public class LevelRange implements IntPredicate {
    private final int min;
    private final int max;

    public static LevelRange all() {
        return new LevelRange(0, Integer.MAX_VALUE);
    }

    public static LevelRange of(@Nullable Object o) {
        if (o instanceof Number number) {
            return new LevelRange(number.intValue());
        }

        if (o instanceof List<?> list) {
            return switch (list.size()) {
                case 0 -> all();
                case 1 -> of(list.get(0));
                default -> new LevelRange(UtilsJS.parseInt(list.get(0), 1), UtilsJS.parseInt(list.get(0), 5));
            };
        }

        return all();
    }

    public LevelRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public LevelRange(int level) {
        this(level, level);
    }

    @Override
    public boolean test(int value) {
        return min <= value && value <= max;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }
}
