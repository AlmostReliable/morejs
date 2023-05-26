package com.almostreliable.morejs.features.villager;

import dev.latvian.mods.kubejs.bindings.ItemWrapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class TradeItem {
    public static final TradeItem EMPTY = new TradeItem(ItemStack.EMPTY, null);

    private final ItemStack itemStack;
    @Nullable private final IntRange countRange;

    public static TradeItem of(ItemStack item) {
        return new TradeItem(ItemWrapper.of(item), null);
    }

    public static TradeItem of(ItemStack item, int price) {
        return of(item, price, price);
    }

    public static TradeItem of(ItemStack item, int min, int max) {
        return new TradeItem(ItemWrapper.of(item), new IntRange(min, max));
    }

    public static TradeItem of(ItemStack item, int price, CompoundTag nbt) {
        return of(item, price, price, nbt);
    }

    public static TradeItem of(ItemStack item, int min, int max, CompoundTag nbt) {
        return new TradeItem(ItemWrapper.of(item, nbt), new IntRange(min, max));
    }

    public TradeItem(ItemStack itemStack, @Nullable IntRange countRange) {
        this.itemStack = itemStack;
        this.countRange = countRange;
    }

    public ItemStack createItemStack(RandomSource random) {
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (countRange == null) {
            return itemStack.copy();
        }

        int c = countRange.getRandom(random);
        ItemStack stack = itemStack.copy();
        stack.setCount(c);
        return stack;
    }

    public boolean isEmpty() {
        return itemStack.isEmpty();
    }
}
