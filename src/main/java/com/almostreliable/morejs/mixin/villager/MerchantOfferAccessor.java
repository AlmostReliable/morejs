package com.almostreliable.morejs.mixin.villager;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MerchantOffer.class)
public interface MerchantOfferAccessor {

    @Accessor("CODEC")
    static void morejs$setCodec(Codec<MerchantOffer> codec) {
        throw new AssertionError();
    }
}
