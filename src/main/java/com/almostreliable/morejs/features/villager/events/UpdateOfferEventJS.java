package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.features.villager.VillagerUtils;
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

public abstract class UpdateOfferEventJS implements KubeLivingEntityEvent {

    private final AbstractVillager villager;
    private final MerchantOffers allOffers;
    @Nullable private List<VillagerTrades.ItemListing> cachedWandererTrades;

    public UpdateOfferEventJS(AbstractVillager villager, MerchantOffers allOffers) {
        this.villager = villager;
        this.allOffers = allOffers;
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

    @Nullable
    public MerchantOffer createRandomOffer(List<VillagerTrades.ItemListing> possibleTrades) {
        if (possibleTrades.isEmpty()) {
            return null;
        }

        int i = getLevel().getRandom().nextInt(possibleTrades.size());
        VillagerTrades.ItemListing randomListing = possibleTrades.get(i);
        return randomListing.getOffer(getEntity(), getLevel().getRandom());
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
