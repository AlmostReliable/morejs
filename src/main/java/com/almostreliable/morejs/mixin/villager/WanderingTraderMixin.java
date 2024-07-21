package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.events.UpdateOfferEventJS;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin {

    @Redirect(method = "updateTrades", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/MerchantOffers;add(Ljava/lang/Object;)Z"))
    private boolean mid$foo(MerchantOffers offers, Object o) {
        MerchantOffer offer = (MerchantOffer) o;
        var e = new UpdateOfferEventJS((AbstractVillager) (Object) this,
                offers,
                VillagerTrades.WANDERING_TRADER_TRADES.get(2),
                offer);
        if (Events.UPDATE_OFFER.post(e).interruptFalse()) {
            return false;
        }

        return offers.add(e.getOffer());
    }
}
