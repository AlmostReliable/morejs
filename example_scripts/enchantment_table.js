MoreJS.playerEnchant((event) => {
    if (event.item.id === "minecraft:netherite_pickaxe") {
        event.player.tell("You can't enchant Netherite Pickaxes! Oops!");
        event.cancel();
    }
})

MoreJS.playerEnchant((event) => {
    const player = event.player;
    const level = event.requiredLevel;

    player.tell(
            `Player enchanted '${event.item.id} on level ${level} and ${event.enchantments.size()}x Enchantments`
    );
    player.tell(`Enchantments: ${event.enchantments}`);
});

MoreJS.enchantmentTableChanged(event => {
    const data = event.get(2)
    // data.enchantments.clear()
    data.addEnchantment("minecraft:mending", 1)

    data.setRequiredLevel(30)
    data.randomClue()

    console.log(data.enchantmentIds)
})
