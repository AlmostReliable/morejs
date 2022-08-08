package com.almostreliable.missingname.modules.villager;

import com.almostreliable.missingname.core.Events;
import dev.latvian.mods.kubejs.script.ScriptType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import javax.annotation.Nullable;
import java.util.*;

public class TradingManager {
    public static TradingManager INSTANCE = new TradingManager();

    @Nullable private Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> tradesBackup;
    @Nullable private Int2ObjectMap<List<VillagerTrades.ItemListing>> wandererTradesBackup;

    public void run() {
        if (tradesBackup == null) {
            tradesBackup = createMutableTradesMapByProfessions();
        }

        if (wandererTradesBackup == null) {
            wandererTradesBackup = toListingsListMap(VillagerTrades.WANDERING_TRADER_TRADES);
        }

        updateVanillaTrades(tradesBackup);
        var trades = createMutableTradesMapByProfessions();
        new VillagerTradingEventJS(trades).post(ScriptType.SERVER, Events.VILLAGER_TRADING);
        updateVanillaTrades(trades);

        updateVanillaWanderingTrades(wandererTradesBackup);
        var wandererTrades = toListingsListMap(VillagerTrades.WANDERING_TRADER_TRADES);
        new WandererTradingEventJS(wandererTrades).post(ScriptType.SERVER, Events.WANDERING_TRADING);
        updateVanillaWanderingTrades(wandererTrades);
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
