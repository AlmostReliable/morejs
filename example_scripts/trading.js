MoreJS.villagerTrades((event) => {
    event.removeVanillaTypedTrades(["minecraft:farmer"], 2);
});

// Check trade filter
MoreJS.villagerTrades((event) => {
    event.removeTrades({
        first: "#c:crops/wheat",
        outputCount: [0, 32],
        level: 1,
        professions: "minecraft:farmer",
    });
});

MoreJS.villagerTrades((event) => {
    event.addTrade("minecraft:farmer", 2, Item.of("minecraft:diamond", 10), "minecraft:stick");
    event.addTrade("minecraft:farmer", 2, [Item.of("minecraft:diamond", 10), "minecraft:emerald"], "minecraft:stick");
});

MoreJS.updateOffer((event) => {
    if (event.offer.firstCost.id === "minecraft:beetroot") {
        console.log("Blocked beetroot!");
        return event.cancel();
    }

    event.offer.replaceItems("minecraft:potato", "minecraft:nether_star");
});

MoreJS.updateOffer((event) => {
    // In our example we will remove the clay trade from the mason and replace it
    // with a trade from the shepherd
    if (event.isProfession("minecraft:mason") && event.offer.firstCost.id === "minecraft:clay_ball") {
        // Get all level 2 trades from shepherd
        const shepherdTrades = event.getVillagerTrades("minecraft:shepherd", 2);

        // Now create a random offer from these trades
        const newOffer = event.createRandomOffer(shepherdTrades);

        // Set the new offer, which will override the coal offer
        event.setOffer(newOffer);
    }
});

MoreJS.updateOffer((event) => {
    if (event.isProfession("minecraft:cartographer") && event.random.nextDouble() < 0.7) {
        const randomBiome = Registry.of("worldgen/biome").getValues("#minecraft:is_overworld").getRandom();
        const trade = VillagerUtils.createBiomeMapTrade("minecraft:emerald_block", randomBiome).displayName(
                "Random biome"
        );
        event.setOffer(trade);
    }
});

MoreJS.updateOffer((event) => {
    if (!event.isProfession("farmer")) {
        return;
    }

    const tag = "#minecraft:is_savanna";
    const registry = Registry.of("worldgen/biome");
    const holders = registry.getValues(tag);
    const biome = registry.getRandom();
    const biomeId = registry.getId(biome);
    const biome2 = holders.getRandom();
    const biome2Id = registry.getId(biome2);
    console.log("===============");
    console.log(`All overworld biomes: ${holders.keys}`);
    console.log(`Biome: ${biomeId} contains in ${tag}: ${holders.containsValue(biome)} / ${holders.contains(biomeId)}`);
    console.log(
            `Biome 2: ${biome2Id} contains in ${tag}: ${holders.containsValue(biome2)} / ${holders.contains(biome2Id)}`
    );
    console.log(`Biomes do exist: ${registry.containsValue(biome)} / ${registry.containsValue(biome2)}`);
    console.log(registry.getValues(tag).size());
});

MoreJS.updateOffer((event) => {
    if (!event.isProfession("farmer")) {
        return;
    }

    const ItemAttributeModifiers = Java.loadClass("net.minecraft.world.item.component.ItemAttributeModifiers");

    const attributes = ItemAttributeModifiers.builder()
            .add(
                    "minecraft:generic.attack_damage",
                    {
                        id: "minecraft:base_attack_damage",
                        operation: "add_value",
                        amount: 4.0,
                    },
                    "mainhand"
            )
            .build();
    const item = Item.of("minecraft:stick").set("minecraft:attribute_modifiers", attributes);
    event.offer.output = item;
});
