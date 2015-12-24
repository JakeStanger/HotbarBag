package roboguy99.hotbarBag.handler;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

/**
 * Stops the player's head from moving while the circle is displayed
 * 
 * @author Roboguy99
 *		
 */
@SideOnly(Side.CLIENT)
public class RenderTickHandler
{
	private final RenderOverlayHandler renderOverlayHandler;
	private final Minecraft minecraft;
	
	public RenderTickHandler(RenderOverlayHandler renderOverlayHandler)
	{
		this.renderOverlayHandler = renderOverlayHandler;
		this.minecraft = Minecraft.getMinecraft();
	}
	
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START)
		{
			if (this.minecraft.thePlayer != null)
			{
				if (this.renderOverlayHandler.getIsDisplayed())
				{
					Mouse.getDX();
					Mouse.getDY();
					this.minecraft.mouseHelper.deltaX = this.minecraft.mouseHelper.deltaY = 0;
				}
			}
		}
	}
}
