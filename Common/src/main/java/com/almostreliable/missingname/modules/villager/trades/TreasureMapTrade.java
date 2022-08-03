package com.almostreliable.missingname.modules.villager.trades;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.entity.EntityJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

public class TreasureMapTrade extends TransformableTrade<TreasureMapTrade> {
    @Nullable protected Component displayName;
    protected MapDecoration.Type destinationType = MapDecoration.Type.RED_X;

    // TODO: Make it better
    protected final Function<EntityJS, BlockPos> destinationPositionFunc;

    public TreasureMapTrade(ItemStack[] inputs, Function<EntityJS, BlockPos> destinationPositionFunc) {
        super(inputs);
        this.destinationPositionFunc = destinationPositionFunc;
    }

    public TreasureMapTrade displayName(Component name) {
        this.displayName = name;
        return this;
    }

    public TreasureMapTrade destinationType(MapDecoration.Type type) {
        this.destinationType = type;
        return this;
    }

    @Override
    @Nullable
    public MerchantOffer createOffer(Entity trader, Random rand) {
        if (trader.getLevel() instanceof ServerLevel level) {
            BlockPos pos = destinationPositionFunc.apply(new EntityJS(trader));
            if (pos == null) return null;

            ItemStack map = MapItem.create(level, pos.getX(), pos.getZ(), (byte) 2, true, true);
            MapItem.renderBiomePreviewMap(level, map);
            MapItemSavedData.addTargetDecoration(map, pos, "+", destinationType);
            if (displayName != null) map.setHoverName(displayName);
            return createOffer(map);
        }

        return null;
    }

    public static TreasureMapTrade forStructure(ItemStack[] input, ResourceLocation structure) {
        var destination = TagKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, structure);
        Function<EntityJS, BlockPos> func = (entity) -> {
            if (entity.getLevel().minecraftLevel instanceof ServerLevel level) {
                // Does not work for like 99% of structure ... why? ._.'
                return level.findNearestMapFeature(destination,
                        entity.minecraftEntity.blockPosition(),
                        600,
                        true);
            }
            return null;
        };
        return new TreasureMapTrade(input, func);
    }

    public static TreasureMapTrade forBiome(ItemStack[] input, String biome) {
        TagKey<Biome> btk = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(biome.substring(1)));
        Either<ResourceLocation, TagKey<Biome>> either = biome.startsWith("#") ?
                                                         Either.right(btk) :
                                                         Either.left(new ResourceLocation(biome));

        Predicate<Holder<Biome>> predicate = either.map(
                rl -> h -> h.is(rl),
                tk -> h -> h.is(tk)
        );

        Function<EntityJS, BlockPos> func = (entity) -> {
            if (entity.getLevel().minecraftLevel instanceof ServerLevel level) {
                // 8 is the default increment for finding a biome feature
                Pair<BlockPos, Holder<Biome>> nearestBiome = level.findNearestBiome(predicate,
                        entity.minecraftEntity.blockPosition(),
                        1200,
                        8);
                if (nearestBiome != null) {
                    return nearestBiome.getFirst();
                }
            }
            return null;
        };
        return new TreasureMapTrade(input, func);
    }
}
