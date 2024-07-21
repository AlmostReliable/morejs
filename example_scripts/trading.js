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
    if (event.isProfession("minecraft:cartographer")) {

    }
})
