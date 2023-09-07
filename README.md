<h1 align="center">
    <a href="https://github.com/AlmostReliable/morejs"><img src=https://i.imgur.com/UXnNGYx.png" alt="Preview" width=200></a>
    <p>MoreJS</p>
</h1>

<div align="center">

A [Minecraft] mod to extend [KubeJS] with additional events.

[![Workflow Status][workflow_status_badge]][workflow_status_link]
[![License][license_badge]][license]

[![Version][version_badge]][version_link]
[![Total Downloads CF][total_downloads_cf_badge]][curseforge]
[![Total Downloads MR][total_downloads_mr_badge]][modrinth]

[![Discord][discord_badge]][discord]
[![Wiki][wiki_badge]][wiki]

</div>

## **📑 Overview**
This is a mod for [Minecraft]-[Forge] and [Fabric] and needs [KubeJS].<br>
An overview of a few events: 
| Events                           | Client Script | Server Script |
| -------------------------------- | :-----------: | :-----------: |
| morejs.villager.trades           |               |      ✔️       |
| morejs.wanderer.trades           |               |      ✔️       |
| morejs.player.start_trading      |               |      ✔️       |
| morejs.enchantment_table.changed |               |      ✔️       |
| morejs.enchantment_table.enchant |               |      ✔️       |
| morejs.enchantment_table.tooltip |      ✔️       |               |
| morejs.teleport                  |               |      ✔️       |
| morejs.structure.load            |               |      ✔️       |
| morejs.player.xp_change          |               |      ✔️       |


## **🔧 Installation**
1. Download the latest **mod jar** from the [releases], from [CurseForge] or from [Modrinth].
2. Download the latest **mod jar** of [KubeJS].
3. Install Minecraft [Forge] or [Fabric].
4. Drop both **jar files** into your mods folder.

## **⚙️ More Information**
For more information about the usage and the functionality of the mod, please visit our [wiki].

## **🎓 License**
This project is licensed under the [GNU Lesser General Public License v3.0][license].

<!-- Badges -->
[workflow_status_badge]: https://img.shields.io/github/actions/workflow/status/AlmostReliable/morejs/build.yml?branch=1.20.1&style=for-the-badge
[workflow_status_link]: https://github.com/AlmostReliable/morejs/actions
[license_badge]: https://img.shields.io/github/license/AlmostReliable/morejs?style=for-the-badge
[version_badge]: https://img.shields.io/badge/dynamic/json?color=0078FF&label=release&style=for-the-badge&query=name&url=https://api.razonyang.com/v1/github/tag/AlmostReliable/morejs%3Fprefix=v1.20.1-
[version_link]: https://github.com/AlmostReliable/morejs/releases/latest
[total_downloads_cf_badge]: https://img.shields.io/badge/dynamic/json?color=e04e14&label=CurseForge&style=for-the-badge&query=downloads.total&url=https%3A%2F%2Fapi.cfwidget.com%2F666198&logo=curseforge
[total_downloads_mr_badge]: https://img.shields.io/modrinth/dt/mo64mR1W?color=5da545&label=Modrinth&style=for-the-badge&logo=modrinth
[discord_badge]: https://img.shields.io/discord/917251858974789693?color=5865f2&label=Discord&logo=discord&style=for-the-badge
[wiki_badge]: https://img.shields.io/badge/Read%20the-Wiki-ba00ff?style=for-the-badge

<!-- Links -->
[minecraft]: https://www.minecraft.net/
[kubejs]: https://www.curseforge.com/minecraft/mc-mods/kubejs
[discord]: https://discord.com/invite/ThFnwZCyYY
[releases]: https://github.com/AlmostReliable/morejs/releases
[curseforge]: https://www.curseforge.com/minecraft/mc-mods/morejs
[modrinth]: https://modrinth.com/mod/morejs
[forge]: http://files.minecraftforge.net/
[fabric]: https://fabricmc.net/
[wiki]: https://github.com/AlmostReliable/morejs/wiki
[changelog]: CHANGELOG.md
[license]: LICENSE
