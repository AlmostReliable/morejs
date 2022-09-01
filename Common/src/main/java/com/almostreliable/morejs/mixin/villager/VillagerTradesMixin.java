package com.almostreliable.morejs.mixin.villager;

import com.almostreliable.morejs.features.villager.TradeFilter;
import com.almostreliable.morejs.features.villager.TradeTypes;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

public class VillagerTradesMixin {

    @Mixin(VillagerTrades.EmeraldForItems.class)
    private static class EmeraldForItemsMixin implements TradeFilter.Filterable {
        @Shadow @Final private Item item;

        @Shadow @Final private int cost;

        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(this.item, this.cost), new ItemStack(Items.EMERALD),
                    TradeTypes.EmeraldForItems);
        }
    }

    @Mixin(VillagerTrades.ItemsForEmeralds.class)
    private static class ItemsForEmeraldsMixin implements TradeFilter.Filterable {
        @Shadow @Final private int emeraldCost;

        @Shadow @Final private ItemStack itemStack;

        @Shadow @Final private int numberOfItems;

        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(Items.EMERALD, this.emeraldCost),
                    new ItemStack(this.itemStack.getItem(), this.numberOfItems),
                    TradeTypes.ItemsForEmeralds);
        }
    }

    @Mixin(VillagerTrades.ItemsAndEmeraldsToItems.class)
    private static class ItemsAndEmeraldsToItemsMixin implements TradeFilter.Filterable {

        @Shadow @Final private int emeraldCost;

        @Shadow @Final private ItemStack fromItem;

        @Shadow @Final private ItemStack toItem;

        @Shadow @Final private int fromCount;

        @Shadow @Final private int toCount;

        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(Items.EMERALD, this.emeraldCost),
                    new ItemStack(this.fromItem.getItem(), this.fromCount),
                    new ItemStack(this.toItem.getItem(), this.toCount),
                    TradeTypes.ItemsAndEmeraldsToItems);
        }
    }

    @Mixin(VillagerTrades.SuspiciousStewForEmerald.class)
    private static class SuspiciousStewForEmeraldMixin implements TradeFilter.Filterable {
        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(Items.EMERALD), new ItemStack(Items.SUSPICIOUS_STEW),
                    TradeTypes.SuspiciousStewForEmeralds);
        }
    }

    @Mixin(VillagerTrades.EnchantedItemForEmeralds.class)
    private static class EnchantedItemForEmeraldsMixin implements TradeFilter.Filterable {
        @Shadow @Final private ItemStack itemStack;

        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(Items.EMERALD, 64), this.itemStack, TradeTypes.EnchantedItemForEmeralds);
        }
    }

    @Mixin(VillagerTrades.EmeraldsForVillagerTypeItem.class)
    private static class EmeraldsForVillagerTypeItemMixin implements TradeFilter.Filterable {
        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(Items.EMERALD),
                    new ItemStack(Items.BARRIER),
                    TradeTypes.EmeraldsForVillagerTypeItem);
        }
    }

    @Mixin(VillagerTrades.TippedArrowForItemsAndEmeralds.class)
    private static class TippedArrowForItemsAndEmeraldsMixin implements TradeFilter.Filterable {
        @Shadow @Final private int emeraldCost;

        @Shadow @Final private Item fromItem;

        @Shadow @Final private int fromCount;

        @Shadow @Final private ItemStack toItem;

        @Shadow @Final private int toCount;

        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(Items.EMERALD, this.emeraldCost),
                    new ItemStack(this.fromItem, this.fromCount),
                    new ItemStack(this.toItem.getItem(), this.toCount),
                    TradeTypes.TippedArrowForItemsAndEmeralds);
        }
    }

    @Mixin(VillagerTrades.EnchantBookForEmeralds.class)
    private static class EnchantBookForEmeraldsMixin implements TradeFilter.Filterable {
        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(Items.EMERALD, 64), new ItemStack(Items.ENCHANTED_BOOK),
                    TradeTypes.EnchantBookForEmeralds);
        }
    }

    @Mixin(VillagerTrades.TreasureMapForEmeralds.class)
    private static class TreasureMapForEmeraldsMixin implements TradeFilter.Filterable {
        @Override
        public boolean matchesTradeFilter(TradeFilter filter) {
            return filter.match(new ItemStack(Items.EMERALD, 64), new ItemStack(Items.FILLED_MAP),
                    TradeTypes.TreasureMapForEmeralds);
        }
    }
}
