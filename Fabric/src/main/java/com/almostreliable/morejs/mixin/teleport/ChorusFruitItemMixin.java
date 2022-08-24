package com.almostreliable.morejs.mixin.teleport;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.teleport.EntityTeleportsEventJS;
import com.almostreliable.morejs.features.teleport.TeleportType;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitItemMixin {
    @Unique private boolean morejs$cancelTeleport = false;

    @ModifyArgs(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;randomTeleport(DDDZ)Z"))
    private void morejs$invokeTeleportEvent(Args args, ItemStack item, Level level, LivingEntity entity) {
        if (args.size() < 3) {
            return;
        }
        var e = new EntityTeleportsEventJS(entity, args.get(0), args.get(1), args.get(2), TeleportType.CHORUS_FRUIT);
        e.post(ScriptType.SERVER, Events.TELEPORT);
        if (e.isCancelled()) {
            morejs$cancelTeleport = true;
            return;
        }
        args.set(0, e.getX());
        args.set(1, e.getY());
        args.set(2, e.getZ());
    }

    @Inject(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;randomTeleport(DDDZ)Z"), cancellable = true)
    private void morejs$cancelTeleportEventIfNeeded(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (morejs$cancelTeleport) {
            cir.setReturnValue(itemStack);
            morejs$cancelTeleport = false;
        }
    }
}
