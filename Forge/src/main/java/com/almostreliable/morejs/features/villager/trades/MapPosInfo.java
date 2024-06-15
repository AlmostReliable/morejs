package com.almostreliable.morejs.features.villager.trades;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public record MapPosInfo(BlockPos pos, Component name) {
    @FunctionalInterface
    public interface Provider {
        @Nullable
        MapPosInfo apply(ServerLevel level, Entity entity);
    }
}
