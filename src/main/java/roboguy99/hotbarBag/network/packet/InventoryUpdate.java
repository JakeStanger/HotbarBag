package roboguy99.hotbarBag.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import roboguy99.hotbarBag.HotbarBag;
import roboguy99.hotbarBag.inventory.BagInventory;
import roboguy99.hotbarBag.inventory.InventoryHelper;

/**
 * Packet for updating the inventory
 * 
 * @author Roboguy99
 *		
 */
public class InventoryUpdate implements IMessage
{
	private int sectorMouseIsIn;
	
	public InventoryUpdate()
	{
	};
	
	public InventoryUpdate(int sectorMouseIsIn, BagInventory bagInventory)
	{
		this.sectorMouseIsIn = sectorMouseIsIn;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.sectorMouseIsIn = ByteBufUtils.readVarInt(buf, 1);
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, this.sectorMouseIsIn, 1);
	}
	
	public static class Handle implements IMessageHandler<InventoryUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(InventoryUpdate message, MessageContext ctx)
		{
			InventoryHelper.updateInventory(new BagInventory(ctx.getServerHandler().playerEntity.getHeldItem()), ctx.getServerHandler().playerEntity, message.sectorMouseIsIn);
			HotbarBag.logger.info("Inventories successfully updated");
			return null;
		}
	}
}
