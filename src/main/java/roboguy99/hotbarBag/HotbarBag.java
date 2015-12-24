package roboguy99.hotbarBag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import roboguy99.hotbarBag.handler.ClientTickHandler;
import roboguy99.hotbarBag.handler.RenderOverlayHandler;
import roboguy99.hotbarBag.handler.RenderTickHandler;
import roboguy99.hotbarBag.item.ItemBag;
import roboguy99.hotbarBag.network.CommonProxy;
import roboguy99.hotbarBag.network.packet.InventoryUpdate;
import roboguy99.hotbarBag.network.packet.InventoryUpdate.Handle;

/**
 * Main class. Handles mod initialisation.
 * 
 * @author Roboguy99
 *		
 */
@Mod(modid = HotbarBag.modID, version = HotbarBag.modVersion, name = HotbarBag.name)
public class HotbarBag
{
	// Mod data. Fallback if mc-mod.info fails to load.
	public static final String modID = "HotbarBag";
	public static final String modVersion = "0.9.0";
	public static final String name = "Hotbar Bag";
	
	public static final Logger logger = LogManager.getLogger("Hotbar Bag");
	
	public static KeyBinding keyHUD;
	
	@SidedProxy(clientSide = "roboguy99.hotbarBag.network.ClientProxy", serverSide = "roboguy99.hotbarBag.network.CommonProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper networkWrapper;
	
	/**
	 * Used to keep track of each GUI. May not actually be needed.
	 */
	private static int modGuiIndex = 0;
	
	public static final int GUI_INVENTORY = modGuiIndex++;
	public static final int GUI_CONFIG = modGuiIndex++;
	
	public static HotbarBag instance;
	
	private RenderOverlayHandler renderOverlayEventHandler;
	
	public Config config;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) // Pre-initialisation loading
	{
		logger.info("Pre-initialising");
		this.instance = this;
		
		proxy.registerProxies();
		
		this.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("hotbarBag_inv");
		this.networkWrapper.registerMessage(Handle.class, InventoryUpdate.class, 0, Side.SERVER);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new CommonProxy());
		
		logger.info("Loading config");
		this.config = new Config(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) // Initialisation loading
	{
		logger.info("Initialising");
		
		new ItemBag();
		
		this.keyHUD = new KeyBinding("key.bagHUD", Keyboard.KEY_F, "key.categories.inventory");
		
		ClientRegistry.registerKeyBinding(keyHUD);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		this.renderOverlayEventHandler = new RenderOverlayHandler();
		
		FMLCommonHandler.instance().bus().register(new ClientTickHandler(this.renderOverlayEventHandler));
		FMLCommonHandler.instance().bus().register(new RenderTickHandler(this.renderOverlayEventHandler));
		
		MinecraftForge.EVENT_BUS.register(this.renderOverlayEventHandler);
	}
}
