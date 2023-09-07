package com.almostreliable.morejs.mixin.enchanting;

import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @RemapForJS("getId")
    public ResourceLocation morejs$getId() {
        //noinspection ConstantConditions
        return BuiltInRegistries.ENCHANTMENT.getKey((Enchantment) (Object) this);
    }
}
