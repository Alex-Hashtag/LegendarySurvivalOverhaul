package sfiomn.legendarysurvivaloverhaul.common.attachments;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage.BodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.food.FoodAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.health.HealthAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.temperature.TemperatureAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.thirst.ThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.common.attachments.wetness.WetnessAttachment;

import java.util.function.Supplier;

public final class ModAttachments
{
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, LegendarySurvivalOverhaul.MOD_ID);
    public static final Supplier<AttachmentType<TemperatureAttachment>> TEMPERATURE = ATTACHMENTS.register(
            "temperature",
            () -> AttachmentType.serializable(TemperatureAttachment::new)
                    .copyOnDeath()
                    .build()
    );
    public static final Supplier<AttachmentType<WetnessAttachment>> WETNESS = ATTACHMENTS.register(
            "wetness",
            () -> AttachmentType.serializable(WetnessAttachment::new)
                    .copyOnDeath()
                    .build()
    );
    public static final Supplier<AttachmentType<ThirstAttachment>> THIRST = ATTACHMENTS.register(
            "thirst",
            () -> AttachmentType.serializable(ThirstAttachment::new)
                    .copyOnDeath()
                    .build()
    );
    public static final Supplier<AttachmentType<HealthAttachment>> HEALTH = ATTACHMENTS.register(
            "health",
            () -> AttachmentType.serializable(HealthAttachment::new)
                    .copyOnDeath()
                    .build()
    );
    // Food is runtime-only (no persistence)
    public static final Supplier<AttachmentType<FoodAttachment>> FOOD = ATTACHMENTS.register(
            "food",
            () -> AttachmentType.builder(FoodAttachment::new).build()
    );
    public static final Supplier<AttachmentType<BodyDamageAttachment>> BODY_DAMAGE = ATTACHMENTS.register(
            "body_damage",
            () -> AttachmentType.serializable(BodyDamageAttachment::new)
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
