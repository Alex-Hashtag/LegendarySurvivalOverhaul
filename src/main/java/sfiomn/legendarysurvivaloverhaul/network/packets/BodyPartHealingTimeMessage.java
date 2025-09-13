package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries.SupplementariesUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

import java.util.function.Supplier;

public class BodyPartHealingTimeMessage
{
    private CompoundTag compound;
    // CLIENT to SERVER side message

    public BodyPartHealingTimeMessage(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem, boolean applyEffect)
    {
        CompoundTag bodyPartHealNbt = new CompoundTag();
        bodyPartHealNbt.putString("bodyPartEnum", bodyPart.name());
        bodyPartHealNbt.putBoolean("mainHand", hand == InteractionHand.MAIN_HAND);
        bodyPartHealNbt.putBoolean("consumeItem", consumeItem);
        bodyPartHealNbt.putBoolean("applyEffect", applyEffect);
        this.compound = bodyPartHealNbt;
    }

    public BodyPartHealingTimeMessage(Tag nbt) {
        this.compound = (CompoundTag) nbt;
    }

    public BodyPartHealingTimeMessage() {}

    public static void encode(BodyPartHealingTimeMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.compound);
    }

    public static BodyPartHealingTimeMessage decode(FriendlyByteBuf buffer)
    {
        return new BodyPartHealingTimeMessage(buffer.readNbt());
    }

    public static void handle(BodyPartHealingTimeMessage message, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context context = supplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
            ServerPlayer player = context.getSender();
            if (player != null) {
                context.enqueueWork(() -> applyHealingItemOnServer(player, message.compound));
            }
        }
        supplier.get().setPacketHandled(true);
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

        ResourceLocation itemStackRegistryName = ForgeRegistries.ITEMS.getKey(usedItemStack.getItem());
        JsonHealingConsumable jhc = BodyDamageDataManager.getHealingItem(itemStackRegistryName);

        player.serverLevel().playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (shouldConsume && !player.isCreative())
            usedItemStack.shrink(1);

        if (jhc != null) {
            if (shouldApplyEffect)
                player.addEffect(new MobEffectInstance(MobEffectRegistry.RECOVERY.get(), jhc.recoveryEffectDuration, jhc.recoveryEffectAmplifier, false, false, true));
            BodyDamageUtil.applyHealingTimeBodyPart(player, bodyPartEnum, jhc.healingValue, jhc.healingTime);
        }
    }

    public static void sendToServer(BodyPartEnum bodyPart, InteractionHand hand, boolean consumeItem, boolean applyEffect) {
        BodyPartHealingTimeMessage bodyPartHealingTimeMessageToServer = new BodyPartHealingTimeMessage(bodyPart, hand, consumeItem, applyEffect);
        NetworkHandler.INSTANCE.sendToServer(bodyPartHealingTimeMessageToServer);
    }
}
