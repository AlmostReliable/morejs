package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.util.Utils;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.ItemCost;

@RemapPrefixForJS("morejs$")
public interface OfferExtension {

    boolean morejs$isDisabled();

    void morejs$setDisabled(boolean disabled);

    ItemCost morejs$getFirstCost();

    void morejs$setFirstCost(ItemStack itemStack);

    ItemCost morejs$getSecondCost();

    void morejs$setSecondCost(ItemStack itemStack);

    ItemStack morejs$getOutput();

    void morejs$setOutput(ItemStack itemStack);

    void morejs$setMaxUses(int maxUses);

    void morejs$setDemand(int demand);

    void morejs$setVillagerExperience(int villagerExperience);

    void morejs$setPriceMultiplier(float priceMultiplier);

    void morejs$setRewardExp(boolean rewardExp);

    boolean morejs$isRewardingExp();

    default void morejs$replaceEmeralds(Item replacement) {
        if (morejs$getFirstCost().test(new ItemStack(Items.EMERALD))) {
            morejs$setFirstCost(new ItemStack(replacement, morejs$getFirstCost().count()));
        }

        if (morejs$getSecondCost().test(new ItemStack(Items.EMERALD))) {
            morejs$setSecondCost(new ItemStack(replacement, morejs$getSecondCost().count()));
        }

        if (morejs$getOutput().getItem() == Items.EMERALD) {
            morejs$setOutput(new ItemStack(replacement, morejs$getOutput().getCount()));
        }
    }

    default void morejs$replaceItems(Ingredient filter, ItemStack itemStack) {
        if (Utils.matchesItemCost(filter, morejs$getFirstCost())) {
            morejs$setFirstCost(itemStack.copy());
        }

        if (Utils.matchesItemCost(filter, morejs$getSecondCost())) {
            morejs$setSecondCost(itemStack.copy());
        }

        if (filter.test(morejs$getOutput())) {
            morejs$setOutput(itemStack.copy());
        }
    }
}
