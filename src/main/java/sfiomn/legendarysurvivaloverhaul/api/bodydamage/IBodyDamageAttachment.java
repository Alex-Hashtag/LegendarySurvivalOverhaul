package sfiomn.legendarysurvivaloverhaul.api.bodydamage;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Attachment for handling localized body damage
 */
public interface IBodyDamageAttachment
{
    int getExpectedBrokenHearts();

    float getBodyPartDamage(BodyPartEnum part);

    float getBodyPartHealthRatio(BodyPartEnum part);

    float getBodyPartMaxHealth(BodyPartEnum part);

    void setBodyPartDamage(BodyPartEnum part, float healthValue);

    void setBodyPartMaxHealth(BodyPartEnum part, float maxHealthValue);

    void healWithFoodExhaustion(Player player, BodyPartEnum part, float healingValue);

    void heal(BodyPartEnum part, float healingValue);

    void hurt(BodyPartEnum part, float damageValue);

    void applyHealingTime(BodyPartEnum part, int healingTicks, float healingPerTick);

    int getRemainingHealingTicks(BodyPartEnum part);

    float getHealingPerTicks(BodyPartEnum part);

    void updateBrokenHearts(Player player);

    /**
     * Check if at least one body part has health below provided health percent
     *
     * @param healthPercent health percent of the limb
     * @return isWounded or not
     */
    boolean isWoundedBelow(float healthPercent);

    /**
     * Get the body part ratio related to the malus body part
     */
    float getHealthRatioForMalusBodyPart(MalusBodyPartEnum part);

    /**
     * Force the health body damage sync server - client
     */
    void setManualDirty();

    /**
     * (Don't use this!) <br>
     * Checks if the capability needs an update
     *
     * @return boolean has localized body damage changed
     */
    boolean isDirty();

    /**
     * (Don't use this!) <br>
     * Sets the capability as updated
     */
    void setClean();

    /**
     * (Don't use this!) <br>
     * Gets the current tick of the packet timer
     *
     * @return int packetTimer
     */
    int getPacketTimer();
    
    /**
     * (Don't use this!) <br>
     * Gets the health blink timer for rendering
     * @return int healthBlinkTimer
     */
    int getHealthBlinkTimer();

    /**
     * (Don't use this!) <br>
     * Runs a tick update for the player's localized body damage capability
     *
     * @param player
     * @param world
     * @param isStart
     */
    void tickUpdate(Player player, Level world, boolean isStart);
}
