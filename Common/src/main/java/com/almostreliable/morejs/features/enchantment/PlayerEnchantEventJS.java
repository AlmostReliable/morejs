package com.almostreliable.morejs.features.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.List;

public class PlayerEnchantEventJS extends EnchantmentTableServerEventJS {

    private final int clickedButton;

    public PlayerEnchantEventJS(int clickedButton, ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuProcess state) {
        super(item, secondItem, level, pos, player, state);
        this.clickedButton = clickedButton;
    }

    public int getClickedButton() {
        return clickedButton;
    }

    public int getCosts() {
        return menu.costs[clickedButton];
    }

    public List<ResourceLocation> getEnchantmentIds() {
        return state
                .getEnchantments(clickedButton)
                .stream()
                .map(ei -> ei.enchantment)
                .map(BuiltInRegistries.ENCHANTMENT::getKey)
                .toList();
    }

    public List<EnchantmentInstance> getEnchantments() {
        return state.getEnchantments(clickedButton);
    }
}
