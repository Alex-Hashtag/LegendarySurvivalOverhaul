package sfiomn.legendarysurvivaloverhaul.common.capabilities.health;

import net.minecraft.util.Mth;
import net.minecraft.nbt.CompoundTag;
import sfiomn.legendarysurvivaloverhaul.api.health.IHealthCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class HealthCapability implements IHealthCapability
{
	private float additionalHealth;
	private int brokenHearts;
	private float shieldHealth;
	
	// Unsaved Data
	private float oldAdditionalHealth;
	private int oldBrokenHearts;
	private float oldShieldHealth;
	private int packetTimer;
	
	public HealthCapability()
	{
		this.init();
	}
	
	public void init()
	{
		additionalHealth = 0;
		brokenHearts = 0;
		shieldHealth = 0;
		
		oldAdditionalHealth = 0;
		oldBrokenHearts = 0;
		oldShieldHealth = 0;
		packetTimer = 0;
	}

	@Override
	public void addAdditionalHealth(float healthValue) {
		this.setAdditionalHealth(this.getAdditionalHealth() + healthValue);
	}

	@Override
	public void addBrokenHeart(int heartValue) {
		this.setBrokenHearts(this.getBrokenHearts() + heartValue);
	}

	@Override
	public void addShieldHealth(float shieldValue) {
		this.setShieldHealth(this.getShieldHealth() + shieldValue);
	}

	@Override
	public void setAdditionalHealth(float newHealthValue) {
		this.additionalHealth = Math.min(newHealthValue, (float) Config.Baked.maxAdditionalHealth);
	}

	@Override
	public void setBrokenHearts(int newHeartValue) {
		this.brokenHearts = Math.max(0, newHeartValue);
	}

	@Override
	public void setShieldHealth(float newHealthValue) {
		this.shieldHealth = Mth.clamp(newHealthValue, 0.0f, (float) Config.Baked.maxShieldHealth);
	}

	@Override
	public float getAdditionalHealth()
	{
		return additionalHealth;
	}

	@Override
	public int getBrokenHearts() {
		return brokenHearts;
	}

	@Override
	public float getShieldHealth() {
		return shieldHealth;
	}

	@Override
	public boolean isDirty()
	{
		return additionalHealth != oldAdditionalHealth ||
				brokenHearts != oldBrokenHearts ||
				shieldHealth != oldShieldHealth;
	}

	@Override
	public void setClean()
	{
		oldAdditionalHealth = additionalHealth;
		oldBrokenHearts = brokenHearts;
		oldShieldHealth = shieldHealth;
	}

	@Override
	public int getPacketTimer()
	{
		return packetTimer;
	}
	
	public CompoundTag writeNBT() 
	{
		CompoundTag compound = new CompoundTag();
		
		compound.putFloat("additionalHealth", getAdditionalHealth());
		compound.putInt("brokenHearts", getBrokenHearts());
		compound.putFloat("shieldHealth", getShieldHealth());
		
		return compound;
	}

	public void readNBT(CompoundTag compound)
	{
		this.init();

		if (compound.contains("additionalHealth"))
			this.setAdditionalHealth(compound.getFloat("additionalHealth"));
		if (compound.contains("brokenHearts"))
			this.setBrokenHearts(compound.getInt("brokenHearts"));
		if (compound.contains("shieldHealth"))
			this.setShieldHealth(compound.getFloat("shieldHealth"));
	}
}
