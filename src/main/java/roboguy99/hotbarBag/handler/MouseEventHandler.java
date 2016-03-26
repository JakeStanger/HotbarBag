package roboguy99.hotbarBag.handler;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;
import roboguy99.hotbarBag.item.ItemBag;
import roboguy99.hotbarBag.network.ClientProxy;

@SideOnly(Side.CLIENT)
public class MouseEventHandler 
{
	RenderOverlayHandler renderOverlayHandler;
	
	public MouseEventHandler(RenderOverlayHandler renderOverlayHandler)
	{
		this.renderOverlayHandler = renderOverlayHandler;
	}
	
	@SubscribeEvent
	 public void onMouseEvent(MouseEvent event) 
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBag)
		{
			if(ClientProxy.keyHUD.getIsKeyPressed())
			{
				if(event.dwheel != 0)
				{
					event.setCanceled(true);
					Mouse.setCursorPosition(10, 10);
				}
				if(event.button == 1)
				{
					event.setCanceled(true);
					this.renderOverlayHandler.setIsDisplayed(false);
				}
			}
		}
	}
}
