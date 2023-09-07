package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.potion.PotionBrewingAccessor;
import com.almostreliable.morejs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public abstract class PotionBrewingRegisterEvent extends EventJS {

    protected void validate(Ingredient topInput, Ingredient bottomInput, ItemStack output) {
        Preconditions.checkArgument(topInput.getItems().length > 0, "Top input must have at least one item");
        Preconditions.checkArgument(bottomInput.getItems().length > 0, "Bottom input must have at least one item");
        Preconditions.checkArgument(!output.isEmpty(), "Output must not be empty");
    }

    protected void validateSimple(Potion from, Ingredient ingredient, Potion to) {
        Preconditions.checkNotNull(from, "Input potion must not be null");
        Preconditions.checkNotNull(ingredient, "Ingredient must not be null");
        Preconditions.checkNotNull(to, "Output potion must not be null");
        Preconditions.checkArgument(ingredient.getItems().length > 0, "Ingredient must have at least one item");
    }

    public abstract void addCustomBrewing(Ingredient topInput, Ingredient bottomInput, ItemStack output);

    public abstract void addPotionBrewing(Ingredient ingredient, Potion input, Potion output);

    public void addPotionBrewing(Ingredient ingredient, Potion output) {
        addPotionBrewing(ingredient, Potions.WATER, output);
    }

    public void removeByPotion(@Nullable Potion input, @Nullable Ingredient ingredient, @Nullable Potion output) {
        PotionBrewingAccessor.getMixes().removeIf(mix -> {
            boolean matchesInput = input == null || getInputPotionFromMix(mix) == input;
            boolean matchesIngredient = ingredient == null || Utils.matchesIngredient(ingredient, mix.ingredient);
            boolean matchesOutput = output == null || getOutputPotionFromMix(mix) == output;
            boolean matches = matchesInput && matchesIngredient && matchesOutput;
            if (matches) {
                ConsoleJS.STARTUP.info(
                        "Removed potion brewing recipe: " + BuiltInRegistries.POTION.getKey(getInputPotionFromMix(mix)) + " + " +
                        StringUtils.abbreviate(mix.ingredient.toJson().toString(), 64) + " -> " +
                        BuiltInRegistries.POTION.getKey(getOutputPotionFromMix(mix)));
            }
            return matches;
        });
    }

    protected abstract Potion getInputPotionFromMix(PotionBrewing.Mix<Potion> mix);

    protected abstract Potion getOutputPotionFromMix(PotionBrewing.Mix<Potion> mix);
}
