package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.potion.PotionBrewingAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

public class PotionBrewingRegisterEventFabric extends PotionBrewingRegisterEvent {
    @Override
    public void addCustomBrewing(Ingredient topInput, Ingredient bottomInput, ItemStack output) {
        validate(topInput, bottomInput, output);
        BrewingRegistry.addBrewing(topInput, bottomInput, output);
    }

    @Override
    public void addPotionBrewing(Ingredient ingredient, Potion input, Potion output) {
        validateSimple(input, ingredient, output);
        PotionBrewingAccessor.getMixes().add(new PotionBrewing.Mix<>(input, ingredient, output));
    }

    @Override
    protected Potion getInputPotionFromMix(PotionBrewing.Mix<Potion> mix) {
        return mix.from;
    }

    @Override
    protected Potion getOutputPotionFromMix(PotionBrewing.Mix<Potion> mix) {
        return mix.to;
    }

}
