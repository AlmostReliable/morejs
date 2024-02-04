package com.almostreliable.morejs.features.enchantment;

import com.almostreliable.morejs.Debug;
import com.almostreliable.morejs.MoreJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantmentMenuProcess {
    private final EnchantmentMenu menu;
    private final Int2ObjectOpenHashMap<List<EnchantmentInstance>> enchantments = new Int2ObjectOpenHashMap<>();
    private boolean freezeBroadcast = false;
    /**
     * Approach to fix changing the slot triggers multiple {@link EnchantmentMenu#slotsChanged}... Mojang pls...
     */
    private ItemStack currentItem = ItemStack.EMPTY;
    private Boolean itemIsEnchantable = null;
    private EnchantmentState state = EnchantmentState.IDLE;
    private Player player;

    public EnchantmentMenuProcess(EnchantmentMenu menu) {
        this.menu = menu;
    }

    public boolean isFreezeBroadcast() {
        return freezeBroadcast;
    }

    public void setFreezeBroadcast(boolean freezeBroadcast) {
        this.freezeBroadcast = freezeBroadcast;
    }

    public boolean matchesCurrentItem(ItemStack item) {
        return !currentItem.isEmpty() && ItemStack.matches(currentItem, item);
    }

    public void setCurrentItem(ItemStack currentItem) {
        this.currentItem = currentItem;
        if (currentItem.isEmpty()) {
            itemIsEnchantable = null;
        }
    }

    public void clearEnchantments() {
        if (Debug.ENCHANTMENT) {
            MoreJS.LOG.warn("");
            MoreJS.LOG.warn("<{}> Clearing enchantments", player);
        }
        enchantments.clear();
    }

    public boolean storeItemIsEnchantable(@Nullable Boolean override, ItemStack item) {
        itemIsEnchantable = override != null ? override : item.isEnchantable();
        return itemIsEnchantable;
    }

    public boolean isItemEnchantable(ItemStack item) {
        return itemIsEnchantable == null ? item.isEnchantable() : itemIsEnchantable;
    }

    public void setEnchantments(int index, List<EnchantmentInstance> enchantments) {
        if (Debug.ENCHANTMENT) {
            var s = formatEnchantments(enchantments);
            MoreJS.LOG.info("<{}> Setting enchantments for index {} [{}]", player, index, s);
        }

        this.enchantments.put(index, new ArrayList<>(enchantments));
    }

    private String formatEnchantments(List<EnchantmentInstance> enchantments) {
        return enchantments
                .stream()
                .map(i -> i.enchantment.getFullname(i.level).toString())
                .collect(Collectors.joining(","));
    }

    public List<EnchantmentInstance> getEnchantments(int index) {
        return this.enchantments.computeIfAbsent(index, $ -> new ArrayList<>());
    }

    public EnchantmentState getState() {
        return state;
    }

    public void setState(EnchantmentState storeEnchantments) {
        if (Debug.ENCHANTMENT) MoreJS.LOG.warn("<{}> State: {}", player, storeEnchantments);
        this.state = storeEnchantments;
    }

    public EnchantmentMenu getMenu() {
        return menu;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void prepareEvent(ItemStack item) {
        setCurrentItem(item);
        clearEnchantments();
        setFreezeBroadcast(true);
        setState(EnchantmentState.STORE_ENCHANTMENTS);
    }

    public void abortEvent(ItemStack item) {
        setCurrentItem(item);
        clearEnchantments();
        setState(EnchantmentState.IDLE);
    }

    public ItemStack getCurrentItem() {
        return currentItem;
    }
}
