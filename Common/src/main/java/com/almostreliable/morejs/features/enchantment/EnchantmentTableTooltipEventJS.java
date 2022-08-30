package com.almostreliable.morejs.features.enchantment;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class EnchantmentTableTooltipEventJS extends EnchantmentTableEventJS {
    private final int slot;
    private final List<Object> components;
    @Nullable EnchantmentInstance clue;

    public EnchantmentTableTooltipEventJS(ItemStack item, ItemStack secondItem, Level level, Player player, EnchantmentMenu menu, int slot, List<Object> components) {
        super(item, secondItem, level, player, menu);
        this.slot = slot;
        this.components = components;
    }

    public List<Object> getLines() {
        return components;
    }

    public int getSlot() {
        return slot;
    }

    public int getRequiredLevel() {
        return menu.costs[slot];
    }

    public EnchantmentInstance getClue() {
        if (clue == null) {
            Enchantment enchantment = Enchantment.byId(menu.enchantClue[slot]);
            if (enchantment == null) {
                throw new IllegalStateException("Enchantment not found for id: " + menu.enchantClue[slot]);
            }
            clue = new EnchantmentInstance(enchantment, menu.levelClue[slot]);
        }
        return clue;
    }
}
