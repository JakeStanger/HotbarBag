package roboguy99.hotbarBag.gui;

import java.awt.Color;

import cpw.mods.fml.client.config.GuiSlider;
import cpw.mods.fml.client.config.GuiSlider.ISlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import roboguy99.hotbarBag.Config;
import roboguy99.hotbarBag.HotbarBag;
import roboguy99.hotbarBag.inventory.BagInventory;
import roboguy99.hotbarBag.network.packet.InventoryUpdate;
import roboguy99.hotbarBag.network.packet.SettingsUpdate;

/**
 * In-game configuration GUI
 * 
 * @author Roboguy99
 *		
 */
public class GuiConfig extends GuiScreen implements ISlider
{
	private Config config;
	private BagInventory inventory;
	private final Minecraft minecraft = Minecraft.getMinecraft();
	private ItemStack heldItem;
	
	private static final ResourceLocation texture = new ResourceLocation("roboguy99", "textures/gui/Config.png"); // Background texture
	
	// Gui sizes
	private static final int xSize = 215;
	private static final int ySize = 226;
	
	// Component IDs
	private static final int ID_BACKGROUND_RED = 0;
	private static final int ID_BACKGROUND_GREEN = 1;
	private static final int ID_BACKGROUND_BLUE = 2;
	
	private static final int ID_HIGHLIGHT_RED = 3;
	private static final int ID_HIGHLIGHT_GREEN = 4;
	private static final int ID_HIGHLIGHT_BLUE = 5;
	
	private static final int ID_TRIANGLES = 6;
	private static final int ID_RADIUS = 7;
	private static final int ID_ITEM_RADIUS = 8;
	private static final int ID_ITEM_RADIUS_AUTOMATIC = 9;
	
	private static final int ID_NAME = 10;
	
	private static final int ID_MUTE = 11;
	private static final int ID_RESET = 12;
	private static final int ID_SAVE = 13;
	
	private int guiLeft, guiTop;
	
	// Min/max constants
	private static final int COLOUR_MIN = 0;
	private static final int COLOUR_MAX = 255;
	
	private static final int TRIANGLES_MIN = 10000;
	private static final int TRIANGLES_MAX = 100000;
	
	private static final int RADIUS_MIN = 10;
	private static final int RADIUS_MAX = 120;
	
	// Components
	private GuiSlider sldHighlightRed;
	private GuiSlider sldHighlightGreen;
	private GuiSlider sldHighlightBlue;
	
	private GuiSlider sldBackgroundRed;
	private GuiSlider sldBackgroundGreen;
	private GuiSlider sldBackgroundBlue;
	
	private GuiSlider sldTriangles;
	private GuiSlider sldRadius;
	private GuiSlider sldItemRadius;
	
	private GuiButton btnAutoItemRadius;
	private GuiButton btnMute;
	private GuiButton btnDefault;
	private GuiButton btnSave;
	
	private GuiTextField txtName;
	
	@Override
	public void initGui()
	{
		this.config = HotbarBag.instance.config;
		
		this.heldItem = this.minecraft.thePlayer.getHeldItem();
		this.inventory = new BagInventory(this.heldItem);
		
		inventory.readSettingsFromNBT(heldItem.getTagCompound());
		
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		
		// Create instances of each component TODO Switch out the huge ocean of magic numbers before you're banned from technology forever
		this.sldHighlightRed = new GuiSlider(this.ID_HIGHLIGHT_RED, this.guiLeft + 5, this.guiTop + 17, 100, 20, "Red ", "", this.COLOUR_MIN, this.COLOUR_MAX, this.config.getHighlightRed(), false, true, this);
		this.sldHighlightGreen = new GuiSlider(this.ID_HIGHLIGHT_GREEN, this.guiLeft + 5, this.guiTop + 17 + 22, 100, 20, "Green ", "", this.COLOUR_MIN, this.COLOUR_MAX, this.config.getHighlightGreen(), false, true, this);
		this.sldHighlightBlue = new GuiSlider(this.ID_HIGHLIGHT_BLUE, this.guiLeft + 5, this.guiTop + 17 + 22 * 2, 100, 20, "Blue ", "", this.COLOUR_MIN, this.COLOUR_MAX, this.config.getHighlightBlue(), false, true, this);
		
		this.sldBackgroundRed = new GuiSlider(this.ID_BACKGROUND_RED, this.guiLeft + 5 + 100 + 5, this.guiTop + 17, 100, 20, "Red ", "", this.COLOUR_MIN, this.COLOUR_MAX, this.config.getBackgroundRed(), false, true, this);
		this.sldBackgroundGreen = new GuiSlider(this.ID_BACKGROUND_GREEN, this.guiLeft + 5 + 100 + 5, this.guiTop + 17 + 22, 100, 20, "Green ", "", this.COLOUR_MIN, this.COLOUR_MAX, this.config.getBackgroundGreen(), false, true, this);
		this.sldBackgroundBlue = new GuiSlider(this.ID_BACKGROUND_BLUE, this.guiLeft + 5 + 100 + 5, this.guiTop + 17 + 22 * 2, 100, 20, "Blue ", "", this.COLOUR_MIN, this.COLOUR_MAX, this.config.getBackgroundBlue(), false, true, this);
		
		this.sldTriangles = new GuiSlider(this.ID_TRIANGLES, this.guiLeft + 5, this.guiTop + 17 + 22 * 3 + 10, 100, 20, "Triangles ", "", this.TRIANGLES_MIN, this.TRIANGLES_MAX, this.config.getTriangles(), false, true, this);
		this.sldRadius = new GuiSlider(this.ID_RADIUS, this.guiLeft + 5, this.guiTop + 17 + 22 * 4 + 10, 100, 20, "Radius ", "", this.RADIUS_MIN, this.RADIUS_MAX, this.config.getRadius(), false, true, this);
		this.sldItemRadius = new GuiSlider(this.ID_ITEM_RADIUS, this.guiLeft + 5, this.guiTop + 17 + 22 * 5 + 10, 100, 20, "Item Radius ", "", this.RADIUS_MIN, this.RADIUS_MAX, this.config.getItemRadius(), false, true, this);
		
		this.btnAutoItemRadius = new GuiButton(this.ID_ITEM_RADIUS_AUTOMATIC, this.guiLeft + 5 + 100 + 5, this.guiTop + 17 + 22 * 5 + 10, 52, 20, this.config.isUpdateItemRadiusAutomatic() ? "Automatic" : "Manual");
		this.btnMute = new GuiButton(this.ID_MUTE, this.guiLeft + 5, this.guiTop + 17 + 22 * 7 + 30, 52, 20, this.config.isMuted() ? "Muted" : "Unmuted");
		this.btnDefault = new GuiButton(this.ID_RESET, this.guiLeft + 5 + 52 + 5, this.guiTop + 17 + 22 * 7 + 30, 52, 20, "Reset");
		this.btnSave = new GuiButton(this.ID_SAVE, this.guiLeft + 5 + 52 * 2 + 5 * 2, this.guiTop + 17 + 22 * 7 + 30, 52, 20, "Save");
		
		// Add each component to the GUI
		this.buttonList.add(this.sldHighlightRed);
		this.buttonList.add(this.sldHighlightGreen);
		this.buttonList.add(this.sldHighlightBlue);
		
		this.buttonList.add(this.sldBackgroundRed);
		this.buttonList.add(this.sldBackgroundGreen);
		this.buttonList.add(this.sldBackgroundBlue);
		
		this.buttonList.add(this.sldTriangles);
		this.buttonList.add(this.sldRadius);
		this.buttonList.add(this.sldItemRadius);
		this.buttonList.add(this.btnAutoItemRadius);
		
		this.txtName = new GuiTextField(this.fontRendererObj, this.guiLeft + 5, this.guiTop + 17 + 22 * 6 + 20, 205, 20);
		
		this.buttonList.add(this.btnMute);
		this.buttonList.add(this.btnDefault);
		this.buttonList.add(this.btnSave);
		
		// Text field setup TODO finish text field
		this.txtName.setEnableBackgroundDrawing(true);
		this.txtName.setMaxStringLength(30);
		this.txtName.setText(this.config.DEFAULT_NAME);
		this.txtName.setFocused(false);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		int k = this.guiLeft;
		int l = this.guiTop;
		
		// Draw background
		this.mc.getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		
		this.btnAutoItemRadius.displayString = this.config.isUpdateItemRadiusAutomatic() ? "Automatic" : "Manual";
		this.btnMute.displayString = this.config.isMuted() ? "Muted" : "Unmuted";
		
		this.txtName.drawTextBox();
		
		// Add text labels, coloured to RGB values.
		this.fontRendererObj.drawString("Highlight Colour", this.guiLeft + 17, this.guiTop + 5, new Color(this.config.getHighlightRed(), this.config.getHighlightGreen(), this.config.getHighlightBlue()).getRGB(), false);
		this.fontRendererObj.drawString("Background Colour", this.guiLeft + 5 + 100 + 10, this.guiTop + 5, new Color(this.config.getBackgroundRed(), this.config.getBackgroundGreen(), this.config.getBackgroundBlue()).getRGB(), false);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void onChangeSliderValue(GuiSlider slider)
	{
		if (slider == this.sldBackgroundRed) this.config.setBackgroundRed(slider.getValueInt());
		if (slider == this.sldBackgroundGreen) this.config.setBackgroundGreen(slider.getValueInt());
		if (slider == this.sldBackgroundBlue) this.config.setBackgroundBlue(slider.getValueInt());
		
		if (slider == this.sldHighlightRed) this.config.setHighlightRed(slider.getValueInt());
		if (slider == this.sldHighlightGreen) this.config.setHighlightGreen(slider.getValueInt());
		if (slider == this.sldHighlightBlue) this.config.setHighlightBlue(slider.getValueInt());
		
		if (slider == this.sldTriangles) this.config.setTriangles(slider.getValueInt());
		if (slider == this.sldRadius)
		{
			this.config.setRadius(slider.getValueInt());
			if (this.config.isUpdateItemRadiusAutomatic())
			{
				int value = Math.round(slider.getValueInt() * 0.85f);
				
				this.sldItemRadius.setValue(value);
				this.sldItemRadius.updateSlider();
				
				this.config.setItemRadius(value);
			}
		}
		if (slider == this.sldItemRadius) this.config.setItemRadius(slider.getValueInt());
	}
	
	@Override
	protected void actionPerformed(GuiButton btn)
	{
		if (btn.id == this.ID_ITEM_RADIUS_AUTOMATIC) this.config.setUpdateItemRadiusAutomatic(!this.config.isUpdateItemRadiusAutomatic());
		if (btn.id == this.ID_MUTE) this.config.setMuted(!this.config.isMuted());
		
		if (btn.id == this.ID_RESET) // Reset everything back to the default value
		{
			this.config.setAllValuesToDefault();
			HotbarBag.networkWrapper.sendToServer(new SettingsUpdate()); // We only wan't to write, updating would undo the defaults
			this.updateSliders();
			this.txtName.setText(this.config.DEFAULT_NAME);
		}
		if (btn.id == this.ID_SAVE) this.writeAndUpdate();
	}
	
	/**
	 * Write the current settings to NBT, then update the config values
	 */
	private void writeAndUpdate()
	{
		this.config.setBackgroundRed(this.sldBackgroundRed.getValueInt());
		this.config.setBackgroundGreen(this.sldBackgroundGreen.getValueInt());
		this.config.setBackgroundBlue(this.sldBackgroundBlue.getValueInt());
		
		this.config.setHighlightRed(this.sldHighlightRed.getValueInt());
		this.config.setHighlightGreen(this.sldHighlightGreen.getValueInt());
		this.config.setHighlightBlue(this.sldHighlightBlue.getValueInt());
		
		this.config.setTriangles(this.sldTriangles.getValueInt());
		this.config.setRadius(this.sldRadius.getValueInt());
		this.config.setItemRadius(this.sldItemRadius.getValueInt());
		
		HotbarBag.networkWrapper.sendToServer(new SettingsUpdate());
	}
	
	/**
	 * Set all of the sliders to the config values, then update them on screen
	 */
	private void updateSliders()
	{
		this.sldBackgroundRed.setValue(this.config.getBackgroundRed());
		this.sldBackgroundGreen.setValue(this.config.getBackgroundGreen());
		this.sldBackgroundBlue.setValue(this.config.getBackgroundBlue());
		
		this.sldHighlightRed.setValue(this.config.getHighlightRed());
		this.sldHighlightGreen.setValue(this.config.getHighlightGreen());
		this.sldHighlightBlue.setValue(this.config.getHighlightBlue());
		
		this.sldTriangles.setValue(this.config.getTriangles());
		this.sldRadius.setValue(this.config.getRadius());
		this.sldItemRadius.setValue(this.config.getItemRadius());
		
		this.sldBackgroundRed.updateSlider();
		this.sldBackgroundGreen.updateSlider();
		this.sldBackgroundBlue.updateSlider();
		
		this.sldHighlightRed.updateSlider();
		this.sldHighlightGreen.updateSlider();
		this.sldHighlightBlue.updateSlider();
		
		this.sldTriangles.updateSlider();
		this.sldRadius.updateSlider();
		this.sldItemRadius.updateSlider();
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
}
	