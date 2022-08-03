package com.almostreliable.missingname.modules.villager.trades;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Random;

public class SimpleTrade extends TransformableTrade<SimpleTrade> {
    protected ItemStack output;

    public SimpleTrade(ItemStack[] input, ItemStack output) {
        super(input);
        this.output = output;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity trader, Random rand) {
        return createOffer(output);
    }
}
