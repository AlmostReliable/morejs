package com.almostreliable.morejs.features.villager;

import java.util.function.IntPredicate;

public class LevelRange implements IntPredicate {
    private final int min;
    private final int max;

    public static LevelRange all() {
        return new LevelRange(0, Integer.MAX_VALUE);
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
