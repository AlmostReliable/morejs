package com.almostreliable.morejs.mixin.potion;

import com.almostreliable.morejs.features.potion.BrewingRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {

    @Inject(method = "hasMix", at = @At("RETURN"), cancellable = true)
    private static void hasCustomPotionMix(ItemStack bottomInput, ItemStack topInput, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && BrewingRegistry.hasPotionMix(bottomInput, topInput)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isPotionIngredient", at = @At("RETURN"), cancellable = true)
    private static void isCustomPotionIngredient(ItemStack topInput, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && BrewingRegistry.isPotionIngredient(topInput)) {
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "mix", at = @At("RETURN"), cancellable = true)
    private static void getCustomPotionMix(ItemStack topInput, ItemStack bottomInput, CallbackInfoReturnable<ItemStack> cir) {
        if (cir.getReturnValue() == bottomInput) {
            ItemStack mix = BrewingRegistry.mix(topInput, bottomInput);
            if(mix != null) {
                cir.setReturnValue(mix);
            }
        }
    }
}
