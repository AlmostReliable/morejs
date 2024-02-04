package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.features.villager.trades.*;
import com.almostreliable.morejs.util.WeightedList;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;

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
                .filter(p -> !p.name().equals("none"))
                .toList();
    }

    public static VillagerProfession getProfession(ResourceLocation id) {
        VillagerProfession villagerProfession = Registry.VILLAGER_PROFESSION.get(id);
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

    public static TreasureMapTrade createStructureMapTrade(TradeItem[] inputs, WeightedList<Object> structures) {
        return TreasureMapTrade.forStructure(inputs, structures);
    }

    public static TreasureMapTrade createBiomeMapTrade(TradeItem[] inputs, WeightedList<Object> biomes) {
        return TreasureMapTrade.forBiome(inputs, biomes);
    }

    public static TreasureMapTrade createCustomMapTrade(TradeItem[] inputs, MapPosInfo.Provider func) {
        return new TreasureMapTrade(inputs, func);
    }

    public static EnchantedItemTrade createEnchantedItemTrade(TradeItem[] inputs, Item output) {
        return new EnchantedItemTrade(inputs, output);
    }

    public static StewTrade createStewTrade(TradeItem[] inputs, MobEffect[] effects, int duration) {
        return new StewTrade(inputs, effects, duration);
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
