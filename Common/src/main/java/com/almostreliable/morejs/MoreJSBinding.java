package com.almostreliable.morejs;

import com.almostreliable.morejs.features.villager.IntRange;
import com.almostreliable.morejs.features.villager.TradeFilter;
import com.almostreliable.morejs.features.villager.TradeTypes;
import com.almostreliable.morejs.util.LevelUtils;
import com.almostreliable.morejs.util.ResourceOrTag;
import com.almostreliable.morejs.util.Utils;
import com.almostreliable.morejs.util.WeightedList;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoreJSBinding {
    @Nullable
    public static BlockPos findStructure(BlockPos position, ServerLevel level, String structure, int chunkRadius) {
        ResourceOrTag<ConfiguredStructureFeature<?, ?>> rot = ResourceOrTag.get(structure,
                Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
        return LevelUtils.findStructure(position, level, rot, chunkRadius);
    }

    @Nullable
    public static BlockPos findBiome(BlockPos position, ServerLevel level, String biome, int chunkRadius) {
        ResourceOrTag<Biome> rot = ResourceOrTag.get(biome, Registry.BIOME_REGISTRY);
        return LevelUtils.findBiome(position, level, rot, chunkRadius);
    }

    public static IntRange range(@Nullable Object o) {
        if (o instanceof Number number) {
            return new IntRange(number.intValue());
        }

        if (o instanceof List<?> list) {
            return switch (list.size()) {
                case 0 -> IntRange.all();
                case 1 -> range(list.get(0));
                default -> new IntRange(UtilsJS.parseInt(list.get(0), 1), UtilsJS.parseInt(list.get(0), 5));
            };
        }

        return IntRange.all();
    }

    public static WeightedList.Builder<Object> weightedList() {
        return new WeightedList.Builder<>();
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

    public static TradeFilter ofTradeFilter(@Nullable Object o) {
        if (o instanceof TradeFilter filter) {
            return filter;
        }

        if (!(o instanceof Map<?, ?> map)) {
            return new TradeFilter(ItemStackJS.EMPTY, ItemStackJS.EMPTY, ItemStackJS.EMPTY);
        }

        if (!map.containsKey("firstItem") || !map.containsKey("secondItem") || !map.containsKey("outputItem")) {
            ConsoleJS.SERVER.error("Trade filter must contain firstItem, secondItem and outputItem");
            return new TradeFilter(ItemStackJS.EMPTY, ItemStackJS.EMPTY, ItemStackJS.EMPTY);
        }

        TradeFilter filter = new TradeFilter(
                ItemStackJS.of(map.get("firstItem")),
                ItemStackJS.of(map.get("secondItem")),
                ItemStackJS.of(map.get("outputItem"))
        );

        filter.setFirstCountMatcher(range(map.get("firstCount")));
        filter.setSecondCountMatcher(range(map.get("secondCount")));
        filter.setOutputCountMatcher(range(map.get("outputCount")));

        if (map.get("types") instanceof List<?> list) {
            Set<String> allTypes = Stream.of(TradeTypes.values()).map(TradeTypes::name).collect(Collectors.toSet());
            Set<TradeTypes> types = list
                    .stream()
                    .map(Object::toString)
                    .filter(allTypes::contains)
                    .map(TradeTypes::valueOf)
                    .collect(Collectors.toSet());
            filter.setTradeTypes(types);
        }

        if (map.get("professions") instanceof List<?> list) {
            Set<VillagerProfession> professions = list
                    .stream()
                    .map(Object::toString)
                    .map(ResourceLocation::tryParse)
                    .filter(Objects::nonNull)
                    .map(Registry.VILLAGER_PROFESSION::getOptional)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toSet());
            filter.setProfessions(professions);
        }

        filter.setMerchantLevelMatcher(range(map.get("level")));
        return filter;
    }
}
