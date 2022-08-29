package com.almostreliable.morejs.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

import javax.annotation.Nullable;

public class LevelUtils {

    @Nullable
    public static BlockPos findStructure(BlockPos position, ServerLevel level, ResourceOrTag<ConfiguredStructureFeature<?, ?>> rot, int chunkRadius) {
        return level
                .registryAccess()
                .registry(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
                .flatMap(rot::asHolderSet)
                .map(holderSet -> level
                        .getChunkSource()
                        .getGenerator()
                        .findNearestMapFeature(level, holderSet, position, chunkRadius, true))
                .map(Pair::getFirst)
                .orElse(null);
    }

    @Nullable
    public static BlockPos findBiome(BlockPos position, ServerLevel level, ResourceOrTag<Biome> rot, int chunkRadius) {
        Pair<BlockPos, Holder<Biome>> nearestBiome = level.findNearestBiome(rot.asHolderPredicate(),
                position,
                chunkRadius * 16,
                8);
        if (nearestBiome != null) {
            return nearestBiome.getFirst();
        }
        return null;
    }
}
