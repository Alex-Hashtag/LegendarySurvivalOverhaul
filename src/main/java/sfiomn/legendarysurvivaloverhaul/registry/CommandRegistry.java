package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import sfiomn.legendarysurvivaloverhaul.common.commands.*;

public class CommandRegistry
{

    public static final CommandBase TEMPERATURE = new TemperatureCommand();
    public static final CommandBase BODY_DAMAGE = new BodyDamageCommand();
    public static final CommandBase HEALTH_COMMAND = new HealthCommand();
    public static final CommandBase THIRST = new ThirstCommand();
    public static final CommandBase WETNESS = new WetnessCommand();
    public static final CommandBase FERN_TEST = new FernTestCommand();

    public static void registerCommandsEvent(RegisterCommandsEvent event)
    {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(TEMPERATURE.getBuilder());
        dispatcher.register(BODY_DAMAGE.getBuilder());
        dispatcher.register(HEALTH_COMMAND.getBuilder());
        dispatcher.register(THIRST.getBuilder());
        dispatcher.register(WETNESS.getBuilder());
        dispatcher.register(FERN_TEST.getBuilder());
    }
}
