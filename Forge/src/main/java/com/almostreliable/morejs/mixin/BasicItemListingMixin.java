package com.almostreliable.morejs.mixin;

import com.almostreliable.morejs.features.villager.TradeFilter;
import com.almostreliable.morejs.features.villager.TradeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.BasicItemListing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BasicItemListing.class)
public class BasicItemListingMixin implements TradeFilter.Filterable {
    @Shadow(remap = false) @Final protected ItemStack price;

    @Shadow(remap = false) @Final protected ItemStack price2;

    @Shadow(remap = false) @Final protected ItemStack forSale;

    @Override
    public boolean matchesTradeFilter(TradeFilter filter) {
        return filter.match(this.price, this.price2, this.forSale, TradeTypes.ForgeBasic);
    }
}
