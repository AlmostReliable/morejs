package com.almostreliable.morejs.features.enchantment;

import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.EnchantmentMenu;

import java.util.Optional;

public interface EnchantmentMenuExtension {

    static EnchantmentMenuExtension morejs$cast(EnchantmentMenu menu) {
        return (EnchantmentMenuExtension) menu;
    }

    Optional<EnchantmentMenuState> morejs$getState();

    Container morejs$getContainer();

    int[] morejs$getCosts();

    int[] morejs$getEnchantmentClues();

    int[] morejs$getLevelClues();

    RandomSource morejs$getRandom();
}
