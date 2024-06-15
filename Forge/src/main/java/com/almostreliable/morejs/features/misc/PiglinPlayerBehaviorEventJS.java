package com.almostreliable.morejs.features.misc;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Optional;

public class PiglinPlayerBehaviorEventJS extends PlayerEventJS {

    private final Player player;
    private final Piglin piglin;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Player> playerNotWearingGoldArmor;
    private PiglinBehavior behavior = PiglinBehavior.KEEP;
    private boolean ignoreHoldingCheck;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public PiglinPlayerBehaviorEventJS(Piglin piglin, Player player, Optional<Player> playerNotWearingGoldArmor) {
        this.piglin = piglin;
        this.player = player;
        this.playerNotWearingGoldArmor = playerNotWearingGoldArmor;
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
        return playerNotWearingGoldArmor.isPresent();
    }

    @Nullable
    public Player getPreviousTargetPlayer() {
        return playerNotWearingGoldArmor.orElse(null);
    }

    public PiglinBehavior getBehavior() {
        return behavior;
    }

    public void setBehavior(PiglinBehavior behavior) {
        this.behavior = behavior;
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
