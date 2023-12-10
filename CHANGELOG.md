# Changelog

## [0.5.0] - 2023-10-21
- Fix [#8](https://github.com/AlmostReliable/morejs/issues/8)

## [0.4.0] - 2023-10-21
- Added `structureAfterPlace` event. Thanks to [Pietro Lopes](https://github.com/pietro-lopes)

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
- Added trade filters

## [0.0.1] - 2022-08-30
- Added `morejs.villager.trading` event.
- Added `morejs.wanderer.trading` event.
- Added `morejs.enchantment_table.changed` event.
- Added `morejs.enchantment_table.enchant` event.
- Added `morejs.enchantment_table.tooltip` event.
- Added `morejs.teleport` event.
- Added `morejs.structure.load` event.
- Added `morejs.player.xp_change` event.
- Added `morejs.player.start_trading` event.

<!-- Versions -->
[0.5.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.12.1-0.5.0
[0.4.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.12.1-0.4.0-beta
[0.3.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.12.1-0.3.0-beta
[0.2.0]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.2.0-beta
[0.0.8]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.8-beta
[0.0.7]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.7-beta
[0.0.6]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.6-beta
[0.0.4]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.4-beta
[0.0.3]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.3-beta
[0.0.2]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.2-beta
[0.0.1]: https://github.com/AlmostReliable/morejs/releases/tag/v1.19-0.0.1-beta
