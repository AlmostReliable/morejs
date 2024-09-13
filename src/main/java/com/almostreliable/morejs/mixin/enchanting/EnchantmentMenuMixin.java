package com.almostreliable.morejs.mixin.enchanting;

import com.almostreliable.morejs.Debug;
import com.almostreliable.morejs.MoreJS;
import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.enchantment.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// lower priority to ensure we run first. 42 is the answer to everything.
@Mixin(value = EnchantmentMenu.class, priority = 42)
public abstract class EnchantmentMenuMixin extends AbstractContainerMenu implements EnchantmentMenuExtension {
    @Nullable
    @Unique
    private EnchantmentMenuState morejs$state;
    @Shadow @Final private Container enchantSlots;
    @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final private RandomSource random;

    @Shadow @Final public int[] costs;

    @Shadow @Final public int[] enchantClue;

    @Shadow @Final public int[] levelClue;

    protected EnchantmentMenuMixin(@Nullable MenuType<?> menuType, int i) {
        // Ignore this
        super(menuType, i);
    }

    @Override
    public Optional<EnchantmentMenuState> morejs$getState() {
        return Optional.ofNullable(this.morejs$state);
    }

    @Override
    public Container morejs$getContainer() {
        return this.enchantSlots;
    }

    @Override
    public int[] morejs$getCosts() {
        return this.costs;
    }

    @Override
    public int[] morejs$getEnchantmentClues() {
        return this.enchantClue;
    }

    @Override
    public int[] morejs$getLevelClues() {
        return this.levelClue;
    }

    @Override
    public RandomSource morejs$getRandom() {
        return this.random;
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void initializeProcess(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        if (inventory.player instanceof ServerPlayer) {
            this.morejs$state = new EnchantmentMenuState((EnchantmentMenu) (Object) this, inventory.player);
        }
    }

    @Inject(method = "slotsChanged", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/Container;getItem(I)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER), cancellable = true)
    private void slotchanged$PrepareChangeEvent(Container inventory, CallbackInfo ci, @Local ItemStack item) {
        morejs$getState().ifPresent(state -> {
            if (state.matchesCurrentItem(item)) {
                ci.cancel();
                return;
            }

            if (item.isEmpty()) {
                state.reset(item);
                return;
            }

            state.prepareEvent(item);
        });
    }

    @Redirect(method = "slotsChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEnchantable()Z"))
    private boolean slotChanged$InvokeEnchantableEvent(ItemStack itemStack, Container container) {
        EnchantmentMenuState ems = morejs$getState().orElse(null);
        if (ems == null) {
            return itemStack.isEnchantable();
        }

        MutableBoolean enchantable = new MutableBoolean(itemStack.isEnchantable());
        this.access.execute((level, pos) -> {
            if (Debug.ENCHANTMENT) {
                MoreJS.LOG.warn("Invoke 'isEnchantable' event for item <{}> with player <{}>",
                        itemStack,
                        ems.getPlayer());
            }

            ItemStack secondItem = container.getItem(1);
            var e = new IsEnchantableEventJS(itemStack, secondItem, level, pos, ems, enchantable);
            Events.IS_ENCHANTABLE.post(e);
        });

        return ems.storeItemIsEnchantable(enchantable.booleanValue());
    }

    @Inject(method = "lambda$slotsChanged$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/EnchantmentMenu;broadcastChanges()V"))
    private void morejs$invokeChangeEvent(ItemStack item, Level level, BlockPos pos, CallbackInfo ci) {
        morejs$getState().ifPresent(state -> {
            if (Debug.ENCHANTMENT) {
                MoreJS.LOG.warn("Invoke 'enchantmentTableChanged' event for item <{}> with player <{}>",
                        item,
                        state.getPlayer());
            }

            ItemStack secondItem = this.enchantSlots.getItem(1);
            state.setState(EnchantmentState.USE_STORED_ENCHANTMENTS);
            Events.ENCHANTMENT_TABLE_CHANGED.post(new EnchantmentTableServerEventJS(item,
                    secondItem,
                    level,
                    pos,
                    state.getPlayer(),
                    state));

            // TODO check if needed
//            if (item.isEmpty() || !state.isItemEnchantable(item)) {
//                state.clearEnchantments();
//            }

        });

    }

    @Inject(method = "getEnchantmentList", at = @At("RETURN"), cancellable = true)
    private void handleEnchantmentGetter(RegistryAccess registryAccess, ItemStack itemStack, int index, int powerLevel, CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        morejs$getState().ifPresent(state -> {
            switch (state.getState()) {
                case STORE_ENCHANTMENTS -> state.setEnchantments(index, cir.getReturnValue());
                case USE_STORED_ENCHANTMENTS -> {
                    var enchantments = new ArrayList<>(state.getEnchantments(index));
                    cir.setReturnValue(enchantments);
                }
            }
        });
    }

    @Inject(method = "lambda$clickMenuButton$1", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/inventory/EnchantmentMenu;getEnchantmentList(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;II)Ljava/util/List;", shift = At.Shift.AFTER), cancellable = true)
    private void morejs$invokeEnchantmentTableEnchant(ItemStack mainItem, int btnRow, Player player, int minLevel, ItemStack secondItem, Level level, BlockPos pos, CallbackInfo ci, @Local List<EnchantmentInstance> enchantments) {
        morejs$getState().ifPresent(state -> {
            if (player != state.getPlayer()) {
                MoreJS.LOG.error("<{}> Player changed during clickMenuButton", state.getPlayer());
                return;
            }

            var requiredLevel = state.getMenu().costs[btnRow];
            var e = new PlayerEnchantEventJS(mainItem, secondItem, level, pos, player, state, requiredLevel, enchantments);
            if (Events.ENCHANTMENT_TABLE_ENCHANT.post(e).interruptFalse()) {
               ci.cancel();
               return;
            }

            state.reset(ItemStack.EMPTY);
        });
    }

//    @Inject(method = "clickMenuButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V"), cancellable = true)
//    private void clickMenuButton$InvokeEnchantEvent(Player player, int clickedBtn, CallbackInfoReturnable<Boolean> cir) {
//        this.access.execute((level, pos) -> {
//            morejs$getState().ifPresent(state -> {
//                if (player != state.getPlayer()) {
//                    MoreJS.LOG.error("<{}> Player changed during clickMenuButton", state.getPlayer());
//                    return;
//                }
//
//                ItemStack item = this.enchantSlots.getItem(0);
//                ItemStack secondItem = this.enchantSlots.getItem(1);
//                var e = new PlayerEnchantEventJS(clickedBtn, item, secondItem, level, pos, player, state);
//                if (Events.ENCHANTMENT_TABLE_ENCHANT.post(e).interruptFalse()) {
//                    cir.setReturnValue(false);
//                }
//
//                if (e.itemWasChanged()) {
//                    cir.setReturnValue(false);
//                    ItemStack newItem = e.getItem().copy();
//                    state.reset(newItem);
//                    this.enchantSlots.setItem(0, newItem);
//                }
//
//                state.reset(ItemStack.EMPTY);
//            });
//
//        });
//    }
}
