package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import java.util.Collection;

public class HealthCommand extends CommandBase
{
    public HealthCommand()
    {
        super(Commands.literal("healthoverhaul")
                .requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.entities())
                        .then(Commands.literal("get")
                                .then(Commands.literal("shield")
                                        .executes(src -> new HealthCommand().getShield(src.getSource(), EntityArgument.getEntities(src, "target"))))
                                .then(Commands.literal("additionalHealth")
                                        .executes(src -> new HealthCommand().getHealth(src.getSource(), EntityArgument.getEntities(src, "target"))))
                                .then(Commands.literal("brokenHearts")
                                        .executes(src -> new HealthCommand().getBrokenHeart(src.getSource(), EntityArgument.getEntities(src, "target")))))
                        .then(Commands.literal("set")
                                .then(Commands.literal("shield")
                                        .then(Commands.argument("shieldValue", DoubleArgumentType.doubleArg(0, Config.Baked.maxShieldHealth))
                                                .executes(src -> new HealthCommand().setShield(src.getSource(), EntityArgument.getEntities(src, "target"), DoubleArgumentType.getDouble(src, "shieldValue")))))
                                .then(Commands.literal("additionalHealth")
                                        .then(Commands.argument("healthValue", DoubleArgumentType.doubleArg(0, Config.Baked.maxAdditionalHealth))
                                                .executes(src -> new HealthCommand().setHealth(src.getSource(), EntityArgument.getEntities(src, "target"), DoubleArgumentType.getDouble(src, "healthValue")))))
                        )
                )
        );
    }

    private int getShield(CommandSourceStack src, Collection<? extends Entity> entities)
    {
        try
        {
            StringBuilder reply = new StringBuilder();
            for (Entity entity : entities)
            {
                if (entity instanceof Player player && src.getEntity() instanceof Player)
                {
                    reply.append("Player ").append(player.getName().getString()).append(" ");
                    float shieldValue = AttachmentUtil.getHealthAttachment(player).getShieldHealth();

                    reply.append("Shield: ")
                            .append(shieldValue)
                            .append("\n");

                    src.sendSuccess(() -> Component.literal(reply.toString()), false);
                }
            }
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }

    private int getHealth(CommandSourceStack src, Collection<? extends Entity> entities)
    {
        try
        {
            StringBuilder reply = new StringBuilder();
            for (Entity entity : entities)
            {
                if (entity instanceof Player player && src.getEntity() instanceof Player)
                {
                    reply.append("Player ").append(player.getName().getString()).append(" ");
                    float healthValue = AttachmentUtil.getHealthAttachment(player).getAdditionalHealth();

                    reply.append("Additional Health: ")
                            .append(healthValue)
                            .append("\n");

                    src.sendSuccess(() -> Component.literal(reply.toString()), false);
                }
            }
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }

    private int getBrokenHeart(CommandSourceStack src, Collection<? extends Entity> entities)
    {
        try
        {
            StringBuilder reply = new StringBuilder();
            for (Entity entity : entities)
            {
                if (entity instanceof Player player && src.getEntity() instanceof Player)
                {
                    reply.append("Player ").append(player.getName().getString()).append(" ");
                    float brokenHearts = (int) player.getAttributeValue(AttributeRegistry.BROKEN_HEART);

                    reply.append("Broken Hearts: ")
                            .append(brokenHearts)
                            .append("\n");

                    src.sendSuccess(() -> Component.literal(reply.toString()), false);
                }
            }
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
        }
        return Command.SINGLE_SUCCESS;
    }

    private int setShield(CommandSourceStack src, Collection<? extends Entity> entities, double shieldValue) throws CommandSyntaxException
    {
        for (Entity entity : entities)
        {
            if (entity instanceof Player player)
            {
                AttachmentUtil.getHealthAttachment(player).setShieldHealth((float) shieldValue);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    private int setHealth(CommandSourceStack src, Collection<? extends Entity> entities, double healthValue) throws CommandSyntaxException
    {
        for (Entity entity : entities)
        {
            if (entity instanceof Player player)
            {
                AttachmentUtil.getHealthAttachment(player).setAdditionalHealth((float) healthValue);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
