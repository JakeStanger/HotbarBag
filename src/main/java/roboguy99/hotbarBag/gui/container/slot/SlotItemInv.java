package roboguy99.hotbarBag.gui.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import roboguy99.hotbarBag.item.ItemBag;

/**
 * Custom slot which does not allow ItemBag instances to be placed inside of it.
 * 
 * @author Roboguy99
 *		
 */
public class SlotItemInv extends Slot
{
	public SlotItemInv(IInventory inventory, int index, int xPos, int yPos)
	{
		super(inventory, index, xPos, yPos);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return !(itemStack.getItem() instanceof ItemBag); //TODO This isn't working?
	}
}
