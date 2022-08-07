package com.almostreliable.missingname.modules.villager.trades;

import com.almostreliable.missingname.modules.villager.OfferModification;
import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.entity.EntityJS;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("UnusedReturnValue")
public abstract class TransformableTrade<T extends VillagerTrades.ItemListing>
        implements VillagerTrades.ItemListing {

    protected final ItemStack firstInput;
    protected final ItemStack secondInput;
    @Nullable private Transformer transformer;
    protected int maxUses = 16;
    protected int villagerExperience = 2;
    protected float priceMultiplier = 0.05F;

    public TransformableTrade(ItemStack[] inputs) {
        Preconditions.checkArgument(1 <= inputs.length && inputs.length <= 2, "Inputs must be 1 or 2 items");
        this.firstInput = inputs[0];
        this.secondInput = inputs.length == 2 ? inputs[1] : ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public final MerchantOffer getOffer(Entity entity, Random random) {
        MerchantOffer offer = createOffer(entity, random);
        if (offer == null) {
            return null;
        }
        if (transformer != null) {
            transformer.accept(new OfferModification(offer), new EntityJS(entity), random);
        }
        return offer;
    }

    @Nullable
    public abstract MerchantOffer createOffer(Entity entity, Random random);


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

    protected MerchantOffer createOffer(ItemStack output) {
        return new MerchantOffer(firstInput, secondInput, output, maxUses, villagerExperience, priceMultiplier);
    }

    public interface Transformer {
        void accept(OfferModification offer, EntityJS entity, Random random);
    }
}
