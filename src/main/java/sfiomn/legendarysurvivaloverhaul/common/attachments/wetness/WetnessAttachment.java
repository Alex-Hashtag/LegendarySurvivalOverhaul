package sfiomn.legendarysurvivaloverhaul.common.attachments.wetness;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sfiomn.legendarysurvivaloverhaul.api.wetness.IWetnessAttachment;
import sfiomn.legendarysurvivaloverhaul.common.integration.curios.CuriosUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.meadow.MeadowUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

public class WetnessAttachment implements IWetnessAttachment, INBTSerializable<CompoundTag>
{
    public static final int WETNESS_LIMIT = 400;

    private int wetness;
    private int wetnessTickTimer; //Update immediately first time around

    private int packetTimer;
    private int oldWetness;
    private boolean dirty = false;

    public WetnessAttachment()
    {
        this.init();
    }

    public void init()
    {
        this.wetness = 0;
        this.wetnessTickTimer = 0;

        this.packetTimer = 0;
        this.oldWetness = this.wetness;
        this.dirty = false;
    }

    @Override
    public int getWetness()
    {
        return this.wetness;
    }

    @Override
    public void setWetness(int wetness)
    {
        this.wetness = Mth.clamp(wetness, 0, WETNESS_LIMIT);
    }

    @Override
    public int getWetnessTickTimer()
    {
        return this.wetnessTickTimer;
    }

    @Override
    public void setWetnessTickTimer(int tickTimer)
    {
        this.wetnessTickTimer = tickTimer;
    }

    @Override
    public void addWetness(int wetness)
    {
        this.setWetness(this.wetness + wetness);
    }

    @Override
    public void addWetnessTickTimer(int tickTimer)
    {
        this.setWetnessTickTimer(this.getWetnessTickTimer() + tickTimer);
    }

    /**
     * This probably isn't too terribly performance friendly but it's something at least<br>
     * <br>
     * TODO: optimization!!
     */
    @Override
    public void tickUpdate(Player player, Level level, boolean isStart)
    {
        if (getWetnessTickTimer() == -1 || CuriosUtil.isCurioItemEquipped(player, ItemRegistry.SPONGE.get()))
        {
            if (this.getWetness() > 0)
                this.setWetness(0);
            return;
        }

        if (isStart)
        {
            packetTimer++;
            return;
        }

        this.addWetnessTickTimer(1);
        if (this.getWetnessTickTimer() < Config.Baked.wetnessTickTimer)
            return;
        this.setWetnessTickTimer(0);

        if (this.wetness > 0 && player.getRemainingFireTicks() > 0 && !player.fireImmune())
            this.addWetness(-10);

        BlockPos pos = player.blockPosition();

        if (player.getVehicle() != null)
        {
            ResourceLocation entityRegistryName = BuiltInRegistries.ENTITY_TYPE.getKey(player.getVehicle().getType());
            if (entityRegistryName != null && Config.Baked.wetnessImmunityMounts.contains(entityRegistryName.toString()))
            {
                if (this.wetness > 0)
                    this.addWetness(Config.Baked.wetnessDecrease);
                return;
            }
        }

        // If the player is not riding a living entity, shift the position used for calculations up by one block
        // This way, sitting in a boat that's floating on the water won't increase a player's wetness
        if (player.getVehicle() != null && !(player.getVehicle() instanceof LivingEntity) && !player.getVehicle().hasImpulse)
        {
            pos = pos.above();
            if (this.wetness > 0 && level.getFluidState(pos).isEmpty())
                worldParticles(player, level);
        } else
        {
            if (this.wetness > 0 && (level.getFluidState(pos).isEmpty() || level.getFluidState(pos.above()).isEmpty()))
                worldParticles(player, level);
        }

        FluidState fluidState = level.getFluidState(pos);
        BlockState blockState = level.getBlockState(pos);
        FluidState fluidStateUp = level.getFluidState(pos.above());

        // If no fluid on the pos of the player (or above if in a boat)
        // only check for raining on pos above player (to avoid issue with half blocks)
        if (fluidState.isEmpty() && !blockState.is(Blocks.WATER_CAULDRON) && !MeadowUtil.isInWoodenWaterCauldron(blockState))
        {
            Biome.Precipitation precipitation = WorldUtil.getPrecipitationAt(level, player, pos);
            if (wetness < WETNESS_LIMIT && precipitation == Biome.Precipitation.RAIN)
                this.addWetness(Config.Baked.wetnessRainIncrease);
            else if (this.wetness > 0 && precipitation == Biome.Precipitation.NONE)
                this.addWetness(Config.Baked.wetnessDecrease);
        } else
        {
            Fluid fluid = Fluids.EMPTY;
            float fractionalLevel = 0.0f;

            if (!fluidState.isEmpty())
            {
                fluid = fluidState.getType();
                fractionalLevel = MathUtil.invLerp(1, 8, fluidState.getAmount());
            } else if (blockState.is(Blocks.WATER_CAULDRON) || MeadowUtil.isInWoodenWaterCauldron(blockState))
            {
                fluid = Fluids.WATER;
                if (blockState.hasProperty(LayeredCauldronBlock.LEVEL))
                    fractionalLevel = MathUtil.invLerp(1, 3, blockState.getValue(LayeredCauldronBlock.LEVEL));
            }

            // if player is out of water
            if (((float) player.position().y()) > ((float) pos.getY()) + fractionalLevel + 0.0625f)
                return;

            // add the amount of fluid in the upper block as well
            if (!fluidStateUp.isEmpty())
                fractionalLevel += MathUtil.invLerp(1, 8, fluidStateUp.getAmount());

            if (this.wetness > 0 && fluid instanceof LavaFluid)
            {
                this.addWetness(-Math.round(20.0f * fractionalLevel));
            }
            // Last fallback, just assume that it's the same as water and go from there
            else if (this.wetness < WETNESS_LIMIT)
            {
                this.addWetness(Math.round(Config.Baked.wetnessFluidIncrease * fractionalLevel));
            }
        }
    }

    private void worldParticles(Player player, Level level)
    {
        Vec3 pos = player.position();
        AABB box = player.getBoundingBox();

        int particleSpawnRate = Math.round((1.0f - MathUtil.invLerp(0, WETNESS_LIMIT, this.wetness)) * 10f);

        if (particleSpawnRate == 0 || level.getLevelData().getGameTime() % particleSpawnRate == 0)
            ((ServerLevel) level).sendParticles(ParticleTypes.FALLING_WATER, pos.x, pos.y + (box.getYsize() / 2), pos.z, 1, box.getXsize() / 3, box.getYsize() / 4, box.getZsize() / 3, 0);
    }

    @Override
    public boolean isDirty()
    {
        return this.wetness != this.oldWetness || this.dirty;
    }

    @Override
    public void setClean()
    {
        this.oldWetness = this.wetness;
    }

    @Override
    public void setDirty()
    {
        this.dirty = true;
    }

    @Override
    public int getPacketTimer()
    {
        return this.packetTimer;
    }

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();

        compound.putInt("wetness", this.getWetness());
        compound.putInt("wetnessTickTimer", this.getWetnessTickTimer());

        return compound;
    }

    public void readNBT(CompoundTag compound)
    {
        this.init();

        if (compound.contains("wetness"))
            this.setWetness(compound.getInt("wetness"));
        if (compound.contains("wetnessTickTimer"))
            this.setWetnessTickTimer(compound.getInt("wetnessTickTimer"));
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        return writeNBT();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
    {
        readNBT(nbt);
    }
}
