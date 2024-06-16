package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.BrewingRecipeRegistryAccessor;
import com.almostreliable.morejs.mixin.PotionBrewingBuilderAccessor;
import com.almostreliable.morejs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.BrewingRecipe;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.function.Predicate;

public class PotionBrewingRegisterEvent implements KubeEvent {

    private final PotionBrewing.Builder potionBrewing;
    private final PotionBrewingBuilderAccessor potionBrewingAccessor;

    public PotionBrewingRegisterEvent(PotionBrewing.Builder potionBrewing) {
        this.potionBrewing = potionBrewing;
        this.potionBrewingAccessor = (PotionBrewingBuilderAccessor) potionBrewing;
    }

    private static ResourceLocation key(Potion potion) {
        return BuiltInRegistries.POTION.getKey(potion);
    }

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

    public void addCustomBrewing(Ingredient topInput, Ingredient ingredient, ItemStack output) {
        validate(topInput, ingredient, output);
        potionBrewing.addRecipe(topInput, ingredient, output);
    }

    public void addPotionBrewing(Ingredient ingredient, Potion input, Potion output) {
        validateSimple(input, ingredient, output);
        Holder<Potion> inputRef = BuiltInRegistries.POTION.wrapAsHolder(input);
        Holder<Potion> outputRef = BuiltInRegistries.POTION.wrapAsHolder(output);
        potionBrewingAccessor.morejs$getPotionMixes().add(new PotionBrewing.Mix<>(inputRef, ingredient, outputRef));
    }

    public void addPotionBrewing(Ingredient ingredient, Potion output) {
        addPotionBrewing(ingredient, Potions.WATER.value(), output);
    }

    public void removeByPotion(@Nullable Potion input, @Nullable Ingredient ingredient, @Nullable Potion output) {
        potionBrewingAccessor.morejs$getPotionMixes().removeIf(mix -> {
            boolean matchesInput = input == null || getInputPotionFromMix(mix) == input;
            boolean matchesIngredient = ingredient == null || Utils.matchesIngredient(ingredient, mix.ingredient);
            boolean matchesOutput = output == null || getOutputPotionFromMix(mix) == output;
            boolean matches = matchesInput && matchesIngredient && matchesOutput;
            if (matches) {
                ConsoleJS.STARTUP.info(
                        "Removed potion brewing recipe: " +
                        key(getInputPotionFromMix(mix)) + " + " +
                        StringUtils.abbreviate(mix.ingredient.toString(), 64) + " -> " +
                        key(getOutputPotionFromMix(mix)));
            }
            return matches;
        });
    }


    protected Potion getInputPotionFromMix(PotionBrewing.Mix<Potion> mix) {
        return mix.from.value();
    }


    protected Potion getOutputPotionFromMix(PotionBrewing.Mix<Potion> mix) {
        return mix.to.value();
    }

    protected Item getOutputItemFromMix(PotionBrewing.Mix<Item> mix) {
        return mix.to.value();
    }

    public void removeContainer(Ingredient ingredient) {
        HashSet<Item> removed = new HashSet<>();

        var containerIt = potionBrewingAccessor.morejs$getContainers().listIterator();
        while (containerIt.hasNext()) {
            Ingredient ac = containerIt.next();
            if (Utils.matchesIngredient(ac, ingredient)) {
                containerIt.remove();
                for (ItemStack item : ac.getItems()) {
                    removed.add(item.getItem());
                }
            }
        }


        var mixIt = potionBrewingAccessor.morejs$getContainerMixes().listIterator();
        while (mixIt.hasNext()) {
            PotionBrewing.Mix<Item> mix = mixIt.next();
            var output = getOutputItemFromMix(mix);
            if (ingredient.test(output.getDefaultInstance())) {
                mixIt.remove();
                removed.add(output);
            }
        }

        for (Item item : removed) {
            ConsoleJS.STARTUP.info("Removed potion container: " + BuiltInRegistries.ITEM.getKey(item));
        }
    }

    public void validateContainer(Item from, Ingredient ingredient, Item output) {
        Preconditions.checkArgument(from != null && from != Items.AIR, "Input must not be null or air");
        Preconditions.checkNotNull(ingredient, "Ingredient must not be null");
        Preconditions.checkArgument(ingredient.getItems().length > 0, "Ingredient must have at least one item");
        Preconditions.checkArgument(output != null && output != Items.AIR, "Output must not be null or air");
    }

    public void addContainerRecipe(Item from, Ingredient ingredient, Item output) {
        validateContainer(from, ingredient, output);
        //noinspection deprecation
        var mix = new PotionBrewing.Mix<>(from.builtInRegistryHolder(), ingredient, output.builtInRegistryHolder());
        potionBrewingAccessor.morejs$getContainerMixes().add(mix);
    }


    public void removeByCustom(@Nullable Ingredient topInput, @Nullable Ingredient bottomInput, @Nullable Ingredient output) {
        ListIterator<IBrewingRecipe> it = BrewingRecipeRegistryAccessor.getRecipes().listIterator();
        while (it.hasNext()) {
            IBrewingRecipe recipe = it.next();
            if (!(recipe instanceof BrewingRecipe br)) { // BrewingRecipe is the vanilla one
                continue;
            }

            boolean matchesInput = topInput == null || Utils.matchesIngredient(topInput, br.getIngredient());
            boolean matchesIngredient = bottomInput == null || Utils.matchesIngredient(bottomInput, br.getInput());
            boolean matchesOutput = output == null || output.test(br.getOutput());


            if (matchesInput && matchesIngredient && matchesOutput) {
                String s = String.format("Removing custom brewing recipe: [Input: %s][Ingredient: %s][Output: %s]",
                        br.getInput(),
                        br.getIngredient(),
                        br.getOutput());
                ConsoleJS.STARTUP.info(s);
                it.remove();
            }
        }
    }

    public void removeByCustom(Predicate<IBrewingRecipe> predicate) {
        ListIterator<IBrewingRecipe> it = BrewingRecipeRegistryAccessor.getRecipes().listIterator();
        while (it.hasNext()) {
            IBrewingRecipe recipe = it.next();
            if (recipe instanceof BrewingRecipe) {
                continue;
            }

            if (predicate.test(recipe)) {
                ConsoleJS.STARTUP.info("Removing custom brewing recipe: " + recipe);
                it.remove();
            }
        }
    }
}
