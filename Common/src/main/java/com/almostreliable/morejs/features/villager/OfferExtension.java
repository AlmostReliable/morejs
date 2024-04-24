package com.almostreliable.morejs.features.villager;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public interface OfferExtension {

    boolean isDisabled();

    void setDisabled(boolean disabled);

    ItemStack getFirstInput();

    void setFirstInput(ItemStack itemStack);

    ItemStack getSecondInput();

    void setSecondInput(ItemStack itemStack);

    ItemStack getOutput();

    void setOutput(ItemStack itemStack);

    void setMaxUses(int maxUses);

    void setDemand(int demand);

    void setVillagerExperience(int villagerExperience);

    void setPriceMultiplier(float priceMultiplier);

    default void replaceEmeralds(Item replacement) {
        if (getFirstInput().getItem() == Items.EMERALD) {
            setFirstInput(new ItemStack(replacement, getFirstInput().getCount()));
        }

        if (getSecondInput().getItem() == Items.EMERALD) {
            setSecondInput(new ItemStack(replacement, getSecondInput().getCount()));
        }

        if (getOutput().getItem() == Items.EMERALD) {
            setOutput(new ItemStack(replacement, getOutput().getCount()));
        }
    }

    default void replaceItems(Ingredient filter, ItemStack itemStack) {
        if (filter.test(getFirstInput())) {
            setFirstInput(itemStack.copy());
        }

        if (filter.test(getSecondInput())) {
            setSecondInput(itemStack.copy());
        }

        if (filter.test(getOutput())) {
            setOutput(itemStack.copy());
        }
    }
}
