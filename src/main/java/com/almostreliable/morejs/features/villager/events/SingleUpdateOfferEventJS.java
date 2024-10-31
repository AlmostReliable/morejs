package com.almostreliable.morejs.features.villager.events;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class SingleUpdateOfferEventJS extends UpdateOfferEventJS {

    private final VillagerTrades.ItemListing[] possibleTrades;
    private MerchantOffer offer;
    @Nullable private List<VillagerTrades.ItemListing> cachedWandererTrades;

    public SingleUpdateOfferEventJS(AbstractVillager villager, MerchantOffers allOffers, VillagerTrades.ItemListing[] possibleTrades, MerchantOffer offer) {
        super(villager, allOffers);
        this.possibleTrades = possibleTrades;
        this.offer = offer;
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
        MerchantOffer newOffer = trade.getOffer(getEntity(), getLevel().getRandom());
        if (newOffer != null) {
            this.offer = newOffer;
        }
    }

    @Nullable
    public MerchantOffer createRandomOffer() {
        return createRandomOffer(getUsedTrades());
    }
}
