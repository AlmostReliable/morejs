package com.almostreliable.morejs.features.enchantment;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.BiConsumer;

public class EnchantmentTableServerEventJS extends EnchantmentTableEventJS {

    protected final EnchantmentMenuProcess state;
    private final BlockPos pos;
    private final boolean cancelable;

    public EnchantmentTableServerEventJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuProcess state) {
        this(item, secondItem, level, pos, player, state, false);
    }

    public EnchantmentTableServerEventJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuProcess state, boolean cancelable) {
        super(item, secondItem, level, player, state.getMenu());
        this.pos = pos;
        this.state = state;
        this.cancelable = cancelable;
    }

    public BlockPos getPosition() {
        return pos;
    }

    public Data get(int index) {
        Preconditions.checkElementIndex(index, getSize());
        return new Data(index);
    }

    @Override
    public boolean canCancel() {
        return this.cancelable;
    }

    public int getSize() {
        return state.getMenu().costs.length;
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

        protected List<EnchantmentInstance> getEnchantments() {
            return EnchantmentTableServerEventJS.this.state.getEnchantments(index);
        }
    }
}
