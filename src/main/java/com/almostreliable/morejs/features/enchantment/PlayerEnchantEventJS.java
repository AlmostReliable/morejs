package com.almostreliable.morejs.features.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PlayerEnchantEventJS extends EnchantmentTableServerEventJS {

    private final int clickedButton;

    public PlayerEnchantEventJS(int clickedButton, ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuState state) {
        super(item, secondItem, level, pos, player, state);
        this.clickedButton = clickedButton;
    }

    public int getClickedButton() {
        return clickedButton;
    }

    public EnchantmentData getSelected() {
        return get(clickedButton);
    }
}
