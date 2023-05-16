package com.almostreliable.morejs.mixin.enchanting;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.enchantment.FilterAvailableEnchantmentsEventJS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getAvailableEnchantmentResults", at = @At("RETURN"), cancellable = true)
    private static void morejs$getAvailableEnchantmentResults(int powerLevel, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        FilterAvailableEnchantmentsEventJS event = new FilterAvailableEnchantmentsEventJS(cir.getReturnValue(), powerLevel, stack);
        Events.FILTER_AVAILABLE_ENCHANTMENTS.post(event);
        cir.setReturnValue(event.getEnchantmentInstances());
    }

}
