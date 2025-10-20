package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import sfiomn.legendarysurvivaloverhaul.common.commands.BodyDamageCommand;
import sfiomn.legendarysurvivaloverhaul.common.commands.CommandBase;
import sfiomn.legendarysurvivaloverhaul.common.commands.HealthCommand;
import sfiomn.legendarysurvivaloverhaul.common.commands.ModDataCommand;
import sfiomn.legendarysurvivaloverhaul.common.commands.TemperatureCommand;
import sfiomn.legendarysurvivaloverhaul.common.commands.ThirstCommand;
import sfiomn.legendarysurvivaloverhaul.common.commands.WetnessCommand;

public class CommandRegistry {

	public static final CommandBase TEMPERATURE = new TemperatureCommand();
	public static final CommandBase BODY_DAMAGE = new BodyDamageCommand();
	public static final CommandBase HEALTH_COMMAND = new HealthCommand();
	public static final CommandBase THIRST = new ThirstCommand();
	public static final CommandBase WETNESS = new WetnessCommand();
	public static final CommandBase MOD_DATA = new ModDataCommand();

	public static void registerCommandsEvent(RegisterCommandsEvent event)
	{
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

		dispatcher.register(TEMPERATURE.getBuilder());
		dispatcher.register(BODY_DAMAGE.getBuilder());
		dispatcher.register(HEALTH_COMMAND.getBuilder());
		dispatcher.register(THIRST.getBuilder());
		dispatcher.register(WETNESS.getBuilder());
		dispatcher.register(MOD_DATA.getBuilder());
	}
}
