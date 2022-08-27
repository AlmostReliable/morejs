package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.features.villager.trades.*;
import com.almostreliable.morejs.util.WeightedList;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Set;

public class VillagerUtils {
    public static final Set<Class<? extends VillagerTrades.ItemListing>> VANILLA_TRADE_TYPES = Set.of(
            VillagerTrades.DyedArmorForEmeralds.class,
            VillagerTrades.EnchantBookForEmeralds.class,
            VillagerTrades.EnchantedItemForEmeralds.class,
            VillagerTrades.ItemsForEmeralds.class,
            VillagerTrades.ItemsAndEmeraldsToItems.class,
            VillagerTrades.EmeraldForItems.class,
            VillagerTrades.TippedArrowForItemsAndEmeralds.class,
            VillagerTrades.SuspiciousStewForEmerald.class,
            VillagerTrades.TreasureMapForEmeralds.class
    );

    public static boolean isVanillaTrade(VillagerTrades.ItemListing listing) {
        return VANILLA_TRADE_TYPES.contains(listing.getClass());
    }

    public static boolean isModdedTrade(VillagerTrades.ItemListing listing) {
        return !isVanillaTrade(listing) && !isMoreJSTrade(listing);
    }

    public static boolean isMoreJSTrade(VillagerTrades.ItemListing listing) {
        return listing instanceof TransformableTrade<?> || listing instanceof CustomTrade;
    }

    public static Collection<VillagerProfession> getProfessions() {
        return Registry.VILLAGER_PROFESSION
                .stream()
                .filter(p -> !p.getName().equals("none"))
                .toList();
    }

    public static SimpleTrade createSimpleTrade(ItemStack[] inputs, ItemStack output) {
        return new SimpleTrade(inputs, output);
    }

    public static CustomTrade createCustomTrade(TransformableTrade.Transformer transformer) {
        return new CustomTrade(transformer);
    }

    public static TreasureMapTrade createStructureMapTrade(ItemStack[] inputs, WeightedList<Object> structures) {
        return TreasureMapTrade.forStructure(inputs, structures);
    }

    public static TreasureMapTrade createBiomeMapTrade(ItemStack[] inputs, WeightedList<Object> biomes) {
        return TreasureMapTrade.forBiome(inputs, biomes);
    }

    public static TreasureMapTrade createCustomMapTrade(ItemStack[] inputs, MapPosInfo.Provider func) {
        return new TreasureMapTrade(inputs, func);
    }

    public static EnchantedItemTrade createEnchantedItemTrade(ItemStack[] inputs, Item output) {
        return new EnchantedItemTrade(inputs, output);
    }

    public static StewTrade createStewTrade(ItemStack[] inputs, MobEffect[] effects, int duration) {
        return new StewTrade(inputs, effects, duration);
    }

    public static PotionTrade createPotionTrade(ItemStack[] inputs) {
        return new PotionTrade(inputs);
    }
}
