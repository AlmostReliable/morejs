package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PotionTrade extends TransformableTrade<PotionTrade> {

    List<Potion> potions;
    private Item itemForPotion;
    private boolean onlyBrewablePotion;
    private boolean noBrewablePotion;

    public PotionTrade(TradeItem[] inputs) {
        super(inputs);
        this.itemForPotion = Items.POTION;
        potions = Registry.POTION.stream().toList();
    }

    public PotionTrade item(Item item) {
        this.itemForPotion = item;
        return this;
    }

    public PotionTrade potions(Potion... potions) {
        this.potions = Arrays.stream(potions).peek(e -> {
            if (e == null) {
                ConsoleJS.SERVER.error("Null potion in array: " + Arrays.toString(potions));
            }
        }).filter(Objects::nonNull).toList();
        return this;
    }

    public PotionTrade onlyBrewablePotion() {
        this.onlyBrewablePotion = true;
        return this;
    }

    public PotionTrade noBrewablePotion() {
        this.noBrewablePotion = false;
        return this;
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity entity, RandomSource random) {
        List<Potion> allowedPotions = potions.stream().filter(p -> {
            if (p.getEffects().isEmpty()) {
                return false;
            }

            if (this.onlyBrewablePotion) {
                return PotionBrewing.isBrewablePotion(p);
            }

            if (this.noBrewablePotion) {
                return !PotionBrewing.isBrewablePotion(p);
            }

            return true;
        }).toList();

        if (allowedPotions.isEmpty()) {
            return null;
        }

        Potion potion = allowedPotions.get(random.nextInt(potions.size()));
        ItemStack potionStack = PotionUtils.setPotion(new ItemStack(itemForPotion), potion);
        return createOffer(potionStack, random);
    }
}
