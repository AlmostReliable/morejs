package com.almostreliable.morejs.features.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class IsEnchantableEventJS extends EnchantmentTableServerEventJS {
    private final MutableBoolean enchantable;

    public IsEnchantableEventJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, EnchantmentMenuState state, MutableBoolean enchantable) {
        super(item, secondItem, level, pos, state.getPlayer(), state);
        this.enchantable = enchantable;
    }

    public void setIsEnchantable(boolean flag) {
        enchantable.setValue(flag);
    }

    public boolean getIsEnchantable() {
        return enchantable.getValue();
    }
}
