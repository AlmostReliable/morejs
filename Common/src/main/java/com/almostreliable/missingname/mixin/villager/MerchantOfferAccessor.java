package com.almostreliable.missingname.mixin.villager;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MerchantOffer.class)
public interface MerchantOfferAccessor {

    @Accessor("baseCostA")
    ItemStack getFirstInput();

    @Accessor("baseCostA")
    void setFirstInput(ItemStack itemStack);

    @Accessor("costB")
    ItemStack getSecondInput();

    @Accessor("costB")
    void setSecondInput(ItemStack itemStack);

    @Accessor("result")
    ItemStack getOutput();

    @Accessor("result")
    void setOutput(ItemStack itemStack);


    @Accessor("maxUses")
    void setMaxUses(int maxUses);

    @Accessor("demand")
    void setDemand(int demand);

    @Accessor("xp")
    void setVillagerExperience(int villagerExperience);

    @Accessor("priceMultiplier")
    void setPriceMultiplier(float priceMultiplier);
}
