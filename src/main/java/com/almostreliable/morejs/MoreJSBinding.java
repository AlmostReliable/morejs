package com.almostreliable.morejs;

import com.almostreliable.morejs.features.villager.IntRange;
import com.almostreliable.morejs.features.villager.TradeItem;
import com.almostreliable.morejs.util.Utils;
import com.almostreliable.morejs.util.WeightedList;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.StringUtilsWrapper;
import dev.latvian.mods.rhino.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;
import java.util.List;

public class MoreJSBinding {
    @Nullable
    public static BlockPos findStructure(BlockPos position, ServerLevel level, HolderSet<Structure> structures, int chunkRadius) {
        var result = level
                .getChunkSource()
                .getGenerator()
                .findNearestMapStructure(level, structures, position, chunkRadius, true);
        if (result == null) {
            return null;
        }

        return result.getFirst();
    }

    @Nullable
    public static BlockPos findBiome(BlockPos position, ServerLevel level, HolderSet<Biome> biomes, int chunkRadius) {
        Pair<BlockPos, Holder<Biome>> nearestBiome = level.findClosestBiome3d(biomes::contains,
                position,
                chunkRadius * 16,
                32,
                64);
        if (nearestBiome != null) {
            return nearestBiome.getFirst();
        }

        return null;
    }

    public static WeightedList.Builder<Object> weightedList() {
        return new WeightedList.Builder<>();
    }

    public static IntRange range(@Nullable Object o) {
        if (o instanceof Number number) {
            return new IntRange(number.intValue());
        }

        if (o instanceof List<?> list) {
            return switch (list.size()) {
                case 0 -> IntRange.all();
                case 1 -> range(list.get(0));
                default -> new IntRange(StringUtilsWrapper.parseInt(list.get(0), 1),
                        StringUtilsWrapper.parseInt(list.get(1), 5));
            };
        }

        return IntRange.all();
    }

    public static WeightedList<Object> ofWeightedList(@Nullable Object o) {
        if (o instanceof WeightedList.Builder b) {
            //noinspection unchecked
            return b.build();
        }

        if (o instanceof WeightedList) {
            return Utils.cast(o);
        }

        var builder = new WeightedList.Builder<>();

        for (Object entry : Utils.asList(o)) {
            List<Object> weightValue = Utils.asList(entry);
            if (weightValue.size() == 2) {
                builder.add(StringUtilsWrapper.parseInt(weightValue.get(0), 1), weightValue.get(1));
            } else {
                builder.add(1, entry);
            }
        }
        return builder.build();
    }

    public static TradeItem ofTradeItem(Context cx, @Nullable Object o) {
        if (o instanceof TradeItem item) {
            return item;
        }

        return TradeItem.of(ItemWrapper.wrap(cx, o));
    }
}
