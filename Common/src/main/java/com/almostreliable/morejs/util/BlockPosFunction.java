package com.almostreliable.morejs.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

@FunctionalInterface
public interface BlockPosFunction {
    @Nullable
    BlockPos apply(ServerLevel level, Entity entity);
}
