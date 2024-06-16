package com.almostreliable.morejs;

import com.almostreliable.morejs.core.ReloadListener;
import com.almostreliable.morejs.features.villager.MerchantOfferCodecPatch;
import com.almostreliable.morejs.features.villager.TradingManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@Mod(BuildConfig.MOD_ID)
public class MoreJSForge {

    public MoreJSForge(IEventBus bus) {
        ForgeEventLoaders.load(bus);
        NeoForge.EVENT_BUS.addListener(this::reloadListener);
        NeoForge.EVENT_BUS.addListener(this::onServerEnding);
        MerchantOfferCodecPatch.patch();
    }

    private void reloadListener(AddReloadListenerEvent event) {
        event.addListener(new ReloadListener());
    }


    private void onServerEnding(ServerStoppedEvent e) {
        TradingManager.INSTANCE.reset();
    }
}
