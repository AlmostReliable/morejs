package com.almostreliable.morejs.features.enchantment;

import com.almostreliable.morejs.features.villager.IntRange;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public class EnchantmentData {

    private final List<EnchantmentInstance> enchantments;
    private final int dataSlotIndex;
    private final EnchantmentMenu menu;
    private final Level level;

    public EnchantmentData(List<EnchantmentInstance> enchantments, int dataSlotIndex, EnchantmentMenu menu, Level level) {
        this.enchantments = enchantments;
        this.dataSlotIndex = dataSlotIndex;
        this.menu = menu;
        this.level = level;
    }

    public int getRequiredLevel() {
        return menu.costs[dataSlotIndex];
    }

    public void setRequiredLevel(int level) {
        menu.costs[dataSlotIndex] = level;
    }

    @Nullable
    public EnchantmentInstance getClue() {
        Registry<Enchantment> registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var ext = EnchantmentMenuExtension.morejs$cast(menu);
        var id = ext.morejs$getEnchantmentClues()[dataSlotIndex];
        var level = ext.morejs$getLevelClues()[dataSlotIndex];
        return registry.getHolder(id).map(enchantment -> new EnchantmentInstance(enchantment, level)).orElse(null);
    }

    public void setClue(Holder<Enchantment> enchantment, int enchantmentLevel) {
        Registry<Enchantment> registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var ext = EnchantmentMenuExtension.morejs$cast(menu);
        ext.morejs$getEnchantmentClues()[dataSlotIndex] = registry.getId(enchantment.value());
        ext.morejs$getLevelClues()[dataSlotIndex] = enchantmentLevel;
    }

    public void setClue(EnchantmentInstance ei) {
        setClue(ei.enchantment, ei.level);
    }

    public void randomClue() {
        if (enchantments.isEmpty()) {
            clearClue();
            return;
        }

        var ext = EnchantmentMenuExtension.morejs$cast(menu);
        EnchantmentInstance ei = enchantments.get(ext.morejs$getRandom().nextInt(enchantments.size()));
        setClue(ei);
    }

    public void clearClue() {
        var ext = EnchantmentMenuExtension.morejs$cast(menu);
        ext.morejs$getEnchantmentClues()[dataSlotIndex] = -1;
        ext.morejs$getLevelClues()[dataSlotIndex] = 0;
    }

    public List<EnchantmentInstance> getEnchantments() {
        return enchantments;
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
        Registry<Enchantment> registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        return registry.getHolder(id).filter(ref -> {
            for (EnchantmentInstance ei : getEnchantments()) {
                if (ei.enchantment == ref && range.test(ei.level)) return true;
            }

            return false;
        }).isPresent();
    }

    public void removeEnchantments(BiPredicate<Holder<Enchantment>, Integer> consumer) {
        getEnchantments().removeIf(i -> consumer.test(i.enchantment, i.level));
    }

    public void addEnchantment(Holder<Enchantment> enchantment, int level) {
        Objects.requireNonNull(enchantment, "Enchantment does not exist");
        getEnchantments().add(new EnchantmentInstance(enchantment, level));
    }
}
