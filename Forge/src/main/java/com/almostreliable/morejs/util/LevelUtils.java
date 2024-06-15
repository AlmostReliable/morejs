package com.almostreliable.morejs.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;

public class LevelUtils {

    @Nullable
    public static BlockPos findStructure(BlockPos position, ServerLevel level, ResourceOrTag<Structure> rot, int chunkRadius) {
        return level
                .registryAccess()
                .registry(Registries.STRUCTURE)
                .flatMap(rot::asHolderSet)
                .map(holderSet -> level
                        .getChunkSource()
                        .getGenerator()
                        .findNearestMapStructure(level, holderSet, position, chunkRadius, true))
                .map(Pair::getFirst)
                .orElse(null);
    }

    @Nullable
    public static BlockPos findBiome(BlockPos position, ServerLevel level, ResourceOrTag<Biome> rot, int chunkRadius) {
        Pair<BlockPos, Holder<Biome>> nearestBiome = level.findClosestBiome3d(rot.asHolderPredicate(),
                position,
                chunkRadius * 16,
                32,
                64);
        if (nearestBiome != null) {
            return nearestBiome.getFirst();
        }
        return null;
    }
}
