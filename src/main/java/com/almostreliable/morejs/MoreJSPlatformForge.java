package com.almostreliable.morejs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MoreJSPlatformForge implements MoreJSPlatform {

    @Override
    public float getEnchantmentPower(Level level, BlockPos pos) {
        return level.getBlockState(pos).getEnchantPowerBonus(level, pos);
    }

    @Override
    public int getEnchantmentCost(Level level, BlockPos blockPos, int i, int enchantmentMaxLevel, ItemStack item, int cost) {
        return 0;
//        return ForgeEventFactory.onEnchantmentLevelSet(level, blockPos, i, enchantmentMaxLevel, item, cost);
    }
}
