package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.events.VillagerTradingEventJS;
import com.almostreliable.morejs.features.villager.events.WandererTradingEventJS;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TradingManager {
    public static void invokeVillagerTradeEvent() {
        synchronized (VillagerTrades.TRADES) {
            var allTrades = createTradesTable();

            Events.VILLAGER_TRADING.post(new VillagerTradingEventJS(allTrades));

            allTrades.rowMap().forEach((profession, tradesPerLevel) -> {
                Int2ObjectMap<VillagerTrades.ItemListing[]> newTrades = new Int2ObjectOpenHashMap<>();
                tradesPerLevel.forEach((level, listings) -> {
                    var listingsArray = listings.toArray(new VillagerTrades.ItemListing[0]);
                    newTrades.put(level.intValue(), listingsArray);
                });

                VillagerTrades.TRADES.put(profession, newTrades);
            });
        }
    }

    private static Table<VillagerProfession, Integer, List<VillagerTrades.ItemListing>> createTradesTable() {
        Table<VillagerProfession, Integer, List<VillagerTrades.ItemListing>> allTrades = HashBasedTable.create();
        for (var entry : VillagerTrades.TRADES.entrySet()) {
            var profession = entry.getKey();
            var trades = entry.getValue();
            trades.forEach((level, listingsArray) -> {
                List<VillagerTrades.ItemListing> listings = new ArrayList<>(Arrays.asList(listingsArray));
                allTrades.put(profession, level, listings);
            });
        }

        return allTrades;
    }

    public static void invokeWanderingTradeEvent() {
        synchronized (VillagerTrades.WANDERING_TRADER_TRADES) {
            var allTrades = new Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>>();
            VillagerTrades.WANDERING_TRADER_TRADES.forEach((integer, itemListings) -> {
                allTrades.put(integer.intValue(), new ArrayList<>(Arrays.asList(itemListings)));
            });

            Events.WANDERING_TRADING.post(new WandererTradingEventJS(allTrades));

            allTrades.forEach((level, listings) -> {
                var listingsArray = listings.toArray(new VillagerTrades.ItemListing[0]);
                VillagerTrades.WANDERING_TRADER_TRADES.put(level.intValue(), listingsArray);
            });
        }
    }
}
