package com.almostreliable.morejs.features.misc;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;

public class PiglinPlayerBehaviorEventJS extends PlayerEventJS {

    private final Player player;
    private final Piglin piglin;
    private final boolean aggressiveAlready;

    private PiglinBehavior behavior = PiglinBehavior.KEEP;
    private boolean ignoreHoldingCheck;

    public PiglinPlayerBehaviorEventJS(Piglin piglin, Player player, boolean aggressiveAlready) {
        this.piglin = piglin;
        this.player = player;
        this.aggressiveAlready = aggressiveAlready;
    }

    public void setBehavior(PiglinBehavior behavior) {
        this.behavior = behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = PiglinBehavior.valueOf(behavior.toUpperCase());
    }

    public void ignoreHoldingCheck() {
        this.ignoreHoldingCheck = true;
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

    public PiglinBehavior getBehavior() {
        return behavior;
    }

    public boolean isIgnoreHoldingCheck() {
        return ignoreHoldingCheck;
    }

    public enum PiglinBehavior {
        ATTACK,
        IGNORE,
        KEEP
    }
}
