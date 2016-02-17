package roboguy99.hotbarBag.gui;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.config.GuiSlider;
import cpw.mods.fml.client.config.GuiSlider.ISlider;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
	private NBTTagCompound compound;
	
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
	
	//Settings values
	private int backgroundRed;
	private int backgroundGreen;
	private int backgroundBlue;
	private int backgroundAlpha;
	
	private int highlightRed;
	private int highlightGreen;
	private int highlightBlue;
	private int highlightAlpha;
	
	private int borderRed;
	private int borderGreen;
	private int borderBlue;
	private int borderAlpha;
	
	private int mouseposRed;
	private int mouseposGreen;
	private int mouseposBlue;
	private int mouseposAlpha;
	
	private int triangles;
	private int radius;
	private int itemRadius;
	
	private boolean muted;
	private boolean itemRadiusAuto;
	private String name;
	
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
		
		this.config = Config.instance;
		
		this.heldItem = this.mc.thePlayer.getHeldItem();
		this.inventory = new BagInventory(this.heldItem);
		this.compound = this.heldItem.getTagCompound();
		
		this.loadValuesFromNBT();
		
		// Create instances of each component
		//Page 1
		this.sldHighlightRed = new GuiSlider(this.ID_HIGHLIGHT_RED, this.MARGIN_LEFT, this.MARGIN_TOP + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Red ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.highlightRed, false, true, this);
		this.sldHighlightGreen = new GuiSlider(this.ID_HIGHLIGHT_GREEN, this.MARGIN_LEFT, this.MARGIN_TOP + this.VERTICAL_SPACE + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Green ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.highlightGreen, false, true, this);
		this.sldHighlightBlue = new GuiSlider(this.ID_HIGHLIGHT_BLUE, this.MARGIN_LEFT, this.MARGIN_TOP + this.VERTICAL_SPACE + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Blue ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.highlightBlue, false, true, this);
		this.sldHighlightAlpha = new GuiSlider(this.ID_HIGHLIGHT_ALPHA, this.MARGIN_LEFT, this.MARGIN_TOP + this.VERTICAL_SPACE + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Alpha ", "%", config.ALPHA_MIN, config.ALPHA_MAX, this.highlightAlpha, false, true, this);
		
		this.sldBackgroundRed = new GuiSlider(this.ID_BACKGROUND_RED, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Red ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.backgroundRed, false, true, this);
		this.sldBackgroundGreen = new GuiSlider(this.ID_BACKGROUND_GREEN, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.VERTICAL_SPACE + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Green ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.backgroundGreen, false, true, this);
		this.sldBackgroundBlue = new GuiSlider(this.ID_BACKGROUND_BLUE, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.VERTICAL_SPACE + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Blue ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.backgroundBlue, false, true, this);
		this.sldBackgroundAlpha = new GuiSlider(this.ID_BACKGROUND_ALPHA, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.VERTICAL_SPACE + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Alpha ", "%", config.ALPHA_MIN, config.ALPHA_MAX, this.backgroundAlpha, false, true, this);
		
		this.sldBorderRed = new GuiSlider(this.ID_BORDER_RED, this.MARGIN_LEFT, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Red ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.borderRed, false, true, this);
		this.sldBorderGreen = new GuiSlider(this.ID_BORDER_GREEN, this.MARGIN_LEFT, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Green ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.borderGreen, false, true, this);
		this.sldBorderBlue = new GuiSlider(this.ID_BORDER_BLUE, this.MARGIN_LEFT, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Blue ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.borderBlue, false, true, this);
		this.sldBorderAlpha = new GuiSlider(this.ID_BORDER_ALPHA, this.MARGIN_LEFT, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Alpha ", "%", config.ALPHA_MIN, config.ALPHA_MAX, this.borderAlpha, false, true, this);
		
		this.sldMouseposRed = new GuiSlider(this.ID_MOUSEPOS_RED, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Red ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.mouseposRed, false, true, this);
		this.sldMouseposGreen = new GuiSlider(this.ID_MOUSEPOS_GREEN, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Green ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.mouseposGreen, false, true, this);
		this.sldMouseposBlue = new GuiSlider(this.ID_MOUSEPOS_BLUE, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Blue ", "", config.COLOUR_MIN, config.COLOUR_MAX, this.mouseposBlue, false, true, this);
		this.sldMouseposAlpha = new GuiSlider(this.ID_MOUSEPOS_ALPHA, this.MARGIN_MIDCOL, this.MARGIN_TOP + this.MARGIN_TOP_SECONDROW + 3*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Alpha ", "%", config.ALPHA_MIN, config.ALPHA_MAX, this.mouseposAlpha, false, true, this);
		
		//Page 2
		this.sldTriangles = new GuiSlider(this.ID_TRIANGLES, this.MARGIN_LEFT, this.MARGIN_TOP, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Triangles ", "", config.TRIANGLES_MIN, config.TRIANGLES_MAX, this.triangles, false, true, this);
		this.sldRadius = new GuiSlider(this.ID_RADIUS, this.MARGIN_LEFT, this.MARGIN_TOP + this.SLIDER_HEIGHT + this.VERTICAL_SPACE, this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Radius ", "", config.RADIUS_MIN, config.RADIUS_MAX, this.radius, false, true, this);
		this.sldItemRadius = new GuiSlider(this.ID_ITEM_RADIUS, this.MARGIN_LEFT, this.MARGIN_TOP + 2*(this.SLIDER_HEIGHT + this.VERTICAL_SPACE), this.SLIDER_WIDTH, this.SLIDER_HEIGHT, "Item Radius ", "", config.RADIUS_MIN, config.RADIUS_MAX, this.itemRadius, false, true, this);
		
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
		this.txtName.setText(this.name);
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
		
		this.btnAutoItemRadius.displayString = this.itemRadiusAuto ? "Automatic" : "Manual";
		this.btnMute.displayString = this.muted ? "Muted" : "Unmuted";
		
		if(this.itemRadiusAuto) this.sldItemRadius.enabled = false;
		else this.sldItemRadius.enabled = true;
		
		if(this.pageNum == 0)
		{
			// Add text labels, coloured to RGB values.
			this.fontRendererObj.drawString("Highlight Colour", this.MARGIN_LEFT + 12, this.MARGIN_TOP - 5, new Color(this.highlightRed, this.highlightGreen, this.highlightBlue).getRGB(), false);
			this.fontRendererObj.drawString("Background Colour", this.MARGIN_MIDCOL + 4, this.MARGIN_TOP - 5, new Color(this.backgroundRed, this.backgroundGreen, this.backgroundBlue).getRGB(), false);
			
			this.fontRendererObj.drawString("Border Colour", this.MARGIN_LEFT + 14, this.guiTop + this.MARGIN_TOP_SECONDROW, new Color(this.borderRed, this.borderGreen, this.borderBlue).getRGB(), false);
			this.fontRendererObj.drawString("Mouse Pos Colour", this.MARGIN_MIDCOL + 6, this.guiTop + this.MARGIN_TOP_SECONDROW, new Color(this.mouseposRed, this.mouseposGreen, this.mouseposBlue).getRGB(), false);
		}
		if(this.pageNum == 1)
		{
			this.txtName.drawTextBox();
			/*this.fontRendererObj.drawString("More triangles", this.MARGIN_MIDCOL + 6, this.MARGIN_TOP, Color.BLACK.getRGB(), false); //TODO replace this with a tooltip
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
		
		if (slider == this.sldBackgroundRed) this.backgroundRed = this.sldBackgroundRed.getValueInt();
		if (slider == this.sldBackgroundGreen) this.backgroundGreen = this.sldBackgroundGreen.getValueInt();
		if (slider == this.sldBackgroundBlue) this.backgroundBlue = this.sldBackgroundBlue.getValueInt();
		if (slider == this.sldBackgroundAlpha) this.backgroundAlpha = this.sldBackgroundAlpha.getValueInt();
		
		if (slider == this.sldHighlightRed) this.highlightRed = this.sldHighlightRed.getValueInt();
		if (slider == this.sldHighlightGreen) this.highlightGreen = this.sldHighlightGreen.getValueInt();
		if (slider == this.sldHighlightBlue) this.highlightBlue = this.sldHighlightBlue.getValueInt();
		if (slider == this.sldHighlightAlpha) this.highlightAlpha = this.sldHighlightAlpha.getValueInt();
		
		if (slider == this.sldBorderRed) this.borderRed = this.sldBorderRed.getValueInt();
		if (slider == this.sldBorderGreen) this.borderGreen = this.sldBorderGreen.getValueInt();
		if (slider == this.sldBorderBlue) this.borderBlue = this.sldBorderBlue.getValueInt();
		if (slider == this.sldBorderAlpha) this.borderAlpha = this.sldBorderAlpha.getValueInt();
		
		if (slider == this.sldMouseposRed) this.mouseposRed = this.sldMouseposRed.getValueInt();
		if (slider == this.sldMouseposGreen) this.mouseposGreen = this.sldMouseposGreen.getValueInt();
		if (slider == this.sldMouseposBlue) this.mouseposBlue = this.sldMouseposBlue.getValueInt();
		if (slider == this.sldMouseposAlpha) this.mouseposAlpha = this.sldMouseposAlpha.getValueInt();
		
		if (slider == this.sldTriangles) this.triangles = this.sldTriangles.getValueInt();
		if (slider == this.sldRadius)
		{
			this.radius = this.sldRadius.getValueInt();
			if (this.itemRadiusAuto)
			{
				int value = Math.round(slider.getValueInt() * 0.833f); //TODO add ratio slider
				
				this.sldItemRadius.setValue(value);
				this.sldItemRadius.updateSlider();
				
				this.itemRadius = this.sldItemRadius.getValueInt();
			}
		}
		if (slider == this.sldItemRadius) this.itemRadius = this.sldItemRadius.getValueInt();
	}
	
	@Override
	protected void actionPerformed(GuiButton btn)
	{
		if(btn.id != this.ID_NEXT && btn.id != this.ID_PREV)
		{
			if(btn != this.btnSave) this.btnSave.enabled = true;
			this.wasJustReset = false;
		}
		
		if (btn == this.btnAutoItemRadius) this.itemRadiusAuto = !this.itemRadiusAuto;
		if (btn == this.btnMute) this.muted = !this.muted;
		
		if (btn == this.btnReset) // Reset everything back to the default value
		{
			this.wasJustReset = true;
			this.btnReset.enabled = false;
			
			this.setAllValuesToDefault();
			
			this.updateSliders();
			this.txtName.setText(config.DEFAULT_NAME);
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
	 * Write the current settings to NBT, then update the inventory values
	 */
	private void writeAndUpdate()
	{
		Object[][] settingsList = new Object[6][4];
		
		settingsList[0][0] = this.sldBackgroundRed.getValueInt();
		settingsList[0][1] = this.sldBackgroundGreen.getValueInt();
		settingsList[0][2] = this.sldBackgroundBlue.getValueInt();
		settingsList[0][3] = this.sldBackgroundAlpha.getValueInt();
		
		settingsList[1][0] = this.sldHighlightRed.getValueInt();
		settingsList[1][1] = this.sldHighlightGreen.getValueInt();
		settingsList[1][2] = this.sldHighlightBlue.getValueInt();
		settingsList[1][3] = this.sldHighlightAlpha.getValueInt();
		
		settingsList[2][0] = this.sldBorderRed.getValueInt();
		settingsList[2][1] = this.sldBorderGreen.getValueInt();
		settingsList[2][2] = this.sldBorderBlue.getValueInt();
		settingsList[2][3] = this.sldBorderAlpha.getValueInt();
		
		settingsList[3][0] = this.sldMouseposRed.getValueInt();
		settingsList[3][1] = this.sldMouseposGreen.getValueInt();
		settingsList[3][2] = this.sldMouseposBlue.getValueInt();
		settingsList[3][3] = this.sldMouseposAlpha.getValueInt();
		
		settingsList[4][0] = this.sldTriangles.getValueInt();
		settingsList[4][1] = this.sldRadius.getValueInt();
		settingsList[4][2] = this.sldItemRadius.getValueInt();
		
		settingsList[5][0] = this.muted;
		settingsList[5][1] = this.itemRadiusAuto;
		settingsList[5][2] = this.txtName.getText();
		
		HotbarBag.networkWrapper.sendToServer(new SettingsUpdate(settingsList));
	}
	
	public void setAllValuesToDefault()
	{
		Config config = Config.instance;
		
		Object[][] settingsList = new Object[6][4];
		
		settingsList[0][0] = config.DEFAULT_BACKGROUND_RED;
		settingsList[0][1] = config.DEFAULT_BACKGROUND_GREEN;
		settingsList[0][2] = config.DEFAULT_BACKGROUND_BLUE;
		settingsList[0][3] = config.DEFAULT_BACKGROUND_ALPHA;
		
		settingsList[1][0] = config.DEFAULT_HIGHLIGHT_RED;
		settingsList[1][1] = config.DEFAULT_HIGHLIGHT_GREEN;
		settingsList[1][2] = config.DEFAULT_HIGHLIGHT_BLUE;
		settingsList[1][3] = config.DEFAULT_HIGHLIGHT_ALPHA;
		
		settingsList[2][0] = config.DEFAULT_BORDER_RED;
		settingsList[2][1] = config.DEFAULT_BORDER_GREEN;
		settingsList[2][2] = config.DEFAULT_BORDER_BLUE;
		settingsList[2][3] = config.DEFAULT_BORDER_ALPHA;
		
		settingsList[3][0] = config.DEFAULT_MOUSEPOS_RED;
		settingsList[3][1] = config.DEFAULT_MOUSEPOS_GREEN;
		settingsList[3][2] = config.DEFAULT_MOUSEPOS_BLUE;
		settingsList[3][3] = config.DEFAULT_MOUSEPOS_ALPHA;
		
		settingsList[4][0] = config.DEFAULT_TRIANGLES;
		settingsList[4][1] = config.DEFAULT_RADIUS;
		settingsList[4][2] = config.DEFAULT_ITEM_RADIUS;
		
		settingsList[5][0] = config.DEFAULT_MUTE;
		settingsList[5][1] = config.DEFAULT_UPDATE_ITEMS_AUTO;
		settingsList[5][2] = config.DEFAULT_NAME;
		
		//Send packet
		HotbarBag.networkWrapper.sendToServer(new SettingsUpdate(settingsList));
		
		//Update local fields
		this.backgroundRed = config.DEFAULT_BACKGROUND_RED;
		this.backgroundGreen = config.DEFAULT_BACKGROUND_GREEN;
		this.backgroundBlue = config.DEFAULT_BACKGROUND_BLUE;
		this.backgroundAlpha = config.DEFAULT_BACKGROUND_ALPHA;
		
		this.highlightRed = config.DEFAULT_HIGHLIGHT_RED;
		this.highlightGreen = config.DEFAULT_HIGHLIGHT_GREEN;
		this.highlightBlue = config.DEFAULT_HIGHLIGHT_BLUE;
		this.highlightAlpha = config.DEFAULT_HIGHLIGHT_ALPHA;
		
		this.borderRed = config.DEFAULT_BORDER_RED;
		this.borderGreen = config.DEFAULT_BORDER_GREEN;
		this.borderBlue = config.DEFAULT_BORDER_BLUE;
		this.borderAlpha = config.DEFAULT_BORDER_ALPHA;
		
		this.mouseposRed = config.DEFAULT_MOUSEPOS_RED;
		this.mouseposGreen = config.DEFAULT_MOUSEPOS_GREEN;
		this.mouseposBlue = config.DEFAULT_MOUSEPOS_BLUE;
		this.mouseposAlpha = config.DEFAULT_MOUSEPOS_ALPHA;
		
		this.triangles = config.DEFAULT_TRIANGLES;
		this.radius = config.DEFAULT_RADIUS;
		this.itemRadius = config.DEFAULT_ITEM_RADIUS;
		
		this.muted = config.DEFAULT_MUTE;
		this.itemRadiusAuto = config.DEFAULT_UPDATE_ITEMS_AUTO;
		this.name = config.DEFAULT_NAME;
	}
	
	/**
	 * Set all of the sliders to the inventory values, then update them on screen
	 */
	private void updateSliders()
	{	
		//Set slider values
		this.sldBackgroundRed.setValue(this.backgroundRed);
		this.sldBackgroundGreen.setValue(this.backgroundGreen);
		this.sldBackgroundBlue.setValue(this.backgroundBlue);
		this.sldBackgroundAlpha.setValue(this.backgroundAlpha);
		
		this.sldHighlightRed.setValue(this.highlightRed);
		this.sldHighlightGreen.setValue(this.highlightGreen);
		this.sldHighlightBlue.setValue(this.highlightBlue);
		this.sldHighlightAlpha.setValue(this.highlightAlpha);
		
		this.sldBorderRed.setValue(this.borderRed);
		this.sldBorderGreen.setValue(this.borderGreen);
		this.sldBorderBlue.setValue(this.borderBlue);
		this.sldBorderAlpha.setValue(this.borderAlpha);
		
		this.sldMouseposRed.setValue(this.mouseposRed);
		this.sldMouseposGreen.setValue(this.mouseposGreen);
		this.sldMouseposBlue.setValue(this.mouseposBlue);
		this.sldMouseposAlpha.setValue(this.mouseposAlpha);
		
		this.sldTriangles.setValue(this.triangles);
		this.sldRadius.setValue(this.itemRadius);
		this.sldItemRadius.setValue(this.itemRadius);
		
		this.sldBackgroundRed.updateSlider();
		this.sldBackgroundGreen.updateSlider();
		this.sldBackgroundBlue.updateSlider();
		this.sldBackgroundAlpha.updateSlider();
		
		//Update sliders on screen
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
		
		//Change button states
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
	
	/**
	 * Set fields in the class to NBT data
	 */
	private void loadValuesFromNBT()
	{
		this.backgroundRed = this.inventory.getBackgroundRed(this.compound);
		this.backgroundGreen = this.inventory.getBackgroundGreen(this.compound);
		this.backgroundBlue = this.inventory.getBackgroundBlue(this.compound);
		this.backgroundAlpha = this.inventory.getBackgroundAlpha(this.compound);
		
		this.highlightRed = this.inventory.getHighlightRed(this.compound);
		this.highlightGreen = this.inventory.getHighlightGreen(this.compound);
		this.highlightBlue = this.inventory.getHighlightBlue(this.compound);
		this.highlightAlpha = this.inventory.getHighlightAlpha(this.compound);
		
		this.borderRed = this.inventory.getBorderRed(this.compound);
		this.borderGreen = this.inventory.getBorderGreen(this.compound);
		this.borderBlue = this.inventory.getBorderBlue(this.compound);
		this.borderAlpha = this.inventory.getBorderAlpha(this.compound);
		
		this.mouseposRed = this.inventory.getMouseposRed(this.compound);
		this.mouseposGreen = this.inventory.getMouseposGreen(this.compound);
		this.mouseposBlue = this.inventory.getMouseposBlue(this.compound);
		this.mouseposAlpha = this.inventory.getMouseposAlpha(this.compound);
		
		this.triangles = this.inventory.getTriangles(this.compound);
		this.radius = this.inventory.getRadius(this.compound);
		this.itemRadius = this.inventory.getItemRadius(this.compound);
		
		this.muted = inventory.isMuted(this.compound);
		this.itemRadiusAuto = inventory.isUpdateItemRadiusAutomatic(this.compound);
		this.name = inventory.getName(this.compound);
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
	