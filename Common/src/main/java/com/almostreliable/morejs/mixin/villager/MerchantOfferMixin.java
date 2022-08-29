package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.features.villager.OfferExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantOffer.class)
public class MerchantOfferMixin implements OfferExtension {

    @Mutable @Shadow @Final private ItemStack baseCostA;
    @Mutable @Shadow @Final private ItemStack costB;
    @Mutable @Shadow @Final private ItemStack result;
    @Mutable @Shadow @Final private int maxUses;
    @Shadow private int demand;
    @Shadow private int xp;
    @Shadow private float priceMultiplier;
    @Unique private boolean morejs$isDisabled;

    @Override
    public boolean isDisabled() {
        return this.morejs$isDisabled;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.morejs$isDisabled = disabled;
    }

    @Override
    public ItemStack getFirstInput() {
        return this.baseCostA;
    }

    @Override
    public void setFirstInput(ItemStack itemStack) {
        this.baseCostA = itemStack;
    }

    @Override
    public ItemStack getSecondInput() {
        return this.costB;
    }

    @Override
    public void setSecondInput(ItemStack itemStack) {
        this.costB = itemStack;
    }

    @Override
    public ItemStack getOutput() {
        return this.result;
    }

    @Override
    public void setOutput(ItemStack itemStack) {
        this.result = itemStack;
    }

    @Override
    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    @Override
    public void setDemand(int demand) {
        this.demand = demand;
    }

    @Override
    public void setVillagerExperience(int villagerExperience) {
        this.xp = villagerExperience;
    }

    @Override
    public void setPriceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    @Inject(method = "isOutOfStock", at = @At("HEAD"), cancellable = true)
    private void morejs$cancelIfDisabled(CallbackInfoReturnable<Boolean> cir) {
        if (this.morejs$isDisabled) {
            cir.setReturnValue(true);
        }
    }
}
