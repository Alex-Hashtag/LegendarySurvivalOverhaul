package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.fml.DistExecutor;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.function.Supplier;

public class UpdateHeartsPacket
{
	private CompoundTag compound;
	
	public UpdateHeartsPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}
	
	public UpdateHeartsPacket() {}

	public static void encode(UpdateHeartsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static UpdateHeartsPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateHeartsPacket(buffer.readNbt());
	}
	
	public static void handle(UpdateHeartsPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncHearts(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncHearts(CompoundTag compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				LocalPlayer player = Minecraft.getInstance().player;

				if (player != null) {
					HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

					healthCapability.readNBT(compound);
				}
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Tag compound) {
		NetworkHandler.INSTANCE.send(packetDistributor, new UpdateHeartsPacket(compound));
	}
}
