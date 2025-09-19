package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;


import net.neoforged.neoforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureDimension;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureDimensionListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureDimensionsPacket
{
	private final Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions;
	private final int size;

	public SyncTemperatureDimensionsPacket(Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions)
	{
		this.temperatureDimensions = Map.copyOf(temperatureDimensions);
		this.size = temperatureDimensions.size();
	}

	public static void encode(SyncTemperatureDimensionsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureDimension> e : message.temperatureDimensions.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureDimension.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureDimensionsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureDimension.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureDimensions.put(key, t));
			}
		}

		return new SyncTemperatureDimensionsPacket(temperatureDimensions);
	}
	
	public static void handle(SyncTemperatureDimensionsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureDimensions(message.temperatureDimensions)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureDimensions(Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				TemperatureDimensionListener.acceptServerTemperatureDimensions(temperatureDimensions);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureDimensionsPacket(temperatureDimensions));
	}
}
