package com.almostreliable.morejs.features.misc;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;


public class ExperiencePlayerEventJS extends PlayerEventJS {

    private final Player player;
    private int amount;

    public ExperiencePlayerEventJS(Player player, int amount) {
        this.player = player;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    public float getExperienceProgress() {
        return player.experienceProgress;
    }

    public void setExperienceProgress(float progress) {
        player.experienceProgress = progress;
    }

    public int getExperienceLevel() {
        return player.experienceLevel;
    }

    public void setExperienceLevel(int level) {
        player.experienceLevel = level;
    }

    public int getTotalExperience() {
        return player.totalExperience;
    }

    public void setTotalExperience(int experience) {
        player.totalExperience = experience;
    }

    public int getXpNeededForNextLevel() {
        return player.getXpNeededForNextLevel();
    }

    public int getRemainingExperience() {
        return (int) (getXpNeededForNextLevel() - (getExperienceProgress() * getXpNeededForNextLevel()));
    }

    public boolean willLevelUp() {
        return getExperienceProgress() + (getAmount() / (float) getXpNeededForNextLevel()) >= 1.0F;
    }
}
