package com.almostreliable.morejs.features.villager;

import java.util.function.IntPredicate;

public class IntRange implements IntPredicate {
    private final int min;
    private final int max;

    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public IntRange(int level) {
        this(level, level);
    }

    public static IntRange all() {
        return new IntRange(0, Integer.MAX_VALUE);
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
