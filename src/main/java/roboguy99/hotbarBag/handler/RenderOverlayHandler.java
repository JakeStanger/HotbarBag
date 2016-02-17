package roboguy99.hotbarBag.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

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
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import roboguy99.hotbarBag.Config;
import roboguy99.hotbarBag.inventory.BagInventory;
import roboguy99.hotbarBag.item.ItemBag;
import roboguy99.hotbarBag.network.ClientProxy;

/**
 * Draw the circle and icons. Where the magic happens.
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
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("roboguy99", "textures/misc/enchanted_item_glint.png");
	
	private int sectorMouseIsIn = 0;
	private int lastSector = this.sectorMouseIsIn;
	
	private Config config;
	private BagInventory inventory;
	private NBTTagCompound compound;
	
	private boolean isDisplayed, isFirstTick;
	
	private int backgroundRed, backgroundGreen, backgroundBlue, backgroundAlpha;
	private int highlightRed, highlightGreen, highlightBlue, highlightAlpha;
	private int borderRed, borderGreen, borderBlue, borderAlpha;
	private int mouseposRed, mouseposGreen, mouseposBlue, mouseposAlpha;
	private int triangles, radius, itemRadius;
	private boolean muted;
	
	private ItemStack[] inventoryContents;
	
	private float centreX, centreY;
	private float multiplier, anglePerSector;
	
	private float animRadius, animItemRadius;
	
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
					this.compound = minecraft.thePlayer.getHeldItem().getTagCompound();
					
					int sectors = 0;
					for(int i = 0; i < this.inventory.getSizeInventory(); i++)
					{
						if (this.inventory.getStackInSlot(i) != null) sectors++; // Num of sectors = num of items
					}
					
					if (sectors > 1)
					{	
						if(this.isFirstTick) //TODO add some nullPointerException something or other just because
						{
							backgroundRed = this.inventory.getBackgroundRed(this.compound);
							backgroundGreen = this.inventory.getBackgroundGreen(this.compound);
							backgroundBlue = this.inventory.getBackgroundBlue(this.compound);
							backgroundAlpha = this.inventory.getBackgroundAlpha(this.compound);
							
							highlightRed = this.inventory.getHighlightRed(this.compound);
							highlightGreen = this.inventory.getHighlightGreen(this.compound);
							highlightBlue = this.inventory.getHighlightBlue(this.compound);
							highlightAlpha = this.inventory.getHighlightAlpha(this.compound);
							
							borderRed = this.inventory.getBorderRed(this.compound);
							borderGreen = this.inventory.getBorderGreen(this.compound);
							borderBlue = this.inventory.getBorderBlue(this.compound);
							borderAlpha = this.inventory.getBorderAlpha(this.compound);
							
							mouseposRed = this.inventory.getMouseposRed(this.compound);
							mouseposGreen = this.inventory.getMouseposGreen(this.compound);
							mouseposBlue = this.inventory.getMouseposBlue(this.compound);
							mouseposAlpha = this.inventory.getMouseposAlpha(this.compound);
							
							triangles = this.inventory.getTriangles(this.compound);
							radius = this.inventory.getRadius(this.compound);
							itemRadius = this.inventory.getItemRadius(this.compound);
							
							muted = this.inventory.isMuted(this.compound);
							
							config = Config.instance;
							
							this.animRadius = 0;
							this.animItemRadius = 0;
							
							List<ItemStack> inventoryContentsList = new ArrayList<ItemStack>();
							
							for(int i = 0; i < this.inventory.getSizeInventory(); i++)
							{
								if (this.inventory.getStackInSlot(i) != null)
								{
									inventoryContentsList.add(this.inventory.getStackInSlot(i));
								}
							}
							
							inventoryContents = inventoryContentsList.toArray(new ItemStack[inventoryContentsList.size()]);
							
							centreX = event.resolution.getScaledWidth() / 2;
							centreY = event.resolution.getScaledHeight() / 2 - 15;
							
							multiplier = triangles / inventoryContents.length;
							anglePerSector = 360 / inventoryContents.length;
						}
						
						this.isFirstTick = false;
						this.isDisplayed = true;
						
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
						
						if (this.sectorMouseIsIn != this.lastSector && !muted)
						{
							PositionedSoundRecord sound = PositionedSoundRecord.func_147673_a(new ResourceLocation("roboguy99:tick"));
							minecraft.getSoundHandler().stopSound(sound); //TODO stop sound stacking
							minecraft.getSoundHandler().playSound(sound);
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
										
										boolean renderEffect = inventoryContents[i].hasEffect(0);
										
										for(double j = 0 + i * multiplier; j <= triangles  / inventoryContents.length + i * multiplier; j += 1) // Draw circle sector
										{
											if(!renderEffect) //Solid colour
											{
												GL11.glColor4f(backgroundRed / 255F, backgroundGreen / 255F, backgroundBlue / 255F, backgroundAlpha / 100F);
												if (drawHighlighted) GL11.glColor4f(highlightRed / 255F, highlightGreen / 255F, highlightBlue / 255F, highlightAlpha / 100F);
											}
											else //Several colours for enchanted item
											{
												Random random = new Random();
												GL11.glColor4f(backgroundRed / 255F + (random.nextBoolean() ? 0.0F : -0.1F), backgroundGreen / 255F + ((random.nextBoolean() ? 0.1F : -0.1F)), backgroundBlue / 255F + (random.nextBoolean() ? 0.1F : -0.1F), backgroundAlpha / 100F);
												if (drawHighlighted) GL11.glColor4f(highlightRed / 255F + (random.nextBoolean() ? 0.1F : -0.1F), highlightGreen / 255F + ((random.nextBoolean() ? 0.1F : -0.1F)), highlightBlue / 255F + (random.nextBoolean() ? 0.1F : -0.1F), highlightAlpha / 100F);
											}
											
											double t = 2 * Math.PI * j / triangles;
											
											double drawX = centreX + Math.sin(t) * animRadius;
											double drawY = centreY + Math.cos(t) * animRadius;
											GL11.glVertex2d(drawX, drawY);
										}
									}
								}
								GL11.glEnd();
								
								//Draw border
								GL11.glLineWidth(2f); 
								GL11.glColor4f(borderRed / 255F, borderGreen / 255F, borderBlue / 255F, borderAlpha / 100F);   
								GL11.glBegin(GL11.GL_LINE_LOOP);
								{
									for (int i=0; i < 360; i++) GL11.glVertex2d(Math.cos(Math.toRadians(i))*animRadius + centreX, Math.sin(Math.toRadians(i))*animRadius+ centreY);
								}
							    GL11.glEnd();
								
								//Draw mouse position line 
								GL11.glLineWidth(1f); 
								GL11.glColor4f(mouseposRed / 255F, mouseposGreen / 255F, mouseposBlue / 255F, mouseposAlpha / 100F);
								GL11.glBegin(GL11.GL_LINES);
								{
									GL11.glVertex2f(centreX, centreY);
									
									double mouseLineX = animRadius*-Math.cos((2*Math.PI)-Math.toRadians(mouseAngle+90)) + centreX;
									double mouseLineY = animRadius*-Math.sin((2*Math.PI)-Math.toRadians(mouseAngle+90)) + centreY;
									
									GL11.glVertex2d(mouseLineX, mouseLineY);
								}
								GL11.glEnd();
								
							}
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							
							
							//Draw item info
							ItemStack highlightedItem = inventory.getStackInSlot(sectorMouseIsIn);
							int stackSize = highlightedItem.stackSize;
							int textHeight = 10;
							for(int i = 0; i < highlightedItem.getTooltip(minecraft.thePlayer, false).size(); i++)
							{
								String itemName = highlightedItem.getTooltip(minecraft.thePlayer, false).get(i).toString();
								
								if(itemName != null && itemName != "")
								{
									int textColour = i == 0 ? 0x66ff33 : (i == 1 ? 0xff00ff : 0x0000ff);
									minecraft.fontRenderer.drawString(itemName, 10, textHeight, textColour);
									textHeight += 10;
								}
							}
							if(highlightedItem.getItem().getCreativeTab() != null)
							{
								minecraft.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization(highlightedItem.getItem().getCreativeTab().getTranslatedTabLabel()), 10, textHeight, 0xfafafa);
								textHeight += 10;
							}
							minecraft.fontRenderer.drawString("Stack size: " + stackSize, 10, textHeight, 0x0066ff);
						}
						
						//Draw item icon
						GL11.glDisable(GL11.GL_LIGHTING);
						double radiansPerSector = 2 * Math.PI / inventoryContents.length;
						double offset = 0 - (Math.PI / 2 + radiansPerSector / 2) + radiansPerSector;
						
						for(int i = 0; i < inventoryContents.length; i++) // Draw icons
						{
							double radians = 0 - (offset + radiansPerSector * i);
							
							double itemX = this.animItemRadius * Math.cos(radians) + centreX - TEXTURE_HALF_SIZE;
							double itemY = this.animItemRadius * Math.sin(radians) + centreY - TEXTURE_HALF_SIZE;
							
							this.renderItemIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), inventoryContents[i], itemX, itemY);
						}
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glPopMatrix();
						
						if(this.animRadius < this.radius) this.animRadius += 5;
						if(this.animItemRadius < this.itemRadius) this.animItemRadius += 5*0.833f; //TODO Add option to change animation speed
					}
					else if (sectors == 1) // If we only have 1 item...
					{
						this.sectorMouseIsIn = 0; // Just switch it, because there's no point displaying a choice of 1
						this.isDisplayed = false;
						this.isFirstTick = true;
					}
					else
					{
						this.isDisplayed = false;
						this.isFirstTick = true;
					}
				}
				else
				{
					this.isDisplayed = false;
					this.isFirstTick = true;
				}
			}
			else
			{
				this.isDisplayed = false;
			}
		}
		else
		{
			this.isDisplayed = false;
			this.isFirstTick = true;
		}
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
	public void renderItemIntoGUI(FontRenderer fontRenderer, TextureManager textureManager, ItemStack itemStack, double x, double y)
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
}
