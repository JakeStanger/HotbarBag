package roboguy99.hotbarBag;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Configuration file loading and in-game settings holder.
 * 
 * @author Roboguy99
 *		
 */
public class Config
{
	public static Config instance;
	
	public final int DEFAULT_BACKGROUND_RED, DEFAULT_BACKGROUND_GREEN, DEFAULT_BACKGROUND_BLUE, DEFAULT_BACKGROUND_ALPHA;
	public final int DEFAULT_HIGHLIGHT_RED, DEFAULT_HIGHLIGHT_GREEN, DEFAULT_HIGHLIGHT_BLUE, DEFAULT_HIGHLIGHT_ALPHA;
	public final int DEFAULT_BORDER_RED, DEFAULT_BORDER_GREEN, DEFAULT_BORDER_BLUE, DEFAULT_BORDER_ALPHA;
	public final int DEFAULT_MOUSEPOS_RED, DEFAULT_MOUSEPOS_GREEN, DEFAULT_MOUSEPOS_BLUE, DEFAULT_MOUSEPOS_ALPHA;
	public final int DEFAULT_TRIANGLES, DEFAULT_RADIUS, DEFAULT_ITEM_RADIUS;
	
	public final boolean DEFAULT_MUTE, DEFAULT_UPDATE_ITEMS_AUTO;
	
	public final String DEFAULT_NAME = "Hotbar Bag"; // This does not need to be configured
	
	// Min and max constants
	public static final int COLOUR_MIN = 0;
	public static final int COLOUR_MAX = 255;
	
	public static final int ALPHA_MIN = 0;
	public static final int ALPHA_MAX = 100;
	
	public static final int TRIANGLES_MIN = 10000;
	public static final int TRIANGLES_MAX = 100000;
	
	public static final int RADIUS_MIN = 10;
	public static final int RADIUS_MAX = 120;
	
	private final Configuration config;
	
	public Config(FMLPreInitializationEvent event)
	{
		this.instance = this;
		
		this.config = new Configuration(event.getSuggestedConfigurationFile());
		this.config.load();
			this.DEFAULT_BACKGROUND_RED = this.config.getInt("BackgroundRed", "Background Colours", 127, 0, 255, "RGB red value for selector background");
			this.DEFAULT_BACKGROUND_GREEN = this.config.getInt("BackgroundGreen", "Background Colours", 127, 0, 255, "RGB green value for selector background");
			this.DEFAULT_BACKGROUND_BLUE = this.config.getInt("BackgroundBlue", "Background Colours", 127, 0, 255, "RGB blue value for selector background");
			this.DEFAULT_BACKGROUND_ALPHA = this.config.getInt("BackgroundAlpha", "Background Colours", 60, 0, 100, "Transparency option for selector background (0 = completely transparent)");
			
			this.DEFAULT_HIGHLIGHT_RED = this.config.getInt("HighlightRed", "Highlight Colours", 0, 0, 255, "RGB red value for selector highlighted sector");
			this.DEFAULT_HIGHLIGHT_GREEN = this.config.getInt("HighlightGreen", "Highlight Colours", 102, 0, 255, "RGB green value for selector highlighted sector");
			this.DEFAULT_HIGHLIGHT_BLUE = this.config.getInt("HighlightBlue", "Highlight Colours", 153, 0, 255, "RGB blue value for selector highlighted sector");
			this.DEFAULT_HIGHLIGHT_ALPHA = this.config.getInt("HighlightAlpha", "Highlight Colours", 80, 0, 100, "Transparency option for selector highlighted sector (0 = completely transparent)");
			
			this.DEFAULT_BORDER_RED = this.config.getInt("BorderRed", "Border Colours", 70, 0, 255, "RGB red value for selector border");
			this.DEFAULT_BORDER_GREEN = this.config.getInt("BorderGreen", "Border Colours", 70, 0, 255, "RGB green value for selector border");
			this.DEFAULT_BORDER_BLUE = this.config.getInt("BorderBlue", "Border Colours", 70, 0, 255, "RGB blue value for selector border");
			this.DEFAULT_BORDER_ALPHA = this.config.getInt("BorderAlpha", "Border Colours", 100, 0, 100, "Transparency option for selector border (0 = completely transparent)");
			
			this.DEFAULT_MOUSEPOS_RED = this.config.getInt("MouseposRed", "Mousepos Colours", 222, 0, 255, "RGB red value for selector mouse position indicator");
			this.DEFAULT_MOUSEPOS_GREEN = this.config.getInt("MouseposGreen", "Mousepos Colours", 0, 0, 255, "RGB green value for selector mouse position indicator");
			this.DEFAULT_MOUSEPOS_BLUE = this.config.getInt("MouseposBlue", "Mousepos Colours", 0, 0, 255, "RGB blue value for selector mouse position indicator");
			this.DEFAULT_MOUSEPOS_ALPHA = this.config.getInt("MouseposAlpha", "Mousepos Colours", 80, 0, 100, "Transparency option for selector mouse position indicator (0 = completely transparent)");
			
			this.DEFAULT_TRIANGLES = this.config.getInt("Triangles", "Circle Settings", 20000, 10000, 100000, "Number of triangles used to draw circle. More triangles = higher definition = more lag");
			this.DEFAULT_RADIUS = this.config.getInt("Radius", "Circle Settings", 100, 10, 120, "The radius of the circle");
			this.DEFAULT_ITEM_RADIUS = this.config.getInt("ItemRadius", "Circle Settings", 85, 10, 120, "The radius of the items");
			
			this.DEFAULT_UPDATE_ITEMS_AUTO = this.config.getBoolean("AutoItemRadius", "General", true, "Update item radius automatically when radius is changed. You can still manually change both values.");
			this.DEFAULT_MUTE = this.config.getBoolean("Mute", "General", false, "Mute the sound when moving the mouse between sectors");
		this.config.save();
	}
}
