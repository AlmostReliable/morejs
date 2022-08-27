package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.util.LevelUtils;
import com.almostreliable.morejs.util.ResourceOrTag;
import com.almostreliable.morejs.util.WeightedList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nullable;
import java.util.Random;

public class TreasureMapTrade extends TransformableTrade<TreasureMapTrade> {
    @Nullable protected Component displayName;
    protected MapDecoration.Type destinationType = MapDecoration.Type.RED_X;

    protected final MapPosInfo.Provider destinationPositionFunc;
    private boolean renderBiomePreviewMap = true;

    private byte mapViewScale = 2;

    public TreasureMapTrade(ItemStack[] inputs, MapPosInfo.Provider destinationPositionFunc) {
        super(inputs);
        this.destinationPositionFunc = destinationPositionFunc;
    }

    public TreasureMapTrade displayName(Component name) {
        this.displayName = name;
        return this;
    }

    public TreasureMapTrade marker(MapDecoration.Type type) {
        this.destinationType = type;
        return this;
    }

    public TreasureMapTrade noPreview() {
        this.renderBiomePreviewMap = false;
        return this;
    }

    public TreasureMapTrade scale(byte scale) {
        this.mapViewScale = scale;
        return this;
    }

    @Override
    @Nullable
    public MerchantOffer createOffer(Entity trader, Random rand) {
        if (trader.getLevel() instanceof ServerLevel level) {
            MapPosInfo info = destinationPositionFunc.apply(level, trader);
            if (info == null) return null;

            ItemStack map = MapItem.create(level, info.pos().getX(), info.pos().getZ(), this.mapViewScale, true, true);
            if (renderBiomePreviewMap) MapItem.renderBiomePreviewMap(level, map);
            MapItemSavedData.addTargetDecoration(map, info.pos(), "+", destinationType);
            map.setHoverName(displayName == null ? info.name() : displayName);
            return createOffer(map);
        }

        return null;
    }

    public static TreasureMapTrade forStructure(ItemStack[] input, WeightedList<Object> entries) {
        var list = entries.map(o -> {
            if (o == null) {
                return null;
            }
            return ResourceOrTag.get(o.toString(), Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
        });

        MapPosInfo.Provider func = (level, entity) -> {
            var roll = list.roll(level.random);
            BlockPos pos = LevelUtils.findStructure(entity.blockPosition(), level, roll, 100);
            if (pos == null) return null;
            return new MapPosInfo(pos, roll.getName());
        };
        return new TreasureMapTrade(input, func);
    }

    public static TreasureMapTrade forBiome(ItemStack[] input, WeightedList<Object> entries) {
        var list = entries.map(o -> {
            if (o == null) {
                return null;
            }
            return ResourceOrTag.get(o.toString(), Registry.BIOME_REGISTRY);
        });

        MapPosInfo.Provider func = (level, entity) -> {
            var roll = list.roll(level.random);
            BlockPos pos = LevelUtils.findBiome(entity.blockPosition(), level, roll, 250);
            if (pos == null) return null;
            return new MapPosInfo(pos, roll.getName());
        };
        return new TreasureMapTrade(input, func);
    }
}
