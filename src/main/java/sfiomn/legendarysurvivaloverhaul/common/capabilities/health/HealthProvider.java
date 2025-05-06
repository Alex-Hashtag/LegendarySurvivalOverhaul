package sfiomn.legendarysurvivaloverhaul.common.capabilities.health;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HealthProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag>
{
	public static Capability<HealthCapability> HEALTH_CAPABILITY = CapabilityManager.get(new CapabilityToken<HealthCapability>() { });
	private final LazyOptional<HealthCapability> instance = LazyOptional.of(this::getInstance);
	private HealthCapability healthCapability = null;

	private HealthCapability getInstance() {
		if (this.healthCapability == null) {
			this.healthCapability = new HealthCapability();
		}
		return this.healthCapability;
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction)
	{
		if (capability == HEALTH_CAPABILITY)
			return instance.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT()
	{
		return getInstance().writeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		getInstance().readNBT(nbt);
	}
}
