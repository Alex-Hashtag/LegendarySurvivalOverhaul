package sfiomn.legendarysurvivaloverhaul.common.integration.medsandherbs;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import static sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry.PAINKILLER;
import static sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry.PAINKILLER_ADDICTION;

public class MedsAndHerbsUtil
{

    public static boolean triggerMorphineBehavior(Player player)
    {
        if (player.hasEffect(PAINKILLER_ADDICTION))
        {
            player.displayClientMessage(Component.translatable("message.legendarysurvivaloverhaul.morphine_use_under_painkiller_addiction"), true);

            return false;
        }

        player.addEffect(new MobEffectInstance(PAINKILLER, Config.Baked.morphinePainkillerTickDuration, 0, false, false, true));
        if (Config.Baked.morphineSyringeApplyPainkillerAddiction)
            player.addEffect(new MobEffectInstance(PAINKILLER_ADDICTION, Config.Baked.painkillerAddictionDuration, 0, false, false, true));
        return true;
    }
}
