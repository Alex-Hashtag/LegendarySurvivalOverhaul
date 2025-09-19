package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;


import net.neoforged.fml.DistExecutor;
import net.neoforged.neoforge.network.PacketDistributor;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.function.Supplier;

public class UpdateTemperaturesPacket
{
	private CompoundTag compound;
	
	public UpdateTemperaturesPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}
	
	public UpdateTemperaturesPacket() {}

	public static void encode(UpdateTemperaturesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static UpdateTemperaturesPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateTemperaturesPacket(buffer.readNbt());
	}
	
	public static void handle(UpdateTemperaturesPacket message, Supplier<NetworkEvent.Context> supplier)
	{
		final NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> syncTemperature(message.compound)));
		
		supplier.get().setPacketHandled(true);
	}
	
	public static DistExecutor.SafeRunnable syncTemperature(CompoundTag compound)
	{
		return new DistExecutor.SafeRunnable()
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public void run()
			{
				LocalPlayer player = Minecraft.getInstance().player;

				if (player != null) {
					TemperatureCapability temperature = CapabilityUtil.getTempCapability(player);

					temperature.readNBT(compound);
				}
			}
		};
	}

	public static void sendTo(PacketDistributor.PacketTarget packetDistributor, Tag compound) {
		NetworkHandler.INSTANCE.send(packetDistributor, new UpdateTemperaturesPacket(compound));
	}
}
