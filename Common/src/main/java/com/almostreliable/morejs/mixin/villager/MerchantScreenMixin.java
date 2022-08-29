package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.features.villager.OfferExtension;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin extends AbstractContainerScreen<MerchantMenu> {


    @Shadow int scrollOff;
    @Shadow @Final private MerchantScreen.TradeOfferButton[] tradeOfferButtons;
    @Shadow private int shopItem;

    public MerchantScreenMixin(MerchantMenu abstractContainerMenu, Inventory inventory, Component component) {
        // IGNORE THIS.
        super(abstractContainerMenu, inventory, component);
    }

    private static boolean morejs$offerIsDisabled(MerchantOffer offer) {
        return ((OfferExtension) offer).isDisabled();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/MerchantOffer;isOutOfStock()Z", ordinal = 0))
    private void morejs$disableButtonsIfNeeded(PoseStack poseStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        try {
            var offer = this.menu.getOffers().get(this.shopItem);
            if (morejs$offerIsDisabled(offer) && this.isHovering(186, 35, 22, 21, mouseX, mouseY)) {
                this.renderTooltip(poseStack,
                        new TextComponent("You don't meet the requirements to buy this item."),
                        mouseX,
                        mouseY);
            }
        } catch (Exception e) {
            ConsoleJS.CLIENT.warn("Error while trying to get the trade offer. Mismatch for index: " + this.shopItem);
        }
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/MerchantOffer;isOutOfStock()Z", ordinal = 0))
    private boolean morejs$renderDeprecatedTooltipOnNotDisabled(MerchantOffer merchantOffer) {
        return merchantOffer.isOutOfStock() && !morejs$offerIsDisabled(merchantOffer);
    }
}
