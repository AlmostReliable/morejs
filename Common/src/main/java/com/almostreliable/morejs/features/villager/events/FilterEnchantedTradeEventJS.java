package com.almostreliable.morejs.features.villager.events;

import com.almostreliable.morejs.core.Events;
import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterEnchantedTradeEventJS extends LivingEntityEventJS {

    private final AbstractVillager villager;
    private final RandomSource randomSource;
    private final Stream<Enchantment> stream;
    private final Set<Enchantment> enchantmentsToRemove = new HashSet<>();
    @Nullable private Set<Enchantment> enchantmentsToAdd;
    private boolean allowTreasures;
    private boolean allowCurses;
    private boolean onlyTradeables;
    private Predicate<Enchantment> customFilter;

    public static Stream<Enchantment> invokeAndHandle(AbstractVillager villager, RandomSource randomSource, Stream<Enchantment> stream) {
        var event = new FilterEnchantedTradeEventJS(villager, randomSource, stream);
        Events.FILTER_ENCHANTED_BOOK_TRADE.post(event);
        return event.apply();
    }

    public FilterEnchantedTradeEventJS(AbstractVillager villager, RandomSource randomSource, Stream<Enchantment> stream) {
        this.villager = villager;
        this.randomSource = randomSource;
        this.stream = stream;
    }

    public boolean isVillager() {
        return getEntity() instanceof Villager;
    }

    public boolean isWanderer() {
        return getEntity() instanceof WanderingTrader;
    }

    public void allowTreasures(boolean flag) {
        allowTreasures = flag;
    }

    public void allowCurses(boolean flag) {
        allowCurses = flag;
    }

    public void onlyTradeables(boolean flag) {
        onlyTradeables = flag;
    }

    public void customFilter(Predicate<Enchantment> customFilter) {
        this.customFilter = customFilter;
    }

    @Override
    public AbstractVillager getEntity() {
        return villager;
    }

    public void removeEnchantments(Enchantment... enchantments) {
        for (Enchantment enchantment : enchantments) {
            if (enchantment == null) continue;
            enchantmentsToRemove.add(enchantment);
        }
    }

    public void addEnchantments(Enchantment... enchantments) {
        if (enchantmentsToAdd == null) {
            enchantmentsToAdd = new HashSet<>();
        }

        for (Enchantment enchantment : enchantments) {
            if (enchantment == null) continue;
            enchantmentsToAdd.add(enchantment);
        }
    }

    public RandomSource getRandom() {
        return randomSource;
    }

    public List<Enchantment> getPotentialEnchantments() {
        return apply().toList();
    }

    public void printPotentialEnchantments() {
        ConsoleJS.SERVER.info("Potential Enchantments: " + apply()
                .flatMap(e -> Registry.ENCHANTMENT.getResourceKey(e).stream())
                .map(key -> key.location().toString())
                .collect(Collectors.joining(", ")));
    }

    private Stream<Enchantment> apply() {
        var streamRef = stream.filter(enchantment -> {
            if (enchantmentsToRemove.contains(enchantment)) {
                return false;
            }

            if (!allowCurses && enchantment.isCurse()) {
                return false;
            }

            if (!allowTreasures && enchantment.isTreasureOnly()) {
                return false;
            }

            if (onlyTradeables && !enchantment.isTradeable()) {
                return false;
            }

            return customFilter.test(enchantment);
        });

        if (enchantmentsToAdd != null) {
            List<Enchantment> tmpEnchantments = streamRef.collect(Collectors.toCollection(ArrayList::new));
            tmpEnchantments.addAll(enchantmentsToAdd);
            return tmpEnchantments.stream();
        }

        return streamRef;
    }
}
