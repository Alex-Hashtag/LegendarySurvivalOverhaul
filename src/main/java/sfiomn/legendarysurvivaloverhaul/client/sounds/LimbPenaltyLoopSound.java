package sfiomn.legendarysurvivaloverhaul.client.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbPenalty;
import sfiomn.legendarysurvivaloverhaul.client.events.ClientLimbPenaltyHooks;

/**
 * Sound heard by the local player for as long as a limb penalty is active (heartbeat, ear ringing). <br>
 * It follows the player and stops by itself once the penalty is lost.
 */
public class LimbPenaltyLoopSound extends AbstractTickableSoundInstance
{
    private final LimbPenalty penalty;
    private final float baseVolume;

    public LimbPenaltyLoopSound(SoundEvent soundEvent, LimbPenalty penalty, float volume)
    {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.penalty = penalty;
        this.baseVolume = volume;
        this.volume = ClientLimbPenaltyHooks.compensateHearing(volume);
        this.looping = true;
        this.delay = 0;
        this.relative = true;
        this.attenuation = Attenuation.NONE;
    }

    @Override
    public void tick()
    {
        if (!ClientLimbPenaltyHooks.has(this.penalty))
            this.stop();
        else
            this.volume = ClientLimbPenaltyHooks.compensateHearing(this.baseVolume);
    }

    @Override
    public boolean canStartSilent()
    {
        return true;
    }
}
