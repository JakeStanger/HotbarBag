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
	@SubscribeEvent
	 public void onMouseEvent(MouseEvent event) 
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBag)
		{
			if(ClientProxy.keyHUD.getIsKeyPressed())
			{
				event.setCanceled(true);
			}
		}
	}
}
