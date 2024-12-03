package sfiomn.legendarysurvivaloverhaul.api.health;

public interface IHealthCapability
{
	public void addAdditionalHealth(float healthValue);

	public void addBrokenHeart(int heartValue);

	public void addShieldHealth(float healthValue);

	public void setAdditionalHealth(float newHealthValue);

	public void setBrokenHearts(int newHeartValue);

	public void setShieldHealth(float newHealthValue);

	public float getAdditionalHealth();

	public int getBrokenHearts();

	public float getShieldHealth();
	
	/**
	 * (Don't use this!) <br>
	 * Checks if the capability needs an update
	 * @return boolean has health changed
	 */
	public boolean isDirty();
	/**
	 * (Don't use this!) <br>
	 * Sets the capability as updated
	 */
	public void setClean();
	
	/**
	 * (Don't use this!) <br>
	 * Gets the current tick of the packet timer
	 * @return int packetTimer
	 */
	public int getPacketTimer();
}
