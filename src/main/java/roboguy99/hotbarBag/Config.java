package roboguy99.hotbarBag;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

/**
 * Configuration file loading and in-game settings holder.
 * 
 * @author Roboguy99
 *		
 */
public class Config
{
	public final int DEFAULT_BACKGROUND_RED, DEFAULT_BACKGROUND_GREEN, DEFAULT_BACKGROUND_BLUE;
	public final int DEFAULT_HIGHLIGHT_RED, DEFAULT_HIGHLIGHT_GREEN, DEFAULT_HIGHLIGHT_BLUE;
	public final int DEFAULT_TRIANGLES, DEFAULT_RADIUS, DEFAULT_ITEM_RADIUS;
	public final boolean DEFAULT_MUTE, DEFAULT_UPDATE_ITEMS_AUTO;
	public final String DEFAULT_NAME = "Hotbar Bag"; // This does not need to be configured
	
	private int backgroundRed, backgroundGreen, backgroundBlue, highlightRed, highlightGreen, highlightBlue, triangles, radius, itemRadius;
	private boolean updateItemRadiusAutomatically, mute;
	private String name;
	
	private final Configuration config;
	
	public Config(FMLPreInitializationEvent event)
	{
		this.config = new Configuration(event.getSuggestedConfigurationFile());
		this.config.load();
		
		this.DEFAULT_BACKGROUND_RED = this.config.getInt("BackgroundRed", "Background Colours", 127, 0, 255, "RGB red value for selector background");
		this.DEFAULT_BACKGROUND_GREEN = this.config.getInt("BackgroundGreen", "Background Colours", 127, 0, 255, "RGB green value for selector background");
		this.DEFAULT_BACKGROUND_BLUE = this.config.getInt("BackgroundBlue", "Background Colours", 127, 0, 255, "RGB blue value for selector background");
		
		this.DEFAULT_HIGHLIGHT_RED = this.config.getInt("HighlightRed", "Highlight Colours", 0, 0, 255, "RGB red value for selector highlighted sector");
		this.DEFAULT_HIGHLIGHT_GREEN = this.config.getInt("HighlightGreen", "Highlight Colours", 102, 0, 255, "RGB green value for selector highlighted sector");
		this.DEFAULT_HIGHLIGHT_BLUE = this.config.getInt("HighlightBlue", "Highlight Colours", 153, 0, 255, "RGB blue value for selector highlighted sector");
		
		this.DEFAULT_TRIANGLES = this.config.getInt("Triangles", "Circle Settings", 20000, 10000, 100000, "Number of triangles used to draw circle. More triangles = higher definition = more lag");
		this.DEFAULT_RADIUS = this.config.getInt("Radius", "Circle Settings", 100, 10, 120, "The radius of the circle");
		this.DEFAULT_ITEM_RADIUS = this.config.getInt("ItemRadius", "Circle Settings", 85, 10, 120, "The radius of the items");
		
		this.DEFAULT_UPDATE_ITEMS_AUTO = this.config.getBoolean("AutoItemRadius", "General", true, "Update item radius automatically when radius is changed. You can still manually change both values.");
		this.DEFAULT_MUTE = this.config.getBoolean("Mute", "General", false, "Mute the sound when moving the mouse between sectors");
	}
	
	public void setAllValuesToDefault()
	{
		this.setBackgroundRed(this.DEFAULT_BACKGROUND_RED);
		this.setBackgroundGreen(this.DEFAULT_BACKGROUND_GREEN);
		this.setBackgroundBlue(this.DEFAULT_BACKGROUND_BLUE);
		
		this.setHighlightRed(this.DEFAULT_HIGHLIGHT_RED);
		this.setHighlightGreen(this.DEFAULT_HIGHLIGHT_GREEN);
		this.setHighlightBlue(this.DEFAULT_HIGHLIGHT_BLUE);
		
		this.setTriangles(this.DEFAULT_TRIANGLES);
		this.setRadius(this.DEFAULT_RADIUS);
		this.setItemRadius(this.DEFAULT_ITEM_RADIUS);
		
		this.setUpdateItemRadiusAutomatic(this.DEFAULT_UPDATE_ITEMS_AUTO);
		this.setMuted(this.DEFAULT_MUTE);
		this.setName(this.DEFAULT_NAME);
	}
	
	/**
	 * @return the backgroundRed
	 */
	public int getBackgroundRed()
	{
		return this.backgroundRed;
	}
	
	/**
	 * @return the backgroundGreen
	 */
	public int getBackgroundGreen()
	{
		return this.backgroundGreen;
	}
	
	/**
	 * @return the backgroundBlue
	 */
	public int getBackgroundBlue()
	{
		return this.backgroundBlue;
	}
	
	/**
	 * @return the highlightRed
	 */
	public int getHighlightRed()
	{
		return this.highlightRed;
	}
	
	/**
	 * @return the highlightGreen
	 */
	public int getHighlightGreen()
	{
		return this.highlightGreen;
	}
	
	/**
	 * @return the highlightBlue
	 */
	public int getHighlightBlue()
	{
		return this.highlightBlue;
	}
	
	/**
	 * @return the triangles
	 */
	public int getTriangles()
	{
		return this.triangles;
	}
	
	/**
	 * @return the radius
	 */
	public int getRadius()
	{
		return this.radius;
	}
	
	/**
	 * @return the itemRadius
	 */
	public int getItemRadius()
	{
		return this.itemRadius;
	}
	
	/**
	 * @return the updateItemRadiusAutomatically
	 */
	public boolean isUpdateItemRadiusAutomatic()
	{
		return this.updateItemRadiusAutomatically;
	}
	
	/**
	 * @return the mute
	 */
	public boolean isMuted()
	{
		return this.mute;
	}
	
	/**
	 * @param backgroundRed the backgroundRed to set
	 */
	public void setBackgroundRed(int backgroundRed)
	{
		this.backgroundRed = backgroundRed;
	}
	
	/**
	 * @param backgroundGreen the backgroundGreen to set
	 */
	public void setBackgroundGreen(int backgroundGreen)
	{
		this.backgroundGreen = backgroundGreen;
	}
	
	/**
	 * @param backgroundBlue the backgroundBlue to set
	 */
	public void setBackgroundBlue(int backgroundBlue)
	{
		this.backgroundBlue = backgroundBlue;
	}
	
	/**
	 * @param highlightRed the highlightRed to set
	 */
	public void setHighlightRed(int highlightRed)
	{
		this.highlightRed = highlightRed;
	}
	
	/**
	 * @param highlightGreen the highlightGreen to set
	 */
	public void setHighlightGreen(int highlightGreen)
	{
		this.highlightGreen = highlightGreen;
	}
	
	/**
	 * @param highlightBlue the highlightBlue to set
	 */
	public void setHighlightBlue(int highlightBlue)
	{
		this.highlightBlue = highlightBlue;
	}
	
	/**
	 * @param triangles the triangles to set
	 */
	public void setTriangles(int triangles)
	{
		this.triangles = triangles;
	}
	
	/**
	 * @param radius the radius to set
	 */
	public void setRadius(int radius)
	{
		this.radius = radius;
	}
	
	/**
	 * @param itemRadius the itemRadius to set
	 */
	public void setItemRadius(int itemRadius)
	{
		this.itemRadius = itemRadius;
	}
	
	/**
	 * @param updateItemRadiusAutomatically the updateItemRadiusAutomatically to set
	 */
	public void setUpdateItemRadiusAutomatic(boolean updateItemRadiusAutomatically)
	{
		this.updateItemRadiusAutomatically = updateItemRadiusAutomatically;
	}
	
	/**
	 * @param mute the mute to set
	 */
	public void setMuted(boolean mute)
	{
		this.mute = mute;
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
