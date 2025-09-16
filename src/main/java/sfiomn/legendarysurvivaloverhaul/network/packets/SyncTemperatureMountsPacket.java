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
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureMountListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureMountsPacket
{
	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts;
	private final int size;

	public SyncTemperatureMountsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts)
	{
		this.temperatureMounts = Map.copyOf(temperatureMounts);
		this.size = temperatureMounts.size();
	}

	public static void encode(SyncTemperatureMountsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureMounts.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureMountsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureMounts.put(key, t));
			}
		}

		return new SyncTemperatureMountsPacket(temperatureMounts);
	}
	
	public static void handle(SyncTemperatureMountsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureMounts(message.temperatureMounts)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureMounts(Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				TemperatureMountListener.acceptServerTemperatureMounts(temperatureMounts);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncTemperatureMountsPacket(temperatureMounts));
	}
}
