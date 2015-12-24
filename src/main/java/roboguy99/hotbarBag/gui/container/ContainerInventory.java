package roboguy99.hotbarBag.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import roboguy99.hotbarBag.gui.container.slot.SlotItemInv;
import roboguy99.hotbarBag.inventory.BagInventory;

/**
 * Container for item inventory
 * 
 * @author Roboguy99
 *		
 */
public class ContainerInventory extends Container
{
	public final BagInventory inventory;
	
	// Weird maths stuff for sizes of things
	private static final int INV_START = BagInventory.SIZE, INV_END = INV_START + 26, HOTBAR_START = INV_END + 1, HOTBAR_END = HOTBAR_START + 8;
	
	public ContainerInventory(EntityPlayer entityPlayer, InventoryPlayer inventoryPlayer, BagInventory bagInventory)
	{
		this.inventory = bagInventory;
		
		int i; // Defined here to save a little bit of memory
		int slot = 0;
		
		// Bag inventory (will always be rounded up to nearest 9)
		for(i = 0; i < BagInventory.SIZE / 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new SlotItemInv(this.inventory, slot, 8 + j * 18, 38 + i * 18));
				slot++;
			}
		}
		
		// Player inventory
		for(i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		// Hotbar
		for(i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer)
	{
		return this.inventory.isUseableByPlayer(entityPlayer);
	}
	
	/**
	 * Called when a player shift-clicks on a slot.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index)
	{
		ItemStack itemStack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemStack = itemstack1.copy();
			
			// If item is in our custom Inventory or armour slot
			if (index < INV_START)
			{
				// Try to place in player inventory / action bar
				if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END + 1, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemStack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory or armour slots
			else
			{
				// Shift-click items between inventory and bag inventory
				if (index >= INV_START)
				{
					// place in custom inventory
					if (!this.mergeItemStack(itemstack1, 0, INV_START, false))
					{
						return null;
					}
				}
			}
			
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemStack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}
		
		return itemStack;
	}
	
	/**
	 * Called when the player clicks a slot. Overridden to stop the bag from being picked up
	 */
	@Override
	public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player)
	{
		// this will prevent the player from interacting with the item that opened the inventory:
		if (slot >= 0 && this.getSlot(slot) != null && this.getSlot(slot).getStack() == player.getHeldItem())
		{
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}
}
