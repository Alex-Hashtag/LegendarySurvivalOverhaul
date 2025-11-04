package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.attachments.wetness.WetnessAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

public class WetnessCommand extends CommandBase
{
    public WetnessCommand()
    {
        super(Commands.literal("wetness")
                .requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
                .then(Commands.literal("get").executes(src -> new WetnessCommand().get(src.getSource())))
                .then(Commands.literal("set")
                        .then(Commands.argument("value", IntegerArgumentType.integer(0, WetnessAttachment.WETNESS_LIMIT))
                                .executes(src -> new WetnessCommand().setWetness(src.getSource(), IntegerArgumentType.getInteger(src, "value")))))
        );
    }

    @Override
    public int get(CommandSourceStack source)
    {
        try
        {
            if (source.getEntity() instanceof Player player)
            {
                WetnessAttachment cap = AttachmentUtil.getWetnessAttachment(player);

                int wetness = cap.getWetness();
                int ticksWet = cap.getWetnessTickTimer();

                String reply = "Wetness: " + wetness + "/" + WetnessAttachment.WETNESS_LIMIT + "\nTicks Wet: " + ticksWet;

                source.sendSuccess(() -> Component.literal(reply), false);
            }
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }

    private int setWetness(CommandSourceStack src, int value) throws CommandSyntaxException
    {
        WetnessAttachment cap = AttachmentUtil.getWetnessAttachment(src.getPlayerOrException());
        cap.setWetness(value);
        src.sendSuccess(() -> Component.literal("Set wetness to " + value), false);
        return Command.SINGLE_SUCCESS;
    }
}
