package com.almostreliable.missingname.modules.villager.trades;

import com.almostreliable.missingname.util.LevelUtils;
import com.almostreliable.missingname.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

public class TreasureMapTrade extends TransformableTrade<TreasureMapTrade> {
    @Nullable protected Component displayName;
    protected MapDecoration.Type destinationType = MapDecoration.Type.RED_X;

    // TODO: Make it better
    protected final Function<Entity, BlockPos> destinationPositionFunc;
    private boolean renderBiomePreviewMap = true;

    private byte mapViewScale = 2;

    public TreasureMapTrade(ItemStack[] inputs, Function<Entity, BlockPos> destinationPositionFunc) {
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
            BlockPos pos = destinationPositionFunc.apply(trader);
            if (pos == null) return null;

            ItemStack map = MapItem.create(level, pos.getX(), pos.getZ(), this.mapViewScale, true, true);
            if (renderBiomePreviewMap) MapItem.renderBiomePreviewMap(level, map);
            MapItemSavedData.addTargetDecoration(map, pos, "+", destinationType);
            if (displayName != null) map.setHoverName(displayName);
            return createOffer(map);
        }

        return null;
    }

    public static TreasureMapTrade forStructure(ItemStack[] input, String structure) {
        TextComponent c = new TextComponent("Treasure Map: " + Utils.format(structure));
        return new TreasureMapTrade(input,
                entity -> Utils
                        .cast(entity.level, ServerLevel.class)
                        .map(level -> LevelUtils.findStructure(entity.blockPosition(), level, structure, 600))
                        .orElse(null)).displayName(c);
    }

    public static TreasureMapTrade forBiome(ItemStack[] input, String biome) {
        TextComponent c = new TextComponent("Treasure Map: " + Utils.format(biome));
        return new TreasureMapTrade(input,
                entity -> Utils
                        .cast(entity.getLevel(), ServerLevel.class)
                        .map(level -> LevelUtils.findBiome(entity.blockPosition(), level, biome, 1200))
                        .orElse(null)).displayName(c);
    }
}
