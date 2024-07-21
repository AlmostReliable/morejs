package com.almostreliable.morejs;

import com.almostreliable.morejs.features.villager.IntRange;
import com.almostreliable.morejs.features.villager.TradeItem;
import com.almostreliable.morejs.util.LevelUtils;
import com.almostreliable.morejs.util.ResourceOrTag;
import com.almostreliable.morejs.util.Utils;
import com.almostreliable.morejs.util.WeightedList;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;
import java.util.List;

public class MoreJSBinding {
    @Nullable
    public static BlockPos findStructure(BlockPos position, ServerLevel level, String structure, int chunkRadius) {
        ResourceOrTag<Structure> rot = ResourceOrTag.get(structure, Registries.STRUCTURE);
        return LevelUtils.findStructure(position, level, rot, chunkRadius);
    }

    @Nullable
    public static BlockPos findBiome(BlockPos position, ServerLevel level, String biome, int chunkRadius) {
        ResourceOrTag<Biome> rot = ResourceOrTag.get(biome, Registries.BIOME);
        return LevelUtils.findBiome(position, level, rot, chunkRadius);
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
                default -> new IntRange(UtilsJS.parseInt(list.get(0), 1), UtilsJS.parseInt(list.get(1), 5));
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
                builder.add(UtilsJS.parseInt(weightValue.get(0), 1), weightValue.get(1));
            } else {
                builder.add(1, entry);
            }
        }
        return builder.build();
    }

    public static TradeItem ofTradeItem(RegistryAccessContainer registries, @Nullable Object o) {
        if (o instanceof TradeItem item) {
            return item;
        }

        return TradeItem.of(ItemStackJS.wrap(registries, o));
    }
}
