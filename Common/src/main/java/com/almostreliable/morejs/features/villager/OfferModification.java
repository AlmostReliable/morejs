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
        return offerAsAccessor.getFirstInput();
    }

    public void setFirstInput(ItemStack itemStack) {
        offerAsAccessor.setFirstInput(itemStack);
    }

    public ItemStack getSecondInput() {
        return offerAsAccessor.getSecondInput();
    }

    public void setSecondInput(ItemStack itemStack) {
        offerAsAccessor.setSecondInput(itemStack);
    }

    public ItemStack getOutput() {
        return offerAsAccessor.getOutput();
    }

    public void setOutput(ItemStack itemStack) {
        offerAsAccessor.setOutput(itemStack);
    }

    public int getMaxUses() {
        return offer.getMaxUses();
    }

    public void setMaxUses(int maxUses) {
        offerAsAccessor.setMaxUses(maxUses);
    }

    public int getDemand() {
        return offer.getDemand();
    }

    public void setDemand(int demand) {
        offerAsAccessor.setDemand(demand);
    }

    public int getVillagerExperience() {
        return offer.getXp();
    }

    public void setVillagerExperience(int villagerExperience) {
        offerAsAccessor.setVillagerExperience(villagerExperience);
    }

    public float getPriceMultiplier() {
        return offer.getPriceMultiplier();
    }

    public void setPriceMultiplier(float priceMultiplier) {
        offerAsAccessor.setPriceMultiplier(priceMultiplier);
    }

    public MerchantOffer getMerchantOffer() {
        return offer;
    }
}
