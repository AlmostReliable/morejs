package com.almostreliable.morejs.features.enchantment;

import com.almostreliable.morejs.MoreJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EnchantmentMenuProcess {
    private final EnchantmentMenu menu;
    private boolean freezeBroadcast = false;
    /**
     * Approach to fix changing the slot triggers multiple {@link EnchantmentMenu#slotsChanged}... Mojang pls...
     */
    private ItemStack currentItem = ItemStack.EMPTY;

    private final Int2ObjectOpenHashMap<List<EnchantmentInstance>> enchantments = new Int2ObjectOpenHashMap<>();
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
    }

    public void clearEnchantments() {
        MoreJS.LOG.warn("Clearing enchantments");
        enchantments.clear();
    }

    public void setEnchantments(int index, List<EnchantmentInstance> enchantments) {
        MoreJS.LOG.info("Setting enchantments for index " + index + ": " + enchantments);
        this.enchantments.put(index, new ArrayList<>(enchantments));
    }

    public List<EnchantmentInstance> getEnchantments(int index) {
        return this.enchantments.computeIfAbsent(index, $ -> new ArrayList<>());
    }

    public void setState(EnchantmentState storeEnchantments) {
        MoreJS.LOG.warn("State: " + storeEnchantments);
        this.state = storeEnchantments;
    }

    public EnchantmentState getState() {
        return state;
    }

    public EnchantmentMenu getMenu() {
        return menu;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
