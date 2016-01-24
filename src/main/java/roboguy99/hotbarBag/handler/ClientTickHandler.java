package roboguy99.hotbarBag.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import roboguy99.hotbarBag.HotbarBag;
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
		
		if (event.phase == TickEvent.Phase.START && minecraft.theWorld != null)
		{
			if(ClientProxy.keyHUD.getIsKeyPressed() && !this.pressedLastTick) this.firstTick = true;
			else this.firstTick = false;
			
			if (this.pressedLastTick && !ClientProxy.keyHUD.getIsKeyPressed())
			{
				HotbarBag.networkWrapper.sendToServer(new InventoryUpdate(this.renderOverlayHandler.getMouseSector(), this.renderOverlayHandler.getInventory()));
			}
			this.pressedLastTick = ClientProxy.keyHUD.getIsKeyPressed();
			
			if(this.firstTick && minecraft.thePlayer.getHeldItem() != null) this.renderOverlayHandler.getInventory().readSettingsFromNBT(minecraft.thePlayer.getHeldItem().getTagCompound());
		}
	}
}
