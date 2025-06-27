package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartsDamageSourceListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncBodyPartsDamageSourcesPacket
{
	private final Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources;
	private final int size;

	public SyncBodyPartsDamageSourcesPacket(Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources)
	{
		this.damageSources = Map.copyOf(damageSources);
		this.size = damageSources.size();
	}

	public static void encode(SyncBodyPartsDamageSourcesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonBodyPartsDamageSource> e : message.damageSources.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonBodyPartsDamageSource.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyPartsDamageSourcesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonBodyPartsDamageSource.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> damageSources.put(key, t));
			}
		}

		return new SyncBodyPartsDamageSourcesPacket(damageSources);
	}
	
	public static void handle(SyncBodyPartsDamageSourcesPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureItems(message.damageSources)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureItems(Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				BodyPartsDamageSourceListener.acceptServerDamageSources(damageSources);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncBodyPartsDamageSourcesPacket(damageSources));
	}
}
