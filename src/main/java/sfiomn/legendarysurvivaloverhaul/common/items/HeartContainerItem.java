package sfiomn.legendarysurvivaloverhaul.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.network.packets.UpdateHeartsPacket;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HeartContainerItem extends Item
{
	public HeartContainerItem(Item.Properties properties)
	{
		super(properties);
	}

	@Override
	public int getUseDuration(@NotNull ItemStack stack) {
		return 30;
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		if (Config.Baked.healthOverhaulEnabled) {
			HealthCapability cap = CapabilityUtil.getHealthCapability(player);

			if (cap.getAdditionalHealth() > Config.Baked.maxAdditionalHealth) {
				return InteractionResultHolder.fail(player.getItemInHand(hand));
			}
		}

		player.startUsingItem(hand);
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity)
	{
		if (entity instanceof Player player)
		{
			stack = super.finishUsingItem(stack, level, player);

			if (!level.isClientSide)
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));

			if (Config.Baked.healthOverhaulEnabled) {
				HealthCapability cap = CapabilityUtil.getHealthCapability(player);

				cap.addAdditionalHealth(2);

				level.playSound(null, player, SoundRegistry.HEART_CONTAINER.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
				for (int i = 0; i < 100; i++) {
					level.addParticle(ParticleTypes.CRIMSON_SPORE, player.position().x, player.position().add(0, 1.5, 0).y, player.position().z, 0.2, 0.2, 0.2);
				}

				if (level.isClientSide) {
					Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
				}
			}

			stack.shrink(1);
		}

		return stack;
	}
	
}
