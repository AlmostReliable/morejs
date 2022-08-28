package com.almostreliable.morejs.mixin;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.misc.ExperiencePlayerEventJS;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyVariable(method = "giveExperiencePoints", at = @At("HEAD"), argsOnly = true)
    private int morejs$invokeExperienceEvent(int amount) {
        var e = new ExperiencePlayerEventJS((Player) (Object) this, amount);
        e.post(Events.XP_CHANGE);
        if (e.isCancelled()) {
            return Integer.MIN_VALUE;
        }
        return e.getAmount();
    }

    @Inject(method = "giveExperiencePoints", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;increaseScore(I)V"), cancellable = true)
    private void morejs$cancelExperienceEventIfNeeded(int i, CallbackInfo ci) {
        if (i == Integer.MIN_VALUE) {
            ci.cancel();
        }
    }
}
