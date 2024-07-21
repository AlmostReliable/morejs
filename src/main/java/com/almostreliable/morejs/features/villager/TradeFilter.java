package com.almostreliable.morejs.features.villager;

import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Optional;

public record TradeFilter(Optional<Ingredient> first, Optional<Ingredient> second, Optional<Ingredient> output,
                          Optional<IntRange> firstCount, Optional<IntRange> secondCount, Optional<IntRange> outputCount,
                          Optional<IntRange> level, Optional<List<TradeTypes>> types,
                          Optional<HolderSet<VillagerProfession>> professions) {
}
