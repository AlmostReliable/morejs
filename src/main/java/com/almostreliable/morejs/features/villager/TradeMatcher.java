package com.almostreliable.morejs.features.villager;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.ItemCost;

import javax.annotation.Nullable;
import java.util.Optional;

public record TradeMatcher(TradeFilter filter, OnMatch onMatch) {

    public boolean matchMerchantLevel(int level) {
        if (filter.level().isEmpty()) {
            return true;
        }

        return filter.level().get().test(level);
    }

    public boolean matchProfession(VillagerProfession profession) {
        if (filter.professions().isEmpty()) {
            return true;
        }

        var holder = BuiltInRegistries.VILLAGER_PROFESSION.wrapAsHolder(profession);
        return filter.professions().get().contains(holder);
    }

    public boolean matchType(TradeTypes type) {
        if (filter.types().isEmpty()) {
            return true;
        }

        return filter.types().get().contains(type);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean match(Optional<Ingredient> filter, Optional<IntRange> countFilter, ItemStack itemStack) {
        var matchesCount = countFilter.isEmpty() || countFilter.get().test(itemStack.getCount());
        var matchesFilter = filter.isEmpty() || filter.get().test(itemStack);
        return matchesCount && matchesFilter;
    }

    public boolean match(ItemStack costA, ItemStack costB, ItemStack output, TradeTypes type) {
        boolean firstMatch = match(filter.first(), filter.firstCount(), costA);
        boolean secondMatch = match(filter.second(), filter.secondCount(), costB);
        boolean outputMatch = match(filter.output(), filter.outputCount(), output);
        boolean matched = matchType(type) && firstMatch && secondMatch && outputMatch;
        if (matched) {
            onMatch.notify(costA, costB, output);
        }

        return matched;
    }

    public boolean match(ItemStack first, ItemStack output, TradeTypes type) {
        return match(first, ItemStack.EMPTY, output, type);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public boolean match(ItemCost costA, Optional<ItemCost> costB, ItemStack output, TradeTypes type) {
        return match(costA.itemStack(), costB.map(ItemCost::itemStack).orElse(ItemStack.EMPTY), output, type);
    }

    public boolean match(ItemStack costA, ItemCost costB, ItemStack output, TradeTypes type) {
        return match(costA, costB.itemStack(), output, type);
    }

    public boolean match(ItemCost costA, ItemStack output, TradeTypes type) {
        return match(costA, Optional.empty(), output, type);
    }

    public interface Filterable {
        @SuppressWarnings("BooleanMethodIsAlwaysInverted") // Fuck of jetbrains
        default boolean matchesTradeFilter(TradeMatcher filter) {
            // default behavior given, as this interface is used in a mixin and not every class will implement it.
            return false;
        }
    }

    public interface OnMatch {
        void notify(ItemStack first, @Nullable ItemStack second, ItemStack output);
    }
}
