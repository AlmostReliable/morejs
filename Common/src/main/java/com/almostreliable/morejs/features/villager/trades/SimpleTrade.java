package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;

public class SimpleTrade extends TransformableTrade<SimpleTrade> {
    protected TradeItem output;

    public SimpleTrade(TradeItem[] input, TradeItem output) {
        super(input);
        this.output = output;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity trader, RandomSource random) {
        ItemStack oi = this.output.createItemStack(random);
        return createOffer(oi, random);
    }
}
