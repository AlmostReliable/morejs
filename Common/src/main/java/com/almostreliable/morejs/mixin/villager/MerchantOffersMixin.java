package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.MoreJS;
import com.almostreliable.morejs.features.villager.OfferExtension;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(MerchantOffers.class)
public class MerchantOffersMixin {

    @Inject(method = "createFromStream", at = @At("RETURN"))
    private static void morejs$createFromStream(FriendlyByteBuf friendlyByteBuf, CallbackInfoReturnable<MerchantOffers> cir) {
        cir.getReturnValue().forEach(o -> {
            boolean disabled = friendlyByteBuf.readBoolean();
            ((OfferExtension) o).setDisabled(disabled);
        });
    }

    @Inject(method = "writeToStream", at = @At("RETURN"))
    private void morejs$writeCustomData(FriendlyByteBuf friendlyByteBuf, CallbackInfo ci) {
        for (MerchantOffer o : morejs$getSelf()) {
            friendlyByteBuf.writeBoolean(((OfferExtension) o).isDisabled());
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("RETURN"))
    private void morejs$storeDisabled(@Nullable CompoundTag tag, CallbackInfo ci) {
        if (tag == null) return;
        try {
            MerchantOffers offers = morejs$getSelf();
            if (!tag.contains(MoreJS.DISABLED_TAG)) return;
            ListTag list = tag.getList(MoreJS.DISABLED_TAG, Tag.TAG_BYTE);
            if (list.size() != offers.size()) return;
            for (int i = 0; i < offers.size(); i++) {
                Tag hopefullyByteTag = list.get(i);
                boolean disabled = hopefullyByteTag instanceof ByteTag bt && bt.getAsByte() != 0;
                ((OfferExtension) offers.get(i)).setDisabled(disabled);
            }
        } catch (Exception e) {
            MoreJS.LOG.warn("Failed to receive disabled offers", e);
        }
    }

    @Inject(method = "createTag", at = @At("RETURN"))
    private void appendDisabledOnTag(CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag tag = cir.getReturnValue();
        if (tag == null) return;

        try {
            MerchantOffers offers = morejs$getSelf();
            ListTag list = new ListTag();
            for (MerchantOffer offer : offers) {
                boolean disabled = ((OfferExtension) offer).isDisabled();
                list.add(ByteTag.valueOf(disabled));
            }
            tag.put(MoreJS.DISABLED_TAG, list);
        } catch (Exception e) {
            MoreJS.LOG.warn("Failed to store disabled offers", e);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private MerchantOffers morejs$getSelf() {
        return (MerchantOffers) (Object) this;
    }
}
