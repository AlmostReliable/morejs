package com.almostreliable.morejs;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.IntRange;
import com.almostreliable.morejs.features.villager.TradeFilter;
import com.almostreliable.morejs.features.villager.TradeItem;
import com.almostreliable.morejs.features.villager.VillagerUtils;
import com.almostreliable.morejs.util.WeightedList;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class Plugin implements KubeJSPlugin {

    @Override
    public void registerBindings(BindingRegistry event) {
        event.add("VillagerUtils", VillagerUtils.class);
        event.add("TradeItem", TradeItem.class);
        event.add("MoreUtils", MoreJSBinding.class);
        event.add("EnchantmentInstance", EnchantmentInstance.class);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry typeWrappers) {
        typeWrappers.register(TradeItem.class, MoreJSBinding::ofTradeItem);
        typeWrappers.register(IntRange.class, MoreJSBinding::range);
        typeWrappers.register(WeightedList.class, MoreJSBinding::ofWeightedList);
        typeWrappers.register(TradeFilter.class, MoreJSBinding::ofTradeFilter);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(Events.GROUP);
    }
}
