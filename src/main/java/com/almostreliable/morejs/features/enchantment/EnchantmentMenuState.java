package com.almostreliable.morejs.features.enchantment;

import com.almostreliable.morejs.Debug;
import com.almostreliable.morejs.MoreJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantmentMenuState {
    private final EnchantmentMenu menu;
    private final Int2ObjectOpenHashMap<List<EnchantmentInstance>> enchantments = new Int2ObjectOpenHashMap<>();
    private final Player player;
    private boolean freezeBroadcast = false;
    /**
     * Approach to fix changing the slot triggers multiple {@link EnchantmentMenu#slotsChanged}... Mojang pls...
     */
    private ItemStack currentItem = ItemStack.EMPTY;
    private Boolean itemIsEnchantable = null;
    private EnchantmentState state = EnchantmentState.IDLE;

    public EnchantmentMenuState(EnchantmentMenu menu, Player player) {
        this.menu = menu;
        this.player = player;
    }

    public boolean isFreezeBroadcast() {
        return freezeBroadcast;
    }

    public void setFreezeBroadcast(boolean freezeBroadcast) {
        this.freezeBroadcast = freezeBroadcast;
    }

    public boolean matchesCurrentItem(ItemStack item) {
        return ItemStack.matches(currentItem, item);
    }

    public void setCurrentItem(ItemStack currentItem) {
        this.currentItem = currentItem.copy();
        if (currentItem.isEmpty()) {
            itemIsEnchantable = null;
        }
    }

    public void clearEnchantments() {
        enchantments.clear();
    }

    public boolean storeItemIsEnchantable(boolean itemIsEnchantable) {
        this.itemIsEnchantable = itemIsEnchantable;
        return this.itemIsEnchantable;
    }

    public void setEnchantments(int index, List<EnchantmentInstance> enchantments) {
        if (Debug.ENCHANTMENT) {
            var s = enchantments.stream().map(ei -> {
                var key = ei.enchantment instanceof Holder.Reference<Enchantment> ref ? ref.key().location().toString()
                                                                                      : "<unknown>";
                var level = ei.level;
                return String.format("%s <Level: %d>", key, level);
            }).collect(Collectors.joining(", "));
            MoreJS.LOG.warn("Setting enchantments for index {} [{}] <{}>", index, s, player);
        }

        this.enchantments.put(index, new ArrayList<>(enchantments));
    }

    public List<EnchantmentInstance> getEnchantments(int index) {
        return this.enchantments.computeIfAbsent(index, $ -> new ArrayList<>());
    }

    public EnchantmentState getState() {
        return state;
    }

    public void setState(EnchantmentState storeEnchantments) {
        this.state = storeEnchantments;
    }

    public EnchantmentMenu getMenu() {
        return menu;
    }

    public Player getPlayer() {
        return player;
    }

    public void prepareEvent(ItemStack item) {
        if (Debug.ENCHANTMENT) {
            MoreJS.LOG.warn("Prepare enchantment state for item <{}> with player <{}>", item, player);
        }

        setCurrentItem(item);
        clearEnchantments();
        setFreezeBroadcast(true);
        setState(EnchantmentState.STORE_ENCHANTMENTS);
    }

    public void reset(ItemStack item) {
        if (Debug.ENCHANTMENT) {
            MoreJS.LOG.warn("RESETTING enchantment state for item <{}> with player <{}>", item, player);
        }

        setCurrentItem(item);
        clearEnchantments();
        itemIsEnchantable = null;
        setState(EnchantmentState.IDLE);
    }

    public ItemStack getCurrentItem() {
        return currentItem;
    }
}
