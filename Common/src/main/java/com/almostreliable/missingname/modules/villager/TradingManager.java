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

    @Nullable private Map<VillagerProfession, Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>>> tradesBackup;

    public void run() {
        if (tradesBackup == null) {
            tradesBackup = createMutableTradesMap();
        }

        updateVanillaTrades(tradesBackup);
        Map<VillagerProfession, Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>>> trades = createMutableTradesMap();
        new VillagerTradingEventJS(trades).post(ScriptType.SERVER, Events.VILLAGER_TRADING);
        updateVanillaTrades(trades);
    }

    private Map<VillagerProfession, Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>>> createMutableTradesMap() {
        synchronized (VillagerTrades.TRADES) {
            Map<VillagerProfession, Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>>> result = new HashMap<>();

            VillagerTrades.TRADES.forEach((profession, trades) -> {
                Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>> map = new Int2ObjectOpenHashMap<>();
                trades.forEach((level, listings) -> {
                    var newListings = new ArrayList<>(Arrays.stream(listings).toList());
                    map.computeIfAbsent(level, $ -> new ArrayList<>()).addAll(newListings);
                });
                result.put(profession, map);
            });

            return result;
        }
    }

    private void updateVanillaTrades(Map<VillagerProfession, Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>>> trades) {
        synchronized (VillagerTrades.TRADES) {
            VillagerTrades.TRADES.clear();

            trades.forEach((profession, newTrades) -> {
                Int2ObjectMap<VillagerTrades.ItemListing[]> vanillaTrades = new Int2ObjectOpenHashMap<>(newTrades.size());
                newTrades.forEach((level, itemListing) -> {
                    vanillaTrades.put(level.intValue(), itemListing.toArray(new VillagerTrades.ItemListing[0]));
                });
                VillagerTrades.TRADES.put(profession, vanillaTrades);
            });
        }
    }
}
