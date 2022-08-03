package com.almostreliable.missingname;

import com.almostreliable.missingname.modules.villager.LevelRange;
import com.almostreliable.missingname.modules.villager.VillagerHelper;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

public class Plugin extends KubeJSPlugin {

    @Override
    public void addClasses(ScriptType type, ClassFilter filter) {
        filter.allow(BuildConfig.MOD_GROUP);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("VillagerHelper", VillagerHelper.class);
        event.add("LevelRange", LevelRange.class);
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(LevelRange.class, LevelRange::of);
    }
}
