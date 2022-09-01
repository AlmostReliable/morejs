package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.util.TriConsumer;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Set;

public class TradeFilter {

    private final IngredientJS firstMatcher;
    private final IngredientJS secondMatcher;
    private final IngredientJS outputMatcher;
    private IntRange merchantLevelMatch = IntRange.all();
    private IntRange firstCountMatcher = IntRange.all();
    private IntRange secondCountMatcher = IntRange.all();
    private IntRange outputCountMatcher = IntRange.all();
    private TriConsumer<ItemStack, ItemStack, ItemStack> onMatch = ($1, $2, $3) -> {};

    @Nullable private Set<TradeTypes> tradeTypes;

    @Nullable private Set<VillagerProfession> professions;

    public TradeFilter(IngredientJS firstMatcher, IngredientJS secondMatcher, IngredientJS outputMatcher) {
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

    public boolean match(ItemStack first, ItemStack second, ItemStack output, TradeTypes type) {
        boolean firstMatch = firstMatcher.testVanilla(first) && firstCountMatcher.test(first.getCount());
        boolean secondMatch = secondMatcher.testVanilla(second) && secondCountMatcher.test(second.getCount());
        boolean outputMatch = outputMatcher.testVanilla(output) && outputCountMatcher.test(output.getCount());
        boolean matched = matchType(type) && firstMatch && secondMatch && outputMatch;
        if (matched) {
            onMatch.accept(first, second, output);
        }
        return matched;
    }

    public boolean match(ItemStack first, ItemStack output, TradeTypes type) {
        return match(first, ItemStack.EMPTY, output, type);
    }

    public interface Filterable {
        @SuppressWarnings("BooleanMethodIsAlwaysInverted") // Fuck of jetbrains
        default boolean matchesTradeFilter(TradeFilter filter) {
            // default behavior given, as this interface is used in a mixin and not every class will implement it.
            return false;
        }
    }
}
