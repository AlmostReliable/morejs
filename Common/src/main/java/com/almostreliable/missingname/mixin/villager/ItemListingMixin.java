package com.almostreliable.missingname.mixin.villager;

import com.almostreliable.missingname.modules.villager.ItemListingType;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VillagerTrades.ItemListing.class)
public interface ItemListingMixin extends ItemListingType {
}
