package sfiomn.legendarysurvivaloverhaul.common.temperature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureDimension;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.integration.terrafirmacraft.TerraFirmaCraftUtil;

public class DimensionModifier extends ModifierBase
{
    public DimensionModifier()
    {
        super();
    }

    @Override
    public float getWorldInfluence(@Nullable Player player, Level level, BlockPos pos)
    {
        if (TerraFirmaCraftUtil.shouldUseTerraFirmaCraftTemp())
            return 0.0f;

        JsonTemperatureDimension dimensionTemperature = TemperatureDataManager.getDimension(level.dimension().location());
        if (dimensionTemperature == null)
            return TemperatureEnum.NORMAL.getValue();

        return dimensionTemperature.temperature;
    }
}
