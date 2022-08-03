package com.almostreliable.missingname.modules.villager.trades;

import com.almostreliable.missingname.modules.villager.ItemListingType;
import com.almostreliable.missingname.modules.villager.OfferModification;
import dev.latvian.mods.kubejs.entity.EntityJS;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Random;

public class CustomTrade implements VillagerTrades.ItemListing, ItemListingType {

    private final TransformableTrade.Transformer transformer;

    public CustomTrade(TransformableTrade.Transformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public Type getTradeType() {
        return Type.OWN_CUSTOM;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, Random random) {
        MerchantOffer offer = new MerchantOffer(new ItemStack(Items.EMERALD),
                new ItemStack(Items.EMERALD),
                16,
                2,
                0.05f);
        transformer.accept(new OfferModification(offer), new EntityJS(entity), random);
        return offer;
    }
}
