package roboguy99.hotbarBag.network;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import roboguy99.hotbarBag.handler.ClientTickHandler;
import roboguy99.hotbarBag.handler.MouseEventHandler;
import roboguy99.hotbarBag.handler.RenderOverlayHandler;
import roboguy99.hotbarBag.handler.RenderTickHandler;

/**
 * Client proxy class
 * 
 * @author Roboguy99
 *		
 */
public class ClientProxy extends CommonProxy
{
	public static KeyBinding keyHUD;
	public RenderOverlayHandler renderOverlayEventHandler;
	
	@Override
	public void registerProxies()
	{
		this.renderOverlayEventHandler = new RenderOverlayHandler();
		
		FMLCommonHandler.instance().bus().register(new ClientTickHandler(this.renderOverlayEventHandler));
		FMLCommonHandler.instance().bus().register(new RenderTickHandler(this.renderOverlayEventHandler));
		
		MinecraftForge.EVENT_BUS.register(this.renderOverlayEventHandler);
		MinecraftForge.EVENT_BUS.register(new MouseEventHandler());
		
		this.keyHUD = new KeyBinding("key.bagHUD", Keyboard.KEY_F, "key.categories.inventory");
		ClientRegistry.registerKeyBinding(this.keyHUD);
	}
}
