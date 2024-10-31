package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.core.Events;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import javax.annotation.Nullable;

public class PostUpdateOfferEventJS extends UpdateOfferEventJS {

    @SuppressWarnings("ConstantValue")
    public static void invoke(AbstractVillager villager, MerchantOffers allOffers) {
        if (villager instanceof Villager v) {
            var data = v.getVillagerData();
            if (data == null) {
                return;
            }

            if (data.getProfession() == VillagerProfession.NONE) {
                return;
            }
        }

        Events.POST_UPDATE_OFFERS.post(new PostUpdateOfferEventJS(villager, allOffers));
    }

    public PostUpdateOfferEventJS(AbstractVillager villager, MerchantOffers allOffers) {
        super(villager, allOffers);
    }

    public void addOffer(@Nullable MerchantOffer offer) {
        if (offer != null) {
            getAllOffers().add(offer);
        }
    }

    public void addTrade(VillagerTrades.ItemListing trade) {
        var offer = trade.getOffer(getEntity(), getRandom());
        addOffer(offer);
    }
}
