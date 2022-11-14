package com.almostreliable.morejs;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.IntRange;
import com.almostreliable.morejs.features.villager.TradeFilter;
import com.almostreliable.morejs.features.villager.VillagerUtils;
import com.almostreliable.morejs.util.WeightedList;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class Plugin extends KubeJSPlugin {

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.allow(BuildConfig.MOD_GROUP);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("VillagerUtils", VillagerUtils.class);
        event.add("MoreJS", MoreJSBinding.class);

        event.add("Optional", Optional.class);
        event.add("Collectors", Collectors.class);
        event.add("Comparator", Comparator.class);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(IntRange.class, MoreJSBinding::range);
        typeWrappers.registerSimple(WeightedList.class, MoreJSBinding::ofWeightedList);
        typeWrappers.registerSimple(TradeFilter.class, MoreJSBinding::ofTradeFilter);
    }

    @Override
    public void registerEvents() {
        Events.GROUP.register();
    }
}
