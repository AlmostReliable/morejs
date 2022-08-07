package com.almostreliable.missingname.modules.villager;

import com.almostreliable.missingname.modules.villager.trades.CustomTrade;
import com.almostreliable.missingname.modules.villager.trades.SimpleTrade;
import com.almostreliable.missingname.modules.villager.trades.TransformableTrade;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.EventJS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class WandererTradingEventJS extends EventJS {
    private final Int2ObjectMap<List<VillagerTrades.ItemListing>> trades;

    public WandererTradingEventJS(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        this.trades = trades;
    }

    public List<VillagerTrades.ItemListing> getTrades(int level) {
        checkLevel(level);
        return trades.computeIfAbsent(level, $ -> new ArrayList<>());
    }

    public SimpleTrade addTrade(int level, ItemStack[] inputs, ItemStack output) {
        Preconditions.checkArgument(!output.isEmpty(), "Sell item cannot be empty");
        Preconditions.checkArgument(inputs.length != 0, "Buyer items cannot be empty");
        Preconditions.checkArgument(Arrays.stream(inputs).noneMatch(ItemStack::isEmpty),
                "Buyer items cannot be empty");

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

    public void removeVanillaTrades(int level) {
        checkLevel(level);
        getTrades(level).removeIf(VillagerUtils::isVanillaTrade);
    }

    public void removeModdedTrades(int level) {
        checkLevel(level);
        getTrades(level).removeIf(VillagerUtils::isModdedTrade);
    }

    private void checkLevel(int level) {
        Preconditions.checkArgument(1 <= level && level <= 2, "Level must be between 1 and 2");
    }
}
