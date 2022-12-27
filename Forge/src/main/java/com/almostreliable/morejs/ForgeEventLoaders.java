package com.almostreliable.morejs;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.misc.ExperiencePlayerEventJS;
import com.almostreliable.morejs.features.teleport.EntityTeleportsEventJS;
import com.almostreliable.morejs.features.teleport.TeleportType;
import com.almostreliable.morejs.features.villager.ForgeTradingManager;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ForgeEventLoaders {

    public static void load(IEventBus bus) {
        bus.addListener(ForgeEventLoaders::onExperienceChange);
        bus.addListener(ForgeEventLoaders::chorusFruitTeleport);
        bus.addListener(ForgeEventLoaders::enderPearlTeleport);
    }

    private static void onExperienceChange(PlayerXpEvent.XpChange event) {
        var e = new ExperiencePlayerEventJS(event.getEntity(), event.getAmount());
        boolean cancelled = Events.XP_CHANGE.post(e);
        event.setAmount(e.getAmount());
        if (cancelled) {
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
        if (Events.TELEPORT.post(event)) {
            e.setCanceled(true);
            return;
        }

        e.setTargetX(event.getX());
        e.setTargetY(event.getY());
        e.setTargetZ(event.getZ());
    }
}
