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
    @Mutable @Shadow @Final private boolean rewardExp;
    @Shadow private int demand;
    @Shadow private int xp;
    @Shadow private float priceMultiplier;
    @Unique private boolean morejs$isDisabled;

    @Override
    public boolean morejs$isDisabled() {
        return this.morejs$isDisabled;
    }

    @Override
    public void morejs$setDisabled(boolean disabled) {
        this.morejs$isDisabled = disabled;
    }

    @Override
    public ItemStack morejs$getFirstInput() {
        return this.baseCostA;
    }

    @Override
    public void morejs$setFirstInput(ItemStack itemStack) {
        this.baseCostA = itemStack;
    }

    @Override
    public ItemStack morejs$getSecondInput() {
        return this.costB;
    }

    @Override
    public void morejs$setSecondInput(ItemStack itemStack) {
        this.costB = itemStack;
    }

    @Override
    public ItemStack morejs$getOutput() {
        return this.result;
    }

    @Override
    public void morejs$setOutput(ItemStack itemStack) {
        this.result = itemStack;
    }

    @Override
    public void morejs$setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    @Override
    public void morejs$setDemand(int demand) {
        this.demand = demand;
    }

    @Override
    public void morejs$setVillagerExperience(int villagerExperience) {
        this.xp = villagerExperience;
    }

    @Override
    public void morejs$setPriceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public void morejs$setRewardExp(boolean rewardExp) {
        this.rewardExp = rewardExp;
    }

    @Override
    public boolean morejs$isRewardingExp() {
        return this.rewardExp;
    }

    @Inject(method = "isOutOfStock", at = @At("HEAD"), cancellable = true)
    private void morejs$cancelIfDisabled(CallbackInfoReturnable<Boolean> cir) {
        if (this.morejs$isDisabled) {
            cir.setReturnValue(true);
        }
    }
}
