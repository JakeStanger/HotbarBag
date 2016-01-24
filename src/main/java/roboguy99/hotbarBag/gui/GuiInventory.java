package roboguy99.hotbarBag.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import roboguy99.hotbarBag.gui.container.ContainerInventory;
import roboguy99.hotbarBag.inventory.BagInventory;

/**
 * GUI for item inventory
 * 
 * @author Roboguy99
 *		
 */
public class GuiInventory extends GuiContainer
{
	private float xSize_lo, ySize_lo; // _lo to avoid conflict with existing super fields
	
	private static final ResourceLocation texture = new ResourceLocation("roboguy99", "textures/gui/Inventory.png");
	
	private final BagInventory inventory;
	private final FontRenderer fontRenderer;
	
	public GuiInventory(ContainerInventory containerInventory)
	{
		super(containerInventory);
		this.inventory = containerInventory.inventory;
		this.fontRenderer = this.mc.getMinecraft().fontRenderer;
	}
	
	/**
	 * Draw screen and all components
	 */
	@Override
	public void drawScreen(int var1, int var2, float var3)
	{
		super.drawScreen(var1, var2, var3);
		this.xSize_lo = var1;
		this.ySize_lo = var2;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		String s = this.inventory.hasCustomInventoryName() ? this.inventory.getInventoryName() : StatCollector.translateToLocal(this.inventory.getInventoryName()).trim();
		this.fontRenderer.drawString(s, 8, 28, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory").trim(), 8, this.ySize - 96 + 4, 4210752);
	}
	
	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		int i1;
	}
}
