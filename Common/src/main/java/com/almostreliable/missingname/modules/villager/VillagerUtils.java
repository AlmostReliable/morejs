package com.almostreliable.missingname.modules.villager;

import com.almostreliable.missingname.modules.villager.trades.*;
import com.almostreliable.missingname.util.BlockPosFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

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
        return !isVanillaTrade(listing) && !isYakaTrade(listing);
    }

    // TODO rename
    public static boolean isYakaTrade(VillagerTrades.ItemListing listing) {
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

    public static TreasureMapTrade createStructureMapTrade(ItemStack[] inputs, String structure) {
        return TreasureMapTrade.forStructure(inputs, structure);
    }

    public static TreasureMapTrade createBiomeMapTrade(ItemStack[] inputs, String biome) {
        return TreasureMapTrade.forBiome(inputs, biome);
    }

    public static TreasureMapTrade createCustomMapTrade(ItemStack[] inputs, BlockPosFunction func) {
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
