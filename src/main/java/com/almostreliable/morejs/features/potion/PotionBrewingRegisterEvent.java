package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.PotionBrewingBuilderAccessor;
import com.almostreliable.morejs.util.Utils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ConsoleJS;
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

    protected void validate(Ingredient ingredient, Ingredient input, ItemStack output) {
        Preconditions.checkArgument(input.getItems().length > 0, "Input must have at least one item");
        Preconditions.checkArgument(ingredient.getItems().length > 0, "Ingredient must have at least one item");
        Preconditions.checkArgument(!output.isEmpty(), "Output must not be empty");
    }

    protected void validateSimple(Ingredient ingredient, Potion input, Potion output) {
        Preconditions.checkNotNull(input, "Input potion must not be null");
        Preconditions.checkNotNull(ingredient, "Ingredient must not be null");
        Preconditions.checkNotNull(output, "Output potion must not be null");
        Preconditions.checkArgument(ingredient.getItems().length > 0, "Ingredient must have at least one item");
    }

    public void addCustomBrewing(Ingredient ingredient, Ingredient input, ItemStack output) {
        validate(ingredient, input, output);
        potionBrewing.addRecipe(input, ingredient, output);
    }

    public void addPotionBrewing(Ingredient ingredient, Potion input, Potion output) {
        validateSimple(ingredient, input, output);
        Holder<Potion> inputRef = BuiltInRegistries.POTION.wrapAsHolder(input);
        Holder<Potion> outputRef = BuiltInRegistries.POTION.wrapAsHolder(output);
        potionBrewingAccessor.morejs$getPotionMixes().add(new PotionBrewing.Mix<>(inputRef, ingredient, outputRef));
    }

    public void addPotionBrewing(Ingredient ingredient, Potion output) {
        addPotionBrewing(ingredient, Potions.WATER.value(), output);
    }

    public void removePotionBrewing(@Nullable Ingredient ingredient, @Nullable Potion input, @Nullable Potion output) {
        potionBrewingAccessor.morejs$getPotionMixes().removeIf(mix -> {
            boolean matchesInput = input == null || mix.from().value() == input;
            boolean matchesIngredient = ingredient == null || Utils.matchesIngredient(ingredient, mix.ingredient());
            boolean matchesOutput = output == null || mix.to().value() == output;
            boolean matches = matchesInput && matchesIngredient && matchesOutput;
            if (matches) {
                ConsoleJS.STARTUP.info(
                        "Removed potion brewing recipe: " +
                        mix.from() + " + " +
                        StringUtils.abbreviate(mix.ingredient().toString(), 64) + " -> " +
                        mix.to());
            }
            return matches;
        });
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
            var output = mix.to().value();
            if (ingredient.test(output.getDefaultInstance())) {
                mixIt.remove();
                removed.add(output);
            }
        }

        for (Item item : removed) {
            ConsoleJS.STARTUP.info("Removed potion container: " + BuiltInRegistries.ITEM.getKey(item));
        }
    }

    public void validateContainer(Ingredient ingredient, Item input, Item output) {
        Preconditions.checkArgument(input != null && input != Items.AIR, "Input must not be null or air");
        Preconditions.checkNotNull(ingredient, "Ingredient must not be null");
        Preconditions.checkArgument(ingredient.getItems().length > 0, "Ingredient must have at least one item");
        Preconditions.checkArgument(output != null && output != Items.AIR, "Output must not be null or air");
    }

    public void addContainerRecipe(Ingredient ingredient, Item input, Item output) {
        validateContainer(ingredient, input, output);
        //noinspection deprecation
        var mix = new PotionBrewing.Mix<>(input.builtInRegistryHolder(), ingredient, output.builtInRegistryHolder());
        potionBrewingAccessor.morejs$getContainerMixes().add(mix);
    }

    public void removeCustomBrewing(@Nullable Ingredient ingredient, @Nullable Ingredient input, @Nullable Ingredient output) {
        ListIterator<IBrewingRecipe> it = potionBrewingAccessor.morejs$getRecipes().listIterator();
        while (it.hasNext()) {
            IBrewingRecipe recipe = it.next();
            if (!(recipe instanceof BrewingRecipe br)) { // BrewingRecipe is the vanilla one
                continue;
            }

            boolean matchesInput = ingredient == null || Utils.matchesIngredient(ingredient, br.getIngredient());
            boolean matchesIngredient = input == null || Utils.matchesIngredient(input, br.getInput());
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

    public void removeCustomBrewing(Predicate<IBrewingRecipe> predicate) {
        ListIterator<IBrewingRecipe> it = potionBrewingAccessor.morejs$getRecipes().listIterator();
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
