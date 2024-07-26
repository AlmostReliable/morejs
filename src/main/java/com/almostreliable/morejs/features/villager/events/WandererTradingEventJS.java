package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.features.villager.TradeFilter;
import com.almostreliable.morejs.features.villager.TradeItem;
import com.almostreliable.morejs.features.villager.TradeMatcher;
import com.almostreliable.morejs.features.villager.VillagerUtils;
import com.almostreliable.morejs.features.villager.trades.CustomTrade;
import com.almostreliable.morejs.features.villager.trades.SimpleTrade;
import com.almostreliable.morejs.features.villager.trades.TransformableTrade;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WandererTradingEventJS implements KubeEvent {
    private final Int2ObjectMap<List<VillagerTrades.ItemListing>> trades;

    public WandererTradingEventJS(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        this.trades = trades;
    }

    public List<VillagerTrades.ItemListing> getTrades(int level) {
        checkLevel(level);
        return trades.computeIfAbsent(level, $ -> new ArrayList<>());
    }

    public SimpleTrade addTrade(int level, TradeItem[] inputs, TradeItem output) {
        Preconditions.checkArgument(!output.isEmpty(), "Sell item cannot be empty");
        Preconditions.checkArgument(inputs.length != 0, "Buyer items cannot be empty");
        Preconditions.checkArgument(Arrays.stream(inputs).noneMatch(TradeItem::isEmpty), "Buyer items cannot be empty");

        SimpleTrade trade = VillagerUtils.createSimpleTrade(inputs, output);
        return addTrade(level, trade);
    }

    public <T extends VillagerTrades.ItemListing> T addTrade(int level, T trade) {
        Objects.requireNonNull(trade);
        getTrades(level).add(trade);
        return trade;
    }

    public void addCustomTrade(int level, TransformableTrade.Transformer transformer) {
        getTrades(level).add(new CustomTrade(transformer));
    }

    public void removeTrades(TradeFilter filter) {
        trades.forEach((level, listings) -> {
            var matcher = new TradeMatcher(filter, (first, second, output) -> ConsoleJS.SERVER.info(
                    "Removing wanderer trade for level " + level + ": " + first + " & " + second + " -> " + output));

            if (matcher.matchMerchantLevel(level)) {
                listings.removeIf(itemListing -> {
                    if (itemListing instanceof TradeMatcher.Filterable filterable) {
                        return filterable.matchesTradeFilter(matcher);
                    }
                    return false;
                });
            }
        });
    }

    public void removeVanillaTrades() {
        getTrades(1).removeIf(VillagerUtils::isVanillaTypedTrade);
        getTrades(2).removeIf(VillagerUtils::isVanillaTypedTrade);
    }

    public void removeVanillaTrades(int level) {
        checkLevel(level);
        getTrades(level).removeIf(VillagerUtils::isVanillaTypedTrade);
    }

    public void removeModdedTrades() {
        getTrades(1).removeIf(VillagerUtils::isModdedTypedTrade);
        getTrades(2).removeIf(VillagerUtils::isModdedTypedTrade);
    }

    public void removeModdedTrades(int level) {
        checkLevel(level);
        getTrades(level).removeIf(VillagerUtils::isModdedTypedTrade);
    }

    private void checkLevel(int level) {
        Preconditions.checkArgument(1 <= level && level <= 2, "Level must be between 1 and 2");
    }
}
