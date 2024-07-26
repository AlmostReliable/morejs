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

MoreJS.villagerTrades(event => {
    const trade = VillagerUtils.createCustomMapTrade(["10x minecraft:diamond", "minecraft:paper"], (level, entity) => {
        const rndBiome = Registry.of("worldgen/biome").getValues("#minecraft:is_overworld").getRandom()
        return MoreUtils.findBiome(entity.blockPosition(), level, rndBiome, 250);
    })

    event.addTrade("minecraft:cartographer", 1, trade);
})

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
    if (event.isProfession("minecraft:cartographer") && event.random.nextDouble() < 0.2) {
        const randomBiome = Registry.of("worldgen/biome").getValues("#minecraft:is_savanna").getRandom();
        const trade = VillagerUtils.createBiomeMapTrade("minecraft:emerald_block", randomBiome).displayName(
                "Random savanna biome"
        );
        event.setOffer(trade);
    }
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
