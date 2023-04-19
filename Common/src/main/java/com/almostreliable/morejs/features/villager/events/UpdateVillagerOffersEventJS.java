package com.almostreliable.morejs.features.villager.events;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.List;

public class UpdateVillagerOffersEventJS extends UpdateAbstractVillagerOffersEventJS {
    private final Villager villager;

    public UpdateVillagerOffersEventJS(Villager villager, MerchantOffers offers, VillagerTrades.ItemListing[] currentUsedItemListings, List<MerchantOffer> addedOffers) {
        super(villager, offers, currentUsedItemListings, addedOffers);
        this.villager = villager;
    }

    @Override
    public Villager getEntity() {
        return villager;
    }

    public VillagerData getVillagerData() {
        return villager.getVillagerData();
    }

    public boolean isProfession(VillagerProfession profession) {
        return villager.getVillagerData().getProfession() == profession;
    }

    public int getVillagerLevel() {
        return villager.getVillagerData().getLevel();
    }

    public VillagerProfession getProfession() {
        return villager.getVillagerData().getProfession();
    }
}
