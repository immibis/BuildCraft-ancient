package net.minecraft.src.buildcraft.transport.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.core.network.PacketIds;
import net.minecraft.src.buildcraft.core.network.PacketSlotChange;
import net.minecraft.src.buildcraft.factory.TileAssemblyTable;
import net.minecraft.src.buildcraft.transport.PipeLogicDiamond;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;
import net.minecraft.src.forge.IPacketHandler;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(NetworkManager network, String channel, byte[] bytes) {
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(bytes));
		try
		{
			NetServerHandler net = (NetServerHandler)network.getNetHandler();
			int packetID = data.read();
			switch (packetID) {

			case PacketIds.DIAMOND_PIPE_SELECT:
				PacketSlotChange packet = new PacketSlotChange();
				packet.readData(data);
				onDiamondPipeSelect(net.getPlayerEntity(), packet);
				break;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	private TileGenericPipe getPipe(World world, int x, int y, int z) {
		if(!world.blockExists(x, y, z))
			return null;
		
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(!(tile instanceof TileGenericPipe))
			return null;

		return (TileGenericPipe)tile;
	}
	
	private void onDiamondPipeSelect(EntityPlayerMP player, PacketSlotChange packet) {
		TileGenericPipe pipe = getPipe(player.worldObj, packet.posX, packet.posY, packet.posZ);
		if(pipe == null)
			return;
		
		if(!(pipe.pipe.logic instanceof PipeLogicDiamond))
			return;
		
		pipe.pipe.logic.setInventorySlotContents(packet.slot, packet.stack);
	}

}
