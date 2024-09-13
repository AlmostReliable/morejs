package com.almostreliable.morejs.features.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.List;

public class PlayerEnchantEventJS extends EnchantmentTableEventJS {
    private final BlockPos pos;
    private final int requiredLevel;
    private final List<EnchantmentInstance> enchantments;

    public PlayerEnchantEventJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuState state, int requiredLevel, List<EnchantmentInstance> enchantments) {
        super(item, secondItem, level, player, state.getMenu());
        this.pos = pos;
        this.requiredLevel = requiredLevel;
        this.enchantments = enchantments;
    }

    public List<EnchantmentInstance> getEnchantments() {
        return enchantments;
    }

    public List<ResourceLocation> getEnchantmentIds() {
        return getEnchantments()
                .stream()
                .flatMap(e -> e.enchantment.unwrapKey().stream())
                .map(ResourceKey::location)
                .toList();
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public BlockPos getPosition() {
        return pos;
    }
}
