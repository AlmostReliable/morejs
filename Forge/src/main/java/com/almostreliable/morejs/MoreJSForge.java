package com.almostreliable.morejs;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.core.ReloadListener;
import com.almostreliable.morejs.features.potion.PotionBrewingRegisterEventForge;
import com.almostreliable.morejs.features.villager.ForgeTradingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BuildConfig.MOD_ID)
public class MoreJSForge {

    public MoreJSForge() {
        MinecraftForge.EVENT_BUS.addListener(this::reloadListener);
        ForgeEventLoaders.load(MinecraftForge.EVENT_BUS);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onServerCommon);
        MinecraftForge.EVENT_BUS.addListener(this::onServerEnding);
    }

    private void reloadListener(AddReloadListenerEvent event) {
        event.addListener(new ReloadListener());
    }

    private void onClientCommon(FMLClientSetupEvent event) {
        new PotionBrewingRegisterEventForge().post(ScriptType.STARTUP, Events.REGISTER_POTION_BREWING);
    }

    private void onServerCommon(FMLDedicatedServerSetupEvent event) {
        new PotionBrewingRegisterEventForge().post(ScriptType.STARTUP, Events.REGISTER_POTION_BREWING);
    }

    private void onServerEnding(ServerStoppedEvent e) {
        ForgeTradingManager.INSTANCE.reset();
    }
}
