package com.almostreliable.morejs.features.enchantment;

import com.almostreliable.morejs.features.villager.IntRange;
import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class EnchantmentTableServerEventJS extends EnchantmentTableEventJS {

    protected final EnchantmentMenuProcess state;
    private final BlockPos pos;
    private boolean itemChanged;

    public EnchantmentTableServerEventJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuProcess state) {
        super(item, secondItem, level, player, state.getMenu());
        this.pos = pos;
        this.state = state;
    }

    public BlockPos getPosition() {
        return pos;
    }

    public Data get(int index) {
        Preconditions.checkElementIndex(index, getSize());
        return new Data(index);
    }

    public int getSize() {
        return state.getMenu().costs.length;
    }

    public void setItem(ItemStack item) {
        this.itemChanged = true;
        this.item = item;
    }

    public boolean itemWasChanged() {
        return itemChanged;
    }

    public class Data {
        protected final int index;

        protected Data(int index) {
            this.index = index;
        }

        public int getRequiredLevel() {
            return EnchantmentTableServerEventJS.this.menu.costs[index];
        }

        public int getEnchantmentCount() {
            return getEnchantments().size();
        }

        public void forEachEnchantments(BiConsumer<Enchantment, Integer> consumer) {
            getEnchantments().forEach(i -> consumer.accept(i.enchantment, i.level));
        }

        public void clearEnchantments() {
            getEnchantments().clear();
        }

        public List<ResourceLocation> getEnchantmentIds() {
            return getEnchantments()
                    .stream()
                    .map(e -> Registry.ENCHANTMENT.getKey(e.enchantment))
                    .filter(Objects::nonNull)
                    .toList();
        }

        public boolean hasEnchantment(ResourceLocation id) {
            return hasEnchantment(id, IntRange.all());
        }

        public boolean hasEnchantment(ResourceLocation id, IntRange range) {
            Enchantment enchantment = Registry.ENCHANTMENT.getOptional(id).orElse(null);
            if (enchantment == null) return false;
            for (EnchantmentInstance enchantmentInstance : getEnchantments()) {
                if (enchantmentInstance.enchantment == enchantment && range.test(enchantmentInstance.level)) {
                    return true;
                }
            }
            return false;
        }

        protected List<EnchantmentInstance> getEnchantments() {
            return EnchantmentTableServerEventJS.this.state.getEnchantments(index);
        }
    }
}
