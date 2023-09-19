package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.potion.PotionBrewingAccessor;
import com.almostreliable.morejs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.HashSet;

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
                        "Removed potion brewing recipe: " + Registry.POTION.getKey(getInputPotionFromMix(mix)) + " + " +
                        StringUtils.abbreviate(mix.ingredient.toJson().toString(), 64) + " -> " +
                        Registry.POTION.getKey(getOutputPotionFromMix(mix)));
            }
            return matches;
        });
    }

    protected abstract Potion getInputPotionFromMix(PotionBrewing.Mix<Potion> mix);

    protected abstract Potion getOutputPotionFromMix(PotionBrewing.Mix<Potion> mix);

    protected abstract Item getOutputItemFromMix(PotionBrewing.Mix<Item> mix);

    public void removeContainer(Ingredient ingredient) {
        HashSet<Item> removed = new HashSet<>();

        var containerIt = PotionBrewingAccessor.getAllowedContainers().listIterator();
        while (containerIt.hasNext()) {
            Ingredient ac = containerIt.next();
            if (Utils.matchesIngredient(ac, ingredient)) {
                containerIt.remove();
                for (ItemStack item : ac.getItems()) {
                    removed.add(item.getItem());
                }
            }
        }


        var mixIt = PotionBrewingAccessor.getContainerMixes().listIterator();
        while (mixIt.hasNext()) {
            PotionBrewing.Mix<Item> mix = mixIt.next();
            Item output = getOutputItemFromMix(mix);
            if (ingredient.test(output.getDefaultInstance())) {
                mixIt.remove();
                removed.add(output);
            }
        }

        for (Item item : removed) {
            ConsoleJS.STARTUP.info("Removed potion container: " + Registry.ITEM.getKey(item));
        }
    }

    public void validateContainer(Item from, Ingredient ingredient, Item output) {
        Preconditions.checkArgument(from != null && from != Items.AIR, "Input must not be null or air");
        Preconditions.checkNotNull(ingredient, "Ingredient must not be null");
        Preconditions.checkArgument(ingredient.getItems().length > 0, "Ingredient must have at least one item");
        Preconditions.checkArgument(output != null && output != Items.AIR, "Output must not be null or air");
    }

    public abstract void addContainerRecipe(Item from, Ingredient ingredient, Item output);
}
