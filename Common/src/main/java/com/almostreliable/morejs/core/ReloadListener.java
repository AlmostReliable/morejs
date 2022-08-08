package com.almostreliable.morejs.core;

import com.almostreliable.morejs.BuildConfig;
import com.almostreliable.morejs.features.villager.TradingManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ReloadListener implements PreparableReloadListener {
    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller preparationProfile, ProfilerFiller reloadProfile, Executor backgroundExecutor, Executor gameExecutor) {
        return CompletableFuture.runAsync(() -> {}, gameExecutor).thenCompose(barrier::wait).thenAccept($ -> {
            TradingManager.INSTANCE.run();
        });
    }

    @Override
    public String getName() {
        return BuildConfig.MOD_ID + ":reloadlistener";
    }
}
