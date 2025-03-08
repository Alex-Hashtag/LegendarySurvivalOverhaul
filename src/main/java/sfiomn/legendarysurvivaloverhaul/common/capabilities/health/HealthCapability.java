package sfiomn.legendarysurvivaloverhaul.common.capabilities.health;

import net.minecraft.util.Mth;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import sfiomn.legendarysurvivaloverhaul.api.health.IHealthCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

public class HealthCapability implements IHealthCapability
{
	private float additionalHealth;
	private float shieldHealth;
	
	// Unsaved Data
	private int brokenHearts;
	private int oldBrokenHearts;
	private float oldAdditionalHealth;
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

		oldBrokenHearts = 0;
		oldAdditionalHealth = 0;
		oldShieldHealth = 0;
		packetTimer = 0;
	}

	public void tickUpdate(Player player, Level level, TickEvent.Phase phase) {
		if (phase == TickEvent.Phase.START) {
			this.packetTimer++;
			return;
		}

		brokenHearts = (int) (player.getAttributeValue(AttributeRegistry.BROKEN_HEART.get()));
	}

	@Override
	public void addAdditionalHealth(float healthValue) {
		this.setAdditionalHealth(this.getAdditionalHealth() + healthValue);
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
	public void setShieldHealth(float newHealthValue) {
		this.shieldHealth = Mth.clamp(newHealthValue, 0.0f, (float) Config.Baked.maxShieldHealth);
	}

	@Override
	public int getBrokenHearts()
	{
		return brokenHearts;
	}

	@Override
	public float getAdditionalHealth()
	{
		return additionalHealth;
	}

	@Override
	public float getShieldHealth() {
		return shieldHealth;
	}

	@Override
	public boolean isDirty()
	{
		return additionalHealth != oldAdditionalHealth ||
				shieldHealth != oldShieldHealth || brokenHearts != oldBrokenHearts;
	}

	@Override
	public void setClean()
	{
		oldAdditionalHealth = additionalHealth;
		oldShieldHealth = shieldHealth;
		oldBrokenHearts = brokenHearts;
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
		compound.putFloat("shieldHealth", getShieldHealth());
		compound.putInt("brokenHearts", getBrokenHearts());
		
		return compound;
	}

	public void readNBT(CompoundTag compound)
	{
		this.init();

		if (compound.contains("additionalHealth"))
			this.setAdditionalHealth(compound.getFloat("additionalHealth"));
		if (compound.contains("shieldHealth"))
			this.setShieldHealth(compound.getFloat("shieldHealth"));
		if (compound.contains("brokenHearts"))
			this.brokenHearts = compound.getInt("brokenHearts");
	}
}
