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
	private int backgroundRed;
	private int backgroundGreen;
	private int backgroundBlue;
	private int backgroundAlpha;
	
	private int highlightRed;
	private int highlightGreen;
	private int highlightBlue;
	private int highlightAlpha;
	
	private int borderRed;
	private int borderGreen;
	private int borderBlue;
	private int borderAlpha;
	
	private int mouseposRed;
	private int mouseposGreen;
	private int mouseposBlue;
	private int mouseposAlpha;
	
	private int triangles;
	private int radius;
	private int itemRadius;
	
	private boolean muted;
	private boolean itemRadiusAuto;
	private String name;
	
	public SettingsUpdate()
	{};
	
	public SettingsUpdate(Object[][] settingsList)
	{
		this.backgroundRed = (Integer) settingsList[0][0];
		this.backgroundGreen = (Integer) settingsList[0][1];
		this.backgroundBlue = (Integer) settingsList[0][2];
		this.backgroundAlpha = (Integer) settingsList[0][3];
		
		this.highlightRed = (Integer) settingsList[1][0];
		this.highlightGreen = (Integer) settingsList[1][1];
		this.highlightBlue = (Integer) settingsList[1][2];
		this.highlightAlpha = (Integer) settingsList[1][3];
		
		this.borderRed = (Integer) settingsList[2][0];
		this.borderGreen = (Integer) settingsList[2][1];
		this.borderBlue = (Integer) settingsList[2][2];
		this.borderAlpha = (Integer) settingsList[2][3];
		
		this.mouseposRed = (Integer) settingsList[3][0];
		this.mouseposGreen = (Integer) settingsList[3][1];
		this.mouseposBlue = (Integer) settingsList[3][2];
		this.mouseposAlpha = (Integer) settingsList[3][3];
		
		this.triangles = (Integer) settingsList[4][0];
		this.radius = (Integer) settingsList[4][1];
		this.itemRadius = (Integer) settingsList[4][2];
		
		this.muted = (Boolean) settingsList[5][0];
		this.itemRadiusAuto = (Boolean) settingsList[5][1];
		this.name = (String) settingsList[5][2];
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.backgroundRed = ByteBufUtils.readVarInt(buf, 2);
		this.backgroundGreen = ByteBufUtils.readVarInt(buf, 2);
		this.backgroundBlue = ByteBufUtils.readVarInt(buf, 2);
		this.backgroundAlpha = ByteBufUtils.readVarInt(buf, 1);
		
		this.highlightRed = ByteBufUtils.readVarInt(buf, 2);
		this.highlightGreen = ByteBufUtils.readVarInt(buf, 2);
		this.highlightBlue = ByteBufUtils.readVarInt(buf, 2);
		this.highlightAlpha = ByteBufUtils.readVarInt(buf, 1);
		
		this.borderRed = ByteBufUtils.readVarInt(buf, 2);
		this.borderGreen = ByteBufUtils.readVarInt(buf, 2);
		this.borderBlue = ByteBufUtils.readVarInt(buf, 2);
		this.borderAlpha = ByteBufUtils.readVarInt(buf, 1);
		
		this.mouseposRed = ByteBufUtils.readVarInt(buf, 2);
		this.mouseposGreen = ByteBufUtils.readVarInt(buf, 2);
		this.mouseposBlue = ByteBufUtils.readVarInt(buf, 2);
		this.mouseposAlpha = ByteBufUtils.readVarInt(buf, 1);
		
		this.triangles = ByteBufUtils.readVarInt(buf, 3);
		this.radius = ByteBufUtils.readVarInt(buf, 1);
		this.itemRadius = ByteBufUtils.readVarInt(buf, 1);
		
		this.muted = ByteBufUtils.readVarInt(buf, 1) == 1;
		this.itemRadiusAuto = ByteBufUtils.readVarInt(buf, 1) == 1;
		this.name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeVarInt(buf, backgroundRed, 2);
		ByteBufUtils.writeVarInt(buf, backgroundGreen, 2);
		ByteBufUtils.writeVarInt(buf, backgroundBlue, 2);
		ByteBufUtils.writeVarInt(buf, backgroundAlpha, 1);
		
		ByteBufUtils.writeVarInt(buf, highlightRed, 2);
		ByteBufUtils.writeVarInt(buf, highlightGreen, 2);
		ByteBufUtils.writeVarInt(buf, highlightBlue, 2);
		ByteBufUtils.writeVarInt(buf, highlightAlpha, 1);
		
		ByteBufUtils.writeVarInt(buf, borderRed, 2);
		ByteBufUtils.writeVarInt(buf, borderGreen, 2);
		ByteBufUtils.writeVarInt(buf, borderBlue, 2);
		ByteBufUtils.writeVarInt(buf, borderAlpha, 1);
		
		ByteBufUtils.writeVarInt(buf, mouseposRed, 2);
		ByteBufUtils.writeVarInt(buf, mouseposGreen, 2);
		ByteBufUtils.writeVarInt(buf, mouseposBlue, 2);
		ByteBufUtils.writeVarInt(buf, mouseposAlpha, 1);
		
		ByteBufUtils.writeVarInt(buf, triangles, 3);
		ByteBufUtils.writeVarInt(buf, radius, 1);
		ByteBufUtils.writeVarInt(buf, itemRadius, 1);
		
		ByteBufUtils.writeVarInt(buf, muted ? 1 : 0, 1);
		ByteBufUtils.writeVarInt(buf, itemRadiusAuto ? 1 : 0, 1);
		ByteBufUtils.writeUTF8String(buf, name);
	}

	public static class SettingsUpdateHandle implements IMessageHandler<SettingsUpdate, IMessage>
	{
		@Override
		public IMessage onMessage(SettingsUpdate message, MessageContext ctx) 
		{
			ItemStack heldItem = ctx.getServerHandler().playerEntity.getHeldItem();
			BagInventory inventory;
			if(heldItem.getItem() instanceof ItemBag)
			{
				NBTTagCompound compound = heldItem.getTagCompound();
				
				inventory = new BagInventory(heldItem);
				
				inventory.setBackgroundRed(message.backgroundRed, compound);
				inventory.setBackgroundGreen(message.backgroundGreen, compound);
				inventory.setBackgroundBlue(message.backgroundBlue, compound);
				inventory.setBackgroundAlpha(message.backgroundAlpha, compound);
				
				inventory.setHighlightRed(message.highlightRed, compound);
				inventory.setHighlightGreen(message.highlightGreen, compound);
				inventory.setHighlightBlue(message.highlightBlue, compound);
				inventory.setHighlightAlpha(message.highlightAlpha, compound);
				
				inventory.setBorderRed(message.borderRed, compound);
				inventory.setBorderGreen(message.borderGreen, compound);
				inventory.setBorderBlue(message.borderBlue, compound);
				inventory.setBorderAlpha(message.borderAlpha, compound);
				
				inventory.setMouseposRed(message.mouseposRed, compound);
				inventory.setMouseposGreen(message.mouseposGreen, compound);
				inventory.setMouseposBlue(message.mouseposBlue, compound);
				inventory.setMouseposAlpha(message.mouseposAlpha, compound);
				
				inventory.setTriangles(message.triangles, compound);
				inventory.setRadius(message.radius, compound);
				inventory.setItemRadius(message.itemRadius, compound);
				
				inventory.setMuted(message.muted, compound);
				inventory.setUpdateItemRadiusAutomatic(message.itemRadiusAuto, compound);
				inventory.setName(message.name, compound);
				
				heldItem.setStackDisplayName(message.name);
			}
			return null;
		}
	}
}
