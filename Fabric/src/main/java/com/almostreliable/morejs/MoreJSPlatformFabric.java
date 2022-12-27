package com.almostreliable.morejs;

import com.almostreliable.morejs.features.villager.TradingManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MoreJSPlatformFabric implements MoreJSPlatform {

    private final TradingManager tradingManager = new TradingManager();

    @Override
    public Platform getPlatform() {
        return Platform.Fabric;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public float getEnchantmentPower(Level level, BlockPos pos) {
        return 1;
    }

    @Override
    public int getEnchantmentCost(Level level, BlockPos blockPos, int i, int enchantmentMaxLevel, ItemStack item, int cost) {
        return cost;
    }

    @Override
    public TradingManager getTradingManager() {
        return tradingManager;
    }
}
