package com.almostreliable.morejs.features.enchantment;

import com.almostreliable.morejs.features.villager.IntRange;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventResult;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.BiConsumer;

public class EnchantmentTableServerEventJS extends EnchantmentTableEventJS {

    protected final EnchantmentMenuState state;
    private final BlockPos pos;
    private boolean itemChanged;
    private final Int2ObjectOpenHashMap<EnchantmentData> enchantments = new Int2ObjectOpenHashMap<>();

    public EnchantmentTableServerEventJS(ItemStack item, ItemStack secondItem, Level level, BlockPos pos, Player player, EnchantmentMenuState state) {
        super(item, secondItem, level, player, state.getMenu());
        this.pos = pos;
        this.state = state;
    }

    public BlockPos getPosition() {
        return pos;
    }

    public EnchantmentData get(int index) {
        Preconditions.checkElementIndex(index, getSize());
        return enchantments.computeIfAbsent(index, i -> {
            List<EnchantmentInstance> eis = state.getEnchantments(i);
            return new EnchantmentData(eis, i, state.getMenu(), getLevel());
        });
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

    @Override
    public void afterPosted(EventResult result) {
        super.afterPosted(result);

        // If the enchantments are cleared we want also to clear the required level.
        enchantments.forEach((integer, data) -> {
            if (data.getEnchantments().isEmpty()) {
                data.setRequiredLevel(0);
                data.clearClue();
            }
        });
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

        public void forEachEnchantments(BiConsumer<Holder<Enchantment>, Integer> consumer) {
            getEnchantments().forEach(i -> consumer.accept(i.enchantment, i.level));
        }

        public void clearEnchantments() {
            getEnchantments().clear();
        }

        public List<ResourceLocation> getEnchantmentIds() {
            return getEnchantments()
                    .stream()
                    .flatMap(e -> e.enchantment.unwrapKey().stream())
                    .map(ResourceKey::location)
                    .toList();
        }

        public boolean hasEnchantment(ResourceLocation id) {
            return hasEnchantment(id, IntRange.all());
        }

        public boolean hasEnchantment(ResourceLocation id, IntRange range) {
            Registry<Enchantment> registry = getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            return registry.getHolder(id).filter(ref -> {
                for (EnchantmentInstance ei : getEnchantments()) {
                    if (ei.enchantment == ref && range.test(ei.level)) return true;
                }

                return false;
            }).isPresent();
        }

        protected List<EnchantmentInstance> getEnchantments() {
            return EnchantmentTableServerEventJS.this.state.getEnchantments(index);
        }
    }
}
