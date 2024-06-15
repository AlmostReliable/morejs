package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.features.villager.events.UpdateAbstractVillagerOffersEventJS;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractVillager.class)
public class AbstractVillagerMixin {

    private final ThreadLocal<List<MerchantOffer>> morejs$addedOffers = ThreadLocal.withInitial(ArrayList::new);

    @Inject(method = "addOffersFromItemListings", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/trading/MerchantOffers;add(Ljava/lang/Object;)Z"))
    private void mid$addOffersFromItemListings(MerchantOffers merchantOffers, VillagerTrades.ItemListing[] itemListings, int i, CallbackInfo ci) {
        morejs$addedOffers.get().add(merchantOffers.get(merchantOffers.size() - 1));
    }

    @Inject(method = "addOffersFromItemListings", at = @At("RETURN"))
    private void post$addOffersFromItemListings(MerchantOffers merchantOffers, VillagerTrades.ItemListing[] itemListings, int i, CallbackInfo ci) {
        List<MerchantOffer> addedOffers = morejs$addedOffers.get();
        UpdateAbstractVillagerOffersEventJS.invokeEvent((AbstractVillager) (Object) this,
                merchantOffers,
                itemListings,
                addedOffers);
        addedOffers.clear();
    }
}
