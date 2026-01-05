package sfiomn.legendarysurvivaloverhaul.client.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.RandomUtils;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public class TemperatureBreathSound
{
    private static int delay;
    private static boolean reset;

    static
    {
        delay = 200 + RandomUtils.nextInt(0, 200);
    }

    public static void tickPlay(Player player)
    {
        if (player == null || !player.isAlive() ||
                player.isSpectator() || player.isCreative() ||
                !player.level().getBlockState(BlockPos.containing(player.getEyePosition())).isAir())
        {
            return;
        }

        TemperatureEnum temperatureEnum = AttachmentUtil.getTempAttachment(player).getTemperatureEnum();

        boolean isFrostbite = temperatureEnum == TemperatureEnum.FROSTBITE;
        boolean isHeatStroke = temperatureEnum == TemperatureEnum.HEAT_STROKE;
        boolean hasColdImmunity = player.hasEffect(MobEffectRegistry.COLD_IMMUNITY);
        boolean hasHeatImmunity = player.hasEffect(MobEffectRegistry.HEAT_IMMUNITY);

        if ((isFrostbite && hasColdImmunity) || (isHeatStroke && hasHeatImmunity) || (!isFrostbite && !isHeatStroke))
        {
            reset(player);
            return;
        }

        reset = false;

        if (delay-- <= 0)
        {
            delay = 200 + player.getRandom().nextInt(200);
            if (isFrostbite)
            {
                Minecraft.getInstance().getSoundManager().play(new DynamicPositionSound(SoundRegistry.SHIVERING.get(), player));
            } else
                Minecraft.getInstance().getSoundManager().play(new DynamicPositionSound(SoundRegistry.PANTING.get(), player));
        }
    }

    public static void reset(Player player)
    {
        if (!reset)
        {
            delay = 200 + player.getRandom().nextInt(200);
            reset = true;
        }
    }
}
