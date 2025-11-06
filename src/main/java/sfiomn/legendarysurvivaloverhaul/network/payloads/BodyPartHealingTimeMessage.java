package sfiomn.legendarysurvivaloverhaul.network.payloads;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries.SupplementariesUtil;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

public record BodyPartHealingTimeMessage(
        CompoundTag compound
) implements CustomPacketPayload
{

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "body_part_healing_time");
    public static final Type<BodyPartHealingTimeMessage> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, BodyPartHealingTimeMessage> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.COMPOUND_TAG,
                    BodyPartHealingTimeMessage::compound,
                    BodyPartHealingTimeMessage::new
            );

    public BodyPartHealingTimeMessage(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem, boolean applyEffect)
    {
        this(createPayload(bodyPart, hand, consumeItem, applyEffect));
    }

    private static CompoundTag createPayload(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem, boolean applyEffect)
    {
        CompoundTag bodyPartHealNbt = new CompoundTag();
        bodyPartHealNbt.putString("bodyPartEnum", bodyPart.name());
        bodyPartHealNbt.putBoolean("mainHand", hand == InteractionHand.MAIN_HAND);
        bodyPartHealNbt.putBoolean("consumeItem", consumeItem);
        bodyPartHealNbt.putBoolean("applyEffect", applyEffect);
        return bodyPartHealNbt;
    }

    public static void handle(BodyPartHealingTimeMessage pkt, IPayloadContext ctx)
    {
        ctx.enqueueWork(() -> {
            if (ctx.player() instanceof ServerPlayer serverPlayer)
            {
                applyHealingItemOnServer(serverPlayer, pkt.compound());
            }
        });
    }

    public static void applyHealingItemOnServer(ServerPlayer player, CompoundTag nbt)
    {
        BodyPartEnum bodyPartEnum = BodyPartEnum.valueOf(nbt.getString("bodyPartEnum"));
        InteractionHand hand = nbt.getBoolean("mainHand") ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        boolean shouldConsume = nbt.getBoolean("consumeItem");
        boolean shouldApplyEffect = nbt.getBoolean("applyEffect");

        ItemStack usedItemStack = player.getItemInHand(hand);
        if (LegendarySurvivalOverhaul.supplementariesLoaded)
        {
            ItemStack itemStackInBasket = SupplementariesUtil.getSelectedItemInLunchBasket(player.getItemInHand(hand));
            if (itemStackInBasket != ItemStack.EMPTY)
                usedItemStack = itemStackInBasket;
        }

        ResourceLocation itemStackRegistryName = BuiltInRegistries.ITEM.getKey(usedItemStack.getItem());
        JsonHealingConsumable jhc = BodyDamageDataManager.getHealingItem(itemStackRegistryName);

        player.serverLevel().playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (shouldConsume && !player.isCreative())
            usedItemStack.shrink(1);

        if (jhc != null)
        {
            if (shouldApplyEffect)
                player.addEffect(new MobEffectInstance(MobEffectRegistry.RECOVERY, jhc.recoveryEffectDuration, jhc.recoveryEffectAmplifier, false, false, true));
            BodyDamageUtil.applyHealingTimeBodyPart(player, bodyPartEnum, jhc.healingValue, jhc.healingTime);
        }
    }

    public static void sendToServer(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem, boolean applyEffect)
    {
        PacketDistributor.sendToServer(new BodyPartHealingTimeMessage(bodyPart, hand, consumeItem, applyEffect));
    }

    @Override
    public Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
