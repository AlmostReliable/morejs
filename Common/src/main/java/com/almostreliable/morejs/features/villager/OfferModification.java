package com.almostreliable.morejs.features.villager;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

public class OfferModification {

    private final MerchantOffer offer;
    private final OfferExtension offerAsAccessor;

    public OfferModification(MerchantOffer offer) {
        this.offer = offer;
        this.offerAsAccessor = (OfferExtension) offer;
    }

    public ItemStack getFirstInput() {
        return offerAsAccessor.morejs$getFirstInput();
    }

    public void setFirstInput(ItemStack itemStack) {
        offerAsAccessor.morejs$setFirstInput(itemStack);
    }

    public ItemStack getSecondInput() {
        return offerAsAccessor.morejs$getSecondInput();
    }

    public void setSecondInput(ItemStack itemStack) {
        offerAsAccessor.morejs$setSecondInput(itemStack);
    }

    public ItemStack getOutput() {
        return offerAsAccessor.morejs$getOutput();
    }

    public void setOutput(ItemStack itemStack) {
        offerAsAccessor.morejs$setOutput(itemStack);
    }

    public int getMaxUses() {
        return offer.getMaxUses();
    }

    public void setMaxUses(int maxUses) {
        offerAsAccessor.morejs$setMaxUses(maxUses);
    }

    public int getDemand() {
        return offer.getDemand();
    }

    public void setDemand(int demand) {
        offerAsAccessor.morejs$setDemand(demand);
    }

    public int getVillagerExperience() {
        return offer.getXp();
    }

    public void setVillagerExperience(int villagerExperience) {
        offerAsAccessor.morejs$setVillagerExperience(villagerExperience);
    }

    public float getPriceMultiplier() {
        return offer.getPriceMultiplier();
    }

    public void setPriceMultiplier(float priceMultiplier) {
        offerAsAccessor.morejs$setPriceMultiplier(priceMultiplier);
    }

    public void setRewardExp(boolean rewardExp) {
        offerAsAccessor.morejs$setRewardExp(rewardExp);
    }

    public boolean isRewardingExp() {
        return offerAsAccessor.morejs$isRewardingExp();
    }

    public MerchantOffer getMerchantOffer() {
        return offer;
    }
}
