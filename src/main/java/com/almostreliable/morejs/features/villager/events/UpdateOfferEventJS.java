package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.features.villager.VillagerUtils;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.entity.KubeLivingEntityEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateOfferEventJS implements KubeLivingEntityEvent {

    private final AbstractVillager villager;
    private final MerchantOffers allOffers;
    private final VillagerTrades.ItemListing[] possibleTrades;
    private MerchantOffer offer;
    @Nullable private List<VillagerTrades.ItemListing> cachedWandererTrades;

    public UpdateOfferEventJS(AbstractVillager villager, MerchantOffers allOffers, VillagerTrades.ItemListing[] possibleTrades, MerchantOffer offer) {
        this.villager = villager;
        this.allOffers = allOffers;
        this.possibleTrades = possibleTrades;
        this.offer = offer;
    }

    public RandomSource getRandom() {
        return villager.getRandom();
    }

    @Override
    public LivingEntity getEntity() {
        return villager;
    }

    @Nullable
    public VillagerData getVillagerData() {
        if (villager instanceof VillagerDataHolder v) {
            return v.getVillagerData();
        }

        return null;
    }

    public boolean isProfession(VillagerProfession profession) {
        if (villager instanceof VillagerDataHolder v) {
            return v.getVillagerData().getProfession() == profession;

        }

        return false;
    }

    public int getVillagerLevel() {
        if (villager instanceof VillagerDataHolder v) {
            return v.getVillagerData().getLevel();
        }

        return -1;
    }

    public VillagerProfession getProfession() {
        if (villager instanceof VillagerDataHolder v) {
            return v.getVillagerData().getProfession();
        }

        return VillagerProfession.NONE;
    }

    public boolean isVillager() {
        return villager instanceof Villager;
    }

    public boolean isWanderer() {
        return villager instanceof WanderingTrader;
    }

    public boolean isUnknownTrader() {
        return !isVillager() && !isWanderer();
    }

    public MerchantOffers getAllOffers() {
        return allOffers;
    }

    public List<VillagerTrades.ItemListing> getUsedTrades() {
        return Arrays.asList(possibleTrades);
    }

    public MerchantOffer getOffer() {
        return offer;
    }

    public void setOffer(MerchantOffer offer) {
        Preconditions.checkNotNull(offer, "Offer must not be null");
        this.offer = offer;
    }

    public void setOffer(VillagerTrades.ItemListing trade) {
        MerchantOffer newOffer = trade.getOffer(villager, getLevel().getRandom());
        if (newOffer != null) {
            this.offer = newOffer;
        }
    }

    @Nullable
    public MerchantOffer createRandomOffer() {
        return createRandomOffer(getUsedTrades());
    }

    @Nullable
    public MerchantOffer createRandomOffer(List<VillagerTrades.ItemListing> possibleTrades) {
        if (possibleTrades.isEmpty()) {
            return null;
        }

        int i = getLevel().getRandom().nextInt(possibleTrades.size());
        VillagerTrades.ItemListing randomListing = possibleTrades.get(i);
        return randomListing.getOffer(villager, getLevel().getRandom());
    }

    public List<VillagerTrades.ItemListing> getVillagerTrades(VillagerProfession profession) {
        return VillagerUtils.getVillagerTrades(profession);
    }

    public List<VillagerTrades.ItemListing> getVillagerTrades(VillagerProfession profession, int level) {
        return VillagerUtils.getVillagerTrades(profession, level);
    }

    public List<VillagerTrades.ItemListing> getWandererTrades() {
        if (cachedWandererTrades == null) {
            cachedWandererTrades = new ArrayList<>();
            for (VillagerTrades.ItemListing[] listings : VillagerTrades.WANDERING_TRADER_TRADES.values()) {
                cachedWandererTrades.addAll(Arrays.asList(listings));
            }
        }

        return cachedWandererTrades;
    }

    public List<VillagerTrades.ItemListing> getWandererTrades(int level) {
        return VillagerUtils.getWandererTrades(level);
    }
}
