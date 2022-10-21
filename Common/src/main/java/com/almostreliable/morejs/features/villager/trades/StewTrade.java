package com.almostreliable.morejs.features.villager.trades;

import com.google.common.base.Preconditions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Arrays;

public class StewTrade extends TransformableTrade<StewTrade> {

    protected MobEffect[] effects;
    protected int duration;

    public StewTrade(ItemStack[] inputs, MobEffect[] effects, int duration) {
        super(inputs);
        for (MobEffect effect : effects) {
            if (effect == null) {
                throw new IllegalArgumentException("Null effect in effects array: " + Arrays.toString(effects));
            }
        }
        Preconditions.checkArgument(duration > 0, "Duration must be greater than 0");
        this.effects = effects;
        this.duration = duration;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity entity, RandomSource random) {
        ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);
        for (MobEffect effect : effects) {
            SuspiciousStewItem.saveMobEffect(stew, effect, this.duration);
        }
        return createOffer(stew);
    }
}
