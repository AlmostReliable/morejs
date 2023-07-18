package com.almostreliable.morejs.mixin.teleport;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.teleport.EntityTeleportsEventJS;
import com.almostreliable.morejs.features.teleport.TeleportType;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearl.class)
public class ThrownEnderPearlMixin {

    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSleeping()Z"), cancellable = true)
    private void morejs$invokeEvent(HitResult hitResult, CallbackInfo ci) {
        var self = (ThrownEnderpearl) (Object) this;
        //noinspection ConstantConditions
        var event = new EntityTeleportsEventJS(self.getOwner(),
                self.getX(),
                self.getY(),
                self.getZ(),
                TeleportType.ENDER_PEARL);
        if (Events.TELEPORT.post(event).interruptFalse()) {
            ci.cancel();
            self.discard();
            return;
        }
        self.setPos(event.getX(), event.getY(), event.getZ());
    }


}
