package com.almostreliable.morejs;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.misc.ExperiencePlayerEventJS;
import com.almostreliable.morejs.features.potion.PotionBrewingRegisterEvent;
import com.almostreliable.morejs.features.teleport.EntityTeleportsEventJS;
import com.almostreliable.morejs.features.teleport.TeleportType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

public class ForgeEventLoaders {

    public static void load(IEventBus bus) {
        NeoForge.EVENT_BUS.addListener(ForgeEventLoaders::onExperienceChange);
        NeoForge.EVENT_BUS.addListener(ForgeEventLoaders::chorusFruitTeleport);
        NeoForge.EVENT_BUS.addListener(ForgeEventLoaders::enderPearlTeleport);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ForgeEventLoaders::onRegisterPotions);
    }

    private static void onRegisterPotions(RegisterBrewingRecipesEvent event) {
        Events.POTION_BREWING_REGISTER.post(new PotionBrewingRegisterEvent(event.getBuilder()));
    }

    private static void onExperienceChange(PlayerXpEvent.XpChange event) {
        var e = new ExperiencePlayerEventJS(event.getEntity(), event.getAmount());
        var result = Events.XP_CHANGE.post(e);
        event.setAmount(e.getAmount());
        if (result.interruptFalse()) {
            event.setCanceled(true);
        }
    }

    private static void chorusFruitTeleport(EntityTeleportEvent.ChorusFruit e) {
        handleEvent(e, TeleportType.CHORUS_FRUIT);
    }

    private static void enderPearlTeleport(EntityTeleportEvent.EnderPearl e) {
        handleEvent(e, TeleportType.ENDER_PEARL);
    }

    private static void handleEvent(EntityTeleportEvent e, TeleportType type) {
        var event = new EntityTeleportsEventJS(e.getEntity(), e.getTargetX(), e.getTargetY(), e.getTargetZ(), type);
        if (Events.TELEPORT.post(event).interruptFalse()) {
            e.setCanceled(true);
            return;
        }

        e.setTargetX(event.getX());
        e.setTargetY(event.getY());
        e.setTargetZ(event.getZ());
    }
}
