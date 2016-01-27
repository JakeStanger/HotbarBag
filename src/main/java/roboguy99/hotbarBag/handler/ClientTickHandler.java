package roboguy99.hotbarBag.handler;

import java.awt.Color;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import roboguy99.hotbarBag.HotbarBag;
import roboguy99.hotbarBag.item.ItemBag;
import roboguy99.hotbarBag.network.ClientProxy;
import roboguy99.hotbarBag.network.packet.InventoryUpdate;

/**
 * Sends update packet when circle is no longer being displayed
 * 
 * @author Roboguy99
 *		
 */
public class ClientTickHandler
{
	private boolean pressedLastTick;
	private boolean firstTick;
	
	private final RenderOverlayHandler renderOverlayHandler;
	
	public ClientTickHandler(RenderOverlayHandler renderOverlayHandler)
	{
		this.renderOverlayHandler = renderOverlayHandler;
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.thePlayer;
		
		if (event.phase == TickEvent.Phase.START && minecraft.theWorld != null)
		{
			if(ClientProxy.keyHUD.getIsKeyPressed() && !this.pressedLastTick) this.firstTick = true;
			else this.firstTick = false;
			
			if (this.pressedLastTick && !ClientProxy.keyHUD.getIsKeyPressed())
			{
				if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBag) //TODO Tidy this
				{	
					if(!(player.inventory.getStackInSlot(0) != null && player.inventory.getStackInSlot(0).getItem() instanceof ItemBag)) HotbarBag.networkWrapper.sendToServer(new InventoryUpdate(this.renderOverlayHandler.getMouseSector(), this.renderOverlayHandler.getInventory()));
					else if(!(player.inventory.getStackInSlot(0) == player.getHeldItem()))player.addChatMessage(new ChatComponentText("Infinite storage is just a " + EnumChatFormatting.RED + "*little bit*" + EnumChatFormatting.WHITE + " overpowered, don't you think?")); //TODO Make this only appear once maybe
					else player.addChatMessage(new ChatComponentText("Please move the bag from hotbar slot 1"));
				}
			}
			this.pressedLastTick = ClientProxy.keyHUD.getIsKeyPressed();
			
			if(this.firstTick && player.getHeldItem() != null) this.renderOverlayHandler.getInventory().readSettingsFromNBT(player.getHeldItem().getTagCompound());
		}
	}
}
