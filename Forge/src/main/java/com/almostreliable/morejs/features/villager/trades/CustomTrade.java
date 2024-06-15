package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.OfferModification;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;

public class CustomTrade implements VillagerTrades.ItemListing {

    private final TransformableTrade.Transformer transformer;

    public CustomTrade(TransformableTrade.Transformer transformer) {
        this.transformer = transformer;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, RandomSource random) {
        MerchantOffer offer = new MerchantOffer(new ItemStack(Items.EMERALD),
                new ItemStack(Items.EMERALD),
                16,
                2,
                0.05f);
        transformer.accept(new OfferModification(offer), entity, random);
        return offer;
    }
}
