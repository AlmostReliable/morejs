package com.almostreliable.morejs.features.enchantment;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.player.PlayerJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
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
    public LevelJS getLevel() {
        return UtilsJS.getLevel(level);
    }

    public PlayerJS<?> getPlayer() {
        return getLevel().getPlayer(player);
    }

    public ItemStackJS getItem() {
        return new ItemStackJS(item);
    }

    public ItemStackJS getSecondItem() {
        return new ItemStackJS(secondItem);
    }
}
