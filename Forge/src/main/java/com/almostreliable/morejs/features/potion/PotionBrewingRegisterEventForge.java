package com.almostreliable.morejs.features.potion;

import com.almostreliable.morejs.mixin.potion.PotionBrewingAccessor;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class PotionBrewingRegisterEventForge extends PotionBrewingRegisterEvent {
    @Override
    public void addCustomBrewing(IngredientJS topInput, IngredientJS bottomInput, ItemStackJS output) {
        validate(topInput, bottomInput, output);
        BrewingRecipeRegistry.addRecipe(bottomInput.createVanillaIngredient(),
                topInput.createVanillaIngredient(),
                output.getItemStack());
    }

    @Override
    public void addPotionBrewing(IngredientJS ingredient, Potion input, Potion output) {
        validateSimple(input, ingredient, output);
        PotionBrewingAccessor
                .getMixes()
                .add(new PotionBrewing.Mix<>(input, ingredient.createVanillaIngredient(), output));
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
