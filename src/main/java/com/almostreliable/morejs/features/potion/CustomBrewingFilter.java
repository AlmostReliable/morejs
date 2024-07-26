package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.util.Utils;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;

import java.util.Optional;
import java.util.function.Predicate;

public record CustomBrewingFilter(Optional<Ingredient> ingredient, Optional<Ingredient> input,
                                  Optional<Ingredient> output) implements Predicate<BrewingRecipe> {

    @Override
    public boolean test(BrewingRecipe brewingRecipe) {
        if (input.filter(input -> Utils.matchesIngredient(input, brewingRecipe.getInput())).isEmpty()) {
            return false;
        }

        if (output.filter(output -> output.test(brewingRecipe.getOutput())).isEmpty()) {
            return false;
        }

        return ingredient
                .filter(ingredient -> Utils.matchesIngredient(ingredient, brewingRecipe.getIngredient()))
                .isPresent();
    }
}
