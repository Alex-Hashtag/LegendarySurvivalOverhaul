package sfiomn.legendarysurvivaloverhaul.common.attachments;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;

import java.util.function.Supplier;

public final class ModAttachments
{
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, LegendarySurvivalOverhaul.MOD_ID);
    public static final Supplier<AttachmentType<TemperatureCapability>> TEMPERATURE = ATTACHMENTS.register(
            "temperature",
            () -> AttachmentType.serializable(TemperatureCapability::new)
                    .copyOnDeath()
                    .build()
    );
    public static final Supplier<AttachmentType<WetnessCapability>> WETNESS = ATTACHMENTS.register(
            "wetness",
            () -> AttachmentType.serializable(WetnessCapability::new)
                    .copyOnDeath()
                    .build()
    );
    public static final Supplier<AttachmentType<ThirstCapability>> THIRST = ATTACHMENTS.register(
            "thirst",
            () -> AttachmentType.serializable(ThirstCapability::new)
                    .copyOnDeath()
                    .build()
    );
    public static final Supplier<AttachmentType<HealthCapability>> HEALTH = ATTACHMENTS.register(
            "health",
            () -> AttachmentType.serializable(HealthCapability::new)
                    .copyOnDeath()
                    .build()
    );
    // Food is runtime-only (no persistence)
    public static final Supplier<AttachmentType<FoodCapability>> FOOD = ATTACHMENTS.register(
            "food",
            () -> AttachmentType.builder(FoodCapability::new).build()
    );
    public static final Supplier<AttachmentType<BodyDamageCapability>> BODY_DAMAGE = ATTACHMENTS.register(
            "body_damage",
            () -> AttachmentType.serializable(BodyDamageCapability::new)
                    .copyOnDeath()
                    .build()
    );

    private ModAttachments()
    {
    }

    // Note: ItemStack attachments are not supported in 1.21+
    // Use DataComponentRegistry.TEMPERATURE_DATA instead

    public static void init(IEventBus modBus)
    {
        ATTACHMENTS.register(modBus);
    }
}
