package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartResistanceItemListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncBodyPartResistanceItemsPacket
{
	private final Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems;
	private final int size;

	public SyncBodyPartResistanceItemsPacket(Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems)
	{
		this.bodyPartResistanceItems = Map.copyOf(bodyPartResistanceItems);
		this.size = bodyPartResistanceItems.size();
	}

	public static void encode(SyncBodyPartResistanceItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonBodyPartResistance> e : message.bodyPartResistanceItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonBodyPartResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyPartResistanceItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonBodyPartResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> bodyPartResistanceItems.put(key, t));
			}
		}

		return new SyncBodyPartResistanceItemsPacket(bodyPartResistanceItems);
	}
	
	public static void handle(SyncBodyPartResistanceItemsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncBodyPartResistanceItems(message.bodyPartResistanceItems)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncBodyPartResistanceItems(Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				BodyPartResistanceItemListener.acceptServerBodyPartResistanceItems(bodyPartResistanceItems);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncBodyPartResistanceItemsPacket(bodyPartResistanceItems));
	}
}
