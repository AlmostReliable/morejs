# Changelog

## Unreleased

- Rename methods in wanderer trading event to match villager trading event

## [0.14.0] - 2024-10-31

- Added `MoreJS.postUpdateOffers` event which triggers after a Villager updates their offers

## [0.13.1] - 2024-09-13

- Fix removing of potion brewing
- Fix enchantments not being applied to the item in the enchantment table

## [0.13.0] - 2024-07-26

- Rename some functions in VillagerUtils to fit other functions.
- Fix trade cache does not get cleared after reload.

## [0.12.0] - 2024-07-26

- Refactor potion brewing event to use a filter object for removing brewing recipes

## [0.11.0] - 2024-07-26

- Update to 1.21

## [0.10.0] - 2024-05-01

- Add `setRewardExp` and `isRewardingExp` for offers

## [0.9.0] - 2024-03-22

- Add `.replaceItems(ingredient, item)` when working with offers similar to `.replaceEmeralds`
- Fix a bug that rare trades from wanderer are not detected on update events

## [0.8.0] - 2024-03-22

- Add `event.clickedButton`, `event.costs`, `event.enchantments`, `event.enchantmentIds`
  to `MoreJSEvents.enchantmentTableEnchant` event

## [0.7.0] - 2024-03-14
- Add `removeContainer` and `addContainerRecipe` to potion event

## [0.6.0] - 2024-02-04
- Fix [#9](https://github.com/AlmostReliable/morejs/issues/9)
- Add utility to `VillagerUtils`
    - `.setAbstractTrades(tradeMap, level, trades)`
    - `.getAbstractTrades(tradeMap, level)`
    - `.getVillagerTrades(profession)`
    - `.getVillagerTrades(profession, level)`
    - `.getRandomVillagerTrade(profession)`
    - `.getRandomVillagerTrade(profession, level)`
    - `.getWandererTrades(level)`
    - `.getRandomWandererTrade(level)`

## [0.5.0] - 2023-10-21
- Fix [#8](https://github.com/AlmostReliable/morejs/issues/8)

## [0.4.0] - 2023-10-21
- Add `structureAfterPlace` event. Thanks to [Pietro Lopes](https://github.com/pietro-lopes)

## [0.3.0] - 2023-09-10
- Update to KubeJS 6.3 1.20.1

## [0.2.0] - 2023-07-18
- Update to KubeJS 6.1

## [0.1.1] - 2023-05-26
- Add `TradeItem` to allow price ranges for trades
- Add custom potion removing for forge

## [0.1.0] - 2023-05-16
- Add event `filterEnchantedBookTrade` and `filterAvailableEnchantments` to filter enchantments

## [0.0.8] - 2023-05-09
- Add event `piglinPlayerBehavior` for piglin behavior (like wearing gold)
- Add events `updateAbstractVillagerOffers`, `updateVillagerOffers` and `updateWandererOffers` to handle villager trading when it's updated
- Add basic villager trade from forge to filters

## [0.0.7] - 2023-01-19
- Change mixin priority for enchantment feature

## [0.0.6] - 2023-01-08
- Fix loading issue for villager trades on Forge
- Fix missing accesswidener in fabric.mod.json

## [0.0.5] - 2022-11-24
- Add `registerPotionBrewing` event

## [0.0.4] - 2022-11-24
- Add `enchantmentTableIsEnchantable` event
- Reset the item for `enchantmentTableChanged`
- Add more utilities to events to check enchantment ids directly

## [0.0.3] - 2022-10-21
- Fix crash with new KubeJS version

## [0.0.2] - 2022-10-21
- Add trade filters

## [0.0.1] - 2022-08-30
- Add `morejs.villager.trading` event.
- Add `morejs.wanderer.trading` event.
- Add `morejs.enchantment_table.changed` event.
- Add `morejs.enchantment_table.enchant` event.
- Add `morejs.enchantment_table.tooltip` event.
- Add `morejs.teleport` event.
- Add `morejs.structure.load` event.
- Add `morejs.player.xp_change` event.
- Add `morejs.player.start_trading` event.

<!-- Versions -->
[0.14.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.21-neoforge-0.14.0
[0.13.1]: https://github.com/AlmostReliable/morejs/releases/tag/v1.21-neoforge-0.13.1
[0.13.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.21-neoforge-0.13.0
[0.12.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.21-neoforge-0.12.0
[0.11.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.21-neoforge-0.11.0
[0.10.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.20.1-0.10.0
[0.9.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.20.1-0.9.0
[0.8.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.20.1-0.8.0
[0.7.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.20.1-0.7.0
[0.6.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.20.1-0.6.0
[0.5.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.20.1-0.5.0
[0.4.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.20.1-0.4.0
[0.3.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.20.1-0.3.0
[0.2.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.2.0-beta
[0.1.1]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.1.1-beta
[0.1.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.1.0-beta
[0.0.8]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.8-beta
[0.0.7]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.7-beta
[0.0.6]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.6-beta
[0.0.5]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.5-beta
[0.0.4]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.4-beta
[0.0.3]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.3-beta
[0.0.2]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.2-beta
[0.0.1]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.1-beta
