package com.almostreliable.morejs.features.villager.trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;

public class SimpleTrade extends TransformableTrade<SimpleTrade> {
    protected ItemStack output;

    public SimpleTrade(ItemStack[] input, ItemStack output) {
        super(input);
        this.output = output;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity trader, RandomSource rand) {
        return createOffer(output);
    }
}
