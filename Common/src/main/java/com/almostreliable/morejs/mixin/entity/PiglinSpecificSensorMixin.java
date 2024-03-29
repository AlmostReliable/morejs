package com.almostreliable.morejs.mixin.entity;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.misc.PiglinPlayerBehaviorEventJS;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mixin(PiglinSpecificSensor.class)
public class PiglinSpecificSensorMixin {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Unique
    private Optional<Player> targetablePlayer = Optional.empty();
    @Unique private boolean ignoreHoldingCheck;
    @Unique private boolean fired;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(method = "doTick", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isEmpty()Z", ordinal = 4, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void morejs$doTick(
            ServerLevel level, LivingEntity entity, CallbackInfo ci,
            Brain<?> brain, Optional<Mob> nearVisNemesis,
            Optional<Hoglin> nearVisHuntableHoglin, Optional<Hoglin> nearVisBabyHoglin,
            Optional<Piglin> nearVisBabyPiglin, Optional<LivingEntity> nearVisZombiefied,
            Optional<Player> playerNotWearingGoldArmor, Optional<Player> playerHoldingWantedItem,
            int i, List<AbstractPiglin> nearVisAdultPiglins, List<AbstractPiglin> nearAdultPiglins,
            NearestVisibleLivingEntities nearEntities, Iterator<?> iterator, LivingEntity nearEntity) {
        if (!(entity instanceof Piglin piglinEntity)
            || !(nearEntity instanceof Player player)
            || !entity.canAttack(player)) {
            return;
        }

        var event = new PiglinPlayerBehaviorEventJS(piglinEntity, player, playerNotWearingGoldArmor);
        Events.PIGLIN_PLAYER_BEHAVIOR.post(event);
        fired = true;

        targetablePlayer = switch (event.getBehavior()) {
            case ATTACK -> Optional.of(event.getPlayer());
            case IGNORE -> Optional.empty();
            case KEEP -> playerNotWearingGoldArmor;
        };

        ignoreHoldingCheck = event.isIgnoreHoldingCheck();
    }

    @Inject(method = "doTick", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void morejs$setTarget(ServerLevel level, LivingEntity entity, CallbackInfo ci, Brain<?> brain) {
        if (fired) {
            brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, targetablePlayer);
            if (ignoreHoldingCheck) {
                brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, Optional.empty());
            }

            fired = false;
        }
    }
}
