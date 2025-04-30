package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBlock;
import sfiomn.legendarysurvivaloverhaul.config.listeners.TemperatureBlockListener;
import sfiomn.legendarysurvivaloverhaul.config.listeners.TemperatureConsumableListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureBlocksPacket
{
	private final Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks;
	private int size;

	public SyncTemperatureBlocksPacket(Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks)
	{
		this.temperatureBlocks = Map.copyOf(temperatureBlocks);
		this.size = temperatureBlocks.size();
	}

	public static void encode(SyncTemperatureBlocksPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonTemperatureBlock>> e : message.temperatureBlocks.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureBlocksPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureBlock.LIST_CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureBlocks.put(key, t));
			}
		}

		return new SyncTemperatureBlocksPacket(temperatureBlocks);
	}
	
	public static void handle(SyncTemperatureBlocksPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureBlocks(message.temperatureBlocks)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureBlocks(Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				TemperatureBlockListener.acceptServerTemperatureBlocks(temperatureBlocks);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureBlocksPacket(temperatureBlocks));
	}
}
