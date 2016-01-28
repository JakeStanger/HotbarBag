package roboguy99.hotbarBag.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import roboguy99.hotbarBag.HotbarBag;
import roboguy99.hotbarBag.inventory.BagInventory;
import roboguy99.hotbarBag.item.ItemBag;

public class SettingsUpdate implements IMessage
{
	NBTTagCompound itemTag;
	NBTTagCompound constructTag;
	
	public SettingsUpdate()
	{};
	
	public SettingsUpdate(NBTTagCompound itemTag, NBTTagCompound constructTag)
	{
		this.itemTag = itemTag;
		this.constructTag = constructTag;
		
		System.out.println(itemTag);
		System.out.println(constructTag);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.itemTag = ByteBufUtils.readTag(buf);
		this.constructTag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, this.itemTag);
		ByteBufUtils.writeTag(buf, this.constructTag);
	}

	public static class SettingsUpdateHandle implements IMessageHandler<SettingsUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(SettingsUpdate message, MessageContext ctx) 
		{
			System.out.println(message.itemTag);
			System.out.println(message.constructTag);
			
			ItemStack heldItem = ctx.getServerHandler().playerEntity.getHeldItem();
			BagInventory inventory;
			if(heldItem.getItem() instanceof ItemBag)
			{
				heldItem.setStackDisplayName(HotbarBag.instance.config.getName());
				inventory = new BagInventory(heldItem);
				inventory.writeSettingsToNBT(message.itemTag, message.constructTag);
			}
			return null;
		}
	}
}
