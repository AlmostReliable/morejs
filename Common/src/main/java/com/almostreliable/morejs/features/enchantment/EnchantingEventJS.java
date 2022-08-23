package com.almostreliable.morejs.features.enchantment;

import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.player.PlayerJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.BiConsumer;

public class EnchantingEventJS extends LevelEventJS {

    private final ItemStack item;
    private final Level level;
    private final BlockPos pos;
    private final Player player;
    protected final EnchantmentMenuProcess state;
    private final boolean cancelable;

    public EnchantingEventJS(ItemStack item, Level level, BlockPos pos, Player player, EnchantmentMenuProcess state, boolean cancelable) {
        this.item = item;
        this.level = level;
        this.pos = pos;
        this.player = player;
        this.state = state;
        this.cancelable = cancelable;
    }

    @Override
    public LevelJS getLevel() {
        return UtilsJS.getLevel(level);
    }

    public BlockPos getPosition() {
        return pos;
    }

    public PlayerJS<?> getPlayer() {
        return getLevel().getPlayer(player);
    }

    public ItemStackJS getItem() {
        return new ItemStackJS(item);
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
            return EnchantingEventJS.this.state.getMenu().costs[index];
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
            return EnchantingEventJS.this.state.getEnchantments(index);
        }
    }
}
