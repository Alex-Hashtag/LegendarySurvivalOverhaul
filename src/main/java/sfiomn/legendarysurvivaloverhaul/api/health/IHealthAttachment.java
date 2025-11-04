package sfiomn.legendarysurvivaloverhaul.api.health;

public interface IHealthAttachment
{
    void addAdditionalHealth(float healthValue);

    void addShieldHealth(float healthValue);

    float getAdditionalHealth();

    void setAdditionalHealth(float newHealthValue);

    float getShieldHealth();

    void setShieldHealth(float newHealthValue);

    /**
     * (Don't use this!) <br>
     * Checks if the capability needs an update
     *
     * @return boolean has health changed
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
}
