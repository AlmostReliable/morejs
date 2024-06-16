package com.almostreliable.morejs.features.enchantment;

import com.almostreliable.morejs.BuildConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
    private final List<Component> components;
    @Nullable EnchantmentInstance clue;

    public EnchantmentTableTooltipEventJS(ItemStack item, ItemStack secondItem, Level level, Player player, EnchantmentMenu menu, int slot, List<Component> components) {
        super(item, secondItem, level, player, menu);
        this.slot = slot;
        this.components = components;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void removeComponent(int index) {
        components.remove(index);
    }

    public void clearComponents() {
        components.clear();
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void addComponent(int index, Component component) {
        components.add(index, component);
    }

    public int getSlot() {
        return slot;
    }

    public int getRequiredLevel() {
        return menu.costs[slot];
    }

    public EnchantmentInstance getClue() {
        if (clue == null) {
            int enchantmentIntId = menu.enchantClue[slot];
            int level = menu.levelClue[slot];
            Registry<Enchantment> enchantments = getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            clue = enchantments
                    .getHolder(enchantmentIntId)
                    .map(ref -> new EnchantmentInstance(ref, level))
                    .orElseThrow(() -> new IllegalStateException("Enchantment not found for id: " + enchantmentIntId));
        }

        return clue;
    }

    public ResourceLocation getClueId() {
        return getClue().enchantment
                .unwrapKey()
                .map(ResourceKey::location)
                .orElse(ResourceLocation.fromNamespaceAndPath(
                        BuildConfig.MOD_ID, "unknown_id"));
    }
}
