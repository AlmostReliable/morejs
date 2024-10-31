package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.features.villager.events.PostUpdateOfferEventJS;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public class VillagerMixin {

    @Inject(method = "updateTrades", at = @At(value = "RETURN"))
    private void morejs$invokePostUpdateOffer(CallbackInfo ci) {
        var self = (AbstractVillager) (Object) this;
        PostUpdateOfferEventJS.invoke(self, self.getOffers());
    }
}
