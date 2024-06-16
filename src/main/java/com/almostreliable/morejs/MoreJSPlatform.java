package com.almostreliable.morejs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface MoreJSPlatform {

    float getEnchantmentPower(Level level, BlockPos pos);

    int getEnchantmentCost(Level level, BlockPos blockPos, int i, int enchantmentMaxLevel, ItemStack item, int cost);
}
