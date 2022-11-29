package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.potion.PotionBrewingAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionBrewingRegisterEventForge extends PotionBrewingRegisterEvent {
    @Override
    public void addCustomBrewing(Ingredient topInput, Ingredient bottomInput, ItemStack output) {
        validate(topInput, bottomInput, output);
        BrewingRecipeRegistry.addRecipe(bottomInput, topInput, output);
    }

    @Override
    public void addPotionBrewing(Ingredient ingredient, Potion input, Potion output) {
        validateSimple(input, ingredient, output);
        PotionBrewingAccessor
                .getMixes()
                .add(new PotionBrewing.Mix<>(ForgeRegistries.POTIONS, input, ingredient, output));
    }

    @Override
    protected Potion getInputPotionFromMix(PotionBrewing.Mix<Potion> mix) {
        return mix.from.get();
    }

    @Override
    protected Potion getOutputPotionFromMix(PotionBrewing.Mix<Potion> mix) {
        return mix.to.get();
    }
}
