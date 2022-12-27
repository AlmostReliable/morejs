package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.events.VillagerTradingEventJS;
import com.almostreliable.morejs.features.villager.events.WandererTradingEventJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import javax.annotation.Nullable;
import java.util.*;

public class TradingManager {
    @Nullable protected Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> tradesBackup;
    @Nullable protected Int2ObjectMap<List<VillagerTrades.ItemListing>> wandererTradesBackup;

    public void invokeVillagerTradeEvent(Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> originalTrades) {
        updateVanillaTrades(originalTrades);
        var trades = createMutableTradesMapByProfessions();
        Events.VILLAGER_TRADING.post(new VillagerTradingEventJS(trades));
        updateVanillaTrades(trades);
    }

    public void invokeWanderingTradeEvent(Int2ObjectMap<List<VillagerTrades.ItemListing>> originalTrades) {
        updateVanillaWanderingTrades(originalTrades);
        var wandererTrades = toListingsListMap(VillagerTrades.WANDERING_TRADER_TRADES);
        Events.WANDERING_TRADING.post(new WandererTradingEventJS(wandererTrades));
        updateVanillaWanderingTrades(wandererTrades);
    }

    public void reload() {
        invokeVillagerTradeEvent(getTradesBackup());
        invokeWanderingTradeEvent(getWandererTradesBackup());
    }

    public Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> getTradesBackup() {
        if (tradesBackup == null) {
            tradesBackup = createMutableTradesMapByProfessions();
        }
        return tradesBackup;
    }

    public Int2ObjectMap<List<VillagerTrades.ItemListing>> getWandererTradesBackup() {
        if (wandererTradesBackup == null) {
            wandererTradesBackup = toListingsListMap(VillagerTrades.WANDERING_TRADER_TRADES);
        }
        return wandererTradesBackup;
    }

    private Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> createMutableTradesMapByProfessions() {
        synchronized (VillagerTrades.TRADES) {
            Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> result = new HashMap<>();

            VillagerTrades.TRADES.forEach((profession, trades) -> {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> map = toListingsListMap(trades);
                result.put(profession, map);
            });

            return result;
        }
    }

    private synchronized Int2ObjectMap<List<VillagerTrades.ItemListing>> toListingsListMap(Int2ObjectMap<VillagerTrades.ItemListing[]> listingsMap) {
        Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>> result = new Int2ObjectOpenHashMap<>();
        listingsMap.forEach((level, listings) -> {
            var newListings = new ArrayList<>(Arrays.stream(listings).toList());
            result.put(level.intValue(), newListings);
        });
        return result;
    }

    private synchronized Int2ObjectMap<VillagerTrades.ItemListing[]> toListingsArrayMap(Int2ObjectMap<List<VillagerTrades.ItemListing>> listingsMap) {
        Int2ObjectOpenHashMap<VillagerTrades.ItemListing[]> result = new Int2ObjectOpenHashMap<>();
        listingsMap.forEach((level, listings) -> {
            result.put(level.intValue(), listings.toArray(new VillagerTrades.ItemListing[0]));
        });
        return result;
    }

    private void updateVanillaTrades(Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> trades) {
        synchronized (VillagerTrades.TRADES) {
            VillagerTrades.TRADES.clear();

            trades.forEach((profession, newTrades) -> {
                Int2ObjectMap<VillagerTrades.ItemListing[]> vanillaTrades = toListingsArrayMap(newTrades);
                VillagerTrades.TRADES.put(profession, vanillaTrades);
            });
        }
    }

    private void updateVanillaWanderingTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        synchronized (VillagerTrades.WANDERING_TRADER_TRADES) {
            VillagerTrades.WANDERING_TRADER_TRADES.clear();
            Int2ObjectMap<VillagerTrades.ItemListing[]> map = toListingsArrayMap(trades);
            VillagerTrades.WANDERING_TRADER_TRADES.putAll(map);
        }
    }
}
