package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.util.Utils;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;
import java.util.function.Predicate;

public record PotionBrewingFilter(Optional<Ingredient> ingredient, Optional<HolderSet<Potion>> input,
                                  Optional<HolderSet<Potion>> output) implements Predicate<PotionBrewing.Mix<Potion>> {

    @Override
    public boolean test(PotionBrewing.Mix<Potion> potionMix) {
        if (input.isPresent() && input.filter(input -> input.contains(potionMix.from())).isEmpty()) {
            return false;
        }

        if (output.isPresent() && output.filter(output -> output.contains(potionMix.to())).isEmpty()) {
            return false;
        }

        return ingredient().isEmpty() || ingredient
                .filter(ingredient -> Utils.matchesIngredient(ingredient, potionMix.ingredient()))
                .isPresent();
    }
}
