package com.almostreliable.morejs.features.enchantment;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnchantmentTableEventJS extends LevelEventJS {
    protected final EnchantmentMenu menu;
    private final ItemStack item;
    private final ItemStack secondItem;
    private final Player player;
    private final Level level;

    public EnchantmentTableEventJS(ItemStack item, ItemStack secondItem, Level level, Player player, EnchantmentMenu menu) {
        this.item = item;
        this.secondItem = secondItem;
        this.level = level;
        this.player = player;
        this.menu = menu;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getSecondItem() {
        return secondItem;
    }
}
