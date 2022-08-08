package com.almostreliable.morejs;

import com.almostreliable.morejs.core.ReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MoreJSFabric implements ModInitializer {

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
}
