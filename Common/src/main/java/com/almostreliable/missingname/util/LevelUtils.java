package com.almostreliable.missingname.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class LevelUtils {
    @Nullable
    public static BlockPos findStructure(BlockPos position, ServerLevel level, String structure, int radius) {
        return level
                .registryAccess()
                .registry(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
                .flatMap(registry -> ResourceOrTag
                        .get(structure, Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY)
                        .map(id -> registry.getHolder(id).map(HolderSet::direct), registry::getTag))
                .map(holderSet -> level
                        .getChunkSource()
                        .getGenerator()
                        .findNearestMapFeature(level, holderSet, position, radius, true))
                .map(Pair::getFirst)
                .orElse(null);
    }

    public static BlockPos findBiome(BlockPos position, ServerLevel level, String biome, int radius) {
        Predicate<Holder<Biome>> predicate = ResourceOrTag
                .get(biome, Registry.BIOME_REGISTRY)
                .map(id -> holder -> holder.is(id), tag -> holder -> holder.is(tag));

        Pair<BlockPos, Holder<Biome>> nearestBiome = level.findNearestBiome(predicate, position, radius, 8);
        if (nearestBiome != null) {
            return nearestBiome.getFirst();
        }

        return null;
    }
}
