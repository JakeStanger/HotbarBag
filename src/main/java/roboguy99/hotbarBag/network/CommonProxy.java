package roboguy99.hotbarBag.network;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import roboguy99.hotbarBag.HotbarBag;
import roboguy99.hotbarBag.gui.GuiConfig;
import roboguy99.hotbarBag.gui.GuiInventory;
import roboguy99.hotbarBag.gui.container.ContainerInventory;
import roboguy99.hotbarBag.inventory.BagInventory;

/**
 * Common proxy class. Handles Guis.
 * 
 * @author Roboguy99
 *		
 */
public class CommonProxy implements IGuiHandler
{
	public BagInventory bagInventory;
	
	public void registerProxies()
	{
	
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == HotbarBag.GUI_INVENTORY) return new ContainerInventory(player, player.inventory, new BagInventory(player.getHeldItem()));
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == HotbarBag.GUI_INVENTORY) return new GuiInventory(new ContainerInventory(player, player.inventory, new BagInventory(player.getHeldItem())));
		if (ID == HotbarBag.GUI_CONFIG) return new GuiConfig();
		return null;
	}
}
