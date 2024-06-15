package com.almostreliable.morejs.features.teleport;

import dev.latvian.mods.kubejs.entity.EntityEventJS;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class EntityTeleportsEventJS extends EntityEventJS {

    private final Entity entity;
    private final TeleportType type;
    private final Level level;
    private double x;
    private double y;
    private double z;

    public EntityTeleportsEventJS(Entity entity, double x, double y, double z, TeleportType type) {
        this(entity, x, y, z, null, type);
    }

    public EntityTeleportsEventJS(Entity entity, double x, double y, double z, @Nullable Level level, TeleportType type) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.level = level;
    }

    public TeleportType getType() {
        return type;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
