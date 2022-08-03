package com.almostreliable.missingname.modules.villager;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.Map;

public interface ItemListingType {
    Map<Class<? extends VillagerTrades.ItemListing>, Type> LISTING_TO_TYPE = ImmutableMap
            .<Class<? extends VillagerTrades.ItemListing>, Type>builder()
            .put(VillagerTrades.DyedArmorForEmeralds.class, Type.DYED_ARMOR)
            .put(VillagerTrades.EnchantBookForEmeralds.class, Type.ENCHANTED_BOOK)
            .put(VillagerTrades.EnchantedItemForEmeralds.class, Type.ENCHANTED_ITEM)
            .put(VillagerTrades.ItemsForEmeralds.class, Type.BASIC)
            .put(VillagerTrades.ItemsAndEmeraldsToItems.class, Type.BASIC)
            .put(VillagerTrades.EmeraldForItems.class, Type.BASIC)
            .put(VillagerTrades.TippedArrowForItemsAndEmeralds.class, Type.POTION)
            .put(VillagerTrades.SuspiciousStewForEmerald.class, Type.STEW)
            .put(VillagerTrades.TreasureMapForEmeralds.class, Type.TREASURE_MAP)
            .build();

    default Type getTradeType() {
        return LISTING_TO_TYPE.getOrDefault(getClass(), Type.MODDED);
    }

    enum Type {
        BASIC,
        DYED_ARMOR,
        ENCHANTED_ITEM,
        ENCHANTED_BOOK,
        POTION,
        STEW,
        TREASURE_MAP,
        MODDED,
        OWN_CUSTOM,
    }
}
