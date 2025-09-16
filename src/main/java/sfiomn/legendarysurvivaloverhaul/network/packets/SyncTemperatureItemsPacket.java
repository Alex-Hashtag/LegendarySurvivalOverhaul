package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.fml.DistExecutor;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureItemListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureItemsPacket
{
	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureItems;
	private final int size;

	public SyncTemperatureItemsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureItems)
	{
		this.temperatureItems = Map.copyOf(temperatureItems);
		this.size = temperatureItems.size();
	}

	public static void encode(SyncTemperatureItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureItems.put(key, t));
			}
		}

		return new SyncTemperatureItemsPacket(temperatureItems);
	}
	
	public static void handle(SyncTemperatureItemsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureItems(message.temperatureItems)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureItems(Map<ResourceLocation, JsonTemperatureResistance> temperatureItems)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				TemperatureItemListener.acceptServerTemperatureItems(temperatureItems);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonTemperatureResistance> temperatureItems) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureItemsPacket(temperatureItems));
	}
}
