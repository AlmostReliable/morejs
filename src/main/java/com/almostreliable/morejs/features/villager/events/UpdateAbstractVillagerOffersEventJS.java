package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.VillagerUtils;
import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import javax.annotation.Nullable;
import java.util.*;

public class UpdateAbstractVillagerOffersEventJS extends LivingEntityEventJS {

    private final AbstractVillager villager;
    private final MerchantOffers offers;
    private final VillagerTrades.ItemListing[] currentUsedItemListings;
    private final List<MerchantOffer> addedOffers;
    @Nullable private List<VillagerTrades.ItemListing> cachedWandererTrades;

    public static void invokeEvent(AbstractVillager villager, MerchantOffers offers, VillagerTrades.ItemListing[] currentUsedItemListings, List<MerchantOffer> addedOffers) {
        if (villager instanceof Villager v) {
            var e = new UpdateVillagerOffersEventJS(v, offers, currentUsedItemListings, addedOffers);
            Events.UPDATE_VILLAGER_OFFERS.post(e);
            return;
        }

        var e = new UpdateAbstractVillagerOffersEventJS(villager, offers, currentUsedItemListings, addedOffers);
        if (villager instanceof WanderingTrader) {
            Events.UPDATE_WANDERER_OFFERS.post(e);
            return;
        }

        Events.UPDATE_ABSTRACT_VILLAGER_OFFERS.post(e);
    }

    public UpdateAbstractVillagerOffersEventJS(AbstractVillager villager, MerchantOffers offers, VillagerTrades.ItemListing[] currentUsedItemListings, List<MerchantOffer> addedOffers) {
        this.villager = villager;
        this.offers = offers;
        this.currentUsedItemListings = currentUsedItemListings;
        this.addedOffers = Collections.unmodifiableList(addedOffers);
    }

    @Override
    public LivingEntity getEntity() {
        return villager;
    }

    @Nullable
    public VillagerData getVillagerData() {
        if (villager instanceof Villager v) {
            return v.getVillagerData();
        }

        return null;
    }

    public boolean isVillager() {
        return villager instanceof Villager;
    }

    public boolean isWanderer() {
        return villager instanceof WanderingTrader;
    }

    public MerchantOffers getOffers() {
        return offers;
    }

    public List<VillagerTrades.ItemListing> getUsedTrades() {
        return Arrays.asList(currentUsedItemListings);
    }

    public Collection<MerchantOffer> getAddedOffers() {
        return addedOffers;
    }

    public void deleteAddedOffers() {
        offers.removeAll(addedOffers);
    }

    @Nullable
    public MerchantOffer addRandomOffer() {
        return addRandomOffer(getUsedTrades());
    }

    @Nullable
    public MerchantOffer addRandomOffer(List<VillagerTrades.ItemListing> possibleTrades) {
        if (possibleTrades.isEmpty()) {
            return null;
        }

        int i = getLevel().getRandom().nextInt(possibleTrades.size());
        VillagerTrades.ItemListing randomListing = possibleTrades.get(i);
        MerchantOffer offer = randomListing.getOffer(villager, getLevel().getRandom());
        if (offer != null) {
            offers.add(offer);
            return offer;
        }

        return null;
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
