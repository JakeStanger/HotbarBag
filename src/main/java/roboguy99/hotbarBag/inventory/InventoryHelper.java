package roboguy99.hotbarBag.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import roboguy99.hotbarBag.item.ItemBag;

/**
 * Helper class for updating the bag's inventory
 * 
 * @author Roboguy99
 *		
 */
public class InventoryHelper
{
	public static void updateInventory(BagInventory inventory, EntityPlayer entityPlayer, int sectorMouseIsIn)
	{
		// Remove any null stacks before items
		List<ItemStack> inventoryContentsList = new ArrayList<ItemStack>();
		
		for(int i = 0; i < inventory.getSizeInventory(); i++)
		{
			if (inventory.getStackInSlot(i) != null)
			{
				inventoryContentsList.add(inventory.getStackInSlot(i));
			}
		}
		
		ItemStack[] inventoryContents = inventoryContentsList.toArray(new ItemStack[inventoryContentsList.size()]);
		
		ItemStack HUDStack = inventory.getStackInSlot(sectorMouseIsIn);
		ItemStack hotbarStack = entityPlayer.inventory.getStackInSlot(0);
		
		boolean sureGoAhead = false;
		if (hotbarStack == null) sureGoAhead = true;
		if (!sureGoAhead) if (!(hotbarStack.getItem() instanceof ItemBag)) sureGoAhead = true;
		
		if (sureGoAhead)
		{
			for(int i = 0; i < inventory.getSizeInventory(); i++) // Empty inventory
				inventory.setInventorySlotContents(i, null);
			for(int i = 0; i < inventoryContents.length; i++)
			{
				if (inventoryContents[i] != null)
				{
					inventory.setInventorySlotContents(i, inventoryContents[i]); // Refill inventory
				}
			}
			
			inventory.setInventorySlotContents(sectorMouseIsIn, hotbarStack);
			
			entityPlayer.inventory.setInventorySlotContents(0, HUDStack);
			entityPlayer.inventory.markDirty();
			//entityPlayer.inventory.currentItem = 0; //TODO Fix this
		}
	}
}
