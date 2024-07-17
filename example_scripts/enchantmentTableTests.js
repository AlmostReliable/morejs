MoreJS.playerEnchant((event) => {
    if (event.item.id === "minecraft:netherite_pickaxe") {
        event.player.tell("You can't enchant Netherite Pickaxes! Oops!");
        event.cancel();
    }
})

MoreJS.playerEnchant((event) => {
    const player = event.player;
    const selected = event.selected;
    const level = selected.requiredLevel;

    player.tell(
            `Player enchanted '${event.item.id} on level ${level} and ${selected.enchantments.size()}x Enchantments`
    );
    player.tell(`Enchantments: ${selected.enchantments}`);
});
