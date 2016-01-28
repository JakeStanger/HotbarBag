package roboguy99.hotbarBag.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import roboguy99.hotbarBag.Config;
import roboguy99.hotbarBag.HotbarBag;

public class SettingsOpen implements IMessage
{
	private int defaultBackgroundRed;
	private int defaultBackgroundGreen;
	private int defaultBackgroundBlue;
	private int defaultBackgroundAlpha;
	
	private int defaultHighlightRed;
	private int defaultHighlightGreen;
	private int defaultHighlightBlue;
	private int defaultHighlightAlpha;
	
	private int defaultBorderRed;
	private int defaultBorderGreen;
	private int defaultBorderBlue;
	private int defaultBorderAlpha;
	
	private int defaultMouseposRed;
	private int defaultMouseposGreen;
	private int defaultMouseposBlue;
	private int defaultMouseposAlpha;
	
	private int defaultTriangles;
	private int defaultRadius;
	private int defaultItemRadius;
	
	private boolean defaultUpdateItemRadiusAutomatically;
	private boolean defaultMuted;
	
	private String defaultName;
	
	public SettingsOpen()
	{
		Config config = HotbarBag.instance.config; //TODO add getConfig() and change all references to use it
		
		this.defaultBackgroundRed = config.DEFAULT_BACKGROUND_RED;
		this.defaultBackgroundGreen = config.DEFAULT_BACKGROUND_GREEN;
		this.defaultBackgroundBlue = config.DEFAULT_BACKGROUND_BLUE;
		this.defaultBackgroundAlpha = config.DEFAULT_BACKGROUND_ALPHA;
		
		this.defaultHighlightRed = config.DEFAULT_HIGHLIGHT_RED;
		this.defaultHighlightGreen = config.DEFAULT_HIGHLIGHT_GREEN;
		this.defaultHighlightBlue = config.DEFAULT_HIGHLIGHT_BLUE;
		this.defaultHighlightAlpha = config.DEFAULT_HIGHLIGHT_ALPHA;
		
		this.defaultBorderRed = config.DEFAULT_BORDER_RED;
		this.defaultBorderGreen = config.DEFAULT_BORDER_GREEN;
		this.defaultBorderBlue = config.DEFAULT_BORDER_BLUE;
		this.defaultBorderAlpha = config.DEFAULT_BORDER_ALPHA;
		
		this.defaultMouseposRed = config.DEFAULT_MOUSEPOS_RED;
		this.defaultMouseposGreen = config.DEFAULT_MOUSEPOS_GREEN;
		this.defaultMouseposBlue = config.DEFAULT_MOUSEPOS_BLUE;
		this.defaultMouseposAlpha = config.DEFAULT_MOUSEPOS_ALPHA;
		
		this.defaultTriangles = config.DEFAULT_TRIANGLES;
		this.defaultRadius = config.DEFAULT_RADIUS;
		this.defaultItemRadius = config.DEFAULT_ITEM_RADIUS;
		
		this.defaultUpdateItemRadiusAutomatically = config.DEFAULT_UPDATE_ITEMS_AUTO;
		this.defaultMuted = config.DEFAULT_MUTE;
		
		this.defaultName = config.DEFAULT_NAME;
	};
	
	/*public SettingsOpen(int[] background, int[] highlight, int[] border, int[] mousepos)
	{
		this.defaultBackgroundRed = background[0];
		this.defaultBackgroundGreen = background[1];
		this.defaultBackgroundBlue = background[2];
		this.defaultBackgroundAlpha = background[3];
		
		this.defaultHighlightRed = highlight[0];
		this.defaultHighlightGreen = highlight[1];
		this.defaultHighlightBlue = highlight[2];
		this.defaultHighlightAlpha = highlight[3];
		
		this.defaultBorderRed = border[0];
		this.defaultBorderGreen = border[1];
		this.defaultBorderBlue = border[2];
		this.defaultBorderAlpha = border[3];
		
		this.defaultMouseposRed = mousepos[0];
		this.defaultMouseposGreen = mousepos[1];
		this.defaultMouseposBlue = mousepos[2];
		this.defaultMouseposAlpha = mousepos[3];
	}*/
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.defaultBackgroundRed = ByteBufUtils.readVarInt(buf, 8);
		this.defaultBackgroundGreen = ByteBufUtils.readVarInt(buf, 8);
		this.defaultBackgroundBlue = ByteBufUtils.readVarInt(buf, 8);
		this.defaultBackgroundAlpha = ByteBufUtils.readVarInt(buf, 8);
		
		this.defaultHighlightRed = ByteBufUtils.readVarInt(buf, 8);
		this.defaultHighlightGreen = ByteBufUtils.readVarInt(buf, 8);
		this.defaultHighlightBlue = ByteBufUtils.readVarInt(buf, 8);
		this.defaultHighlightAlpha = ByteBufUtils.readVarInt(buf, 8);
		
		this.defaultBorderRed = ByteBufUtils.readVarInt(buf, 8);
		this.defaultBorderGreen = ByteBufUtils.readVarInt(buf, 8);
		this.defaultBorderBlue = ByteBufUtils.readVarInt(buf, 8);
		this.defaultBorderAlpha = ByteBufUtils.readVarInt(buf, 8);
		
		this.defaultMouseposRed = ByteBufUtils.readVarInt(buf, 8);
		this.defaultMouseposGreen = ByteBufUtils.readVarInt(buf, 8);
		this.defaultMouseposBlue = ByteBufUtils.readVarInt(buf, 8);
		this.defaultMouseposAlpha = ByteBufUtils.readVarInt(buf, 8);
		
		this.defaultTriangles = ByteBufUtils.readVarInt(buf, 17);
		this.defaultRadius = ByteBufUtils.readVarInt(buf, 7);
		this.defaultItemRadius = ByteBufUtils.readVarInt(buf, 7);
		
		this.defaultUpdateItemRadiusAutomatically = ByteBufUtils.readVarInt(buf, 1) == 1;
		this.defaultMuted = ByteBufUtils.readVarInt(buf, 1) == 1;
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeVarInt(buf, defaultBackgroundRed, 8);
		ByteBufUtils.writeVarInt(buf, defaultBackgroundGreen, 8);
		ByteBufUtils.writeVarInt(buf, defaultBackgroundBlue, 8);
		ByteBufUtils.writeVarInt(buf, defaultBackgroundAlpha, 8);
		
		ByteBufUtils.writeVarInt(buf, defaultHighlightRed, 8);
		ByteBufUtils.writeVarInt(buf, defaultHighlightGreen, 8);
		ByteBufUtils.writeVarInt(buf, defaultHighlightBlue, 8);
		ByteBufUtils.writeVarInt(buf, defaultHighlightAlpha, 8);
		
		ByteBufUtils.writeVarInt(buf, defaultBorderRed, 8);
		ByteBufUtils.writeVarInt(buf, defaultBorderGreen, 8);
		ByteBufUtils.writeVarInt(buf, defaultBorderBlue, 8);
		ByteBufUtils.writeVarInt(buf, defaultBorderAlpha, 8);
		
		ByteBufUtils.writeVarInt(buf, defaultMouseposRed, 8);
		ByteBufUtils.writeVarInt(buf, defaultMouseposGreen, 8);
		ByteBufUtils.writeVarInt(buf, defaultMouseposBlue, 8);
		ByteBufUtils.writeVarInt(buf, defaultMouseposAlpha, 8);
		
		ByteBufUtils.writeVarInt(buf, defaultTriangles, 17);
		ByteBufUtils.writeVarInt(buf, defaultRadius, 7);
		ByteBufUtils.writeVarInt(buf, defaultItemRadius, 7);
		
		ByteBufUtils.writeVarInt(buf, defaultUpdateItemRadiusAutomatically ? 1 : 0, 1);
		ByteBufUtils.writeVarInt(buf, defaultMuted ? 1 : 0, 1);
	}

	public static class SettingsOpenHandle implements IMessageHandler<SettingsOpen, IMessage>
	{
		@Override
		public IMessage onMessage(SettingsOpen message, MessageContext ctx) 
		{	
			ctx.getServerHandler().playerEntity.openGui(HotbarBag.instance, HotbarBag.GUI_CONFIG, Minecraft.getMinecraft().theWorld, 0, 0, 0);
			return null;
		}
	}
}
