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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstConsumableListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncThirstConsumablesPacket
{
	private final Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables;
	private int size;

	public SyncThirstConsumablesPacket(Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables)
	{
		this.thirstConsumables = Map.copyOf(thirstConsumables);
		this.size = thirstConsumables.size();
	}

	public static void encode(SyncThirstConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonThirstConsumable>> e : message.thirstConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonThirstConsumable.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncThirstConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtcSize = buffer.readInt();
			List<JsonThirstConsumable> jtcList = new ArrayList<>();
			for (int j = 0; j < jtcSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonThirstConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtcList::add);
				}
			}
			thirstConsumables.put(key, jtcList);
		}

		return new SyncThirstConsumablesPacket(thirstConsumables);
	}
	
	public static void handle(SyncThirstConsumablesPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncThirstConsumables(message.thirstConsumables)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncThirstConsumables(Map<ResourceLocation, List<JsonThirstConsumable>> thirstBlocks)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				ThirstConsumableListener.acceptServerThirstConsumables(thirstBlocks);
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables) {
		NetworkHandler.INSTANCE.send(packetDistributor, new SyncThirstConsumablesPacket(thirstConsumables));
	}
}
