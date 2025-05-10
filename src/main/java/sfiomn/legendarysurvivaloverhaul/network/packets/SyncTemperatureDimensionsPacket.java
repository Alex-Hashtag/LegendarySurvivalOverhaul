package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureDimensionListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureDimensionsPacket
{
	private final Map<ResourceLocation, JsonTemperature> temperatureDimensions;
	private int size;

	public SyncTemperatureDimensionsPacket(Map<ResourceLocation, JsonTemperature> temperatureDimensions)
	{
		this.temperatureDimensions = Map.copyOf(temperatureDimensions);
		this.size = temperatureDimensions.size();
	}

	public static void encode(SyncTemperatureDimensionsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperature> e : message.temperatureDimensions.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperature.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureDimensionsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperature> temperatureDimensions = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperature.CODEC.parse(NbtOps.INSTANCE, tag);
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

	public static DistExecutor.SafeRunnable syncTemperatureDimensions(Map<ResourceLocation, JsonTemperature> temperatureDimensions)
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

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonTemperature> temperatureDimensions) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureDimensionsPacket(temperatureDimensions));
	}
}
