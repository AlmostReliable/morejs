package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.features.villager.*;
import com.almostreliable.morejs.features.villager.trades.CustomTrade;
import com.almostreliable.morejs.features.villager.trades.SimpleTrade;
import com.almostreliable.morejs.features.villager.trades.TransformableTrade;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VillagerTradingEventJS implements KubeEvent {
    private final Table<VillagerProfession, Integer, List<VillagerTrades.ItemListing>> trades;

    public VillagerTradingEventJS(Table<VillagerProfession, Integer, List<VillagerTrades.ItemListing>> allTrades) {
        trades = allTrades;
    }

    public List<VillagerTrades.ItemListing> getTrades(Holder<VillagerProfession> profession, int level) {
        Preconditions.checkArgument(1 <= level && level <= 5, "Level must be between 1 and 5");
        Preconditions.checkArgument(!profession.value().equals(VillagerProfession.NONE),
                "No or invalid profession specified");
        return Objects.requireNonNull(trades.get(profession.value(), level));
    }

    public SimpleTrade addTrade(Holder<VillagerProfession> profession, int level, TradeItem[] inputs, TradeItem output) {
        Preconditions.checkArgument(!output.isEmpty(), "Sell item cannot be empty");
        Preconditions.checkArgument(inputs.length != 0, "Buyer items cannot be empty");
        Preconditions.checkArgument(Arrays.stream(inputs).noneMatch(TradeItem::isEmpty), "Buyer items cannot be empty");

        SimpleTrade trade = VillagerUtils.createSimpleTrade(inputs, output);
        return addTrade(profession, level, trade);
    }

    public <T extends VillagerTrades.ItemListing> T addTrade(Holder<VillagerProfession> profession, int level, T trade) {
        Objects.requireNonNull(trade);
        getTrades(profession, level).add(trade);
        return trade;
    }

    public void addCustomTrade(Holder<VillagerProfession> profession, int level, TransformableTrade.Transformer transformer) {
        getTrades(profession, level).add(new CustomTrade(transformer));
    }

    public void removeTrades(TradeFilter filter) {
        forEachTrades((listings, level, profession) -> {
            var matcher = new TradeMatcher(filter, (first, second, output) -> {
                String secondStr = second == null || second.isEmpty() ? "" : " & " + second;

                var profId = BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession);
                ConsoleJS.SERVER.info(
                        "Removing villager trade for profession '" + profId + "' for level " + level + ": " + first +
                        secondStr + " -> " + output);
            });

            if (matcher.matchProfession(profession) && matcher.matchMerchantLevel(level)) {
                listings.removeIf(itemListing -> {
                    if (itemListing instanceof TradeMatcher.Filterable filterable) {
                        return filterable.matchesTradeFilter(matcher);
                    }
                    return false;
                });
            }
        });
    }

    public void removeVanillaTypedTrades() {
        forEachTrades((listings, level, profession) -> {
            listings.removeIf(VillagerUtils::isVanillaTrade);
        });
    }

    public void removeVanillaTypedTrades(List<Holder<VillagerProfession>> profession) {
        removeVanillaTypedTrades(profession, IntRange.all());
    }

    public void removeVanillaTypedTrades(List<Holder<VillagerProfession>> professions, IntRange intRange) {
        forEachTrades(professions, intRange, itemListings -> {
            itemListings.removeIf(VillagerUtils::isVanillaTrade);
        });
    }

    public void removeModdedTypedTrades() {
        forEachTrades((listings, level, profession) -> {
            listings.removeIf(VillagerUtils::isModdedTrade);
        });
    }

    public void removeModdedTypedTrades(List<Holder<VillagerProfession>> profession) {
        removeModdedTypedTrades(profession, IntRange.all());
    }

    public void removeModdedTypedTrades(List<Holder<VillagerProfession>> professions, IntRange intRange) {
        forEachTrades(professions, intRange, itemListings -> {
            itemListings.removeIf(VillagerUtils::isModdedTrade);
        });
    }

    public void forEachTrades(ForEachCallback callback) {
        trades.rowMap().forEach((profession, tradesPerLevel) -> {
            tradesPerLevel.forEach((level, itemListings) -> {
                callback.accept(itemListings, level, profession);
            });
        });
    }

    public void forEachTrades(List<Holder<VillagerProfession>> professions, IntRange intRange, Consumer<List<VillagerTrades.ItemListing>> consumer) {
        Set<VillagerProfession> filter = professions.stream().map(Holder::value).collect(Collectors.toSet());
        forEachTrades((itemListings, level, profession) -> {
            if (filter.contains(profession) && intRange.test(level)) {
                consumer.accept(itemListings);
            }
        });
    }

    public interface ForEachCallback {
        void accept(List<VillagerTrades.ItemListing> listings, int level, VillagerProfession profession);
    }
}
