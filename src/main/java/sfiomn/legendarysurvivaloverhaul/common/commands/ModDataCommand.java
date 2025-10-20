package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

public class ModDataCommand extends CommandBase
{
	public ModDataCommand()
	{
		super(Commands.literal("lsodata")
				.requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
				.executes(src -> new ModDataCommand().get(src.getSource()))
		);
	}

	@Override
	public int get(CommandSourceStack source)
	{
		try
		{
			if (source.getEntity() instanceof Player)
			{
				Player player = (Player) source.getEntity();
				StringBuilder output = new StringBuilder();
				
				output.append("╔════════════════════════════════════════╗\n");
				output.append("║  LEGENDARY SURVIVAL OVERHAUL - DEBUG  ║\n");
				output.append("╠════════════════════════════════════════╣\n");
				output.append("║ ℹ️  SYSTEM INFO                        ║\n");
				output.append("║   Side:        ").append(String.format("%-23s", player.level().isClientSide() ? "CLIENT" : "SERVER")).append("║\n");
				output.append("║   Player:      ").append(String.format("%-23s", player.getName().getString())).append("║\n");
				output.append("╠════════════════════════════════════════╣\n");
				
				// Temperature Data
				if (Config.Baked.temperatureEnabled) {
					TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);
					float bodyTemp = MathUtil.round(tempCap.getTemperatureLevel(), 2);
					float targetTemp = MathUtil.round(TemperatureUtil.getPlayerTargetTemperature(player), 2);
					float worldTemp = MathUtil.round(TemperatureUtil.getWorldTemperature(player.level(), player.blockPosition()), 2);
					TemperatureEnum tempEnum = tempCap.getTemperatureEnum();
					
					output.append("║ 🌡️  TEMPERATURE                        ║\n");
					output.append("║   Body Temp:   ").append(String.format("%-23s", bodyTemp + "°C (" + tempEnum.name() + ")")).append("║\n");
					output.append("║   Target Temp: ").append(String.format("%-23s", targetTemp + "°C")).append("║\n");
					output.append("║   World Temp:  ").append(String.format("%-23s", worldTemp + "°C")).append("║\n");
					output.append("╠════════════════════════════════════════╣\n");
				}
				
				// Thirst Data
				if (Config.Baked.thirstEnabled) {
					ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);
					int hydration = thirstCap.getHydrationLevel();
					float saturation = MathUtil.round(thirstCap.getSaturationLevel(), 2);
					float exhaustion = MathUtil.round(thirstCap.getThirstExhaustion(), 2);
					
					output.append("║ 💧 THIRST                              ║\n");
					output.append("║   Hydration:   ").append(String.format("%-23s", hydration + "/20")).append("║\n");
					output.append("║   Saturation:  ").append(String.format("%-23s", saturation)).append("║\n");
					output.append("║   Exhaustion:  ").append(String.format("%-23s", exhaustion + "/4")).append("║\n");
					output.append("╠════════════════════════════════════════╣\n");
				}
				
				// Wetness Data
				if (Config.Baked.wetnessEnabled) {
					WetnessCapability wetnessCap = CapabilityUtil.getWetnessCapability(player);
					int wetness = wetnessCap.getWetness();
					int ticksWet = wetnessCap.getWetnessTickTimer();
					
					output.append("║ 🌊 WETNESS                             ║\n");
					output.append("║   Wetness:     ").append(String.format("%-23s", wetness + "/" + WetnessCapability.WETNESS_LIMIT)).append("║\n");
					output.append("║   Ticks Wet:   ").append(String.format("%-23s", ticksWet)).append("║\n");
					output.append("╠════════════════════════════════════════╣\n");
				}
				
				// Health Data
				if (Config.Baked.healthOverhaulEnabled) {
					HealthCapability healthCap = CapabilityUtil.getHealthCapability(player);
					float currentHealth = MathUtil.round(player.getHealth(), 2);
					float maxHealth = MathUtil.round(player.getMaxHealth(), 2);
					float additionalHealth = MathUtil.round(healthCap.getAdditionalHealth(), 2);
					float shieldHealth = MathUtil.round(healthCap.getShieldHealth(), 2);
					
					output.append("║ ❤️  HEALTH                             ║\n");
					output.append("║   Current:     ").append(String.format("%-23s", currentHealth + "/" + maxHealth)).append("║\n");
					output.append("║   Additional:  ").append(String.format("%-23s", additionalHealth)).append("║\n");
					output.append("║   Shield:      ").append(String.format("%-23s", shieldHealth)).append("║\n");
					output.append("╠════════════════════════════════════════╣\n");
				}
				
				// Body Damage Data
				if (Config.Baked.localizedBodyDamageEnabled) {
					BodyDamageCapability bodyCap = CapabilityUtil.getBodyDamageCapability(player);
					
					output.append("║ 🩹 BODY DAMAGE                         ║\n");
					for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
						float damage = MathUtil.round(bodyCap.getBodyPartDamage(bodyPart), 2);
						float healthRatio = bodyCap.getBodyPartHealthRatio(bodyPart);
						float healthPercent = MathUtil.round(healthRatio * 100, 0);
						String partName = bodyPart.name().replace("_", " ");
						output.append("║   ").append(String.format("%-11s", partName)).append(": ");
						output.append(String.format("%-20s", healthPercent + "% (" + damage + " dmg)")).append("║\n");
					}
					output.append("╠════════════════════════════════════════╣\n");
				}
				
				// Game Mode
				output.append("║ ℹ️  GAME INFO                          ║\n");
				output.append("║   Game Mode:   ").append(String.format("%-23s", player.isCreative() ? "CREATIVE" : player.isSpectator() ? "SPECTATOR" : "SURVIVAL")).append("║\n");
				output.append("╚════════════════════════════════════════╝");

				String finalOutput = output.toString();
				
				// Log to console/log file for easy copy-pasting
				LegendarySurvivalOverhaul.LOGGER.info("LSO Debug Data for player {}:\n{}", player.getName().getString(), finalOutput);
				
				// Send to player
				source.sendSuccess(() -> Component.literal(finalOutput), false);
			}
		}
		catch(Exception e) 
		{
			LegendarySurvivalOverhaul.LOGGER.error("Error executing lsodata command: " + e.getMessage());
			e.printStackTrace();
		}
		return Command.SINGLE_SUCCESS;
	}
}
