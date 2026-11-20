package sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbConditions;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.event.LimbConditionsChangedEvent;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;

import javax.annotation.Nullable;

/**
 * Attachment that handles body damage for a player.
 * <p>
 * All limb data is packed in a single int:
 * <ul>
 *     <li>bits 0-15: limb damage, 2 bits per limb (indexed by {@link BodyPartEnum#ordinal()}), from 0 to {@link #MAX_LIMB_DAMAGE}</li>
 *     <li>bits 16-23: limb shield, 1 bit per limb</li>
 * </ul>
 */
public class BodyDamageAttachment implements IBodyDamageAttachment, INBTSerializable<CompoundTag>
{
    // Time given to the player to choose the limbs to heal after using a healing item
    private static final int HEALING_SESSION_DURATION = 1200;

    // Saved data
    private int limbData;
    private int expectedBrokenHearts;

    // Unsaved data
    private int oldLimbData;
    private int oldExpectedBrokenHearts;
    private int updateTickTimer;
    private boolean manualDirty;
    private int packetTimer;

    // Limb conditions cache, derived from the limb data (see LimbConditions)
    private int limbConditions;
    private int limbConditionsLimbData = -1;
    private int limbConditionsConfigVersion = -1;
    // Last limb conditions announced by LimbConditionsChangedEvent for this player entity
    private int notifiedLimbConditions = LimbConditions.NONE;

    // Pending healing item waiting for the player to choose the limbs to heal (server side)
    private ResourceLocation healingSessionItem;
    private InteractionHand healingSessionHand;
    private int healingSessionCharges;
    private boolean healingSessionItemConsumed;
    private int healingSessionTimer;

    public BodyDamageAttachment()
    {
        this.init();
    }

    public void init()
    {
        this.limbData = 0;
        this.expectedBrokenHearts = 0;
        this.updateTickTimer = 20;
        this.manualDirty = false;
        this.clearHealingSession();
    }

    @Override
    public void setManualDirty()
    {
        this.manualDirty = true;
    }

    @Override
    public boolean isDirty()
    {
        return manualDirty || this.limbData != this.oldLimbData || this.expectedBrokenHearts != this.oldExpectedBrokenHearts;
    }

    @Override
    public void setClean()
    {
        this.manualDirty = false;
        this.oldLimbData = this.limbData;
        this.oldExpectedBrokenHearts = this.expectedBrokenHearts;
    }

    @Override
    public int getPacketTimer()
    {
        return this.packetTimer;
    }

    @Override
    public void tickUpdate(Player player, Level level, boolean isStart)
    {
        if (isStart)
        {
            this.packetTimer++;
            return;
        }

        if (this.healingSessionTimer > 0 && --this.healingSessionTimer == 0)
            this.clearHealingSession();

        if (updateTickTimer++ >= 19)
        {
            updateTickTimer = 0;
            // Refresh broken hearts each second to keep the HUD in sync (the player max health may have changed)
            updateBrokenHearts(player);
        }
    }

    @Override
    public int getLimbDamage(BodyPartEnum part)
    {
        return IBodyDamageAttachment.getLimbDamage(this.limbData, part);
    }

    @Override
    public void setLimbDamage(BodyPartEnum part, int damage)
    {
        int shift = part.ordinal() * LIMB_DAMAGE_BITS;
        int clampedDamage = Mth.clamp(damage, 0, MAX_LIMB_DAMAGE);
        this.limbData = (this.limbData & ~(LIMB_DAMAGE_MASK << shift)) | (clampedDamage << shift);
    }

    @Override
    public boolean hasLimbShield(BodyPartEnum part)
    {
        return (this.limbData & shieldBit(part)) != 0;
    }

    @Override
    public void setLimbShield(BodyPartEnum part, boolean shield)
    {
        if (shield)
            this.limbData |= shieldBit(part);
        else
            this.limbData &= ~shieldBit(part);
    }

    private static int shieldBit(BodyPartEnum part)
    {
        return 1 << (LIMB_SHIELD_OFFSET + part.ordinal());
    }

    @Override
    public boolean isLimbBroken(BodyPartEnum part)
    {
        return getLimbDamage(part) >= MAX_LIMB_DAMAGE;
    }

    @Override
    public float getLimbHealthRatio(BodyPartEnum part)
    {
        return (MAX_LIMB_DAMAGE - getLimbDamage(part)) / (float) MAX_LIMB_DAMAGE;
    }

    @Override
    public boolean hitLimb(BodyPartEnum part)
    {
        if (hasLimbShield(part))
        {
            setLimbShield(part, false);
            return true;
        }
        if (isLimbBroken(part))
            return false;

        setLimbDamage(part, getLimbDamage(part) + 1);
        return true;
    }

    @Override
    public boolean canHealLimb(BodyPartEnum part, boolean canHealBroken)
    {
        int damage = getLimbDamage(part);
        return damage > 0 && (canHealBroken || damage < MAX_LIMB_DAMAGE);
    }

    @Override
    public int healLimb(BodyPartEnum part, int amount, boolean canHealBroken)
    {
        if (amount <= 0 || !canHealLimb(part, canHealBroken))
            return 0;

        int damage = getLimbDamage(part);
        int healed = Math.min(amount, damage);
        setLimbDamage(part, damage - healed);
        return healed;
    }

    @Override
    public void updateBrokenHearts(Player player)
    {
        double expectedBrokenHearts = 0;
        if (Config.Baked.healthOverhaulEnabled && Config.Baked.brokenHeartsPerInjuredLimb > 0)
        {
            int brokenLimbs = 0;
            for (BodyPartEnum part : BodyPartEnum.values())
            {
                if (isLimbBroken(part))
                    brokenLimbs++;
            }

            double brokenHeartsPerLimb = switch (Config.Baked.brokenHeartsPerInjuredLimbMode)
            {
                case PLAYER_DYNAMIC -> Config.Baked.brokenHeartsPerInjuredLimb * HealthUtil.getPlayerStableMaxHealth(player) / 2.0;
                default -> Config.Baked.brokenHeartsPerInjuredLimb / 2.0;
            };
            expectedBrokenHearts = brokenLimbs * brokenHeartsPerLimb;
        }

        this.expectedBrokenHearts = (int) expectedBrokenHearts;
        BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
    }

    @Override
    public boolean isWoundedBelow(float healthPercent)
    {
        for (BodyPartEnum part : BodyPartEnum.values())
        {
            if (getLimbHealthRatio(part) < healthPercent)
                return true;
        }
        return false;
    }

    @Override
    public int getLimbConditions()
    {
        if (this.limbConditionsLimbData != this.limbData || this.limbConditionsConfigVersion != LimbConditions.getConfigVersion())
        {
            this.limbConditions = LimbConditions.compute(this.limbData);
            this.limbConditionsLimbData = this.limbData;
            this.limbConditionsConfigVersion = LimbConditions.getConfigVersion();
        }
        return this.limbConditions;
    }

    /**
     * Post a {@link LimbConditionsChangedEvent} if the limb conditions changed since the last call. <br>
     * Called on the server when the limb data is synced, and on the client when it is received, so only on changes.
     */
    public void notifyLimbConditionsChange(Player player)
    {
        int conditions = getLimbConditions();
        if (conditions == this.notifiedLimbConditions)
            return;

        int oldConditions = this.notifiedLimbConditions;
        this.notifiedLimbConditions = conditions;
        NeoForge.EVENT_BUS.post(new LimbConditionsChangedEvent(player, oldConditions, conditions));
    }

    @Override
    public int getExpectedBrokenHearts()
    {
        return this.expectedBrokenHearts;
    }

    public int getLimbData()
    {
        return this.limbData;
    }

    /**
     * Register a healing item waiting for the player to choose the limbs to heal.
     */
    public void startHealingSession(ResourceLocation item, InteractionHand hand, int charges, boolean itemConsumed)
    {
        this.healingSessionItem = item;
        this.healingSessionHand = hand;
        this.healingSessionCharges = charges;
        this.healingSessionItemConsumed = itemConsumed;
        this.healingSessionTimer = HEALING_SESSION_DURATION;
    }

    @Nullable
    public ResourceLocation getHealingSessionItem()
    {
        return this.healingSessionCharges > 0 ? this.healingSessionItem : null;
    }

    public InteractionHand getHealingSessionHand()
    {
        return this.healingSessionHand;
    }

    public boolean isHealingSessionItemConsumed()
    {
        return this.healingSessionItemConsumed;
    }

    public void setHealingSessionItemConsumed()
    {
        this.healingSessionItemConsumed = true;
    }

    public void useHealingSessionCharge()
    {
        if (--this.healingSessionCharges <= 0)
            clearHealingSession();
    }

    public void clearHealingSession()
    {
        this.healingSessionItem = null;
        this.healingSessionHand = null;
        this.healingSessionCharges = 0;
        this.healingSessionItemConsumed = false;
        this.healingSessionTimer = 0;
    }

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();
        compound.putInt("limbData", this.limbData);
        compound.putInt("expectedBrokenHearts", this.expectedBrokenHearts);
        return compound;
    }

    public void readNBT(CompoundTag compound)
    {
        this.limbData = 0;
        if (compound.contains("limbData"))
            this.limbData = compound.getInt("limbData");
        else
            readLegacyNBT(compound);

        this.expectedBrokenHearts = compound.getInt("expectedBrokenHearts");
    }

    /**
     * Convert the old float based limb health into limb hearts.
     */
    private void readLegacyNBT(CompoundTag compound)
    {
        for (BodyPartEnum part : BodyPartEnum.values())
        {
            float damage = compound.getFloat(part.name() + "_damage");
            float maxHealth = compound.getFloat(part.name() + "_maxHealth");
            if (damage > 0 && maxHealth > 0)
                setLimbDamage(part, Math.round(MAX_LIMB_DAMAGE * Math.min(damage / maxHealth, 1.0f)));
        }
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
