package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.config.listeners.TemperatureOriginListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureOriginsPacket
{
	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins;
	private int size;

	public SyncTemperatureOriginsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins)
	{
		this.temperatureOrigins = Map.copyOf(temperatureOrigins);
		this.size = temperatureOrigins.size();
	}

	public static void encode(SyncTemperatureOriginsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureOrigins.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureOriginsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureOrigins.put(key, t));
			}
		}

		return new SyncTemperatureOriginsPacket(temperatureOrigins);
	}
	
	public static void handle(SyncTemperatureOriginsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureOrigins(message.temperatureOrigins)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureOrigins(Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				TemperatureOriginListener.acceptServerTemperatureOrigins(temperatureOrigins);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureOriginsPacket(temperatureOrigins));
	}
}
