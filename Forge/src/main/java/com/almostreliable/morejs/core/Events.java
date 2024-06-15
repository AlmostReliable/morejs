package com.almostreliable.morejs.core;

import com.almostreliable.morejs.features.enchantment.EnchantmentTableServerEventJS;
import com.almostreliable.morejs.features.enchantment.EnchantmentTableTooltipEventJS;
import com.almostreliable.morejs.features.enchantment.FilterAvailableEnchantmentsEventJS;
import com.almostreliable.morejs.features.enchantment.PlayerEnchantEventJS;
import com.almostreliable.morejs.features.misc.ExperiencePlayerEventJS;
import com.almostreliable.morejs.features.misc.PiglinPlayerBehaviorEventJS;
import com.almostreliable.morejs.features.potion.PotionBrewingRegisterEvent;
import com.almostreliable.morejs.features.structure.StructureAfterPlaceEventJS;
import com.almostreliable.morejs.features.structure.StructureLoadEventJS;
import com.almostreliable.morejs.features.teleport.EntityTeleportsEventJS;
import com.almostreliable.morejs.features.villager.events.*;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface Events {
    EventGroup GROUP = EventGroup.of("MoreJSEvents");
    EventHandler VILLAGER_TRADING = GROUP.server("villagerTrades", () -> VillagerTradingEventJS.class);
    EventHandler WANDERING_TRADING = GROUP.server("wandererTrades", () -> WandererTradingEventJS.class);
    EventHandler PLAYER_START_TRADING = GROUP.server("playerStartTrading", () -> StartTradingEventJS.class);
    EventHandler FILTER_AVAILABLE_ENCHANTMENTS = GROUP.server("filterAvailableEnchantments", () -> FilterAvailableEnchantmentsEventJS.class);
    EventHandler FILTER_ENCHANTED_BOOK_TRADE = GROUP.server("filterEnchantedBookTrade", () -> FilterEnchantedTradeEventJS.class);
    EventHandler UPDATE_ABSTRACT_VILLAGER_OFFERS = GROUP.server("updateAbstractVillagerOffers", () -> UpdateAbstractVillagerOffersEventJS.class);
    EventHandler UPDATE_VILLAGER_OFFERS = GROUP.server("updateVillagerOffers", () -> UpdateVillagerOffersEventJS.class);
    EventHandler UPDATE_WANDERER_OFFERS = GROUP.server("updateWandererOffers", () -> UpdateAbstractVillagerOffersEventJS.class);
    EventHandler ENCHANTMENT_TABLE_IS_ENCHANTABLE = GROUP.server("enchantmentTableIsEnchantable", () -> EnchantmentTableServerEventJS.class);
    EventHandler ENCHANTMENT_TABLE_CHANGED = GROUP.server("enchantmentTableChanged", () -> EnchantmentTableServerEventJS.class);
    EventHandler ENCHANTMENT_TABLE_ENCHANT = GROUP
            .server("enchantmentTableEnchant", () -> PlayerEnchantEventJS.class)
            .hasResult();
    EventHandler ENCHANTMENT_TABLE_TOOLTIP = GROUP.client("enchantmentTableTooltip", () -> EnchantmentTableTooltipEventJS.class);
    EventHandler TELEPORT = GROUP.server("teleport", () -> EntityTeleportsEventJS.class).hasResult();
    EventHandler STRUCTURE_LOAD = GROUP.server("structureLoad", () -> StructureLoadEventJS.class);
    EventHandler STRUCTURE_AFTER_PLACE = GROUP.server("structureAfterPlace", () -> StructureAfterPlaceEventJS.class);
    EventHandler XP_CHANGE = GROUP.server("playerXpChange", () -> ExperiencePlayerEventJS.class).hasResult();
    EventHandler PIGLIN_PLAYER_BEHAVIOR = GROUP.server("piglinPlayerBehavior", () -> PiglinPlayerBehaviorEventJS.class);
    EventHandler POTION_BREWING_REGISTER = GROUP.startup("registerPotionBrewing",
            () -> PotionBrewingRegisterEvent.class);
}
