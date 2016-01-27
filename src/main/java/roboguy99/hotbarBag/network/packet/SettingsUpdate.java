package roboguy99.hotbarBag.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import roboguy99.hotbarBag.HotbarBag;
import roboguy99.hotbarBag.inventory.BagInventory;
import roboguy99.hotbarBag.item.ItemBag;
import sun.security.krb5.Config;

public class SettingsUpdate implements IMessage
{
	public SettingsUpdate()
	{};
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		
	}

	public static class SettingsHandle implements IMessageHandler<SettingsUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(SettingsUpdate message, MessageContext ctx) 
		{
			ItemStack heldItem = ctx.getServerHandler().playerEntity.getHeldItem();
			BagInventory inventory;
			if(heldItem.getItem() instanceof ItemBag)
			{
				heldItem.setStackDisplayName(HotbarBag.instance.config.getName());
				inventory = new BagInventory(heldItem);
				inventory.writeSettingsToNBT(heldItem.getTagCompound());
			}
			return null;
		}
	}
}
