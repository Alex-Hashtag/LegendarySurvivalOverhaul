package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureConsumablesPacket
{
	private final Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables;
	private final int size;

	public SyncTemperatureConsumablesPacket(Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables)
	{
		this.temperatureConsumables = Map.copyOf(temperatureConsumables);
		this.size = temperatureConsumables.size();
	}

	public static void encode(SyncTemperatureConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonTemperatureConsumable>> e : message.temperatureConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonTemperatureConsumable.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncTemperatureConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtcSize = buffer.readInt();
			List<JsonTemperatureConsumable> jtcList = new ArrayList<>();
			for (int j = 0; j < jtcSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonTemperatureConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtcList::add);
				}
			}
			temperatureConsumables.put(key, jtcList);
		}

		return new SyncTemperatureConsumablesPacket(temperatureConsumables);
	}
	
	public static void handle(SyncTemperatureConsumablesPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureConsumables(message.temperatureConsumables)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureConsumables(Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				TemperatureConsumableListener.acceptServerTemperatureConsumables(temperatureConsumables);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureConsumablesPacket(temperatureConsumables));
	}
}
