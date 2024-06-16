package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import com.google.common.base.Preconditions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class StewTrade extends TransformableTrade<StewTrade> {


    private final List<SuspiciousStewEffects.Entry> effects = new ArrayList<>();

    public StewTrade(TradeItem[] inputs) {
        super(inputs);
    }

    public StewTrade addEffect(MobEffect effect, int duration) {
        Preconditions.checkArgument(duration > 0, "Duration must be greater than 0");
        var mobEffectHolder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
        effects.add(new SuspiciousStewEffects.Entry(mobEffectHolder, duration));
        return this;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity entity, RandomSource random) {
        ItemStack stew = new ItemStack(Items.SUSPICIOUS_STEW);

        var effectCopy = List.copyOf(effects);
        stew.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, new SuspiciousStewEffects(effectCopy));
        return createOffer(stew, random);
    }
}
