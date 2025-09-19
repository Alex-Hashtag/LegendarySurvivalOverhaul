package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyDamageHealingConsumableListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncBodyDamageHealingConsumablesPacket
{
	private final Map<ResourceLocation, JsonHealingConsumable> healingConsumables;
	private final int size;

	public SyncBodyDamageHealingConsumablesPacket(Map<ResourceLocation, JsonHealingConsumable> healingConsumables)
	{
		this.healingConsumables = Map.copyOf(healingConsumables);
		this.size = healingConsumables.size();
	}

	public static void encode(SyncBodyDamageHealingConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonHealingConsumable> e : message.healingConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonHealingConsumable.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyDamageHealingConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonHealingConsumable> healingConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonHealingConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> healingConsumables.put(key, t));
			}
		}

		return new SyncBodyDamageHealingConsumablesPacket(healingConsumables);
	}
	
	public static void handle(SyncBodyDamageHealingConsumablesPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperatureItems(message.healingConsumables)));
		
		supplier.get().setPacketHandled(true);
	}

	public static DistExecutor.SafeRunnable syncTemperatureItems(Map<ResourceLocation, JsonHealingConsumable> healingConsumables)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void run()
			{
				BodyDamageHealingConsumableListener.acceptServerHealingConsumables(healingConsumables);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, JsonHealingConsumable> healingConsumables) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncBodyDamageHealingConsumablesPacket(healingConsumables));
	}
}
