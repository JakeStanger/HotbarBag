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
	public final int DEFAULT_BACKGROUND_RED, DEFAULT_BACKGROUND_GREEN, DEFAULT_BACKGROUND_BLUE, DEFAULT_BACKGROUND_ALPHA;
	public final int DEFAULT_HIGHLIGHT_RED, DEFAULT_HIGHLIGHT_GREEN, DEFAULT_HIGHLIGHT_BLUE, DEFAULT_HIGHLIGHT_ALPHA;
	public final int DEFAULT_BORDER_RED, DEFAULT_BORDER_GREEN, DEFAULT_BORDER_BLUE, DEFAULT_BORDER_ALPHA;
	public final int DEFAULT_MOUSEPOS_RED, DEFAULT_MOUSEPOS_GREEN, DEFAULT_MOUSEPOS_BLUE, DEFAULT_MOUSEPOS_ALPHA;
	public final int DEFAULT_TRIANGLES, DEFAULT_RADIUS, DEFAULT_ITEM_RADIUS;
	
	public final boolean DEFAULT_MUTE, DEFAULT_UPDATE_ITEMS_AUTO;
	
	public final String DEFAULT_NAME = "Roboguy99's Hotbar Bag #1"; // This does not need to be configured TODO Append player's name & maybe number
	
	private int backgroundRed, backgroundGreen, backgroundBlue, backgroundAlpha;
	private int highlightRed, highlightGreen, highlightBlue, highlightAlpha;
	private int borderRed, borderGreen, borderBlue, borderAlpha;
	private int mouseposRed, mouseposGreen, mouseposBlue, mouseposAlpha;
	private int triangles, radius, itemRadius;
	private boolean showBorder, showMousepos;
	private boolean updateItemRadiusAutomatically, mute;
	private String name = this.DEFAULT_NAME;
	
	// Min and max constants TODO Move to config class
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
			this.DEFAULT_BORDER_ALPHA = this.config.getInt("BorderAlpha", "Border Colours", 100, 0, 10, "Transparency option for selector border (0 = completely transparent)");
			
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
	
	public void setAllValuesToDefault()
	{
		this.setBackgroundRed(this.DEFAULT_BACKGROUND_RED);
		this.setBackgroundGreen(this.DEFAULT_BACKGROUND_GREEN);
		this.setBackgroundBlue(this.DEFAULT_BACKGROUND_BLUE);
		this.setBackgroundAlpha(this.DEFAULT_BACKGROUND_ALPHA);
		
		this.setHighlightRed(this.DEFAULT_HIGHLIGHT_RED);
		this.setHighlightGreen(this.DEFAULT_HIGHLIGHT_GREEN);
		this.setHighlightBlue(this.DEFAULT_HIGHLIGHT_BLUE);
		this.setHighlightAlpha(this.DEFAULT_HIGHLIGHT_ALPHA);
		
		this.setBorderRed(this.DEFAULT_BORDER_RED);
		this.setBorderGreen(this.DEFAULT_BORDER_GREEN);
		this.setBorderBlue(this.DEFAULT_BORDER_BLUE);
		this.setBorderAlpha(this.DEFAULT_BORDER_ALPHA);
		
		this.setMouseposRed(this.DEFAULT_MOUSEPOS_RED);
		this.setMouseposGreen(this.DEFAULT_MOUSEPOS_GREEN);
		this.setMouseposBlue(this.DEFAULT_MOUSEPOS_BLUE);
		this.setMouseposAlpha(this.DEFAULT_MOUSEPOS_ALPHA);
		
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

	/**
	 * @return the backgroundAlpha
	 */
	public int getBackgroundAlpha() {
		return backgroundAlpha;
	}

	/**
	 * @param backgroundAlpha the backgroundAlpha to set
	 */
	public void setBackgroundAlpha(int backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
	}

	/**
	 * @return the highlightAlpha
	 */
	public int getHighlightAlpha() {
		return highlightAlpha;
	}

	/**
	 * @param highlightAlpha the highlightAlpha to set
	 */
	public void setHighlightAlpha(int highlightAlpha) {
		this.highlightAlpha = highlightAlpha;
	}

	/**
	 * @return the borderRed
	 */
	public int getBorderRed() {
		return borderRed;
	}

	/**
	 * @param borderRed the borderRed to set
	 */
	public void setBorderRed(int borderRed) {
		this.borderRed = borderRed;
	}

	/**
	 * @return the borderGreen
	 */
	public int getBorderGreen() {
		return borderGreen;
	}

	/**
	 * @param borderGreen the borderGreen to set
	 */
	public void setBorderGreen(int borderGreen) {
		this.borderGreen = borderGreen;
	}

	/**
	 * @return the borderBlue
	 */
	public int getBorderBlue() {
		return borderBlue;
	}

	/**
	 * @param borderBlue the borderBlue to set
	 */
	public void setBorderBlue(int borderBlue) {
		this.borderBlue = borderBlue;
	}

	/**
	 * @return the borderAlpha
	 */
	public int getBorderAlpha() {
		return borderAlpha;
	}

	/**
	 * @param borderAlpha the borderAlpha to set
	 */
	public void setBorderAlpha(int borderAlpha) {
		this.borderAlpha = borderAlpha;
	}

	/**
	 * @return the mouseposRed
	 */
	public int getMouseposRed() {
		return mouseposRed;
	}

	/**
	 * @param mouseposRed the mouseposRed to set
	 */
	public void setMouseposRed(int mouseposRed) {
		this.mouseposRed = mouseposRed;
	}

	/**
	 * @return the mouseposGreen
	 */
	public int getMouseposGreen() {
		return mouseposGreen;
	}

	/**
	 * @param mouseposGreen the mouseposGreen to set
	 */
	public void setMouseposGreen(int mouseposGreen) {
		this.mouseposGreen = mouseposGreen;
	}

	/**
	 * @return the mouseposBlue
	 */
	public int getMouseposBlue() {
		return mouseposBlue;
	}

	/**
	 * @param mouseposBlue the mouseposBlue to set
	 */
	public void setMouseposBlue(int mouseposBlue) {
		this.mouseposBlue = mouseposBlue;
	}

	/**
	 * @return the mouseposAlpha
	 */
	public int getMouseposAlpha() {
		return mouseposAlpha;
	}

	/**
	 * @param mouseposAlpha the mouseposAlpha to set
	 */
	public void setMouseposAlpha(int mouseposAlpha) {
		this.mouseposAlpha = mouseposAlpha;
	}

	/**
	 * @return the showBorder
	 */
	public boolean showBorder() {
		return showBorder;
	}

	/**
	 * @param showBorder the showBorder to set
	 */
	public void setShowBorder(boolean showBorder) {
		this.showBorder = showBorder;
	}

	/**
	 * @return the showMousepos
	 */
	public boolean showMousepos() {
		return showMousepos;
	}

	/**
	 * @param showMousepos the showMousepos to set
	 */
	public void setShowMousepos(boolean showMousepos) {
		this.showMousepos = showMousepos;
	}
}
