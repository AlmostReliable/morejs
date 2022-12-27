package com.almostreliable.morejs;

import com.almostreliable.morejs.features.villager.ForgeTradingManager;
import com.almostreliable.morejs.features.villager.TradingManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class MoreJSPlatformForge implements MoreJSPlatform {

    @Override
    public Platform getPlatform() {
        return Platform.Forge;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public float getEnchantmentPower(Level level, BlockPos pos) {
        return level.getBlockState(pos).getEnchantPowerBonus(level, pos);
    }

    @Override
    public int getEnchantmentCost(Level level, BlockPos blockPos, int i, int enchantmentMaxLevel, ItemStack item, int cost) {
        return ForgeEventFactory.onEnchantmentLevelSet(level, blockPos, i, enchantmentMaxLevel, item, cost);
    }

    @Override
    public TradingManager getTradingManager() {
        return ForgeTradingManager.INSTANCE;
    }
}
