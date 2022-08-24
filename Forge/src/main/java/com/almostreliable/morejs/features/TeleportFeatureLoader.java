package com.almostreliable.morejs.features;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.teleport.EntityTeleportsEventJS;
import com.almostreliable.morejs.features.teleport.TeleportType;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class TeleportFeatureLoader {

    public static void load(IEventBus bus) {
        bus.addListener(TeleportFeatureLoader::chorusFruitTeleport);
        bus.addListener(TeleportFeatureLoader::enderPearlTeleport);
    }

    private static void chorusFruitTeleport(EntityTeleportEvent.ChorusFruit e) {
        handleEvent(e, TeleportType.CHORUS_FRUIT);
    }

    private static void enderPearlTeleport(EntityTeleportEvent.EnderPearl e) {
        handleEvent(e, TeleportType.ENDER_PEARL);
    }

    private static void handleEvent(EntityTeleportEvent e, TeleportType type) {
        var event = new EntityTeleportsEventJS(e.getEntity(), e.getTargetX(), e.getTargetY(), e.getTargetZ(), type);
        event.post(ScriptType.SERVER, Events.TELEPORT);
        if (event.isCancelled()) {
            e.setCanceled(true);
            return;
        }

        e.setTargetX(event.getX());
        e.setTargetY(event.getY());
        e.setTargetZ(event.getZ());
    }
}
