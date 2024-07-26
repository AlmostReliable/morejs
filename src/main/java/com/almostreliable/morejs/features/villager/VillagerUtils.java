package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.MoreJSBinding;
import com.almostreliable.morejs.features.villager.trades.*;
import com.almostreliable.morejs.util.BlockPosFinder;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class VillagerUtils {

    public static final Map<VillagerProfession, List<VillagerTrades.ItemListing>> CACHED_PROFESSION_TRADES = new HashMap<>();
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

    public static boolean isVanillaTypedTrade(VillagerTrades.ItemListing listing) {
        return VANILLA_TRADE_TYPES.contains(listing.getClass());
    }

    public static boolean isModdedTypedTrade(VillagerTrades.ItemListing listing) {
        return !isVanillaTypedTrade(listing) && !isCustomTypedTrade(listing);
    }

    public static boolean isCustomTypedTrade(VillagerTrades.ItemListing listing) {
        return listing instanceof TransformableTrade<?> || listing instanceof CustomTrade;
    }

    public static Collection<VillagerProfession> getProfessions() {
        return BuiltInRegistries.VILLAGER_PROFESSION
                .stream()
                .filter(p -> !p.name().equals("none"))
                .toList();
    }

    public static VillagerProfession getProfession(ResourceLocation id) {
        VillagerProfession villagerProfession = BuiltInRegistries.VILLAGER_PROFESSION.get(id);
        if (villagerProfession == VillagerProfession.NONE) {
            throw new IllegalStateException("No profession with id " + id);
        }

        return villagerProfession;
    }

    public static SimpleTrade createSimpleTrade(TradeItem[] inputs, TradeItem output) {
        return new SimpleTrade(inputs, output);
    }

    public static CustomTrade createCustomTrade(TransformableTrade.Transformer transformer) {
        return new CustomTrade(transformer);
    }

    public static TreasureMapTrade createStructureMapTrade(TradeItem[] inputs, HolderSet<Structure> structures) {
        return new TreasureMapTrade(inputs,
                (level, entity) -> MoreJSBinding.findStructure(entity.blockPosition(), level, structures, 100));
    }

    public static TreasureMapTrade createBiomeMapTrade(TradeItem[] inputs, HolderSet<Biome> biomes) {
        return new TreasureMapTrade(inputs,
                (level, entity) -> MoreJSBinding.findBiome(entity.blockPosition(), level, biomes, 250));
    }

    public static TreasureMapTrade createCustomMapTrade(TradeItem[] inputs, BlockPosFinder func) {
        return new TreasureMapTrade(inputs, func);
    }

    public static EnchantedItemTrade createEnchantedItemTrade(TradeItem[] inputs, ItemStack output) {
        return new EnchantedItemTrade(inputs, output, EnchantmentTags.ON_TRADED_EQUIPMENT);
    }

    public static EnchantedItemTrade createEnchantedItemTrade(TradeItem[] inputs, ItemStack output, HolderSet<Enchantment> enchantments) {
        return new EnchantedItemTrade(inputs, output, enchantments);
    }

    public static StewTrade createStewTrade(TradeItem[] inputs) {
        return new StewTrade(inputs);
    }

    public static PotionTrade createPotionTrade(TradeItem[] inputs) {
        return new PotionTrade(inputs);
    }

    public static void setAbstractTrades(Map<Integer, VillagerTrades.ItemListing[]> tradeMap, int level, List<VillagerTrades.ItemListing> listings) {
        tradeMap.put(level, listings.toArray(new VillagerTrades.ItemListing[0]));
    }

    public static List<VillagerTrades.ItemListing> getAbstractTrades(Map<Integer, VillagerTrades.ItemListing[]> tradeMap, int level) {
        var listings = tradeMap.get(level);
        if (listings == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(listings));
    }

    public static List<VillagerTrades.ItemListing> getVillagerTrades(VillagerProfession profession) {
        return CACHED_PROFESSION_TRADES.computeIfAbsent(profession, p -> {
            var levelListings = VillagerTrades.TRADES.get(p);
            if (levelListings == null) {
                return List.of();
            }

            ImmutableList.Builder<VillagerTrades.ItemListing> builder = ImmutableList.builder();
            for (var listings : levelListings.values()) {
                for (var listing : listings) {
                    builder.add(listing);
                }
            }

            return builder.build();
        });
    }

    public static List<VillagerTrades.ItemListing> getVillagerTrades(VillagerProfession profession, int level) {
        var levelListings = VillagerTrades.TRADES.get(profession);
        if (levelListings == null) {
            return List.of();
        }

        var listings = levelListings.get(level);
        if (listings == null) {
            return List.of();
        }

        return Arrays.asList(listings);
    }

    public static VillagerTrades.ItemListing getRandomVillagerTrade(VillagerProfession profession) {
        var trades = getVillagerTrades(profession);
        if (trades.isEmpty()) {
            throw new IllegalStateException("Profession " + profession + " has no trades");
        }

        return trades.get(ThreadLocalRandom.current().nextInt(trades.size()));
    }

    public static VillagerTrades.ItemListing getRandomVillagerTrade(VillagerProfession profession, int level) {
        var trades = getVillagerTrades(profession, level);
        if (trades.isEmpty()) {
            throw new IllegalStateException("Profession " + profession + " on level " + level + " has no trades");
        }

        return trades.get(ThreadLocalRandom.current().nextInt(trades.size()));
    }

    public static List<VillagerTrades.ItemListing> getWandererTrades(int level) {
        var listings = VillagerTrades.WANDERING_TRADER_TRADES.get(level);
        if (listings == null) {
            return List.of();
        }

        return Arrays.asList(listings);
    }

    public static VillagerTrades.ItemListing getRandomWandererTrade(int level) {
        var trades = getWandererTrades(level);
        if (trades.isEmpty()) {
            throw new IllegalStateException("Wanderer on level " + level + " has no trades");
        }

        return trades.get(ThreadLocalRandom.current().nextInt(trades.size()));
    }
}
