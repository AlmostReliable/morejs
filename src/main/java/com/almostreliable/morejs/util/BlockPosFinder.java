package com.almostreliable.morejs.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@FunctionalInterface
public interface BlockPosFinder {

    @Nullable
    BlockPos findPosition(ServerLevel level, Entity entity);
}
