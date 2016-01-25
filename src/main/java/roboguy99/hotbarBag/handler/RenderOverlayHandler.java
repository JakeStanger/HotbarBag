package roboguy99.hotbarBag.handler;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.sun.xml.internal.messaging.saaj.soap.MultipartDataContentHandler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import roboguy99.hotbarBag.Config;
import roboguy99.hotbarBag.HotbarBag;
import roboguy99.hotbarBag.inventory.BagInventory;
import roboguy99.hotbarBag.item.ItemBag;
import roboguy99.hotbarBag.network.ClientProxy;

/**
 * Draw the circle and icons. Where the magic happens
 * 
 * @author Roboguy99
 * 		
 */
@SideOnly(Side.CLIENT)
public class RenderOverlayHandler extends Gui
{
	private static final int TEXTURE_HALF_SIZE = 8;
	
	private static final Minecraft minecraft = Minecraft.getMinecraft();
	
	// Special values for custom item rendering code
	private final RenderBlocks renderBlocksRi = new RenderBlocks();
	private final boolean renderWithColor = true;
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	
	private int sectorMouseIsIn = 0;
	private int lastSector = this.sectorMouseIsIn;
	
	private BagInventory inventory;
	
	private boolean isDisplayed;
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	@SideOnly(Side.CLIENT)
	public void onRenderExperienceBar(RenderGameOverlayEvent.Post event)
	{
		Item holding;
		if (minecraft.thePlayer.getHeldItem() != null)
		{
			if (event.type == ElementType.ALL)
			{
				holding = minecraft.thePlayer.getHeldItem().getItem();
				if (holding instanceof ItemBag && ClientProxy.keyHUD.getIsKeyPressed())
				{
					this.inventory = new BagInventory(minecraft.thePlayer.getHeldItem());
					
					int sectors = 0;
					for(int i = 0; i < this.inventory.getSizeInventory(); i++)
					{
						if (this.inventory.getStackInSlot(i) != null) sectors++; // Num of sectors = num of items
					}
					
					if (sectors > 1)
					{
						Config config = HotbarBag.instance.config;
						
						this.isDisplayed = true;
						
						List<ItemStack> inventoryContentsList = new ArrayList<ItemStack>();
						
						for(int i = 0; i < this.inventory.getSizeInventory(); i++)
						{
							if (this.inventory.getStackInSlot(i) != null)
							{
								inventoryContentsList.add(this.inventory.getStackInSlot(i));
							}
						}
						
						ItemStack[] inventoryContents = inventoryContentsList.toArray(new ItemStack[inventoryContentsList.size()]);
						
						float centreX = event.resolution.getScaledWidth() / 2;
						float centreY = event.resolution.getScaledHeight() / 2 - 15;
						
						double multiplier = config.getTriangles() / inventoryContents.length;
						double anglePerSector = 360 / inventoryContents.length;
						
						int mouseX = event.mouseX;
						int mouseY = event.mouseY;
						
						this.sectorMouseIsIn = 0;
						
						double mouseAngle = Math.toDegrees(Math.atan2(mouseY - centreY, mouseX - centreX)); // Move to same direction we render from
						mouseAngle -= 90; // Move 0 from east to south
						if (mouseAngle < 0) mouseAngle += 360; // Compensate for weird negative angles
						mouseAngle = 360 - mouseAngle; // Convert to anticlockwise
						
						for(int i = 0; i < inventoryContents.length; i++)
						{
							double startAngle = 0, endAngle = 1;
							
							if (i != 0)
							{
								startAngle = anglePerSector * i;
								endAngle = anglePerSector * (i + 1);
							}
							if (startAngle < mouseAngle && mouseAngle < endAngle) this.sectorMouseIsIn = i;
						}
						
						if (this.sectorMouseIsIn != this.lastSector)
						{
							minecraft.getSoundHandler().playSound(PositionedSoundRecord.func_147673_a(new ResourceLocation("roboguy99:tick")));
						}
						this.lastSector = this.sectorMouseIsIn;
						
						GL11.glPushMatrix();
						{
							GL11.glDisable(GL11.GL_TEXTURE_2D);
							{
								GL11.glBegin(GL11.GL_TRIANGLE_FAN);
								{
									GL11.glVertex2f(centreX, centreY);
									for(int i = 0; i < inventoryContents.length; i++) // Draw circle
									{
										boolean drawHighlighted = false;
										if (i == this.sectorMouseIsIn) drawHighlighted = true;
										
										for(double j = 0 + i * multiplier; j <= config.getTriangles()  / inventoryContents.length + i * multiplier; j += 1) // Draw circle sector
										{
											GL11.glColor4f(config.getBackgroundRed() / 255F, config.getBackgroundGreen() / 255F, config.getBackgroundBlue() / 255F, 0.6F);
											if (drawHighlighted) GL11.glColor4f(config.getHighlightRed() / 255F, config.getHighlightGreen() / 255F, config.getHighlightBlue() / 255F, 0.8F);
											
											double t = 2 * Math.PI * j / config.getTriangles();
											
											double drawX = centreX + Math.sin(t) * config.getRadius();
											double drawY = centreY + Math.cos(t) * config.getRadius();
											GL11.glVertex2d(drawX, drawY);
										}
									}
								}
								GL11.glEnd();
								
								GL11.glLineWidth(0.5f); 
								GL11.glColor4f(1, 1, 1, 0.66f);   
								GL11.glBegin(GL11.GL_LINE_LOOP);
								{
									
									for (int i=0; i < 360; i++) GL11.glVertex2d(Math.cos(Math.toRadians(i))*config.getRadius() + centreX ,Math.sin(Math.toRadians(i))*config.getRadius() + centreY);

								}
							   GL11.glEnd();
								
								//Draw mouse position line
								GL11.glLineWidth(1f); 
								GL11.glColor4f(0, 0, 0, 0.66f);
								GL11.glBegin(GL11.GL_LINES);
								{
									GL11.glVertex2f(centreX, centreY);
									
									double mouseLineX = config.getRadius()*-Math.cos((2*Math.PI)-Math.toRadians(mouseAngle+90)) + centreX;
									double mouseLineY = config.getRadius()*-Math.sin((2*Math.PI)-Math.toRadians(mouseAngle+90)) + centreY;
									
									GL11.glVertex2d(mouseLineX, mouseLineY);
								}
								GL11.glEnd();
								
							}
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							
							
							ItemStack highlightedItem = inventory.getStackInSlot(sectorMouseIsIn);
							int textHeight = 0;
							for(int i = 0; i < highlightedItem.getTooltip(minecraft.thePlayer, false).size(); i++)
							{
								String itemName = highlightedItem.getTooltip(minecraft.thePlayer, false).get(i).toString();
								if(itemName != null && itemName != "")
								{
									textHeight += 10;
									int textColour = i == 0 ? 0x66ff33 : (i == 1 ? 0xff00ff : 0x0000ff);
									minecraft.fontRenderer.drawString(itemName, 10, textHeight, textColour);
								}
							}
							
							if(highlightedItem.getItem().getCreativeTab() != null) minecraft.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization(highlightedItem.getItem().getCreativeTab().getTranslatedTabLabel()), 10, textHeight + 10, 0xfafafa);
						}
						GL11.glPopMatrix();
						
						double radiansPerSector = 2 * Math.PI / inventoryContents.length;
						double offset = 0 - (Math.PI / 2 + radiansPerSector / 2) + radiansPerSector;
						
						for(int i = 0; i < inventoryContents.length; i++) // Draw icons
						{
							double radians = 0 - (offset + radiansPerSector * i);
							
							double itemX = config.getItemRadius() * Math.cos(radians) + centreX - TEXTURE_HALF_SIZE; // 8 = offset for texture centre
							double itemY = config.getItemRadius() * Math.sin(radians) + centreY - TEXTURE_HALF_SIZE;
							
							this.renderItemIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), inventoryContents[i], itemX, itemY, true);
						}
						
					}
					else if (sectors == 1) // If we only have 1 item...
					{
						this.sectorMouseIsIn = 0; // Just switch it, because there's no point displaying a choice of 1
						this.isDisplayed = false;
					}
					else
						this.isDisplayed = false;
				}
				else
					this.isDisplayed = false;
			}
			else
				this.isDisplayed = false;
		}
		else
			this.isDisplayed = false;
	}
	
	public BagInventory getInventory()
	{
		return new BagInventory(minecraft.thePlayer.getHeldItem());
	}
	
	public int getMouseSector()
	{
		return this.sectorMouseIsIn;
	}
	
	public boolean getIsDisplayed()
	{
		return this.isDisplayed;
	}
	
	/**
	 * Taken from RenderItem and changed to accept doubles rather than integers for more accurate icon placement
	 * 
	 * @param fontRenderer
	 * @param textureManager
	 * @param itemStack
	 * @param x
	 * @param y
	 * @param renderEffect
	 */
	public void renderItemIntoGUI(FontRenderer fontRenderer, TextureManager textureManager, ItemStack itemStack, double x, double y, boolean renderEffect)
	{
		final int k = itemStack.getItemDamage();
		Object object = itemStack.getIconIndex();
		int l;
		float f;
		float f3;
		float f4;
		
		if (itemStack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemStack.getItem()).getRenderType()))
		{
			textureManager.bindTexture(TextureMap.locationBlocksTexture);
			final Block block = Block.getBlockFromItem(itemStack.getItem());
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			if (block.getRenderBlockPass() != 0)
			{
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			}
			else
			{
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
				GL11.glDisable(GL11.GL_BLEND);
			}
			
			GL11.glPushMatrix();
				GL11.glTranslatef((float) (x - 2), (float) (y + 3), -3.0F + this.zLevel);
				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 1.0F);
				GL11.glScalef(1.0F, 1.0F, -1.0F);
				GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
				f3 = (l >> 16 & 255) / 255.0F;
				f4 = (l >> 8 & 255) / 255.0F;
				f = (l & 255) / 255.0F;
				
				if (this.renderWithColor)
				{
					GL11.glColor4f(f3, f4, f, 1.0F);
				}
				
				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				this.renderBlocksRi.useInventoryTint = this.renderWithColor;
				this.renderBlocksRi.renderBlockAsItem(block, k, 1.0F);
				this.renderBlocksRi.useInventoryTint = true;
				
				if (block.getRenderBlockPass() == 0)
				{
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
				}
			
			GL11.glPopMatrix();
		}
		else if (itemStack.getItem().requiresMultipleRenderPasses())
		{
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			textureManager.bindTexture(TextureMap.locationItemsTexture);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(0, 0, 0, 0);
			GL11.glColorMask(false, false, false, true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			final Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(-1);
			tessellator.addVertex(x - 2, y + 18, this.zLevel);
			tessellator.addVertex(x + 18, y + 18, this.zLevel);
			tessellator.addVertex(x + 18, y - 2, this.zLevel);
			tessellator.addVertex(x - 2, y - 2, this.zLevel);
			tessellator.draw();
			GL11.glColorMask(true, true, true, true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			final Item item = itemStack.getItem();
			for(l = 0; l < item.getRenderPasses(k); ++l)
			{
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				textureManager.bindTexture(item.getSpriteNumber() == 0 ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
				final IIcon iicon = item.getIcon(itemStack, l);
				final int i1 = itemStack.getItem().getColorFromItemStack(itemStack, l);
				f = (i1 >> 16 & 255) / 255.0F;
				final float f1 = (i1 >> 8 & 255) / 255.0F;
				final float f2 = (i1 & 255) / 255.0F;
				
				if (this.renderWithColor)
				{
					GL11.glColor4f(f, f1, f2, 1.0F);
				}
				
				GL11.glDisable(GL11.GL_LIGHTING); // Forge: Make sure that
													// render states are reset,
													// ad renderEffect can derp
													// them up.
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				
				this.renderIcon(x, y, iicon, 16, 16);
				
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_LIGHTING);
				
				if (renderEffect && itemStack.hasEffect(l))
				{
					this.renderEffect(textureManager, x, y);
				}
			}
			
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		else
		{
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			final ResourceLocation resourcelocation = textureManager.getResourceLocation(itemStack.getItemSpriteNumber());
			textureManager.bindTexture(resourcelocation);
			
			if (object == null)
			{
				object = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
			}
			
			l = itemStack.getItem().getColorFromItemStack(itemStack, 0);
			f3 = (l >> 16 & 255) / 255.0F;
			f4 = (l >> 8 & 255) / 255.0F;
			f = (l & 255) / 255.0F;
			
			if (this.renderWithColor)
			{
				GL11.glColor4f(f3, f4, f, 1.0F);
			}
			
			GL11.glDisable(GL11.GL_LIGHTING); // Forge: Make sure that render states are reset, a renderEffect can derp them up.
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			
			this.renderIcon(x, y, (IIcon) object, 16, 16);
			
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			
			if (renderEffect && itemStack.hasEffect(0))
			{
				this.renderEffect(textureManager, x, y);
			}
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	/**
	 * Taken from RenderItem and changed to accept doubles rather than integers for more accurate icon placement
	 * 
	 * @param x
	 * @param y
	 * @param icon
	 * @param width
	 * @param height
	 */
	public void renderIcon(double x, double y, IIcon icon, int width, int height)
	{
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, this.zLevel, icon.getMinU(), icon.getMaxV());
		tessellator.addVertexWithUV(x + width, y + height, this.zLevel, icon.getMaxU(), icon.getMaxV());
		tessellator.addVertexWithUV(x + width, y + 0, this.zLevel, icon.getMaxU(), icon.getMinV());
		tessellator.addVertexWithUV(x + 0, y + 0, this.zLevel, icon.getMinU(), icon.getMinV());
		tessellator.draw();
	}
	
	/**
	 * Taken from RenderItem and changed to accept doubles rather than integers for more accurate icon placement
	 * 
	 * @param manager
	 * @param x
	 * @param y
	 */
	public void renderEffect(TextureManager manager, double x, double y)
	{
		GL11.glDepthFunc(GL11.GL_EQUAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		manager.bindTexture(RES_ITEM_GLINT);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
		this.renderGlint(x * 431278612 + y * 32178161, x - 2, y - 2, 20, 20);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	/**
	 * Taken from RenderItem and changed to accept doubles rather than integers for more accurate icon placement
	 * 
	 * @param d
	 * @param e
	 * @param g
	 * @param p_77018_4_
	 * @param p_77018_5_
	 */
	private void renderGlint(double d, double e, double g, int p_77018_4_, int p_77018_5_)
	{
		for(int j1 = 0; j1 < 2; ++j1)
		{
			OpenGlHelper.glBlendFunc(772, 1, 0, 0);
			final float f = 0.00390625F;
			final float f1 = 0.00390625F;
			final float f2 = Minecraft.getSystemTime() % (3000 + j1 * 1873) / (3000.0F + j1 * 1873) * 256.0F;
			final float f3 = 0.0F;
			final Tessellator tessellator = Tessellator.instance;
			float f4 = 4.0F;
			
			if (j1 == 1)
			{
				f4 = -1.0F;
			}
			
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(e + 0, g + p_77018_5_, this.zLevel, (f2 + p_77018_5_ * f4) * f, (f3 + p_77018_5_) * f1);
			tessellator.addVertexWithUV(e + p_77018_4_, g + p_77018_5_, this.zLevel, (f2 + p_77018_4_ + p_77018_5_ * f4) * f, (f3 + p_77018_5_) * f1);
			tessellator.addVertexWithUV(e + p_77018_4_, g + 0, this.zLevel, (f2 + p_77018_4_) * f, (f3 + 0.0F) * f1);
			tessellator.addVertexWithUV(e + 0, g + 0, this.zLevel, (f2 + 0.0F) * f, (f3 + 0.0F) * f1);
			tessellator.draw();
		}
	}
}
