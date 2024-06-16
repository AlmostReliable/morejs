package com.almostreliable.morejs.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PotionBrewing.Builder.class)
public interface PotionBrewingBuilderAccessor {

    @Accessor("containers")
    List<Ingredient> morejs$getContainers();

    @Accessor("potionMixes")
    List<PotionBrewing.Mix<Potion>> morejs$getPotionMixes();

    @Accessor("containerMixes")
    List<PotionBrewing.Mix<Item>> morejs$getContainerMixes();

    @Accessor(value = "recipes", remap = false)
    List<IBrewingRecipe> morejs$getRecipes();
}
