package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureFuelItem;
import sfiomn.legendarysurvivaloverhaul.config.listeners.TemperatureFuelItemListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureFuelItemsPacket
{
	private final Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems;
	private int size;

	public SyncTemperatureFuelItemsPacket(Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems)
	{
		this.temperatureFuelItems = Map.copyOf(temperatureFuelItems);
		this.size = temperatureFuelItems.size();
	}

	public static void encode(SyncTemperatureFuelItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureFuelItem> e : message.temperatureFuelItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureFuelItem.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureFuelItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureFuelItem.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureFuelItems.put(key, t));
			}
		}

		return new SyncTemperatureFuelItemsPacket(temperatureFuelItems);
	}
	
	public static void handle(SyncTemperatureFuelItemsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureFuelItems(message.temperatureFuelItems)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureFuelItems(Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				TemperatureFuelItemListener.acceptServerTemperatureFuelItems(temperatureFuelItems);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureFuelItemsPacket(temperatureFuelItems));
	}
}
