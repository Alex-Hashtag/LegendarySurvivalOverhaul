package sfiomn.legendarysurvivaloverhaul.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;

public class DataComponentRegistry
{

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, LegendarySurvivalOverhaul.MOD_ID);

    // Temperature data component for thermometers and similar items
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TemperatureData>> TEMPERATURE_DATA =
            DATA_COMPONENTS.register("temperature_data", () -> DataComponentType.<TemperatureData>builder()
                    .persistent(TemperatureData.CODEC)
                    .networkSynchronized(TemperatureData.STREAM_CODEC)
                    .build());

    public static void init(IEventBus modBus)
    {
        DATA_COMPONENTS.register(modBus);
    }

    /**
     * Record to hold temperature data for items like thermometers
     */
    public record TemperatureData(float temperature, long updateTick)
    {

        public static final Codec<TemperatureData> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.FLOAT.fieldOf("temperature").forGetter(TemperatureData::temperature),
                        Codec.LONG.optionalFieldOf("updateTick", 0L).forGetter(TemperatureData::updateTick)
                ).apply(instance, TemperatureData::new)
        );

        public static final StreamCodec<ByteBuf, TemperatureData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT, TemperatureData::temperature,
                ByteBufCodecs.VAR_LONG, TemperatureData::updateTick,
                TemperatureData::new
        );

        public TemperatureData()
        {
            this(TemperatureEnum.NORMAL.getValue(), 0L);
        }

        public TemperatureData withTemperature(float newTemperature)
        {
            return new TemperatureData(newTemperature, this.updateTick);
        }

        public TemperatureData withUpdateTick(long newUpdateTick)
        {
            return new TemperatureData(this.temperature, newUpdateTick);
        }

        public boolean shouldUpdate(long currentTick)
        {
            return (currentTick - this.updateTick) > 10;
        }
    }
}
