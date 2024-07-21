package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.features.villager.OfferExtension;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mixin(MerchantOffer.class)
public class MerchantOfferMixin implements OfferExtension {

    @Mutable @Shadow @Final private ItemCost baseCostA;
    @Mutable @Shadow @Final private Optional<ItemCost> costB;
    @Mutable @Shadow @Final private ItemStack result;
    @Mutable @Shadow @Final private int maxUses;
    @Mutable @Shadow @Final private boolean rewardExp;
    @Shadow private int demand;
    @Mutable @Shadow @Final private int xp;
    @Mutable @Shadow @Final private float priceMultiplier;
    @Unique private boolean morejs$isDisabled;

    @Override
    public MerchantOffer morejs$self() {
        return (MerchantOffer) (Object) this;
    }

    @Override
    public boolean morejs$isDisabled() {
        return this.morejs$isDisabled;
    }

    @Override
    public void morejs$setDisabled(boolean disabled) {
        this.morejs$isDisabled = disabled;
    }

    @Override
    public ItemStack morejs$getFirstCost() {
        return this.baseCostA.itemStack();
    }

    @Override
    public void morejs$setFirstCost(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        this.baseCostA = new ItemCost(
                copy.getItem().builtInRegistryHolder(),
                copy.getCount(),
                DataComponentPredicate.allOf(copy.getComponents()),
                copy
        );
    }

    @Override
    public ItemStack morejs$getSecondCost() {
        return this.costB.map(ItemCost::itemStack).orElse(ItemStack.EMPTY);
    }

    @Override
    public void morejs$setSecondCost(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        var cost = new ItemCost(
                copy.getItem().builtInRegistryHolder(),
                copy.getCount(),
                DataComponentPredicate.allOf(copy.getComponents()),
                copy
        );

        this.costB = Optional.of(cost);
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

    @Inject(method = "writeToStream", at = @At("RETURN"))
    private static void morejs$injectWriteToStream(RegistryFriendlyByteBuf buf, MerchantOffer offer, CallbackInfo ci) {

        buf.writeBoolean(((OfferExtension) offer).morejs$isDisabled());
    }

    @Inject(method = "createFromStream", at = @At("RETURN"))
    private static void morejs$injectcreateFromStream(RegistryFriendlyByteBuf buf, CallbackInfoReturnable<MerchantOffer> cir) {
        boolean isDisabled = buf.readBoolean();
        MerchantOffer offer = cir.getReturnValue();
        ((OfferExtension) offer).morejs$setDisabled(isDisabled);
    }
}
