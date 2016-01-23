package roboguy99.hotbarBag.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import roboguy99.hotbarBag.HotbarBag;
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
		if (event.phase == TickEvent.Phase.START && Minecraft.getMinecraft().theWorld != null)
		{
			if(HotbarBag.keyHUD.getIsKeyPressed() && !this.pressedLastTick) this.firstTick = true;
			else this.firstTick = false;
			
			if (this.pressedLastTick && !HotbarBag.keyHUD.getIsKeyPressed())
			{
				HotbarBag.networkWrapper.sendToServer(new InventoryUpdate(this.renderOverlayHandler.getMouseSector(), this.renderOverlayHandler.getInventory()));
			}
			this.pressedLastTick = HotbarBag.keyHUD.getIsKeyPressed();
			
			if(this.firstTick) this.renderOverlayHandler.getInventory().readSettingsFromNBT(Minecraft.getMinecraft().thePlayer.getHeldItem().getTagCompound());
		}
	}
}
