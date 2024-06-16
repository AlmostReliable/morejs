package com.almostreliable.morejs.mixin;

import com.almostreliable.morejs.features.villager.TradingManager;
import net.neoforged.neoforge.common.VillagerTradingManager;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VillagerTradingManager.class, priority = 1337)
public class VillagerTradingManagerMixin {

    @Inject(method = "loadTrades", at = @At("RETURN"), remap = false)
    private static void postTradeLoading(TagsUpdatedEvent e, CallbackInfo ci) {
        if (e.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
            TradingManager.INSTANCE.start();
        }
    }
}
