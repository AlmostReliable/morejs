package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.util.Utils;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.MerchantOffer;

@RemapPrefixForJS("morejs$")
public interface OfferExtension {

    MerchantOffer morejs$self();

    boolean morejs$isDisabled();

    void morejs$setDisabled(boolean disabled);

    ItemStack morejs$getFirstCost();

    void morejs$setFirstCost(ItemStack itemStack);

    ItemStack morejs$getSecondCost();

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
        if (morejs$self().getItemCostA().test(new ItemStack(Items.EMERALD))) {
            morejs$setFirstCost(new ItemStack(replacement, morejs$getFirstCost().getCount()));
        }

        morejs$self().getItemCostB().ifPresent(cost -> {
            if (cost.test(new ItemStack(Items.EMERALD))) {
                morejs$setSecondCost(new ItemStack(replacement, morejs$getSecondCost().getCount()));
            }
        });


        if (morejs$getOutput().getItem() == Items.EMERALD) {
            morejs$setOutput(new ItemStack(replacement, morejs$getOutput().getCount()));
        }
    }

    default void morejs$replaceItems(Ingredient filter, ItemStack itemStack) {
        if (Utils.matchesItemCost(filter, morejs$self().getItemCostA())) {
            morejs$setFirstCost(itemStack.copy());
        }

        morejs$self().getItemCostB().ifPresent(cost -> {
            if (Utils.matchesItemCost(filter, cost)) {
                morejs$setSecondCost(itemStack.copy());
            }
        });

        if (filter.test(morejs$getOutput())) {
            morejs$setOutput(itemStack.copy());
        }
    }
}
