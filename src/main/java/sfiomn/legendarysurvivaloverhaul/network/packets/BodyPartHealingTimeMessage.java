package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries.SupplementariesUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
 
public record BodyPartHealingTimeMessage(
        CompoundTag compound
) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "body_part_healing_time");

    public BodyPartHealingTimeMessage(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem, boolean applyEffect) {
        this(createPayload(bodyPart, hand, consumeItem, applyEffect));
    }

    private static CompoundTag createPayload(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem, boolean applyEffect) {
        CompoundTag bodyPartHealNbt = new CompoundTag();
        bodyPartHealNbt.putString("bodyPartEnum", bodyPart.name());
        bodyPartHealNbt.putBoolean("mainHand", hand == InteractionHand.MAIN_HAND);
        bodyPartHealNbt.putBoolean("consumeItem", consumeItem);
        bodyPartHealNbt.putBoolean("applyEffect", applyEffect);
        return bodyPartHealNbt;
    }

    public BodyPartHealingTimeMessage(FriendlyByteBuf buf) {
        this(buf.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buf) { buf.writeNbt(compound); }

    @Override
    public ResourceLocation id() { return ID; }

    public static void handle(BodyPartHealingTimeMessage pkt, PlayPayloadContext ctx) {
        ctx.workHandler().submitAsync(() -> ctx.player().ifPresent(p -> applyHealingItemOnServer(p, pkt.compound())));
    }

    public static void applyHealingItemOnServer(ServerPlayer player, CompoundTag nbt) {
        BodyPartEnum bodyPartEnum = BodyPartEnum.valueOf(nbt.getString("bodyPartEnum"));
        InteractionHand hand = nbt.getBoolean("mainHand") ? InteractionHand.MAIN_HAND: InteractionHand.OFF_HAND;
        boolean shouldConsume = nbt.getBoolean("consumeItem");
        boolean shouldApplyEffect = nbt.getBoolean("applyEffect");

        ItemStack usedItemStack = player.getItemInHand(hand);
        if (LegendarySurvivalOverhaul.supplementariesLoaded) {
            ItemStack itemStackInBasket = SupplementariesUtil.getSelectedItemInLunchBasket(player.getItemInHand(hand));
            if (itemStackInBasket != ItemStack.EMPTY)
                usedItemStack = itemStackInBasket;
        }

        ResourceLocation itemStackRegistryName = BuiltInRegistries.ITEM.getKey(usedItemStack.getItem());
        JsonHealingConsumable jhc = BodyDamageDataManager.getHealingItem(itemStackRegistryName);

        player.serverLevel().playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (shouldConsume && !player.isCreative())
            usedItemStack.shrink(1);

        if (jhc != null) {
            if (shouldApplyEffect)
                player.addEffect(new MobEffectInstance(MobEffectRegistry.RECOVERY.get(), jhc.recoveryEffectDuration, jhc.recoveryEffectAmplifier, false, true, true));
            BodyDamageUtil.applyHealingTimeBodyPart(player, bodyPartEnum, jhc.healingValue, jhc.healingTime);
        }
    }

    public static void sendToServer(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem, boolean applyEffect) {
        PacketDistributor.SERVER.noArg().send(new BodyPartHealingTimeMessage(bodyPart, hand, consumeItem, applyEffect));
    }
}
