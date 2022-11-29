package com.almostreliable.morejs.mixin.potion;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {
    @Accessor("POTION_MIXES")
    static List<PotionBrewing.Mix<Potion>> getMixes() {
        throw new AssertionError("Accessor was not transformed");
    }
}
