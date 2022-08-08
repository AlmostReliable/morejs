package com.almostreliable.missingname;

import com.almostreliable.missingname.modules.villager.LevelRange;
import com.almostreliable.missingname.modules.villager.VillagerUtils;
import com.almostreliable.missingname.util.LevelUtils;
import com.google.common.collect.Comparators;
import com.google.common.collect.MoreCollectors;
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
    public void addClasses(ScriptType type, ClassFilter filter) {
        filter.allow(BuildConfig.MOD_GROUP);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("VillagerUtils", VillagerUtils.class);
        event.add("LevelRange", LevelRange.class);
        event.add("LevelUtils", LevelUtils.class);

        event.add("Optional", Optional.class);
        event.add("Collectors", Collectors.class);
        event.add("Comparator", Comparator.class);
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(LevelRange.class, LevelRange::of);
    }
}
