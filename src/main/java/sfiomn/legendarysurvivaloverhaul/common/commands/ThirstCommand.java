package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.attachments.thirst.ThirstAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

public class ThirstCommand extends CommandBase
{
    public ThirstCommand()
    {
        super(Commands.literal("thirst")
                .requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
                .then(Commands.literal("get").executes(src -> new ThirstCommand().get(src.getSource())))
                .then(Commands.literal("set")
                        .then(Commands.literal("hydration")
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 20))
                                        .executes(src -> new ThirstCommand().setHydration(src.getSource(), IntegerArgumentType.getInteger(src, "value")))))
                        .then(Commands.literal("saturation")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.0f, 20.0f))
                                        .executes(src -> new ThirstCommand().setSaturation(src.getSource(), FloatArgumentType.getFloat(src, "value")))))
                        .then(Commands.literal("exhaustion")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.0f, 4.0f))
                                        .executes(src -> new ThirstCommand().setExhaustion(src.getSource(), FloatArgumentType.getFloat(src, "value"))))))
        );
    }

    @Override
    public int get(CommandSourceStack source)
    {
        try
        {
            if (source.getEntity() instanceof Player player)
            {
                ThirstAttachment cap = AttachmentUtil.getThirstAttachment(player);

                int hydration = cap.getHydrationLevel();
                float saturation = MathUtil.round(cap.getSaturationLevel(), 2);
                float exhaustion = MathUtil.round(cap.getThirstExhaustion(), 2);

                String reply = "Hydration: " + hydration + "/20\nSaturation: " + saturation + "\nExhaustion: " + exhaustion;

                source.sendSuccess(() -> Component.literal(reply), false);
            }
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }

    private int setHydration(CommandSourceStack src, int value) throws CommandSyntaxException
    {
        ThirstAttachment cap = AttachmentUtil.getThirstAttachment(src.getPlayerOrException());
        cap.setHydrationLevel(value);
        src.sendSuccess(() -> Component.literal("Set hydration to " + value), false);
        return Command.SINGLE_SUCCESS;
    }

    private int setSaturation(CommandSourceStack src, float value) throws CommandSyntaxException
    {
        ThirstAttachment cap = AttachmentUtil.getThirstAttachment(src.getPlayerOrException());
        cap.setThirstSaturation(value);
        src.sendSuccess(() -> Component.literal("Set saturation to " + value), false);
        return Command.SINGLE_SUCCESS;
    }

    private int setExhaustion(CommandSourceStack src, float value) throws CommandSyntaxException
    {
        ThirstAttachment cap = AttachmentUtil.getThirstAttachment(src.getPlayerOrException());
        cap.setThirstExhaustion(value);
        src.sendSuccess(() -> Component.literal("Set exhaustion to " + value), false);
        return Command.SINGLE_SUCCESS;
    }
}
