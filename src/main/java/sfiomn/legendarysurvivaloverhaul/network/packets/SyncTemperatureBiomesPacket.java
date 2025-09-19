package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBiomeOverride;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBiomeListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureBiomesPacket
{
	private final Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes;
	private final int size;

	public SyncTemperatureBiomesPacket(Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes)
	{
		this.temperatureBiomes = Map.copyOf(temperatureBiomes);
		this.size = temperatureBiomes.size();
	}

	public static void encode(SyncTemperatureBiomesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureBiomeOverride> e : message.temperatureBiomes.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureBiomeOverride.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureBiomesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureBiomeOverride.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureBiomes.put(key, t));
			}
		}

		return new SyncTemperatureBiomesPacket(temperatureBiomes);
	}
	
	public static void handle(SyncTemperatureBiomesPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureBiomes(message.temperatureBiomes)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureBiomes(Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				TemperatureBiomeListener.acceptServerTemperatureBiomes(temperatureBiomes);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureBiomesPacket(temperatureBiomes));
	}
}
