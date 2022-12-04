package com.almostreliable.morejs;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.core.ReloadListener;
import com.almostreliable.morejs.features.potion.PotionBrewingRegisterEventFabric;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MoreJSFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {

    @Override
    public void onInitialize() {
        ResourceManagerHelper
                .get(PackType.SERVER_DATA)
                .registerReloadListener(new IdentifiableResourceReloadListener() {
                    private final ReloadListener listener = new ReloadListener();

                    @Override
                    public ResourceLocation getFabricId() {
                        return new ResourceLocation(listener.getName());
                    }

                    @Override
                    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager rm, ProfilerFiller prepFiller, ProfilerFiller reloadFiller, Executor backgroundExecutor, Executor gameExecutor) {
                        return listener.reload(barrier, rm, prepFiller, reloadFiller, backgroundExecutor, gameExecutor);
                    }
                });
    }

    @Override
    public void onInitializeClient() {
        new PotionBrewingRegisterEventFabric().post(ScriptType.STARTUP, Events.REGISTER_POTION_BREWING);
    }

    @Override
    public void onInitializeServer() {
        new PotionBrewingRegisterEventFabric().post(ScriptType.STARTUP, Events.REGISTER_POTION_BREWING);
    }
}
