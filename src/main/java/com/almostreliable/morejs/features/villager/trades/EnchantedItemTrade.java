package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;

public class EnchantedItemTrade extends TransformableTrade<EnchantedItemTrade> {

    private final Item itemToEnchant;
    private final TagKey<Enchantment> tradeableEnchantments;
    private IntProvider enchantLevels = UniformInt.of(5, 20);

    public EnchantedItemTrade(TradeItem[] inputs, Item itemToEnchant, TagKey<Enchantment> tradeableEnchantments) {
        super(inputs);
        this.itemToEnchant = itemToEnchant;
        this.tradeableEnchantments = tradeableEnchantments;
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
        var possibleEnchantments = registryAccess
                .registryOrThrow(Registries.ENCHANTMENT)
                .getTag(tradeableEnchantments);

        ItemStack result = EnchantmentHelper.enchantItem(random,
                new ItemStack(itemToEnchant),
                levels,
                registryAccess,
                possibleEnchantments);
        return createOffer(result, random);
    }
}
