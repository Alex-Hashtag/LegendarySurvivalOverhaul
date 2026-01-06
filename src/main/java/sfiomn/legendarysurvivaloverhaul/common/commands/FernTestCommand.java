package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.level.gen.ModConfiguredFeatures;

public class FernTestCommand extends CommandBase
{
    public FernTestCommand()
    {
        super(Commands.literal("lso")
                .requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
                .then(Commands.literal("fernpatch")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1, 256))
                                        .then(Commands.argument("attempts", IntegerArgumentType.integer(1, 10000))
                                                .executes(ctx -> new FernTestCommand().spawnFernPatches(
                                                        ctx.getSource(),
                                                        StringArgumentType.getString(ctx, "type"),
                                                        IntegerArgumentType.getInteger(ctx, "radius"),
                                                        IntegerArgumentType.getInteger(ctx, "attempts")
                                                )))))));
    }

    private int spawnFernPatches(CommandSourceStack source, String type, int radius, int attempts)
    {
        try
        {
            ServerLevel level = source.getLevel();
            if (source.getEntity() == null)
            {
                return Command.SINGLE_SUCCESS;
            }

            var registry = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
            Holder<ConfiguredFeature<?, ?>> feature;

            if (type.equalsIgnoreCase("ice"))
            {
                feature = registry.getOrThrow(ModConfiguredFeatures.ICE_FERN_CONFIG_KEY);
            } else if (type.equalsIgnoreCase("sun"))
            {
                feature = registry.getOrThrow(ModConfiguredFeatures.SUN_FERN_CONFIG_KEY);
            } else
            {
                source.sendFailure(Component.literal("Unknown type: " + type + " (use: ice | sun)"));
                return Command.SINGLE_SUCCESS;
            }

            RandomSource random = level.getRandom();
            BlockPos center = source.getEntity().blockPosition();

            int placed = 0;
            for (int i = 0; i < attempts; i++)
            {
                int dx = random.nextInt(radius * 2 + 1) - radius;
                int dz = random.nextInt(radius * 2 + 1) - radius;
                int x = center.getX() + dx;
                int z = center.getZ() + dz;
                int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

                BlockPos origin = new BlockPos(x, y, z);
                if (feature.value().place(level, level.getChunkSource().getGenerator(), random, origin))
                {
                    placed++;
                }
            }

            int finalPlaced = placed;
            source.sendSuccess(() -> Component.literal("Placed " + finalPlaced + " fern patches (type=" + type + ") within radius " + radius), false);
        } catch (Exception e)
        {
            LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
        }

        return Command.SINGLE_SUCCESS;
    }
}
