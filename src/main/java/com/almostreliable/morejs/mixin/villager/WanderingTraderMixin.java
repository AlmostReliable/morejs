package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.features.villager.events.UpdateAbstractVillagerOffersEventJS;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {

    public WanderingTraderMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "updateTrades", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/trading/MerchantOffers;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void morejs$invokeUpdateRareTrades(CallbackInfo ci, VillagerTrades.ItemListing[] normalListings, VillagerTrades.ItemListing[] rareListings, MerchantOffers merchantOffers, int i, VillagerTrades.ItemListing itemListing, MerchantOffer offer) {
        UpdateAbstractVillagerOffersEventJS.invokeEvent((AbstractVillager) (Object) this,
                this.getOffers(),
                rareListings,
                List.of(offer));
    }
}
