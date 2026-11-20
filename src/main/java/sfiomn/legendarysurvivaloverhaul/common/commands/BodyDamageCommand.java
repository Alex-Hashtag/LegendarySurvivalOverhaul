package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.command.EnumArgument;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.LimbPenalty;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.attachments.bodydamage.BodyDamageAttachment;
import sfiomn.legendarysurvivaloverhaul.util.AttachmentUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

/**
 * /bodydamage &lt;targets&gt; get [limb] <br>
 * /bodydamage &lt;targets&gt; set &lt;limb|ALL&gt; &lt;damage&gt; <br>
 * /bodydamage &lt;targets&gt; heal &lt;limb|ALL&gt; &lt;limbHearts&gt; <br>
 * /bodydamage &lt;targets&gt; hit &lt;limb|ALL&gt; &lt;times&gt; <br>
 * /bodydamage &lt;targets&gt; shield &lt;limb|ALL&gt; &lt;true|false&gt;
 */
public class BodyDamageCommand extends CommandBase
{
    private static final String[] LIMB_STATES = {"Healthy", "Bruised", "Fractured", "Broken"};
    private static final ChatFormatting[] LIMB_STATE_COLORS = {ChatFormatting.GREEN, ChatFormatting.YELLOW, ChatFormatting.GOLD, ChatFormatting.DARK_RED};

    public BodyDamageCommand()
    {
        super(Commands.literal("bodydamage")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("target", EntityArgument.players())
                        .then(Commands.literal("get")
                                .executes(src -> get(src.getSource(), EntityArgument.getPlayers(src, "target"), Arrays.asList(BodyPartEnum.values())))
                                .then(Commands.argument("BodyPart", EnumArgument.enumArgument(BodyPartEnum.class))
                                        .executes(src -> get(src.getSource(), EntityArgument.getPlayers(src, "target"), Collections.singletonList(src.getArgument("BodyPart", BodyPartEnum.class))))))
                        .then(limbAction("set", IntegerArgumentType.integer(0, IBodyDamageAttachment.MAX_LIMB_DAMAGE), "Damage",
                                (cap, part, value) -> cap.setLimbDamage(part, value)))
                        .then(limbAction("heal", IntegerArgumentType.integer(0, IBodyDamageAttachment.MAX_LIMB_DAMAGE), "LimbHearts",
                                (cap, part, value) -> cap.healLimb(part, value, true)))
                        .then(limbAction("hit", IntegerArgumentType.integer(1, 100), "Times",
                                (cap, part, value) -> {
                                    for (int i = 0; i < value; i++)
                                        cap.hitLimb(part);
                                }))
                        .then(Commands.literal("shield")
                                .then(Commands.argument("BodyPart", EnumArgument.enumArgument(BodyPartEnum.class))
                                        .then(Commands.argument("Shield", BoolArgumentType.bool())
                                                .executes(src -> apply(src, Collections.singletonList(src.getArgument("BodyPart", BodyPartEnum.class)),
                                                        (cap, part) -> cap.setLimbShield(part, BoolArgumentType.getBool(src, "Shield"))))))
                                .then(Commands.literal("ALL")
                                        .then(Commands.argument("Shield", BoolArgumentType.bool())
                                                .executes(src -> apply(src, Arrays.asList(BodyPartEnum.values()),
                                                        (cap, part) -> cap.setLimbShield(part, BoolArgumentType.getBool(src, "Shield")))))))));
    }

    private interface LimbAction
    {
        void apply(BodyDamageAttachment cap, BodyPartEnum part, int value);
    }

    private static ArgumentBuilder<CommandSourceStack, ?> limbAction(String name, IntegerArgumentType valueType, String valueName, LimbAction action)
    {
        return Commands.literal(name)
                .then(Commands.argument("BodyPart", EnumArgument.enumArgument(BodyPartEnum.class))
                        .then(Commands.argument(valueName, valueType)
                                .executes(src -> apply(src, Collections.singletonList(src.getArgument("BodyPart", BodyPartEnum.class)),
                                        (cap, part) -> action.apply(cap, part, IntegerArgumentType.getInteger(src, valueName))))))
                .then(Commands.literal("ALL")
                        .then(Commands.argument(valueName, valueType)
                                .executes(src -> apply(src, Arrays.asList(BodyPartEnum.values()),
                                        (cap, part) -> action.apply(cap, part, IntegerArgumentType.getInteger(src, valueName))))));
    }

    private static int apply(CommandContext<CommandSourceStack> src, List<BodyPartEnum> bodyParts, BiConsumer<BodyDamageAttachment, BodyPartEnum> action) throws CommandSyntaxException
    {
        Collection<? extends Player> players = EntityArgument.getPlayers(src, "target");
        for (Player player : players)
        {
            BodyDamageAttachment cap = AttachmentUtil.getBodyDamageAttachment(player);
            for (BodyPartEnum bodyPart : bodyParts)
                action.accept(cap, bodyPart);
            cap.updateBrokenHearts(player);
        }
        return get(src.getSource(), players, bodyParts);
    }

    private static int get(CommandSourceStack source, Collection<? extends Player> players, List<BodyPartEnum> bodyParts)
    {
        for (Player player : players)
        {
            BodyDamageAttachment cap = AttachmentUtil.getBodyDamageAttachment(player);

            MutableComponent message = Component.literal("Limbs of ").withStyle(ChatFormatting.GRAY)
                    .append(player.getDisplayName().copy().withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(String.format(Locale.ROOT, " (broken hearts: %d, limb damage threshold: %.1f)",
                            HealthUtil.getEffectiveBrokenHearts(player), BodyDamageUtil.getLimbDamageThreshold(player))));

            for (BodyPartEnum bodyPart : bodyParts)
                message.append("\n").append(formatLimb(cap, bodyPart));

            StringBuilder penalties = new StringBuilder();
            for (LimbPenalty penalty : LimbPenalty.cachedValues())
            {
                if (cap.hasLimbPenalty(penalty))
                    penalties.append(penalties.isEmpty() ? "" : ", ").append(penalty.name());
            }
            message.append(Component.literal("\nPenalties: ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(penalties.isEmpty() ? "none" : penalties.toString()).withStyle(ChatFormatting.LIGHT_PURPLE));

            message.append(Component.literal(String.format(Locale.ROOT, "\nLimb data: 0x%06X, conditions: 0x%08X", cap.getLimbData(), cap.getLimbConditions())).withStyle(ChatFormatting.DARK_GRAY));

            source.sendSuccess(() -> message, false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static Component formatLimb(IBodyDamageAttachment cap, BodyPartEnum bodyPart)
    {
        int damage = cap.getLimbDamage(bodyPart);
        int remaining = IBodyDamageAttachment.MAX_LIMB_DAMAGE - damage;

        // Limb hearts first as they have a fixed width, keeping the lines aligned
        MutableComponent line = Component.literal("❤".repeat(remaining)).withStyle(ChatFormatting.RED)
                .append(Component.literal("❤".repeat(damage)).withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(cap.hasLimbShield(bodyPart) ? " ◆ " : "   ").withStyle(ChatFormatting.AQUA))
                .append(Component.literal(formatName(bodyPart)).withStyle(ChatFormatting.WHITE))
                .append(Component.literal(" " + LIMB_STATES[damage]).withStyle(LIMB_STATE_COLORS[damage]));

        if (cap.hasLimbShield(bodyPart))
            line.append(Component.literal(" +shield").withStyle(ChatFormatting.AQUA));

        return line;
    }

    private static String formatName(BodyPartEnum bodyPart)
    {
        String name = bodyPart.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
