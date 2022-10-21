package com.almostreliable.morejs.features.villager.trades;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EnchantedItemTrade extends TransformableTrade<EnchantedItemTrade> {

    private final Item itemToEnchant;
    List<Enchantment> enchantments;
    private int minEnchantmentAmount = 1;
    private int maxEnchantmentAmount = 1;

    public EnchantedItemTrade(ItemStack[] inputs, Item itemToEnchant) {
        super(inputs);
        this.itemToEnchant = itemToEnchant;
        enchantments = Registry.ENCHANTMENT.stream().toList();
    }

    public EnchantedItemTrade enchantments(Enchantment... enchantments) {
        this.enchantments = Arrays.stream(enchantments).peek(e -> {
            if (e == null) {
                ConsoleJS.SERVER.error("Null enchantment in array: " + Arrays.toString(enchantments));
            }
        }).filter(Objects::nonNull).toList();
        return this;
    }

    public EnchantedItemTrade amount(int min, int max) {
        this.minEnchantmentAmount = min;
        this.maxEnchantmentAmount = max;
        return this;
    }

    public EnchantedItemTrade amount(int amount) {
        return amount(amount, amount);
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity entity, RandomSource random) {
        ItemStack result = itemToEnchant.equals(Items.BOOK) ? new ItemStack(Items.ENCHANTED_BOOK)
                                                            : new ItemStack(itemToEnchant);

        int amount = Mth.nextInt(random, minEnchantmentAmount, maxEnchantmentAmount);
        for (int i = 0; i < amount; i++) {
            Enchantment enchantment = enchantments.get(random.nextInt(enchantments.size()));
            int level = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            if (result.is(Items.ENCHANTED_BOOK)) {
                EnchantmentInstance enchantmentInstance = new EnchantmentInstance(enchantment, level);
                EnchantedBookItem.addEnchantment(result, enchantmentInstance);
            } else {
                result.enchant(enchantment, level);
            }
        }

        return createOffer(result);
    }
}
