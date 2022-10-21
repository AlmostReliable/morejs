package com.almostreliable.morejs.features.enchantment;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.Random;
import java.util.function.BiPredicate;

public class EnchantmentTableChangedJS extends EnchantmentTableServerEventJS {
    private final RandomSource random;

    public EnchantmentTableChangedJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, EnchantmentMenuProcess state, RandomSource random) {
        super(item, secondItem, level, pos, state.getPlayer(), state);
        this.random = random;
    }

    @Override
    protected void afterPosted(boolean result) {
        super.afterPosted(result);

        // If the enchantments are cleared we want also to clear the required level.
        for (int i = 0; i < getSize(); i++) {
            MutableData data = get(i);
            if (data.getEnchantments().isEmpty()) {
                data.setRequiredLevel(0);
            }
        }
    }

    @Override
    public MutableData get(int index) {
        Preconditions.checkElementIndex(index, getSize());
        return new MutableData(index);
    }

    public int getSize() {
        return state.getMenu().costs.length;
    }

    public class MutableData extends Data {

        private MutableData(int index) {
            super(index);
        }

        public void setRequiredLevel(int level) {
            EnchantmentTableChangedJS.this.state.getMenu().costs[index] = level;
        }

        public void updateClue() {
            var enchantments = getEnchantments();
            var instance = enchantments.get(EnchantmentTableChangedJS.this.random.nextInt(enchantments.size()));
            EnchantmentTableChangedJS.this.menu.enchantClue[index] = Registry.ENCHANTMENT.getId(
                    instance.enchantment);
            EnchantmentTableChangedJS.this.menu.levelClue[index] = instance.level;
        }

        public void removeEnchantments(BiPredicate<Enchantment, Integer> consumer) {
            getEnchantments().removeIf(i -> consumer.test(i.enchantment, i.level));
        }

        public void addEnchantment(Enchantment enchantment, int level) {
            Objects.requireNonNull(enchantment, "Enchantment does not exist");
            getEnchantments().add(new EnchantmentInstance(enchantment, level));
        }
    }
}
