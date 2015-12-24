package roboguy99.hotbarBag.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import roboguy99.hotbarBag.HotbarBag;

/**
 * Item class for the bag
 * 
 * @author Roboguy99
 *		
 */
public class ItemBag extends Item
{
	/**
	 * Create a new bag
	 */
	public ItemBag()
	{
		this.setUnlocalizedName("hotbarBag");
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setTextureName("roboguy99:bag");
		this.setMaxStackSize(1);
		
		GameRegistry.registerItem(this, "hotbarBag");
		
		GameRegistry.addRecipe(new ItemStack(this), "ABA", "ACA", "AAA", 'A', Items.leather, 'B', Items.gold_ingot, 'C', Blocks.chest);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack itemStack)
	{
		return 1;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (!entityPlayer.isSneaking()) entityPlayer.openGui(HotbarBag.instance, HotbarBag.GUI_INVENTORY, world, 0, 0, 0);
		else
			entityPlayer.openGui(HotbarBag.instance, HotbarBag.GUI_CONFIG, world, 0, 0, 0); // Open config if player is sneaking
			
		return itemStack;
	}
}
