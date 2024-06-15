package com.almostreliable.morejs.features.villager;

import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

@RemapPrefixForJS("morejs$")
public interface OfferExtension {

    boolean morejs$isDisabled();

    void morejs$setDisabled(boolean disabled);

    ItemStack morejs$getFirstInput();

    void morejs$setFirstInput(ItemStack itemStack);

    ItemStack morejs$getSecondInput();

    void morejs$setSecondInput(ItemStack itemStack);

    ItemStack morejs$getOutput();

    void morejs$setOutput(ItemStack itemStack);

    void morejs$setMaxUses(int maxUses);

    void morejs$setDemand(int demand);

    void morejs$setVillagerExperience(int villagerExperience);

    void morejs$setPriceMultiplier(float priceMultiplier);

    void morejs$setRewardExp(boolean rewardExp);

    boolean morejs$isRewardingExp();

    default void morejs$replaceEmeralds(Item replacement) {
        if (morejs$getFirstInput().getItem() == Items.EMERALD) {
            morejs$setFirstInput(new ItemStack(replacement, morejs$getFirstInput().getCount()));
        }

        if (morejs$getSecondInput().getItem() == Items.EMERALD) {
            morejs$setSecondInput(new ItemStack(replacement, morejs$getSecondInput().getCount()));
        }

        if (morejs$getOutput().getItem() == Items.EMERALD) {
            morejs$setOutput(new ItemStack(replacement, morejs$getOutput().getCount()));
        }
    }

    default void morejs$replaceItems(Ingredient filter, ItemStack itemStack) {
        if (filter.test(morejs$getFirstInput())) {
            morejs$setFirstInput(itemStack.copy());
        }

        if (filter.test(morejs$getSecondInput())) {
            morejs$setSecondInput(itemStack.copy());
        }

        if (filter.test(morejs$getOutput())) {
            morejs$setOutput(itemStack.copy());
        }
    }
}
