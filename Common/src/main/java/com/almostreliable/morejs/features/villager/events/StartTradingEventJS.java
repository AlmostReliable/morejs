package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.features.villager.OfferExtension;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StartTradingEventJS extends PlayerEventJS {
    private final Player player;
    private final Merchant merchant;

    public StartTradingEventJS(Player player, Merchant merchant) {
        this.player = player;
        this.merchant = merchant;
    }

    @Override
    public EntityJS getEntity() {
        return null;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void forEachOffers(BiConsumer<OfferExtension, Integer> consumer) {
        for (int i = 0; i < merchant.getOffers().size(); i++) {
            MerchantOffer offer = merchant.getOffers().get(i);
            consumer.accept((OfferExtension) offer, i);
        }
    }
}
