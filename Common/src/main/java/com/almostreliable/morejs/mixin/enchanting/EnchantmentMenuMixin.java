package com.almostreliable.morejs.mixin.enchanting;

import com.almostreliable.morejs.Debug;
import com.almostreliable.morejs.MoreJS;
import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.enchantment.*;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin extends AbstractContainerMenu implements EnchantmentMenuExtension {
    @Unique
    private EnchantmentMenuProcess morejs$process;
    @Shadow @Final private Container enchantSlots;
    @Shadow @Final private ContainerLevelAccess access;

    @Shadow @Final private RandomSource random;

    protected EnchantmentMenuMixin(@Nullable MenuType<?> menuType, int i) {
        // Ignore this
        super(menuType, i);
    }

    @Override
    public EnchantmentMenuProcess getMoreJSProcess() {
        return this.morejs$process;
    }

    @Override
    public Container getMoreJsEnchantSlots() {
        return this.enchantSlots;
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void initializeProcess(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        //noinspection ConstantConditions
        this.morejs$process = new EnchantmentMenuProcess((EnchantmentMenu) (Object) this);
        morejs$process.setPlayer(inventory.player);
    }

    @Inject(method = "slotsChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V"), cancellable = true)
    private void slotchanged$PrepareChangeEvent(Container container, CallbackInfo ci) {
        this.access.execute((level, pos) -> {
            ItemStack item = container.getItem(0);
            if (this.morejs$process.matchesCurrentItem(item)) {
                ci.cancel();
                return;
            }

            this.morejs$process.prepareEvent(item);
            if (Debug.ENCHANTMENT) MoreJS.LOG.warn("<{}> Pre SlotChange: {}", this.morejs$process.getPlayer(), item);
        });
    }

    @Inject(method = "slotsChanged", at = @At("RETURN"))
    private void slotChanged$InvokeChangeEvent(Container container, CallbackInfo ci) {
        if (container != this.enchantSlots || !this.morejs$process.isFreezeBroadcast()) {
            return;

        }

        ItemStack item = container.getItem(0);
        this.access.execute((level, pos) -> {
            ItemStack secondItem = container.getItem(1);
            this.morejs$process.setFreezeBroadcast(false);
            this.morejs$process.setState(EnchantmentState.USE_STORED_ENCHANTMENTS);
            if (Debug.ENCHANTMENT) MoreJS.LOG.warn("<{}> Post SlotChange: {}", this.morejs$process.getPlayer(), item);
            Events.ENCHANTMENT_TABLE_CHANGED.post(new EnchantmentTableChangedJS(item,
                    secondItem,
                    level,
                    pos,
                    this.morejs$process,
                    this.random));
        });

        if (item.isEmpty() || !item.isEnchantable()) {
            this.morejs$process.clearEnchantments();
        }
    }

    @Inject(method = "getEnchantmentList", at = @At("RETURN"), cancellable = true)
    private void handleEnchantmentGetter(ItemStack itemStack, int index, int powerLevel, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        switch (this.morejs$process.getState()) {
            case STORE_ENCHANTMENTS -> this.morejs$process.setEnchantments(index, cir.getReturnValue());
            case USE_STORED_ENCHANTMENTS -> {
                var enchantments = this.morejs$process.getEnchantments(index);
                if (enchantments == null) {
                    MoreJS.LOG.error(
                            "Enchantment list is null for index " + index + ", when in state USE_STORED_ENCHANTMENTS");
                    return;
                }
                cir.setReturnValue(enchantments);
            }
        }
    }

    @Inject(method = "clickMenuButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V"), cancellable = true)
    private void clickMenuButton$InvokeEnchantEvent(Player player, int i, CallbackInfoReturnable<Boolean> cir) {
        this.access.execute((level, pos) -> {
            if (player != this.morejs$process.getPlayer()) {
                MoreJS.LOG.warn("<{}> Player changed during clickMenuButton", this.morejs$process.getPlayer());
                return;
            }

            ItemStack item = this.enchantSlots.getItem(0);
            ItemStack secondItem = this.enchantSlots.getItem(1);
            var e = new EnchantmentTableServerEventJS(item, secondItem, level, pos, player, this.morejs$process);
            Events.ENCHANTMENT_TABLE_ENCHANT.post(e);
            if (Events.ENCHANTMENT_TABLE_ENCHANT.post(e)) {
                cir.setReturnValue(false);
            }
        });
    }

    @Override
    public void broadcastChanges() {
        if (this.morejs$process.isFreezeBroadcast()) {
            if (Debug.ENCHANTMENT) MoreJS.LOG.warn("<{}> Freezing broadcast", morejs$process.getPlayer());
            return;
        }

        super.broadcastChanges();
    }
}
