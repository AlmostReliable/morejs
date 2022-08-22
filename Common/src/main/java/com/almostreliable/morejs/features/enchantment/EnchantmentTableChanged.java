package com.almostreliable.morejs.features.enchantment;

import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.player.PlayerJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class EnchantmentTableChanged extends LevelEventJS {

    private final ItemStack item;
    private final Level level;
    private final BlockPos pos;
    private final EnchantmentMenuProcess state;
    private final Random random;

    public EnchantmentTableChanged(ItemStack item, Level level, BlockPos pos, EnchantmentMenuProcess state, Random random) {
        this.item = item;
        this.level = level;
        this.pos = pos;
        this.state = state;
        this.random = random;
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);

        // If the enchantments are cleared we want also to clear the required level.
        for (int i = 0; i < getSize(); i++) {
            Data data = get(i);
            if (data.getEnchantments().isEmpty()) {
                data.setRequiredLevel(0);
            }
        }
    }

    public Data get(int index) {
        Preconditions.checkElementIndex(index, getSize());
        return new Data(index);
    }

    public ItemStackJS getItem() {
        return new ItemStackJS(item);
    }

    @Override
    public LevelJS getLevel() {
        return UtilsJS.getLevel(level);
    }

    public PlayerJS<?> getPlayer() {
        return getLevel().getPlayer(state.getPlayer());
    }

    public BlockPos getPosition() {
        return pos;
    }

    public int getSize() {
        return state.getMenu().costs.length;
    }

    public class Data {
        private final int index;

        private Data(int index) {
            this.index = index;
        }

        public int getRequiredLevel() {
            return EnchantmentTableChanged.this.state.getMenu().costs[index];
        }

        public void setRequiredLevel(int level) {
            EnchantmentTableChanged.this.state.getMenu().costs[index] = level;
        }

        public void updateClue() {
            var enchantments = getEnchantments();
            var instance = enchantments.get(EnchantmentTableChanged.this.random.nextInt(enchantments.size()));
            EnchantmentTableChanged.this.state.getMenu().enchantClue[index] = Registry.ENCHANTMENT.getId(
                    instance.enchantment);
            EnchantmentTableChanged.this.state.getMenu().levelClue[index] = instance.level;
        }

        public int getEnchantmentCount() {
            return getEnchantments().size();
        }

        public void forEachEnchantments(BiConsumer<Enchantment, Integer> consumer) {
            getEnchantments().forEach(i -> consumer.accept(i.enchantment, i.level));
        }

        public void removeEnchantments(BiPredicate<Enchantment, Integer> consumer) {
            getEnchantments().removeIf(i -> consumer.test(i.enchantment, i.level));
        }

        public void addEnchantment(Enchantment enchantment, int level) {
            Objects.requireNonNull(enchantment, "Enchantment does not exist");
            getEnchantments().add(new EnchantmentInstance(enchantment, level));
        }

        public void clearEnchantments() {
            getEnchantments().clear();
        }

        private List<EnchantmentInstance> getEnchantments() {
            return EnchantmentTableChanged.this.state.getEnchantments(index);
        }
    }
}
