package roboguy99.hotbarBag.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import roboguy99.hotbarBag.inventory.BagInventory;
import roboguy99.hotbarBag.inventory.InventoryHelper;
import roboguy99.hotbarBag.item.ItemBag;

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
	
	public static class InventoryHandle implements IMessageHandler<InventoryUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(InventoryUpdate message, MessageContext ctx)
		{
			ItemStack heldItem = ctx.getServerHandler().playerEntity.getHeldItem();
			if(heldItem != null && heldItem.getItem() instanceof ItemBag)
			{
				InventoryHelper.updateInventory(new BagInventory(heldItem), ctx.getServerHandler().playerEntity, message.sectorMouseIsIn);
			}
			return null;
		}
	}
}
