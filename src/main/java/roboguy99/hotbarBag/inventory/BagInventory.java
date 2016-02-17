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
	
	private NBTTagCompound compound;
	
	public BagInventory(ItemStack itemStack)
	{
		this.invItem = itemStack;
		
		// If for any reason the itemstack has no NBT data
		if (itemStack != null && !itemStack.hasTagCompound()) // Sometimes if you're too fast the held item returns null when it shouldn't
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
		
		this.compound = itemStack.getTagCompound();
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
		if (this.getName(this.compound) != null) return this.getName(this.compound);
		return HotbarBag.instance.config.DEFAULT_NAME;
	}
	
	@Override
	public boolean hasCustomInventoryName()
	{
		if (this.getName(this.compound) != null) return this.getName(this.compound).length() > 0;
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
	
	/*public void readSettingsFromNBT(NBTTagCompound compound)
	{
		Config config = Config.instance; // Get config instance
		
		NBTTagList settings = compound.getTagList("Settings", NBT.TAG_COMPOUND);
		
		if(settings.getCompoundTagAt(0).hasKey("backgroundRed")) this.setBackgroundRed(settings.getCompoundTagAt(0).getInteger("backgroundRed"), this.compound); 
			else this.setBackgroundRed(config.DEFAULT_BACKGROUND_RED, this.compound);
		if(settings.getCompoundTagAt(0).hasKey("backgroundGreen")) this.setBackgroundGreen(settings.getCompoundTagAt(0).getInteger("backgroundGreen"), this.compound);
			else this.setBackgroundGreen(config.DEFAULT_BACKGROUND_GREEN, this.compound);
		if(settings.getCompoundTagAt(0).hasKey("backgroundBlue")) this.setBackgroundBlue(settings.getCompoundTagAt(0).getInteger("backgroundBlue"), this.compound);
			else this.setBackgroundBlue(config.DEFAULT_BACKGROUND_BLUE, this.compound);
		if(settings.getCompoundTagAt(0).hasKey("backgroundAlpha")) this.setBackgroundAlpha(settings.getCompoundTagAt(0).getInteger("backgroundAlpha"), this.compound);
			else this.setBackgroundAlpha(config.DEFAULT_BACKGROUND_ALPHA, this.compound);
		
		if(settings.getCompoundTagAt(1).hasKey("highlightRed")) this.setHighlightRed(settings.getCompoundTagAt(1).getInteger("highlightRed"), this.compound);
			else this.setHighlightRed(config.DEFAULT_HIGHLIGHT_RED, this.compound);
		if(settings.getCompoundTagAt(1).hasKey("highlightGreen")) this.setHighlightGreen(settings.getCompoundTagAt(1).getInteger("highlightGreen"), this.compound);
			else this.setHighlightGreen(config.DEFAULT_HIGHLIGHT_GREEN, this.compound);
		if(settings.getCompoundTagAt(1).hasKey("highlightBlue")) this.setHighlightBlue(settings.getCompoundTagAt(1).getInteger("highlightBlue"), this.compound);
			else this.setHighlightBlue(config.DEFAULT_HIGHLIGHT_BLUE, this.compound);
		if(settings.getCompoundTagAt(1).hasKey("highlightAlpha")) this.setHighlightAlpha(settings.getCompoundTagAt(1).getInteger("highlightAlpha"), this.compound);
			else this.setHighlightAlpha(config.DEFAULT_HIGHLIGHT_ALPHA, this.compound);
		
		if(settings.getCompoundTagAt(2).hasKey("borderRed")) this.setBorderRed(settings.getCompoundTagAt(2).getInteger("borderRed")); 
			else this.setBorderRed(config.DEFAULT_BORDER_RED);
		if(settings.getCompoundTagAt(2).hasKey("borderGreen")) this.setBorderGreen(settings.getCompoundTagAt(2).getInteger("borderGreen"));
			else this.setBorderGreen(config.DEFAULT_BORDER_GREEN);
		if(settings.getCompoundTagAt(2).hasKey("borderBlue")) this.setBorderBlue(settings.getCompoundTagAt(2).getInteger("borderBlue"));
			else this.setBorderBlue(config.DEFAULT_BORDER_BLUE);
		if(settings.getCompoundTagAt(2).hasKey("borderAlpha")) this.setBorderAlpha(settings.getCompoundTagAt(2).getInteger("borderAlpha"));
			else this.setBorderAlpha(config.DEFAULT_BORDER_ALPHA);
	
		if(settings.getCompoundTagAt(3).hasKey("mouseposRed")) this.setMouseposRed(settings.getCompoundTagAt(3).getInteger("mouseposRed"));
			else this.setMouseposRed(config.DEFAULT_MOUSEPOS_RED);
		if(settings.getCompoundTagAt(3).hasKey("mouseposGreen")) this.setMouseposGreen(settings.getCompoundTagAt(3).getInteger("mouseposGreen"));
			else this.setMouseposGreen(config.DEFAULT_MOUSEPOS_GREEN);
		if(settings.getCompoundTagAt(3).hasKey("mouseposBlue")) this.setMouseposBlue(settings.getCompoundTagAt(3).getInteger("mouseposBlue"));
			else this.setMouseposBlue(config.DEFAULT_MOUSEPOS_BLUE);
		if(settings.getCompoundTagAt(3).hasKey("mouseposAlpha")) this.setMouseposAlpha(settings.getCompoundTagAt(3).getInteger("mouseposAlpha"));
			else this.setBackgroundAlpha(config.DEFAULT_MOUSEPOS_ALPHA);
		
		if(settings.getCompoundTagAt(4).hasKey("triangles")) this.setTriangles(settings.getCompoundTagAt(4).getInteger("triangles"));
			else this.setTriangles(config.DEFAULT_TRIANGLES);
		if(settings.getCompoundTagAt(4).hasKey("radius")) this.setRadius(settings.getCompoundTagAt(4).getInteger("radius"));
			else this.setRadius(config.DEFAULT_RADIUS);
		if(settings.getCompoundTagAt(4).hasKey("itemRadius")) this.setItemRadius(settings.getCompoundTagAt(4).getInteger("itemRadius"));
			else this.setItemRadius(config.DEFAULT_ITEM_RADIUS);
		
		if(settings.getCompoundTagAt(5).hasKey("itemRadiusAuto")) this.setUpdateItemRadiusAutomatic(settings.getCompoundTagAt(5).getBoolean("itemRadiusAuto"));
			else this.setUpdateItemRadiusAutomatic(config.DEFAULT_UPDATE_ITEMS_AUTO);
		if(settings.getCompoundTagAt(5).hasKey("muted")) this.setMuted(settings.getCompoundTagAt(5).getBoolean("muted"));
			else this.setMuted(config.DEFAULT_MUTE);
		if(settings.getCompoundTagAt(5).hasKey("name")) this.setName(settings.getCompoundTagAt(5).getString("name"));
			else this.setName(config.DEFAULT_NAME);
		
	}*/
	
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
	
	/*public void writeSettingsToNBT(NBTTagCompound compound)
	{	
		// Write item settings
		NBTTagList settings = new NBTTagList();
		
		NBTTagCompound background = new NBTTagCompound();
		NBTTagCompound highlight = new NBTTagCompound();
		NBTTagCompound border = new NBTTagCompound();
		NBTTagCompound mousepos = new NBTTagCompound();
		NBTTagCompound circle = new NBTTagCompound();
		NBTTagCompound general = new NBTTagCompound();
		
		background.setInteger("backgroundRed", this.getBackgroundRed());
		background.setInteger("backgroundGreen", this.getBackgroundGreen());
		background.setInteger("backgroundBlue", this.getBackgroundBlue());
		background.setInteger("backgroundAlpha", this.getBackgroundAlpha());
		
		highlight.setInteger("highlightRed", this.getHighlightRed());
		highlight.setInteger("highlightGreen", this.getHighlightGreen());
		highlight.setInteger("highlightBlue", this.getHighlightBlue());
		highlight.setInteger("highlightAlpha", this.getHighlightAlpha());
		
		border.setInteger("borderRed", this.getBorderRed());
		border.setInteger("borderGreen", this.getBorderGreen());
		border.setInteger("borderBlue", this.getBorderBlue());
		border.setInteger("borderAlpha", this.getBorderAlpha());
		
		mousepos.setInteger("mouseposRed", this.getMouseposRed());
		mousepos.setInteger("mouseposGreen", this.getMouseposGreen());
		mousepos.setInteger("mouseposBlue", this.getMouseposBlue());
		mousepos.setInteger("mouseposAlpha", this.getMouseposAlpha());
		
		circle.setInteger("triangles", this.getTriangles());
		circle.setInteger("radius", this.getRadius());
		circle.setInteger("itemRadius", this.getItemRadius());
		
		general.setBoolean("itemRadiusAuto", this.isUpdateItemRadiusAutomatic());
		general.setBoolean("muted", this.isMuted());
		general.setString("name", this.getName());
		
		settings.appendTag(background);
		settings.appendTag(highlight);
		settings.appendTag(border);
		settings.appendTag(mousepos);
		settings.appendTag(circle);
		settings.appendTag(general);
		
		compound.setTag("Settings", settings);
	}*/
	
	/**
	 * @return the backgroundRed
	 */
	public int getBackgroundRed(NBTTagCompound compound)
	{
		return compound.getInteger("backgroundRed");
	}
	
	/**
	 * @return the backgroundGreen
	 */
	public int getBackgroundGreen(NBTTagCompound compound)
	{
		return compound.getInteger("backgroundGreen");
	}
	
	/**
	 * @return the backgroundBlue
	 */
	public int getBackgroundBlue(NBTTagCompound compound)
	{
		return compound.getInteger("backgroundBlue");
	}
	
	/**
	 * @return the highlightRed
	 */
	public int getHighlightRed(NBTTagCompound compound)
	{
		return compound.getInteger("highlightRed");
	}
	
	/**
	 * @return the highlightGreen
	 */
	public int getHighlightGreen(NBTTagCompound compound)
	{
		return compound.getInteger("highlightGreen");
	}
	
	/**
	 * @return the highlightBlue
	 */
	public int getHighlightBlue(NBTTagCompound compound)
	{
		return compound.getInteger("highlightBlue");
	}
	
	/**
	 * @return the triangles
	 */
	public int getTriangles(NBTTagCompound compound)
	{
		return compound.getInteger("triangles");
	}
	
	/**
	 * @return the radius
	 */
	public int getRadius(NBTTagCompound compound)
	{
		return compound.getInteger("radius");
	}
	
	/**
	 * @return the itemRadius
	 */
	public int getItemRadius(NBTTagCompound compound)
	{
		return compound.getInteger("itemRadius");
	}
	
	/**
	 * @return the updateItemRadiusAutomatically
	 */
	public boolean isUpdateItemRadiusAutomatic(NBTTagCompound compound)
	{
		return compound.getBoolean("itemRadiusAuto");
	}
	
	/**
	 * @return the mute
	 */
	public boolean isMuted(NBTTagCompound compound)
	{
		return compound.getBoolean("muted");
	}
	
	/**
	 * @param backgroundRed the backgroundRed to set
	 */
	public void setBackgroundRed(int backgroundRed, NBTTagCompound compound)
	{
		compound.setInteger("backgroundRed", backgroundRed);
	}
	
	/**
	 * @param backgroundGreen the backgroundGreen to set
	 */
	public void setBackgroundGreen(int backgroundGreen, NBTTagCompound compound)
	{
		compound.setInteger("backgroundGreen", backgroundGreen);
	}
	
	/**
	 * @param backgroundBlue the backgroundBlue to set
	 */
	public void setBackgroundBlue(int backgroundBlue, NBTTagCompound compound)
	{
		compound.setInteger("backgroundBlue", backgroundBlue);
	}
	
	/**
	 * @param highlightRed the highlightRed to set
	 */
	public void setHighlightRed(int highlightRed, NBTTagCompound compound)
	{
		compound.setInteger("highlightRed", highlightRed);
	}
	
	/**
	 * @param highlightGreen the highlightGreen to set
	 */
	public void setHighlightGreen(int highlightGreen, NBTTagCompound compound)
	{
		compound.setInteger("highlightGreen", highlightGreen);
	}
	
	/**
	 * @param highlightBlue the highlightBlue to set
	 */
	public void setHighlightBlue(int highlightBlue, NBTTagCompound compound)
	{
		compound.setInteger("highlightBlue", highlightBlue);
	}
	
	/**
	 * @param triangles the triangles to set
	 */
	public void setTriangles(int triangles, NBTTagCompound compound)
	{
		compound.setInteger("triangles", triangles);
	}
	
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(int radius, NBTTagCompound compound)
	{
		compound.setInteger("radius", radius);
	}
	
	/**
	 * @param itemRadius the itemRadius to set
	 */
	public void setItemRadius(int itemRadius, NBTTagCompound compound)
	{
		compound.setInteger("itemRadius", itemRadius);
	}
	
	/**
	 * @param updateItemRadiusAutomatically the updateItemRadiusAutomatically to set
	 */
	public void setUpdateItemRadiusAutomatic(boolean updateItemRadiusAutomatically, NBTTagCompound compound)
	{
		compound.setBoolean("itemRadiusAuto", updateItemRadiusAutomatically);
	}
	
	/**
	 * @param mute the mute to set
	 */
	public void setMuted(boolean mute, NBTTagCompound compound)
	{
		compound.setBoolean("muted", mute);
	}
	
	/**
	 * @return the name
	 */
	public String getName(NBTTagCompound compound)
	{
		return compound.getString("name");
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name, NBTTagCompound compound)
	{
		compound.setString("name", name);
	}

	/**
	 * @return the backgroundAlpha
	 */
	public int getBackgroundAlpha(NBTTagCompound compound) 
	{
		return compound.getInteger("backgroundAlpha");
	}

	/**
	 * @param backgroundAlpha the backgroundAlpha to set
	 */
	public void setBackgroundAlpha(int backgroundAlpha, NBTTagCompound compound) 
	{
		compound.setInteger("backgroundAlpha", backgroundAlpha);
	}

	/**
	 * @return the highlightAlpha
	 */
	public int getHighlightAlpha(NBTTagCompound compound) 
	{
		return compound.getInteger("highlightAlpha");
	}

	/**
	 * @param highlightAlpha the highlightAlpha to set
	 */
	public void setHighlightAlpha(int highlightAlpha, NBTTagCompound compound) 
	{
		compound.setInteger("highlightAlpha", highlightAlpha);
	}

	/**
	 * @return the borderRed
	 */
	public int getBorderRed(NBTTagCompound compound) 
	{
		return compound.getInteger("borderRed");
	}

	/**
	 * @param borderRed the borderRed to set
	 */
	public void setBorderRed(int borderRed, NBTTagCompound compound) 
	{
		compound.setInteger("borderRed", borderRed);
	}

	/**
	 * @return the borderGreen
	 */
	public int getBorderGreen(NBTTagCompound compound) 
	{
		return compound.getInteger("borderGreen");
	}

	/**
	 * @param borderGreen the borderGreen to set
	 */
	public void setBorderGreen(int borderGreen, NBTTagCompound compound) 
	{
		compound.setInteger("borderGreen", borderGreen);
	}

	/**
	 * @return the borderBlue
	 */
	public int getBorderBlue(NBTTagCompound compound) 
	{
		return compound.getInteger("borderBlue");
	}

	/**
	 * @param borderBlue the borderBlue to set
	 */
	public void setBorderBlue(int borderBlue, NBTTagCompound compound) 
	{
		compound.setInteger("borderBlue", borderBlue);
	}

	/**
	 * @return the borderAlpha
	 */
	public int getBorderAlpha(NBTTagCompound compound) 
	{
		return compound.getInteger("borderAlpha");
	}

	/**
	 * @param borderAlpha the borderAlpha to set
	 */
	public void setBorderAlpha(int borderAlpha, NBTTagCompound compound) 
	{
		compound.setInteger("borderAlpha", borderAlpha);
	}

	/**
	 * @return the mouseposRed
	 */
	public int getMouseposRed(NBTTagCompound compound) 
	{
		return compound.getInteger("mouseposRed");
	}

	/**
	 * @param mouseposRed the mouseposRed to set
	 */
	public void setMouseposRed(int mouseposRed, NBTTagCompound compound) 
	{
		compound.setInteger("mouseposRed", mouseposRed);
	}

	/**
	 * @return the mouseposGreen
	 */
	public int getMouseposGreen(NBTTagCompound compound) 
	{
		return compound.getInteger("mouseposGreen");
	}

	/**
	 * @param mouseposGreen the mouseposGreen to set
	 */
	public void setMouseposGreen(int mouseposGreen, NBTTagCompound compound) 
	{
		compound.setInteger("mouseposGreen", mouseposGreen);
	}

	/**
	 * @return the mouseposBlue
	 */
	public int getMouseposBlue(NBTTagCompound compound) 
	{
		return compound.getInteger("mouseposBlue");
	}

	/**
	 * @param mouseposBlue the mouseposBlue to set
	 */
	public void setMouseposBlue(int mouseposBlue, NBTTagCompound compound) 
	{
		compound.setInteger("mouseposBlue", mouseposBlue);
	}

	/**
	 * @return the mouseposAlpha
	 */
	public int getMouseposAlpha(NBTTagCompound compound) 
	{
		return compound.getInteger("mouseposAlpha");
	}

	/**
	 * @param mouseposAlpha the mouseposAlpha to set
	 */
	public void setMouseposAlpha(int mouseposAlpha, NBTTagCompound compound) 
	{
		compound.setInteger("mouseposAlpha", mouseposAlpha);
	}
}
