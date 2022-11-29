package com.almostreliable.morejs.mixin.potion;

import com.almostreliable.morejs.features.potion.BrewingRegistry;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.inventory.BrewingStandMenu$PotionSlot")
public class PotionSlotMixin {

    @Inject(method = "mayPlaceItem", at = @At("RETURN"), cancellable = true)
    private static void mayPlaceCustomItem(ItemStack bottomItem, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && BrewingRegistry.mayPlaceBottomIngredient(bottomItem)) {
            cir.setReturnValue(true);
        }
    }

}
