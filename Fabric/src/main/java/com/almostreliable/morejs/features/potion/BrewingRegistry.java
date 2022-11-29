package com.almostreliable.morejs.features.potion;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BrewingRegistry {
    private static final List<Entry> entries = new ArrayList<>();

    static void addBrewing(Ingredient topInput, Ingredient bottomInput, ItemStack output) {
        entries.add(new Entry(bottomInput, topInput, output));
    }

    public static boolean hasPotionMix(ItemStack bottomInput, ItemStack topInput) {
        for (Entry entry : entries) {
            if (entry.topInput.test(topInput) && entry.bottomInput.test(bottomInput)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPotionIngredient(ItemStack topInput) {
        for (Entry entry : entries) {
            if (entry.topInput.test(topInput)) {
                return true;
            }
        }
        return false;
    }

    public static boolean mayPlaceBottomIngredient(ItemStack item) {
        for (Entry entry : entries) {
            if (entry.bottomInput.test(item)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static ItemStack mix(ItemStack topInput, ItemStack bottomInput) {
        for (Entry entry : entries) {
            if (entry.topInput.test(topInput) && entry.bottomInput.test(bottomInput)) {
                return entry.output.copy();
            }
        }
        return null;
    }

    public record Entry(Ingredient bottomInput, Ingredient topInput, ItemStack output) {
    }
}
