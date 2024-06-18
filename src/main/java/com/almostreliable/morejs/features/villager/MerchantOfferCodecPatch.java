package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.mixin.villager.MerchantOfferAccessor;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.world.item.trading.MerchantOffer;

public class MerchantOfferCodecPatch implements Codec<MerchantOffer> {

    public static final String KEY = "morejs$isDisabled";
    private final Codec<MerchantOffer> codec;

    public static void patch() {
        var patch = new MerchantOfferCodecPatch(MerchantOffer.CODEC);
        MerchantOfferAccessor.morejs$setCodec(patch);
    }

    public MerchantOfferCodecPatch(Codec<MerchantOffer> codec) {
        this.codec = codec;
    }

    @Override
    public <T> DataResult<Pair<MerchantOffer, T>> decode(DynamicOps<T> ops, T input) {
        return codec.decode(ops, input).map(pair -> {
            MerchantOffer offer = pair.getFirst();

            DataResult<T> isDisabledResult = ops.get(input, KEY);
            isDisabledResult.flatMap(ops::getBooleanValue).ifSuccess(disabled -> {
                ((OfferExtension) offer).morejs$setDisabled(disabled);
            });

            return pair;
        });
    }

    @Override
    public <T> DataResult<T> encode(MerchantOffer offer, DynamicOps<T> ops, T prefix) {
        return codec.encode(offer, ops, prefix).map(data -> {
            boolean isDisabled = ((OfferExtension) offer).morejs$isDisabled();
            T isDisabledData = ops.createBoolean(isDisabled);
            return ops.set(data, KEY, isDisabledData);
        });
    }
}
