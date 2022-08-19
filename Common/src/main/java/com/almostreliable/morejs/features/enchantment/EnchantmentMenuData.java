package com.almostreliable.morejs.features.enchantment;

import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EnchantmentMenuData {
    private final EnchantmentMenu owner;
    @Nullable private List<EnchantmentInstance> enchantments;

    public EnchantmentMenuData(EnchantmentMenu owner) {
        this.owner = owner;
    }

    public EnchantmentMenu getOwner() {
        return owner;
    }

    public void setEnchantments(List<EnchantmentInstance> enchantments) {
        this.enchantments = enchantments;
    }

    @Nullable
    public List<EnchantmentInstance> getEnchantments() {
        return enchantments;
    }
}
