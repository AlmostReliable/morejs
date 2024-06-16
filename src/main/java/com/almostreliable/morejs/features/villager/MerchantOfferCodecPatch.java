package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.MoreJS;
import com.almostreliable.morejs.mixin.villager.MerchantOfferAccessor;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.item.trading.MerchantOffer;

public class MerchantOfferCodecPatch {

    public static final String KEY = "morejs$isDisabled";

    public static void patch() {
        var codec = MerchantOffer.CODEC;
        var patch = patch(codec);
        MerchantOfferAccessor.morejs$setCodec(patch);
    }

    public static Codec<MerchantOffer> patch(Codec<MerchantOffer> codec) {
        return new Codec<>() {
            @Override
            public <T> DataResult<Pair<MerchantOffer, T>> decode(DynamicOps<T> ops, T input) {
                var result = codec.decode(ops, input);
                if (result.isError()) {
                    return result;
                }

                Pair<MerchantOffer, T> pair = result.getOrThrow();
                MerchantOffer offer = pair.getFirst();

                DataResult<T> isDisabledResult = ops.get(input, KEY);
                isDisabledResult.flatMap(ops::getBooleanValue).ifSuccess(aBoolean -> {
                    ((OfferExtension) offer).morejs$setDisabled(aBoolean);
                }).ifError((error) -> {
                    MoreJS.LOG.error("Failed to read `isDisabled` from trade offers: {}", error);
                });

                return result;
            }

            @Override
            public <T> DataResult<T> encode(MerchantOffer offer, DynamicOps<T> ops, T prefix) {
                DataResult<T> result = codec.encode(offer, ops, prefix);
                if (result.isError()) {
                    return result;
                }

                T data = result.getOrThrow();
                boolean isDisabled = ((OfferExtension) offer).morejs$isDisabled();
                T isDisabledData = ops.createBoolean(isDisabled);
                ops.set(data, KEY, isDisabledData);

                return result;
            }
        };
    }
}
