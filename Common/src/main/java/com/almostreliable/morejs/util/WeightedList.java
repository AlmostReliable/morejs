package com.almostreliable.morejs.util;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class WeightedList<T> {
    private static final Random RANDOM = new Random();
    private final List<Entry<T>> entries;
    private final int totalWeight;

    private WeightedList(List<Entry<T>> entries) {
        Preconditions.checkNotNull(entries, "entries are null");
        Preconditions.checkArgument(!entries.isEmpty(), "entries cannot be empty");
        this.entries = entries;
        this.totalWeight = entries.stream().mapToInt(Entry::weight).sum();
    }

    public T roll() {
        return roll(RANDOM);
    }

    public T roll(Random random) {
        int i = random.nextInt(totalWeight);
        for (Entry<T> e : entries) {
            i -= e.weight;
            if (i < 0) {
                return e.value;
            }
        }
        throw new IllegalStateException("Rolled past end of list");
    }

    public <T2> WeightedList<T2> map(Function<T, T2> mapper) {
        List<Entry<T2>> newEntries = new ArrayList<>(entries.size());
        for (Entry<T> entry : entries) {
            T2 newValue = mapper.apply(entry.value);
            if (newValue == null) {
                continue;
            }
            newEntries.add(new Entry<>(entry.weight, newValue));
        }
        return new WeightedList<>(newEntries);
    }

    public static class Builder<T> {
        private final List<Entry<T>> entries = new ArrayList<>();

        public Builder<T> add(int weight, T value) {
            entries.add(new Entry<>(weight, value));
            return this;
        }

        public WeightedList<T> build() {
            return new WeightedList<>(entries);
        }
    }

    private record Entry<T>(int weight, T value) {}
}
