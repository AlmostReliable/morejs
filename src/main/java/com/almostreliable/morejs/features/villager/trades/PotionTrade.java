package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.TradeItem;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PotionTrade extends TransformableTrade<PotionTrade> {

    @Nullable
    List<Holder<Potion>> potions;
    private Item itemForPotion;
    private boolean onlyBrewablePotion;
    private boolean noBrewablePotion;

    public PotionTrade(TradeItem[] inputs) {
        super(inputs);
        this.itemForPotion = Items.POTION;
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
        }).filter(Objects::nonNull).map(BuiltInRegistries.POTION::wrapAsHolder).toList();

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

    private List<? extends Holder<Potion>> getFilteredPotions(PotionBrewing potionBrewing, Stream<? extends Holder<Potion>> potions) {
        return potions.filter(potionHolder -> {
            if (potionHolder.value().getEffects().isEmpty()) {
                return false;
            }

            if (this.onlyBrewablePotion) {
                return potionBrewing.isBrewablePotion(potionHolder);
            }

            if (this.noBrewablePotion) {
                return !potionBrewing.isBrewablePotion(potionHolder);
            }

            return true;
        }).toList();
    }

    @Nullable
    @Override
    public MerchantOffer createOffer(Entity entity, RandomSource random) {
        var potionBrewing = entity.level().potionBrewing();
        var allowedPotions =
                potions == null ? getFilteredPotions(potionBrewing, BuiltInRegistries.POTION.holders())
                                : getFilteredPotions(potionBrewing, potions.stream());

        if (allowedPotions.isEmpty()) {
            return null;
        }

        var potion = allowedPotions.get(random.nextInt(potions.size()));
        ItemStack itemStack = new ItemStack(itemForPotion);
        itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
        return createOffer(itemStack, random);
    }
}
