package roboguy99.hotbarBag.gui;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

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
	private ItemStack heldItem;
	
	private static final ResourceLocation texture = new ResourceLocation("roboguy99", "textures/gui/Config.png"); // Background texture
	
	// GUI sizes
	private static final int xSize = 256;
	private static final int ySize = 235;
	
	// GUI positions
	private static int MARGIN_LEFT, MARGIN_TOP, MARGIN_MIDCOL, MARGIN_RIGHTCOL, MARGIN_TOP_SECONDROW;
	private static final int VERTICAL_SPACE = 5;
	
	private static final int SLIDER_WIDTH = 100;
	private static final int SLIDER_HEIGHT = 20;
	
	private static final int BUTTON_WIDTH = 37;
	private static final int BUTTON_HEIGHT = 20;
	
	private static final int TEXTBOX_WIDTH = 20;
	private static final int TEXTBOX_HEIGHT = 205;
	
	// Component IDs
	private static int startId = 0;
	private static final int ID_BACKGROUND_RED = startId;
	private static final int ID_BACKGROUND_GREEN = startId++;
	private static final int ID_BACKGROUND_BLUE = startId++;
	private static final int ID_BACKGROUND_ALPHA = startId++;
	
	private static final int ID_HIGHLIGHT_RED = startId++;
	private static final int ID_HIGHLIGHT_GREEN = startId++;
	private static final int ID_HIGHLIGHT_BLUE = startId++;
	private static final int ID_HIGHLIGHT_ALPHA = startId++;
	
	private static final int ID_BORDER_RED = startId++;
	private static final int ID_BORDER_GREEN = startId++;
	private static final int ID_BORDER_BLUE = startId++;
	private static final int ID_BORDER_ALPHA = startId++;
	
	private static final int ID_MOUSEPOS_RED = startId++;
	private static final int ID_MOUSEPOS_GREEN = startId++;
	private static final int ID_MOUSEPOS_BLUE = startId++;
	private static final int ID_MOUSEPOS_ALPHA = startId++;
	
	private static final int ID_TRIANGLES = startId++;
	private static final int ID_RADIUS = startId++;
	private static final int ID_ITEM_RADIUS = startId++;
	private static final int ID_ITEM_RADIUS_AUTOMATIC = startId++;
	
	private static final int ID_NAME = startId++;
	
	private static final int ID_MUTE = startId++;
	private static final int ID_RESET = startId++;
	private static final int ID_SAVE = startId++;
	
	private static final int ID_NEXT = startId++;
	private static final int ID_PREV = startId++;
	
	// Components
	private GuiSlider sldHighlightRed;
	private GuiSlider sldHighlightGreen;
	private GuiSlider sldHighlightBlue;
	private GuiSlider sldHighlightAlpha;
	
	private GuiSlider sldBackgroundRed;
	private GuiSlider sldBackgroundGreen;
	private GuiSlider sldBackgroundBlue;
	private GuiSlider sldBackgroundAlpha;
	
	private GuiSlider sldBorderRed;
	private GuiSlider sldBorderGreen;
	private GuiSlider sldBorderBlue;
	private GuiSlider sldBorderAlpha;
	
	private GuiSlider sldMouseposRed;
	private GuiSlider sldMouseposGreen;
	private GuiSlider sldMouseposBlue;
	private GuiSlider sldMouseposAlpha;
	
	private GuiSlider sldTriangles;
	private GuiSlider sldRadius;
	private GuiSlider sldItemRadius;
	
	private GuiButton btnShowBorder;
	private GuiButton btnShowMousepos;
	private GuiButton btnAutoItemRadius;
	
	private GuiButton btnMute;
	private GuiButton btnReset;
	private GuiButton btnSave;

	private GuiButton btnNext;
	private GuiButton btnPrev;
	
	private GuiTextField txtName;
	
	//General things
	private int guiLeft, guiTop;
	private int pageNum = 0;
	private static final int MAX_PAGE = 1;
	
	private boolean wasJustReset = true;
	
	@Override
	public void initGui()
	{
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		
		this.MARGIN_LEFT = guiLeft + 5;
		this.MARGIN_TOP = guiTop + 10;
		
		this.MARGIN_MIDCOL = this.MARGIN_LEFT + this.SLIDER_WIDTH + 5;
		this.MARGIN_RIGHTCOL = this.MARGIN_MIDCOL + this.SLIDER_WIDTH + 5;
		this.MARGIN_TOP_SECONDROW = 5*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE);
		
		this.config = HotbarBag.instance.config;
		
		this.heldItem = this.mc.thePlayer.getHeldItem();
		this.inventory = new BagInventory(this.heldItem);
		
		inventory.readSettingsFromNBT(heldItem.getTagCompound());
		
		// Create instances of each component
		//Page 1
		this.sldHighlightRed = new GuiSlider(this.ID_HIGHLIGHT_RED, this.MARGIN_LEFT, this.MARGIN_TOP + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Red ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getHighlightRed(), false, true, this);
		this.sldHighlightGreen = new GuiSlider(this.ID_HIGHLIGHT_GREEN, this.MARGIN_LEFT, this.MARGIN_TOP + this.VERTICAL_SPACE + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Green ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getHighlightGreen(), false, true, this);
		this.sldHighlightBlue = new GuiSlider(this.ID_HIGHLIGHT_BLUE, this.MARGIN_LEFT, this.MARGIN_TOP + this.VERTICAL_SPACE + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Blue ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getHighlightBlue(), false, true, this);
		this.sldHighlightAlpha = new GuiSlider(this.ID_HIGHLIGHT_ALPHA, this.MARGIN_LEFT, this.MARGIN_TOP + this.VERTICAL_SPACE + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Alpha ", "%", config.ALPHA_MIN, config.ALPHA_MAX, this.config.getHighlightAlpha(), false, true, this);
		
		this.sldBackgroundRed = new GuiSlider(this.ID_BACKGROUND_RED, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Red ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getBackgroundRed(), false, true, this);
		this.sldBackgroundGreen = new GuiSlider(this.ID_BACKGROUND_GREEN, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.VERTICAL_SPACE + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Green ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getBackgroundGreen(), false, true, this);
		this.sldBackgroundBlue = new GuiSlider(this.ID_BACKGROUND_BLUE, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.VERTICAL_SPACE + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Blue ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getBackgroundBlue(), false, true, this);
		this.sldBackgroundAlpha = new GuiSlider(this.ID_BACKGROUND_ALPHA, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.VERTICAL_SPACE + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Alpha ", "%", config.ALPHA_MIN, config.ALPHA_MAX, this.config.getBackgroundAlpha(), false, true, this);
		
		this.sldBorderRed = new GuiSlider(this.ID_BORDER_RED, this.MARGIN_LEFT, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Red ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getBorderRed(), false, true, this);
		this.sldBorderGreen = new GuiSlider(this.ID_BORDER_GREEN, this.MARGIN_LEFT, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Green ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getBorderGreen(), false, true, this);
		this.sldBorderBlue = new GuiSlider(this.ID_BORDER_BLUE, this.MARGIN_LEFT, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Blue ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getBorderBlue(), false, true, this);
		this.sldBorderAlpha = new GuiSlider(this.ID_BORDER_ALPHA, this.MARGIN_LEFT, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Alpha ", "%", config.ALPHA_MIN, config.ALPHA_MAX, this.config.getBorderAlpha(), false, true, this);
		
		this.sldMouseposRed = new GuiSlider(this.ID_MOUSEPOS_RED, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Red ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getMouseposRed(), false, true, this);
		this.sldMouseposGreen = new GuiSlider(this.ID_MOUSEPOS_GREEN, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Green ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getMouseposGreen(), false, true, this);
		this.sldMouseposBlue = new GuiSlider(this.ID_MOUSEPOS_BLUE, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Blue ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.config.getMouseposBlue(), false, true, this);
		this.sldMouseposAlpha = new GuiSlider(this.ID_MOUSEPOS_ALPHA, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Alpha ", "%", config.ALPHA_MIN, config.ALPHA_MAX, this.config.getMouseposAlpha(), false, true, this);
		
		//Page 2
		this.sldTriangles = new GuiSlider(this.ID_TRIANGLES, this.MARGIN_LEFT, this.MARGIN_TOP, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Triangles ", "", config.TRIANGLES_MIN, config.TRIANGLES_MAX, this.config.getTriangles(), false, true, this);
		this.sldRadius = new GuiSlider(this.ID_RADIUS, this.MARGIN_LEFT, this.MARGIN_TOP + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Radius ", "", config.RADIUS_MIN, config.RADIUS_MAX, this.config.getRadius(), false, true, this);
		this.sldItemRadius = new GuiSlider(this.ID_ITEM_RADIUS, this.MARGIN_LEFT, this.MARGIN_TOP + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Item Radius ", "", config.RADIUS_MIN, config.RADIUS_MAX, this.config.getItemRadius(), false, true, this);
		
		this.btnAutoItemRadius = new GuiButton(this.ID_ITEM_RADIUS_AUTOMATIC, this.MARGIN_MIDCOL, this.MARGIN_TOP + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.BUTTON_WIDTH+20, this.BUTTON_HEIGHT, "");
		this.btnMute = new GuiButton(this.ID_MUTE, this.MARGIN_LEFT, this.MARGIN_TOP + 4*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.BUTTON_WIDTH+20, this.BUTTON_HEIGHT, "");
		this.btnReset = new GuiButton(this.ID_RESET, this.MARGIN_LEFT + this.BUTTON_WIDTH+20 + 5, this.MARGIN_TOP + 4*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.BUTTON_WIDTH+63, this.BUTTON_HEIGHT, "Reset to defaults");
		
		this.txtName = new GuiTextField(this.fontRendererObj, this.MARGIN_LEFT, this.MARGIN_TOP + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.MARGIN_RIGHTCOL - this.MARGIN_MIDCOL + this.BUTTON_WIDTH+19, this.SLIDER_HEIGHT);
		
		//All pages
		this.btnNext = new GuiButton(this.ID_NEXT, this.MARGIN_RIGHTCOL, this.MARGIN_TOP + this.VERTICAL_SPACE, this.BUTTON_WIDTH, this.BUTTON_HEIGHT, "Next");
		this.btnPrev = new GuiButton(this.ID_PREV, this.MARGIN_RIGHTCOL, this.MARGIN_TOP + 2*this.VERTICAL_SPACE + this.BUTTON_HEIGHT, this.BUTTON_WIDTH, this.BUTTON_HEIGHT, "Prev");
		this.btnSave = new GuiButton(this.ID_SAVE, this.MARGIN_RIGHTCOL, this.MARGIN_TOP + 3*this.VERTICAL_SPACE + 2*this.BUTTON_HEIGHT, this.BUTTON_WIDTH, this.BUTTON_HEIGHT, "Save");
		
		// Add each component to the GUI
		this.buttonList.add(this.sldHighlightRed);
		this.buttonList.add(this.sldHighlightGreen);
		this.buttonList.add(this.sldHighlightBlue);
		this.buttonList.add(this.sldHighlightAlpha);
		
		this.buttonList.add(this.sldBackgroundRed);
		this.buttonList.add(this.sldBackgroundGreen);
		this.buttonList.add(this.sldBackgroundBlue);
		this.buttonList.add(this.sldBackgroundAlpha);
		
		this.buttonList.add(this.sldBorderRed);
		this.buttonList.add(this.sldBorderGreen);
		this.buttonList.add(this.sldBorderBlue);
		this.buttonList.add(this.sldBorderAlpha);
		
		this.buttonList.add(this.sldMouseposRed);
		this.buttonList.add(this.sldMouseposGreen);
		this.buttonList.add(this.sldMouseposBlue);
		this.buttonList.add(this.sldMouseposAlpha);
		
		this.buttonList.add(this.btnNext);
		this.buttonList.add(this.btnPrev);
		this.buttonList.add(this.btnSave);
		
		this.buttonList.add(this.sldTriangles);
		this.buttonList.add(this.sldRadius);
		this.buttonList.add(this.sldItemRadius);
		this.buttonList.add(this.btnAutoItemRadius);
		
		this.buttonList.add(this.btnMute);
		this.buttonList.add(this.btnReset);
		
		// Text field setup TODO finish text field
		Keyboard.enableRepeatEvents(true);
		this.txtName.setTextColor(-1);
		this.txtName.setEnableBackgroundDrawing(true);
		this.txtName.setMaxStringLength(30);
		this.txtName.setText(this.config.getName());
		this.txtName.setFocused(true);
		
		this.loadPage(0); //Always start on the first screen
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
		
		if(this.config.isUpdateItemRadiusAutomatic()) this.sldItemRadius.enabled = false;
		else this.sldItemRadius.enabled = true;
		
		if(this.pageNum == 0)
		{
			// Add text labels, coloured to RGB values.
			this.fontRendererObj.drawString("Highlight Colour", this.MARGIN_LEFT + 12, this.MARGIN_TOP - 5, new Color(this.config.getHighlightRed(), this.config.getHighlightGreen(), this.config.getHighlightBlue()).getRGB(), false);
			this.fontRendererObj.drawString("Background Colour", this.MARGIN_MIDCOL + 4, this.MARGIN_TOP - 5, new Color(this.config.getBackgroundRed(), this.config.getBackgroundGreen(), this.config.getBackgroundBlue()).getRGB(), false);
			
			this.fontRendererObj.drawString("Border Colour", this.MARGIN_LEFT + 14, this.guiTop + this.MARGIN_TOP_SECONDROW, new Color(this.config.getBorderRed(), this.config.getBorderGreen(), this.config.getBorderBlue()).getRGB(), false);
			this.fontRendererObj.drawString("Mouse Pos Colour", this.MARGIN_MIDCOL + 6, this.guiTop + this.MARGIN_TOP_SECONDROW, new Color(this.config.getMouseposRed(), this.config.getMouseposGreen(), this.config.getMouseposBlue()).getRGB(), false);
		}
		if(this.pageNum == 1)
		{
			this.txtName.drawTextBox();
			/*this.fontRendererObj.drawString("More triangles", this.MARGIN_MIDCOL + 6, this.MARGIN_TOP, Color.BLACK.getRGB(), false); //TODO either sort this out or just delete it
			this.fontRendererObj.drawString("=", this.MARGIN_MIDCOL + 40, this.MARGIN_TOP + 6, Color.BLACK.getRGB(), false);
			this.fontRendererObj.drawString("Higher resolution", this.MARGIN_MIDCOL, this.MARGIN_TOP + 12, Color.BLACK.getRGB(), false);*/
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void onChangeSliderValue(GuiSlider slider)
	{
		this.btnSave.enabled = true;
		if(!this.wasJustReset) this.btnReset.enabled = true;
		this.wasJustReset = false;
		
		if (slider == this.sldBackgroundRed) this.config.setBackgroundRed(slider.getValueInt());
		if (slider == this.sldBackgroundGreen) this.config.setBackgroundGreen(slider.getValueInt());
		if (slider == this.sldBackgroundBlue) this.config.setBackgroundBlue(slider.getValueInt());
		if (slider == this.sldBackgroundAlpha) this.config.setBackgroundAlpha(slider.getValueInt());
		
		if (slider == this.sldHighlightRed) this.config.setHighlightRed(slider.getValueInt());
		if (slider == this.sldHighlightGreen) this.config.setHighlightGreen(slider.getValueInt());
		if (slider == this.sldHighlightBlue) this.config.setHighlightBlue(slider.getValueInt());
		if (slider == this.sldHighlightAlpha) this.config.setHighlightAlpha(slider.getValueInt());
		
		if (slider == this.sldBorderRed) this.config.setBorderRed(slider.getValueInt());
		if (slider == this.sldBorderGreen) this.config.setBorderGreen(slider.getValueInt());
		if (slider == this.sldBorderBlue) this.config.setBorderBlue(slider.getValueInt());
		if (slider == this.sldBorderAlpha) this.config.setBorderAlpha(slider.getValueInt());
		
		if (slider == this.sldMouseposRed) this.config.setMouseposRed(slider.getValueInt());
		if (slider == this.sldMouseposGreen) this.config.setMouseposGreen(slider.getValueInt());
		if (slider == this.sldMouseposBlue) this.config.setMouseposBlue(slider.getValueInt());
		if (slider == this.sldMouseposAlpha) this.config.setMouseposAlpha(slider.getValueInt());
		
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
		if(btn.id != this.ID_NEXT && btn.id != this.ID_PREV)
		{
			if(btn != this.btnSave) this.btnSave.enabled = true;
			this.wasJustReset = false;
		}
		
		if (btn == this.btnAutoItemRadius) this.config.setUpdateItemRadiusAutomatic(!this.config.isUpdateItemRadiusAutomatic());
		if (btn == this.btnMute) this.config.setMuted(!this.config.isMuted());
		
		if (btn == this.btnReset) // Reset everything back to the default value
		{
			this.wasJustReset = true;
			this.btnReset.enabled = false;
			
			this.config.setAllValuesToDefault();
			HotbarBag.networkWrapper.sendToServer(new SettingsUpdate()); // We only wan't to write, updating would undo the defaults
			
			this.updateSliders();
			this.txtName.setText(this.config.DEFAULT_NAME);
		}
		if (btn == this.btnSave)
		{
			btn.enabled = false;
			this.writeAndUpdate();
		}
		
		if(btn == this.btnNext && this.pageNum < this.MAX_PAGE) this.loadPage(pageNum + 1);
		if(btn == this.btnPrev && this.pageNum > 0) this.loadPage(pageNum - 1);
	}
	
	/**
	 * Write the current settings to NBT, then update the config values
	 */
	private void writeAndUpdate()
	{
		this.config.setBackgroundRed(this.sldBackgroundRed.getValueInt());
		this.config.setBackgroundGreen(this.sldBackgroundGreen.getValueInt());
		this.config.setBackgroundBlue(this.sldBackgroundBlue.getValueInt());
		this.config.setBackgroundAlpha(this.sldBackgroundAlpha.getValueInt());
		
		this.config.setHighlightRed(this.sldHighlightRed.getValueInt());
		this.config.setHighlightGreen(this.sldHighlightGreen.getValueInt());
		this.config.setHighlightBlue(this.sldHighlightBlue.getValueInt());
		this.config.setHighlightAlpha(this.sldHighlightAlpha.getValueInt());
		
		this.config.setBorderRed(this.sldBorderRed.getValueInt());
		this.config.setBorderGreen(this.sldBorderGreen.getValueInt());
		this.config.setBorderBlue(this.sldBorderBlue.getValueInt());
		this.config.setBorderAlpha(this.sldBorderAlpha.getValueInt());
		
		this.config.setMouseposRed(this.sldMouseposRed.getValueInt());
		this.config.setMouseposGreen(this.sldMouseposGreen.getValueInt());
		this.config.setMouseposBlue(this.sldMouseposBlue.getValueInt());
		this.config.setMouseposAlpha(this.sldMouseposAlpha.getValueInt());
		
		this.config.setTriangles(this.sldTriangles.getValueInt());
		this.config.setRadius(this.sldRadius.getValueInt());
		this.config.setItemRadius(this.sldItemRadius.getValueInt());
		
		this.config.setName(this.txtName.getText());
		
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
		this.sldBackgroundAlpha.setValue(this.config.getBackgroundAlpha());
		
		this.sldHighlightRed.setValue(this.config.getHighlightRed());
		this.sldHighlightGreen.setValue(this.config.getHighlightGreen());
		this.sldHighlightBlue.setValue(this.config.getHighlightBlue());
		this.sldHighlightAlpha.setValue(this.config.getHighlightAlpha());
		
		this.sldBorderRed.setValue(this.config.getBorderRed());
		this.sldBorderGreen.setValue(this.config.getBorderGreen());
		this.sldBorderBlue.setValue(this.config.getBorderBlue());
		this.sldBorderAlpha.setValue(this.config.getBorderAlpha());
		
		this.sldMouseposRed.setValue(this.config.getMouseposRed());
		this.sldMouseposGreen.setValue(this.config.getMouseposGreen());
		this.sldMouseposBlue.setValue(this.config.getMouseposBlue());
		this.sldMouseposAlpha.setValue(this.config.getMouseposAlpha());
		
		this.sldTriangles.setValue(this.config.getTriangles());
		this.sldRadius.setValue(this.config.getRadius());
		this.sldItemRadius.setValue(this.config.getItemRadius());
		
		this.sldBackgroundRed.updateSlider();
		this.sldBackgroundGreen.updateSlider();
		this.sldBackgroundBlue.updateSlider();
		this.sldBackgroundAlpha.updateSlider();
		
		this.sldHighlightRed.updateSlider();
		this.sldHighlightGreen.updateSlider();
		this.sldHighlightBlue.updateSlider();
		this.sldHighlightAlpha.updateSlider();
		
		this.sldBorderRed.updateSlider();
		this.sldBorderGreen.updateSlider();
		this.sldBorderBlue.updateSlider();
		this.sldBorderAlpha.updateSlider();
		
		this.sldMouseposRed.updateSlider();
		this.sldMouseposGreen.updateSlider();
		this.sldMouseposBlue.updateSlider();
		this.sldMouseposAlpha.updateSlider();
		
		this.sldTriangles.updateSlider();
		this.sldRadius.updateSlider();
		this.sldItemRadius.updateSlider();
		
		this.wasJustReset = true;
		this.btnReset.enabled = false;
	}
	
	private void loadPage(int page)
	{
		this.pageNum = page;
		
		if(page == 0)
		{
			this.sldBackgroundRed.visible = true;
			this.sldBackgroundGreen.visible = true;
			this.sldBackgroundBlue.visible = true;
			this.sldBackgroundAlpha.visible = true;
			
			this.sldHighlightRed.visible = true;
			this.sldHighlightGreen.visible = true;
			this.sldHighlightBlue.visible = true;
			this.sldHighlightAlpha.visible = true;
			
			this.sldBorderRed.visible = true;
			this.sldBorderGreen.visible = true;
			this.sldBorderBlue.visible = true;
			this.sldBorderAlpha.visible = true;
			
			this.sldMouseposRed.visible = true;
			this.sldMouseposGreen.visible = true;
			this.sldMouseposBlue.visible = true;
			this.sldMouseposAlpha.visible = true;
			
			this.sldTriangles.visible = false;
			this.sldRadius.visible = false;
			this.sldItemRadius.visible = false;
			
			this.btnAutoItemRadius.visible = false;
			this.btnMute.visible = false;
			this.btnReset.visible = false;
		}
		if(page == 1)
		{
			this.sldBackgroundRed.visible = false;
			this.sldBackgroundGreen.visible = false;
			this.sldBackgroundBlue.visible = false;
			this.sldBackgroundAlpha.visible = false;
			
			this.sldHighlightRed.visible = false;
			this.sldHighlightGreen.visible = false;
			this.sldHighlightBlue.visible = false;
			this.sldHighlightAlpha.visible = false;
			
			this.sldBorderRed.visible = false;
			this.sldBorderGreen.visible = false;
			this.sldBorderBlue.visible = false;
			this.sldBorderAlpha.visible = false;
			
			this.sldMouseposRed.visible = false;
			this.sldMouseposGreen.visible = false;
			this.sldMouseposBlue.visible = false;
			this.sldMouseposAlpha.visible = false;
			
			this.sldTriangles.visible = true;
			this.sldRadius.visible = true;
			this.sldItemRadius.visible = true;
			
			this.btnAutoItemRadius.visible = true;
			this.btnMute.visible = true;
			this.btnReset.visible = true;
		}
		
		if(this.pageNum == this.MAX_PAGE) this.btnNext.enabled = false;
			else this.btnNext.enabled = true;
		if (this.pageNum == 0) this.btnPrev.enabled = false;
			else this.btnPrev.enabled = true;
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	@Override
	protected void keyTyped(char character, int id)
	{
		if(this.txtName.textboxKeyTyped(character, id))
		{
			//Do nothing
		}
		else if (id == 1) //Return game focus
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
	}
	
	@Override
	protected void mouseClicked(int x, int y, int buttonClciked)
	{
		super.mouseClicked(x, y, buttonClciked);
		this.txtName.mouseClicked(x, y, buttonClciked);
	}
	
	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
}
	