package com.almostreliable.missingname.modules.villager;

import com.almostreliable.missingname.modules.villager.trades.*;
import dev.latvian.mods.kubejs.entity.EntityJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.function.Function;

public class VillagerHelper {

    public static Collection<VillagerProfession> getProfessions() {
        return Registry.VILLAGER_PROFESSION
                .stream()
                .filter(p -> !p.getName().equals("none"))
                .toList();
    }

    public static SimpleTrade createSimpleTrade(ItemStack[] inputs, ItemStack output) {
        return new SimpleTrade(inputs, output);
    }

    public static CustomTrade createCustomTrade(TransformableTrade.Transformer transformer) {
        return new CustomTrade(transformer);
    }

    public static TreasureMapTrade createStructureMapTrade(ItemStack[] inputs, ResourceLocation structure) {
        return TreasureMapTrade.forStructure(inputs, structure);
    }

    public static TreasureMapTrade createBiomeMapTrade(ItemStack[] inputs, String biome) {
        return TreasureMapTrade.forBiome(inputs, biome);
    }

    public static TreasureMapTrade createCustomMapTrade(ItemStack[] inputs, Function<EntityJS, BlockPos> func) {
        return new TreasureMapTrade(inputs, func);
    }

    public static EnchantedItemTrade createEnchantedItemTrade(ItemStack[] inputs, Item output) {
        return new EnchantedItemTrade(inputs, output);
    }

    public static StewTrade createStewTrade(ItemStack[] inputs, MobEffect[] effects, int duration) {
        return new StewTrade(inputs, effects, duration);
    }
}
