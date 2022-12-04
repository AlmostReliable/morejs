package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.potion.PotionBrewingAccessor;
import com.almostreliable.morejs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.Registry;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public abstract class PotionBrewingRegisterEvent extends EventJS {

    protected void validate(IngredientJS topInput, IngredientJS bottomInput, ItemStackJS output) {
        Preconditions.checkArgument(!topInput.isEmpty(), "Top input must have at least one item");
        Preconditions.checkArgument(!bottomInput.isEmpty(), "Bottom input must have at least one item");
        Preconditions.checkArgument(!output.isEmpty(), "Output must not be empty");
    }

    protected void validateSimple(Potion from, IngredientJS ingredient, Potion to) {
        Preconditions.checkNotNull(from, "Input potion must not be null");
        Preconditions.checkNotNull(ingredient, "Ingredient must not be null");
        Preconditions.checkNotNull(to, "Output potion must not be null");
        Preconditions.checkArgument(!ingredient.isEmpty(), "Ingredient must have at least one item");
    }

    public abstract void addCustomBrewing(IngredientJS topInput, IngredientJS bottomInput, ItemStackJS output);

    public abstract void addPotionBrewing(IngredientJS ingredient, Potion input, Potion output);

    public void addPotionBrewing(IngredientJS ingredient, Potion output) {
        addPotionBrewing(ingredient, Potions.WATER, output);
    }

    public void removeByPotion(@Nullable Potion input, @Nullable IngredientJS ingredient, @Nullable Potion output) {
        Ingredient vanillaIngredient = ingredient == null ? null : ingredient.createVanillaIngredient();
        PotionBrewingAccessor.getMixes().removeIf(mix -> {
            boolean matchesInput = input == null || getInputPotionFromMix(mix) == input;
            boolean matchesIngredient = vanillaIngredient == null || Utils.matchesIngredient(vanillaIngredient, mix.ingredient);
            boolean matchesOutput = output == null || getOutputPotionFromMix(mix) == output;
            boolean matches = matchesInput && matchesIngredient && matchesOutput;
            if (matches) {
                ConsoleJS.STARTUP.info(
                        "Removed potion brewing recipe: " + Registry.POTION.getKey(getInputPotionFromMix(mix)) + " + " +
                        StringUtils.abbreviate(mix.ingredient.toJson().toString(), 64) + " -> " +
                        Registry.POTION.getKey(getOutputPotionFromMix(mix)));
            }
            return matches;
        });
    }

    protected abstract Potion getInputPotionFromMix(PotionBrewing.Mix<Potion> mix);

    protected abstract Potion getOutputPotionFromMix(PotionBrewing.Mix<Potion> mix);
}
