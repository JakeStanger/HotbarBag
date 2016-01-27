package roboguy99.hotbarBag.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import roboguy99.hotbarBag.Config;
import roboguy99.hotbarBag.HotbarBag;
import roboguy99.hotbarBag.item.ItemBag;

/**
 * Inventory for item. Also handles settings NBT
 * 
 * @author Roboguy99
 * 		
 */
public class BagInventory implements IInventory
{
	public static final int SIZE = 18; // Equal to 2 rows - must be a multiple of 9
	
	private final ItemStack invItem;
	ItemStack[] inventory = new ItemStack[SIZE];
	
	public BagInventory(ItemStack itemStack)
	{
		this.invItem = itemStack;
		//this.readSettingsFromNBT(itemStack.getTagCompound());
		
		// If for any reason the itemstack has no NBT data
		if (itemStack != null && !itemStack.hasTagCompound()) // Sometimes if you're too fast the held item returns null when it isn't
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
		
		// Read inventory from NBT
		if (itemStack != null) this.readFromNBT(itemStack.getTagCompound());
		
		for(int i = 0; i < 17; i++) // Repeat process 17 times to remove all gaps, not just the first one
		{
			for(int j = this.getSizeInventory() - 1; j >= 0; j--)
			{
				if (this.getStackInSlot(j) == null && !(j + 1 == this.getSizeInventory()))
				{
					this.setInventorySlotContents(j, this.getStackInSlot(j + 1));
					this.setInventorySlotContents(j + 1, null);
				}
			}
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return this.inventory[slot];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		ItemStack stack = this.getStackInSlot(slot);
		
		if (stack != null)
		{
			if (stack.stackSize > amount)
			{
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0)
				{
					this.setInventorySlotContents(slot, null);
				}
			}
			else
			{
				this.setInventorySlotContents(slot, null);
			}
			this.markDirty();
		}
		return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot)
	{
		ItemStack stack = this.getStackInSlot(slot);
		
		if (stack != null)
		{
			this.setInventorySlotContents(slot, null);
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack)
	{
		this.inventory[slot] = itemStack;
		
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
		{
			itemStack.stackSize = this.getInventoryStackLimit();
		}
		
		this.markDirty();
	}
	
	@Override
	public String getInventoryName()
	{
		if (HotbarBag.instance.config.getName() != null) return HotbarBag.instance.config.getName();
		return HotbarBag.instance.config.DEFAULT_NAME;
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		if (HotbarBag.instance.config.getName() != null) return HotbarBag.instance.config.getName().length() > 0;
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public void markDirty()
	{
		for(int i = 0; i < this.getSizeInventory(); ++i)
		{
			if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize == 0)
			{
				this.inventory[i] = null;
			}
		}
		
		if (this.invItem != null) this.writeToNBT(this.invItem.getTagCompound());
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer)
	{
		return true;
	}
	
	@Override
	public void openInventory()
	{
	}
	
	@Override
	public void closeInventory()
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack)
	{
		return !(itemStack.getItem() instanceof ItemBag);
	}
	
	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagList items = compound.getTagList("Inventory", NBT.TAG_COMPOUND);
		
		for(int i = 0; i < items.tagCount(); i++)
		{
			NBTTagCompound item = items.getCompoundTagAt(i);
			int slot = item.getInteger("Slot");
			
			// Double-check has loaded correctly.
			if (slot >= 0 && slot < this.getSizeInventory())
			{
				this.inventory[slot] = ItemStack.loadItemStackFromNBT(item);
			}
		}
	}
	
	public void readSettingsFromNBT(NBTTagCompound compound)
	{
		Config config = HotbarBag.instance.config; // Get config instance
		
		NBTTagList settings = compound.getTagList("Settings", NBT.TAG_COMPOUND);
		
		if(settings.getCompoundTagAt(0).hasKey("backgroundRed")) config.setBackgroundRed(settings.getCompoundTagAt(0).getInteger("backgroundRed")); 
			else config.setBackgroundRed(config.DEFAULT_BACKGROUND_RED);
		if(settings.getCompoundTagAt(0).hasKey("backgroundGreen")) config.setBackgroundGreen(settings.getCompoundTagAt(0).getInteger("backgroundGreen"));
			else config.setBackgroundGreen(config.DEFAULT_BACKGROUND_GREEN);
		if(settings.getCompoundTagAt(0).hasKey("backgroundBlue")) config.setBackgroundBlue(settings.getCompoundTagAt(0).getInteger("backgroundBlue"));
			else config.setBackgroundBlue(config.DEFAULT_BACKGROUND_BLUE);
		if(settings.getCompoundTagAt(0).hasKey("backgroundAlpha")) config.setBackgroundAlpha(settings.getCompoundTagAt(0).getInteger("backgroundAlpha"));
			else config.setBackgroundAlpha(config.DEFAULT_BACKGROUND_ALPHA);
		
		if(settings.getCompoundTagAt(1).hasKey("highlightRed")) config.setHighlightRed(settings.getCompoundTagAt(1).getInteger("highlightRed"));
			else config.setHighlightRed(config.DEFAULT_HIGHLIGHT_RED);
		if(settings.getCompoundTagAt(1).hasKey("highlightGreen")) config.setHighlightGreen(settings.getCompoundTagAt(1).getInteger("highlightGreen"));
			else config.setHighlightGreen(config.DEFAULT_HIGHLIGHT_GREEN);
		if(settings.getCompoundTagAt(1).hasKey("highlightBlue")) config.setHighlightBlue(settings.getCompoundTagAt(1).getInteger("highlightBlue"));
			else config.setHighlightBlue(config.DEFAULT_HIGHLIGHT_BLUE);
		if(settings.getCompoundTagAt(1).hasKey("highlightAlpha")) config.setHighlightAlpha(settings.getCompoundTagAt(1).getInteger("highlightAlpha"));
			else config.setHighlightAlpha(config.DEFAULT_HIGHLIGHT_ALPHA);
		
		if(settings.getCompoundTagAt(2).hasKey("borderRed")) config.setBorderRed(settings.getCompoundTagAt(2).getInteger("borderRed")); 
			else config.setBorderRed(config.DEFAULT_BORDER_RED);
		if(settings.getCompoundTagAt(2).hasKey("borderGreen")) config.setBorderGreen(settings.getCompoundTagAt(2).getInteger("borderGreen"));
			else config.setBorderGreen(config.DEFAULT_BORDER_GREEN);
		if(settings.getCompoundTagAt(2).hasKey("borderBlue")) config.setBorderBlue(settings.getCompoundTagAt(2).getInteger("borderBlue"));
			else config.setBorderBlue(config.DEFAULT_BORDER_BLUE);
		if(settings.getCompoundTagAt(2).hasKey("borderAlpha")) config.setBorderAlpha(settings.getCompoundTagAt(2).getInteger("borderAlpha"));
			else config.setBorderAlpha(config.DEFAULT_BORDER_ALPHA);
	
		if(settings.getCompoundTagAt(3).hasKey("mouseposRed")) config.setMouseposRed(settings.getCompoundTagAt(3).getInteger("mouseposRed"));
			else config.setMouseposRed(config.DEFAULT_MOUSEPOS_RED);
		if(settings.getCompoundTagAt(3).hasKey("mouseposGreen")) config.setMouseposGreen(settings.getCompoundTagAt(3).getInteger("mouseposGreen"));
			else config.setMouseposGreen(config.DEFAULT_MOUSEPOS_GREEN);
		if(settings.getCompoundTagAt(3).hasKey("mouseposBlue")) config.setMouseposBlue(settings.getCompoundTagAt(3).getInteger("mouseposBlue"));
			else config.setMouseposBlue(config.DEFAULT_MOUSEPOS_BLUE);
		if(settings.getCompoundTagAt(3).hasKey("mouseposAlpha")) config.setMouseposAlpha(settings.getCompoundTagAt(3).getInteger("mouseposAlpha"));
			else config.setBackgroundAlpha(config.DEFAULT_MOUSEPOS_ALPHA);
		
		if(settings.getCompoundTagAt(4).hasKey("triangles")) config.setTriangles(settings.getCompoundTagAt(4).getInteger("triangles"));
			else config.setTriangles(config.DEFAULT_TRIANGLES);
		if(settings.getCompoundTagAt(4).hasKey("radius")) config.setRadius(settings.getCompoundTagAt(4).getInteger("radius"));
			else config.setRadius(config.DEFAULT_RADIUS);
		if(settings.getCompoundTagAt(4).hasKey("itemRadius")) config.setItemRadius(settings.getCompoundTagAt(4).getInteger("itemRadius"));
			else config.setItemRadius(config.DEFAULT_ITEM_RADIUS);
		
		if(settings.getCompoundTagAt(5).hasKey("itemRadiusAuto")) config.setUpdateItemRadiusAutomatic(settings.getCompoundTagAt(5).getBoolean("itemRadiusAuto"));
			else config.setUpdateItemRadiusAutomatic(config.DEFAULT_UPDATE_ITEMS_AUTO);
		if(settings.getCompoundTagAt(5).hasKey("muted")) config.setMuted(settings.getCompoundTagAt(5).getBoolean("muted"));
			else config.setMuted(config.DEFAULT_MUTE);
		if(settings.getCompoundTagAt(5).hasKey("name")) config.setName(settings.getCompoundTagAt(5).getString("name"));
			else config.setName(config.DEFAULT_NAME);
		
	}
	
	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagList items = new NBTTagList();
		
		for(int i = 0; i < this.getSizeInventory(); i++)
		{
			// Don't write empty slots
			if (this.getStackInSlot(i) != null)
			{
				NBTTagCompound item = new NBTTagCompound();
				item.setInteger("Slot", i);
				
				this.getStackInSlot(i).writeToNBT(item);
				
				items.appendTag(item);
			}
		}
		compound.setTag("Inventory", items);
	}
	
	public void writeSettingsToNBT(NBTTagCompound compound)
	{
		Config config = HotbarBag.instance.config; // Get config instance
		
		// Write item settings
		NBTTagList settings = new NBTTagList();
		
		NBTTagCompound background = new NBTTagCompound();
		NBTTagCompound highlight = new NBTTagCompound();
		NBTTagCompound border = new NBTTagCompound();
		NBTTagCompound mousepos = new NBTTagCompound();
		NBTTagCompound circle = new NBTTagCompound();
		NBTTagCompound general = new NBTTagCompound();
		
		background.setInteger("backgroundRed", config.getBackgroundRed());
		background.setInteger("backgroundGreen", config.getBackgroundGreen());
		background.setInteger("backgroundBlue", config.getBackgroundBlue());
		background.setInteger("backgroundAlpha", config.getBackgroundAlpha());
		
		highlight.setInteger("highlightRed", config.getHighlightRed());
		highlight.setInteger("highlightGreen", config.getHighlightGreen());
		highlight.setInteger("highlightBlue", config.getHighlightBlue());
		highlight.setInteger("highlightAlpha", config.getHighlightAlpha());
		
		border.setInteger("borderRed", config.getBorderRed());
		border.setInteger("borderGreen", config.getBorderGreen());
		border.setInteger("borderBlue", config.getBorderBlue());
		border.setInteger("borderAlpha", config.getBorderAlpha());
		
		mousepos.setInteger("mouseposRed", config.getMouseposRed());
		mousepos.setInteger("mouseposGreen", config.getMouseposGreen());
		mousepos.setInteger("mouseposBlue", config.getMouseposBlue());
		mousepos.setInteger("mouseposAlpha", config.getMouseposAlpha());
		
		circle.setInteger("triangles", config.getTriangles());
		circle.setInteger("radius", config.getRadius());
		circle.setInteger("itemRadius", config.getItemRadius());
		
		general.setBoolean("itemRadiusAuto", config.isUpdateItemRadiusAutomatic());
		general.setBoolean("muted", config.isMuted());
		general.setString("name", config.getName());
		
		settings.appendTag(background);
		settings.appendTag(highlight);
		settings.appendTag(border);
		settings.appendTag(mousepos);
		settings.appendTag(circle);
		settings.appendTag(general);
		
		compound.setTag("Settings", settings);
	}
}
