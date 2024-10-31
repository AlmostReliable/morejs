package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.events.SingleUpdateOfferEventJS;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AbstractVillager.class, priority = 42)
public class AbstractVillagerMixin {

    @Redirect(method = "addOffersFromItemListings", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/MerchantOffers;add(Ljava/lang/Object;)Z"))
    private boolean mid$foo(MerchantOffers offers, Object o, MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] possibleTrades, int maxNumbers, @Local(ordinal = 1) LocalIntRef i) {
        MerchantOffer offer = (MerchantOffer) o;
        var e = new SingleUpdateOfferEventJS((AbstractVillager) (Object) this, offers, possibleTrades, offer);
        if (Events.UPDATE_OFFER.post(e).interruptFalse()) {
            i.set(i.get() - 1);
            return false;
        }

        return offers.add(e.getOffer());
    }
}
