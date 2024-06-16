package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.util.TriConsumer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.ItemCost;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public class TradeFilter {
    @Nullable
    private final Ingredient firstMatcher;
    @Nullable
    private final Ingredient secondMatcher;
    @Nullable
    private final Ingredient outputMatcher;
    private IntRange firstCountMatcher = IntRange.all();
    private IntRange secondCountMatcher = IntRange.all();
    private IntRange outputCountMatcher = IntRange.all();
    private IntRange merchantLevelMatch = IntRange.all();
    private TriConsumer<ItemStack, ItemStack, ItemStack> onMatch = ($1, $2, $3) -> {};

    @Nullable private Set<TradeTypes> tradeTypes;

    @Nullable private Set<VillagerProfession> professions;

    public TradeFilter(@Nullable Ingredient firstMatcher, @Nullable Ingredient secondMatcher, @Nullable Ingredient outputMatcher) {
        this.firstMatcher = firstMatcher;
        this.secondMatcher = secondMatcher;
        this.outputMatcher = outputMatcher;
    }

    public void onMatch(TriConsumer<ItemStack, ItemStack, ItemStack> onMatch) {
        this.onMatch = onMatch;
    }

    public void setMerchantLevelMatcher(IntRange merchantLevelMatch) {
        this.merchantLevelMatch = merchantLevelMatch;
    }

    public void setFirstCountMatcher(IntRange firstCountMatcher) {
        this.firstCountMatcher = firstCountMatcher;
    }

    public void setSecondCountMatcher(IntRange secondCountMatcher) {
        this.secondCountMatcher = secondCountMatcher;
    }

    public void setOutputCountMatcher(IntRange outputCountMatcher) {
        this.outputCountMatcher = outputCountMatcher;
    }

    public void setTradeTypes(Set<TradeTypes> tradeTypes) {
        this.tradeTypes = tradeTypes;
    }

    public void setProfessions(@Nullable Set<VillagerProfession> professions) {
        this.professions = professions;
    }

    public boolean matchMerchantLevel(int level) {
        return merchantLevelMatch.test(level);
    }

    public boolean matchProfession(VillagerProfession profession) {
        return professions == null || professions.contains(profession);
    }

    public boolean matchType(TradeTypes type) {
        return tradeTypes == null || tradeTypes.contains(type);
    }

    private boolean match(@Nullable Ingredient filter, IntRange countFilter, ItemStack itemStack) {
        if (!countFilter.test(itemStack.getCount())) {
            return false;
        }

        if (filter == null) {
            return true;
        }

        return filter.test(itemStack);
    }

    public boolean match(ItemStack costA, ItemStack costB, ItemStack output, TradeTypes type) {
        boolean firstMatch = match(firstMatcher, firstCountMatcher, costA);
        boolean secondMatch = match(secondMatcher, secondCountMatcher, costB);
        boolean outputMatch = match(outputMatcher, outputCountMatcher, output);
        boolean matched = matchType(type) && firstMatch && secondMatch && outputMatch;
        if (matched) {
            onMatch.accept(costA, costB, output);
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
        default boolean matchesTradeFilter(TradeFilter filter) {
            // default behavior given, as this interface is used in a mixin and not every class will implement it.
            return false;
        }
    }
}
