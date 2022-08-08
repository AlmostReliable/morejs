package com.almostreliable.missingname.modules.villager;

import com.almostreliable.missingname.modules.villager.trades.CustomTrade;
import com.almostreliable.missingname.modules.villager.trades.SimpleTrade;
import com.almostreliable.missingname.modules.villager.trades.TransformableTrade;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VillagerTradingEventJS extends EventJS {
    private final Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> trades;

    public VillagerTradingEventJS(Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> trades) {
        this.trades = trades;
    }

    public List<VillagerTrades.ItemListing> getTrades(VillagerProfession profession, int level) {
        Preconditions.checkArgument(1 <= level && level <= 5, "Level must be between 1 and 5");
        Preconditions.checkArgument(!profession.equals(VillagerProfession.NONE), "No or invalid profession specified");
        return trades
                .computeIfAbsent(profession, $ -> new Int2ObjectOpenHashMap<>())
                .computeIfAbsent(level, $ -> new ArrayList<>());
    }

    public SimpleTrade addTrade(VillagerProfession profession, int level, ItemStack[] inputs, ItemStack output) {
        Preconditions.checkArgument(!output.isEmpty(), "Sell item cannot be empty");
        Preconditions.checkArgument(inputs.length != 0, "Buyer items cannot be empty");
        Preconditions.checkArgument(Arrays.stream(inputs).noneMatch(ItemStack::isEmpty),
                "Buyer items cannot be empty");

        SimpleTrade trade = VillagerUtils.createSimpleTrade(inputs, output);
        return addTrade(profession, level, trade);
    }

    public <T extends VillagerTrades.ItemListing> T addTrade(VillagerProfession profession, int level, T trade) {
        Objects.requireNonNull(trade);
        getTrades(profession, level).add(trade);
        return trade;
    }

    public void addCustomTrade(VillagerProfession profession, int level, TransformableTrade.Transformer transformer) {
        getTrades(profession, level).add(new CustomTrade(transformer));
    }

    public void removeVanillaTrades() {
        forEachTrades((listings, level, profession) -> {
            listings.removeIf(VillagerUtils::isVanillaTrade);
        });
    }

    public void removeVanillaTrades(VillagerProfession[] professions, LevelRange levelRange) {
        forEachTrades(professions, levelRange, itemListings -> {
            itemListings.removeIf(VillagerUtils::isVanillaTrade);
        });
    }

    public void removeModdedTrades() {
        forEachTrades((listings, level, profession) -> {
            listings.removeIf(VillagerUtils::isModdedTrade);
        });
    }

    public void removeModdedTrades(VillagerProfession[] professions, LevelRange levelRange) {
        forEachTrades(professions, levelRange, itemListings -> {
            itemListings.removeIf(VillagerUtils::isModdedTrade);
        });
    }

    public void forEachTrades(ForEachCallback callback) {
        trades.forEach((profession, levelTrades) -> {
            levelTrades.forEach((level, itemListings) -> {
                callback.accept(itemListings, level, profession);
            });
        });
    }

    public void forEachTrades(VillagerProfession[] professions, LevelRange levelRange, Consumer<List<VillagerTrades.ItemListing>> consumer) {
        Set<VillagerProfession> filter = Arrays.stream(professions).collect(Collectors.toSet());
        forEachTrades((itemListings, level, profession) -> {
            if (filter.contains(profession) && levelRange.test(level)) {
                consumer.accept(itemListings);
            }
        });
    }

    public interface ForEachCallback {
        void accept(List<VillagerTrades.ItemListing> listings, int level, VillagerProfession profession);
    }
}
