package com.almostreliable.morejs.features.misc;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;

public class PiglinAttackPlayerEventJS extends PlayerEventJS {

    private final Player player;
    private final Piglin piglin;
    private final boolean aggressiveAlready;

    public PiglinAttackPlayerEventJS(Piglin piglin, Player player, boolean aggressiveAlready) {
        this.piglin = piglin;
        this.player = player;
        this.aggressiveAlready = aggressiveAlready;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    public Piglin getPiglin() {
        return piglin;
    }

    public boolean isAggressiveAlready() {
        return aggressiveAlready;
    }
}
