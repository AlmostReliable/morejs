package com.almostreliable.morejs.mixin;

import com.almostreliable.morejs.features.villager.ForgeTradingManager;
import net.minecraftforge.common.VillagerTradingManager;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerTradingManager.class)
public class VillagerTradingManagerMixin {

    @Inject(method = "loadTrades", at = @At("RETURN"), remap = false)
    private static void postTradeLoading(ServerAboutToStartEvent e, CallbackInfo ci) {
        ForgeTradingManager.INSTANCE.start();
    }
}
