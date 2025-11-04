package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.artifacts.ArtifactsUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.beachparty.BeachpartyUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons.EclipticSeasonsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public final class WorldUtil
{
    private static Pair<BlockPos, Float> undergroundEffect = new ImmutablePair<>(new BlockPos(0, 0, 0), 0f);

    private WorldUtil()
    {
    }

    public static BlockPos getSidedBlockPos(Level world, Entity entity)
    {
        if (!world.isClientSide)
        {
            return entity.blockPosition();
        }

        if (entity instanceof Player)
        {
            return BlockPos.containing(entity.position().add(0, 0.5d, 0));
        } else if (entity instanceof ItemFrame)
        {
            return BlockPos.containing(entity.position().add(0, -0.45d, 0));
        } else
        {
            return entity.blockPosition();
        }
    }

    public static boolean isChunkLoaded(Level world, BlockPos pos)
    {
        if (world.isClientSide)
        {
            return true;
        } else
        {
            return world.getChunkSource().hasChunk(pos.getX() >> 4, pos.getZ() >> 4);
        }
    }

    public static boolean isPlayerOrPosUndercover(Level level, Player player, BlockPos pos)
    {
        if (LegendarySurvivalOverhaul.beachpartyLoaded)
            if (BeachpartyUtil.isUnderParasol(level, player, pos))
                return true;

        if (LegendarySurvivalOverhaul.artifactsLoaded)
            if (ArtifactsUtil.isHoldingUmbrella(player))
                return true;

        return !level.canSeeSky(pos) || level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY();
    }

    public static Biome.Precipitation getPrecipitationAt(Level level, Player player, BlockPos pos)
    {
        if (isPlayerOrPosUndercover(level, player, pos))
            return Biome.Precipitation.NONE;

        if (LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
            return EclipticSeasonsUtil.getPrecipitation(level, pos);

        if (!level.isRaining())
            return Biome.Precipitation.NONE;

        return level.getBiome(pos).value().getPrecipitationAt(pos);
    }

    public static boolean isRainingOrSnowingAt(Level level, BlockPos pos)
    {
        if (LegendarySurvivalOverhaul.eclipticSeasonsLoaded)
            return EclipticSeasonsUtil.getPrecipitation(level, pos) != Biome.Precipitation.NONE;

        if (!level.isRaining())
        {
            return false;
        } else if (!level.canSeeSky(pos))
        {
            return false;
        } else return level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() <= pos.getY();
    }

    public static float calculateClientWorldEntityTemperature(Level world, Entity entity)
    {
        return TemperatureUtil.getWorldTemperature(world, getSidedBlockPos(world, entity));
    }

    public static float toFahrenheit(float temperature)
    {
        return 32 + (temperature * 1.8f);
    }

    public static float getUndergroundEffectAtPos(Level level, BlockPos pos)
    {
        if (!WorldUtil.undergroundEffect.getKey().toShortString().equals(pos.toShortString()))
        {
            float averageDepthUnderground = 0;
            Vec3i[] posOffsets =
                    {
                            new Vec3i(0, 0, 0),
                            new Vec3i(10, 0, 0),
                            new Vec3i(-10, 0, 0),
                            new Vec3i(0, 0, 10),
                            new Vec3i(0, 0, -10),
                            new Vec3i(7, 0, 7),
                            new Vec3i(7, 0, -7),
                            new Vec3i(-7, 0, 7),
                            new Vec3i(-7, 0, -7)
                    };

            for (Vec3i offset : posOffsets)
            {
                BlockPos posOffset = pos.offset(offset);
                int surfaceDistance = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, posOffset).getY() - posOffset.getY();

                if (surfaceDistance < Config.Baked.undergroundEffectStartDistanceToWS)
                {
                    continue;
                }

                if (surfaceDistance >= Config.Baked.undergroundEffectEndDistanceToWS)
                {
                    averageDepthUnderground += 1;
                } else
                {
                    averageDepthUnderground += (surfaceDistance - Config.Baked.undergroundEffectStartDistanceToWS) / (float) (Config.Baked.undergroundEffectEndDistanceToWS - Config.Baked.undergroundEffectStartDistanceToWS);
                }
            }
            WorldUtil.undergroundEffect = new ImmutablePair<>(pos, averageDepthUnderground / posOffsets.length);
        }
        return WorldUtil.undergroundEffect.getValue();
    }

    public static Entity getEntityLookedAt(Player player, double finalDistance)
    {
        Entity foundEntity = null;
        double distanceFromEye;
        HitResult positionLookedAt = player.pick(finalDistance, 0.0f, false);
        Vec3 eyePosition = player.getEyePosition(0.0f);

        distanceFromEye = positionLookedAt.getLocation().distanceTo(eyePosition);

        Vec3 lookVector = player.getLookAngle();
        Vec3 reachVector = eyePosition.add(lookVector.x * distanceFromEye, lookVector.y * distanceFromEye, lookVector.z * distanceFromEye);

        AABB expandedPlayerBound = player.getBoundingBox().expandTowards(lookVector.x * distanceFromEye, lookVector.y * distanceFromEye, lookVector.z * distanceFromEye);

        EntityHitResult entityRayTraceResult = ProjectileUtil.getEntityHitResult(player, eyePosition, reachVector, expandedPlayerBound, (entity) -> !entity.isSpectator() && entity.isPickable(), distanceFromEye * distanceFromEye);
        if (entityRayTraceResult != null)
        {
            foundEntity = entityRayTraceResult.getEntity();
        }

        // Here for the understanding of ProjectileHelper
		/*
		Entity lookedEntity = null;
		List<Entity> entitiesInBoundingBox = player.level.getEntities(player, expandedPlayerBound, (entity) -> !entity.isSpectator() && entity.isPickable());

		double minDistance = distanceFromEye;

		for (Entity entity : entitiesInBoundingBox) {
			AxisAlignedBB collisionBox = entity.getBoundingBoxForCulling();
			Optional<Vector3d> interceptPosition = collisionBox.clip(eyePosition, reachVector);

			if (collisionBox.contains(eyePosition)) {
				if (minDistance >= 0.0D) {
					lookedEntity = entity;
					minDistance = 0.0D;
				}
			} else if (interceptPosition.isPresent()) {
				double distanceToEntity = eyePosition.distanceTo(interceptPosition.get());
				if (entity.getRootVehicle() == player.getRootVehicle() && !entity.canRiderInteract()) {
					if (minDistance == 0.0D) {
						lookedEntity = entity;
					}
				} else if (minDistance > distanceToEntity || minDistance == 0.0D) {
					lookedEntity = entity;
					minDistance = distanceToEntity;
				}
			}

			if (lookedEntity != null && minDistance < distanceFromEye)
				foundEntity = lookedEntity;
		}*/

        return foundEntity;
    }

    public static String timeInGame(Minecraft mc)
    {
        int dayCount = 0;
        int dayTime = mc.level != null ? (int) mc.level.dayTime() : 0;
        while (dayTime > 24000)
        {
            dayTime -= 24000;
            dayCount++;
        }

        dayTime = (dayTime + 6000) % 24000;

        String hourOfTheDay = String.format("%2s", dayTime / 1000).replace(' ', '0');
        String minuteOfTheDay = String.format("%2s", (dayTime % 1000) * 6 / 100).replace(' ', '0');

        return "Day " + dayCount + ", " + hourOfTheDay + ":" + minuteOfTheDay;
    }

    public static Vec3i getOppositeVector(Vec3i originalVector)
    {
        return new Vec3i(-originalVector.getX(), -originalVector.getY(), -originalVector.getZ());
    }
}
