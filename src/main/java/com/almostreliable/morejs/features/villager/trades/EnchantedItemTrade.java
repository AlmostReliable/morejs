package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Optional;

public class EnchantedItemTrade extends TransformableTrade<EnchantedItemTrade> {

    private final ItemStack itemToEnchant;
    private IntProvider enchantLevels = UniformInt.of(5, 20);
    private final Either<TagKey<Enchantment>, HolderSet<Enchantment>> tradeableEnchantments;

    public EnchantedItemTrade(TradeItem[] inputs, ItemStack itemToEnchant, TagKey<Enchantment> tradeableEnchantments) {
        super(inputs);
        this.itemToEnchant = itemToEnchant;
        this.tradeableEnchantments = Either.left(tradeableEnchantments);
    }

    public EnchantedItemTrade(TradeItem[] inputs, ItemStack itemToEnchant, HolderSet<Enchantment> enchantments) {
        super(inputs);
        this.itemToEnchant = itemToEnchant;
        this.tradeableEnchantments = Either.right(enchantments);
    }

    public EnchantedItemTrade levels(IntProvider levels) {
        this.enchantLevels = levels;
        return this;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity entity, RandomSource random) {
        int levels = enchantLevels.sample(random);
        var registryAccess = entity.level().registryAccess();
        var possibleEnchantments = tradeableEnchantments.map(tag -> registryAccess
                .registryOrThrow(Registries.ENCHANTMENT)
                .getTag(tag), Optional::of);

        ItemStack result = EnchantmentHelper.enchantItem(random,
                itemToEnchant.copy(),
                levels,
                registryAccess,
                possibleEnchantments);
        return createOffer(result, random);
    }
}
