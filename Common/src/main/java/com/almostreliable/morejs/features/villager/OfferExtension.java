package com.almostreliable.morejs.features.villager;

import net.minecraft.world.item.ItemStack;

public interface OfferExtension {

    void setDisabled(boolean disabled);

    boolean isDisabled();

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
}
