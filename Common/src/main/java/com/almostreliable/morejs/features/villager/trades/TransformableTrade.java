package com.almostreliable.morejs.features.villager.trades;

import com.almostreliable.morejs.features.villager.OfferModification;
import com.almostreliable.morejs.features.villager.TradeItem;
import com.google.common.base.Preconditions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;

@SuppressWarnings("UnusedReturnValue")
public abstract class TransformableTrade<T extends VillagerTrades.ItemListing>
        implements VillagerTrades.ItemListing {

    protected final TradeItem firstInput;
    protected final TradeItem secondInput;
    protected int maxUses = 16;
    protected int villagerExperience = 2;
    protected float priceMultiplier = 0.05F;
    @Nullable private Transformer transformer;

    public TransformableTrade(TradeItem[] inputs) {
        Preconditions.checkArgument(1 <= inputs.length && inputs.length <= 2, "Inputs must be 1 or 2 items");
        this.firstInput = inputs[0];
        this.secondInput = inputs.length == 2 ? inputs[1] : TradeItem.EMPTY;
    }

    @Nullable
    @Override
    public final MerchantOffer getOffer(Entity entity, RandomSource random) {
        MerchantOffer offer = createOffer(entity, random);
        if (offer == null) {
            return null;
        }
        if (transformer != null) {
            transformer.accept(new OfferModification(offer), entity, random);
        }
        return offer;
    }

    @Nullable
    public abstract MerchantOffer createOffer(Entity entity, RandomSource random);


    public T transform(Transformer offerModification) {
        this.transformer = offerModification;
        return getSelf();
    }

    public T maxUses(int maxUses) {
        this.maxUses = maxUses;
        return getSelf();
    }

    public T villagerExperience(int villagerExperience) {
        this.villagerExperience = villagerExperience;
        return getSelf();
    }

    public T priceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
        return getSelf();
    }

    @SuppressWarnings("unchecked")
    protected T getSelf() {
        return (T) this;
    }

    protected MerchantOffer createOffer(ItemStack output, RandomSource random) {
        ItemStack fi = firstInput.createItemStack(random);
        ItemStack si = secondInput.createItemStack(random);
        return new MerchantOffer(fi, si, output, maxUses, villagerExperience, priceMultiplier);
    }

    public interface Transformer {
        void accept(OfferModification offer, Entity entity, RandomSource random);
    }
}
