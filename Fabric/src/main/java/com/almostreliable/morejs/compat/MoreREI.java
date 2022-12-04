package com.almostreliable.morejs.compat;

import com.almostreliable.morejs.features.potion.BrewingRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.common.displays.brewing.BrewingRecipe;

public class MoreREI implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BrewingRegistry
                .getEntries()
                .forEach(entry -> registry.add(new BrewingRecipe(entry.bottomInput(),
                        entry.topInput(),
                        entry.output())));
    }
}
