package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.villager.events.StartTradingEventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantMenu.class)
public abstract class MerchantMenuMixin extends AbstractContainerMenu {

    protected MerchantMenuMixin(@Nullable MenuType<?> menuType, int i) {
        // IGNORE THIS.
        super(menuType, i);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/trading/Merchant;)V", at = @At("RETURN"))
    public void invokeOpenTradeEvent(int windowId, Inventory inventory, Merchant merchant, CallbackInfo ci) {
        if (merchant instanceof ClientSideMerchant && !(inventory.player instanceof ServerPlayer)) return;
        new StartTradingEventJS(inventory.player, merchant).post(ScriptType.SERVER, Events.PLAYER_START_TRADING);
    }
}
