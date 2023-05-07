package com.almostreliable.morejs.mixin.entity;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.misc.PiglinAttackPlayerEventJS;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mixin(PiglinSpecificSensor.class)
public class PiglinSpecificSensorMixin {

    @Unique
    Optional<Player> targetablePlayer = Optional.empty();
    @Unique
    boolean fired;

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "doTick", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isEmpty()Z", ordinal = 4, shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void morejs$doTick(
            ServerLevel level, LivingEntity entity, CallbackInfo ci,
            Brain<?> brain, Optional<Mob> nearVisNemesis,
            Optional<Hoglin> nearVisHuntableHoglin, Optional<Hoglin> nearVisBabyHoglin,
            Optional<Piglin> nearVisBabyPiglin, Optional<LivingEntity> nearVisZombiefied,
            Optional<Player> playerNotWearingGoldArmor, Optional<Player> playerHoldingWantedItem,
            int i, List<AbstractPiglin> nearVisAdultPiglins, List<AbstractPiglin> nearAdultPiglins,
            NearestVisibleLivingEntities nearEntities, Iterator<?> iterator, LivingEntity nearEntity,
            Player player
    ) {
        if (!(entity instanceof Piglin piglinEntity) || !entity.canAttack(player)) return;

        var event = new PiglinAttackPlayerEventJS(piglinEntity, player, playerNotWearingGoldArmor.isPresent());
        fired = true;
        if (Events.PIGLIN_ATTACK_PLAYER.post(event)) {
            targetablePlayer = Optional.empty();
            return;
        }
        targetablePlayer = Optional.of(event.getPlayer());
    }

    @Inject(method = "doTick", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void morejs$setTarget(ServerLevel level, LivingEntity entity, CallbackInfo ci, Brain<?> brain) {
        if (fired) {
            brain.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, targetablePlayer);
            fired = false;
        }
    }
}
