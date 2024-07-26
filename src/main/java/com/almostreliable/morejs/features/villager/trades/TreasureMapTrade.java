package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import com.almostreliable.morejs.util.BlockPosFinder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nullable;

public class TreasureMapTrade extends TransformableTrade<TreasureMapTrade> {
    protected final BlockPosFinder blockPosFinder;
    @Nullable protected Component displayName;
    protected Holder<MapDecorationType> destinationType = MapDecorationTypes.RED_X;
    private boolean renderBiomePreviewMap = true;

    private byte mapViewScale = 2;

    public TreasureMapTrade(TradeItem[] inputs, BlockPosFinder blockPosFinder) {
        super(inputs);
        this.blockPosFinder = blockPosFinder;
    }

    public TreasureMapTrade displayName(Component name) {
        this.displayName = name;
        return this;
    }

    public TreasureMapTrade marker(Holder<MapDecorationType> type) {
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
    public MerchantOffer createOffer(Entity trader, RandomSource random) {
        if (trader.level() instanceof ServerLevel level) {
            var pos = blockPosFinder.findPosition(level, trader);
            if (pos == null) return null;

            ItemStack map = MapItem.create(level, pos.getX(), pos.getZ(), this.mapViewScale, true, true);
            if (renderBiomePreviewMap) MapItem.renderBiomePreviewMap(level, map);
            MapItemSavedData.addTargetDecoration(map, pos, "+", destinationType);
            if (displayName != null) {
                map.set(DataComponents.CUSTOM_NAME, displayName);
            }

            return createOffer(map, random);
        }

        return null;
    }
}
