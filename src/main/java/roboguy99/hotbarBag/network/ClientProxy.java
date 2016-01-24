package roboguy99.hotbarBag.network;

import cpw.mods.fml.client.registry.ClientRegistry;
import roboguy99.hotbarBag.HotbarBag;

/**
 * Client proxy class
 * 
 * @author Roboguy99
 *		
 */
public class ClientProxy extends CommonProxy
{
	@Override
	public void registerProxies()
	{
		ClientRegistry.registerKeyBinding(HotbarBag.keyHUD);
	}
}
