// removeVanillaSplashContainer
MoreJS.registerPotionBrewing(event => {
    event.removeContainer("minecraft:splash_potion");
});

// addCustomContainer
MoreJS.registerPotionBrewing(event => {
    event.addContainerRecipe("minecraft:apple", "minecraft:lingering_potion", "minecraft:diamond");
});

// addPotionBrewing
MoreJS.registerPotionBrewing(event => {
    event.addPotionBrewing("minecraft:apple", "minecraft:water", "minecraft:strong_regeneration");
});

// removePotionBrewing
MoreJS.registerPotionBrewing(event => {
    event.removePotionBrewing("minecraft:glowstone_dust", "minecraft:harming", "minecraft:strong_harming");
});

// addCustomBrewing
MoreJS.registerPotionBrewing(event => {
    event.addCustomBrewing("minecraft:stick", "minecraft:oak_log", "minecraft:acacia_log");
});

// removeCustomBrewing
MoreJS.registerPotionBrewing(event => {
    event.addCustomBrewing("minecraft:emerald", "minecraft:nether_star", "minecraft:diamond");
    event.removeCustomBrewing("minecraft:emerald", "minecraft:nether_star", "minecraft:diamond");
});
