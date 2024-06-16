package com.almostreliable.morejs.mixin.enchanting;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.enchantment.EnchantmentMenuExtension;
import com.almostreliable.morejs.features.enchantment.EnchantmentTableTooltipEventJS;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin extends AbstractContainerScreen<EnchantmentMenu> {

    public EnchantmentScreenMixin(EnchantmentMenu abstractContainerMenu, Inventory inventory, Component component) {
        // Ignore this
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V"))
    private void render$InvokeEnchantmentTooltipMenu(GuiGraphics graphics, int mx, int my, float pTick, CallbackInfo ci, @Local(ordinal = 1) int slot, @Local List<Component> currentComponents) {
        if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) {
            return;
        }

        if (this.menu instanceof EnchantmentMenuExtension extension) {
            EnchantmentTableTooltipEventJS e = new EnchantmentTableTooltipEventJS(extension
                    .getMoreJsEnchantSlots()
                    .getItem(0),
                    extension.getMoreJsEnchantSlots().getItem(1),
                    Minecraft.getInstance().level,
                    Minecraft.getInstance().player,
                    this.menu,
                    slot,
                    currentComponents);
            Events.ENCHANTMENT_TABLE_TOOLTIP.post(e);
        }
    }
}
