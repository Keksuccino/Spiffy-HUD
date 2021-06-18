package de.keksuccino.fancyhud;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;

import de.keksuccino.fancyhud.api.DynamicValueRegistry;
import de.keksuccino.fancyhud.api.IngameHud;
import de.keksuccino.fancyhud.api.hud.HudElementRegistry;
import de.keksuccino.fancyhud.api.item.CustomizationItemRegistry;
import de.keksuccino.fancyhud.customization.CustomizationHandler;
import de.keksuccino.fancyhud.customization.dynamicvalues.DynamicValueHelper;
import de.keksuccino.fancyhud.customization.helper.CustomizationHelper;
import de.keksuccino.fancyhud.customization.rendering.slideshow.SlideshowHandler;
import de.keksuccino.fancyhud.keybinding.Keybinding;
import de.keksuccino.fancyhud.logger.Logging;
import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.config.Config;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;


@Mod("fancyhud")
public class FancyHud {
	
	public static final String VERSION = "1.2.1";
	
	public static final File HOME_DIR = new File("config/fancyhud");
	public static final File CUSTOMIZATION_DIR = new File(HOME_DIR.getPath() + "/customization");
	public static final File SLIDESHOW_DIR = new File(HOME_DIR.getPath() + "/slideshows");
	
	public static Config config;
	
	private static boolean fancymenuLoaded = false;
	private static boolean optifineLoaded = false;

	public FancyHud() {
		
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		if (FMLEnvironment.dist == Dist.CLIENT) {

			if (!HOME_DIR.exists()) {
				HOME_DIR.mkdirs();
			}
			if (!CUSTOMIZATION_DIR.exists()) {
				CUSTOMIZATION_DIR.mkdirs();
			}
			if (!SLIDESHOW_DIR.exists()) {
				SLIDESHOW_DIR.mkdirs();
			}

			if (ModList.get().isLoaded("fancymenu")) {
				System.out.println("[FANCYHUD] FancyMenu found!");
				fancymenuLoaded = true;
			}
			
			updateConfig();
			
			Logging.init();
			
			SlideshowHandler.init();

			CustomizationHandler.init();
			
			CustomizationHelper.init();
			
			if (config.getOrDefault("enablekeybinds", true)) {
        		Keybinding.init();
        	}
			
			Konkrete.addPostLoadingEvent("fancyhud", this::onClientSetup);
			
//			ExampleCustomDynamicVariables.registerDynamicValues();

			//TODO remove debug
//			MinecraftForge.EVENT_BUS.register(new Test());

		} else {
			System.out.println("## WARNING ## 'FancyHud' is a client mod and has no effect when loaded on a server!");
		}
		
	}
	
	private void onClientSetup() {
		try {

			initLocals();

        	try {
                Class.forName("optifine.Installer");
                optifineLoaded = true;
                System.out.println("[FANCYHUD] Optifine found!");
            }
            catch (ClassNotFoundException e) {}
        	
        	DynamicValueHelper.init();
        	
//        	HudElementRegistry.getInstance().register(new ExampleCustomVanillaElementContainer());
        	
//        	CustomizationItemRegistry.getInstance().register(new ExampleCustomizationItemContainer());
	    	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static boolean isFancyMenuLoaded() {
		return fancymenuLoaded;
	}
	
	public static boolean isOptifineLoaded() {
		return optifineLoaded;
	}
	
	public static void updateConfig() {
		
		try {
			
			config = new Config(HOME_DIR.getPath() + "/config.cfg");
			
			//---------------------
			
			config.registerValue("printwarnings", true, "logging");
			
			config.registerValue("editordeleteconfirmation", true, "layouteditor");
			
			config.registerValue("showcustomizationcontrols", true, "customization");
			config.registerValue("enablekeybinds", true, "customization");
			
			config.registerValue("uiscale", 1.0F, "ui");
			
			//---------------------
			
			config.syncConfig();
			
			//---------------------
			
			config.setCategory("printwarnings", "logging");
			
			config.setCategory("editordeleteconfirmation", "layouteditor");
			
			config.setCategory("showcustomizationcontrols", "customization");
			config.setCategory("enablekeybinds", "customization");
			
			config.setCategory("uiscale", "ui");
			
			//---------------------
			
			config.clearUnusedValues();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void initLocals() {
		String baseresdir = "locals/";
		File f = new File("config/fancyhud/locals");
		if (!f.exists()) {
			f.mkdirs();
		}
		
		Locals.copyLocalsFileToDir(new ResourceLocation("fancyhud", baseresdir + "en_us.local"), "en_us", f.getPath());
//		Locals.copyLocalsFileToDir(new ResourceLocation("fancyhud", baseresdir + "de_de.local"), "de_de", f.getPath());
		
		Locals.getLocalsFromDir(f.getPath());
	}
	
	/**
	 * Gets the {@link IngameHud} instance from which you can get positions and sizes of all vanilla HUD elements and more.<br>
	 * Useful for modders who want to override vanilla elements while keeping the ability to customize the element via FancyHud.
	 */
	public static IngameHud getIngameHud() {
		return IngameHud.getInstance();
	}
	
	/**
	 * Gets the {@link DynamicValueRegistry} instance that allows you to register your own dynamic (aka. session-specific values) to the mod.<br>
	 * These can be used in many text-based HUD elements and items.
	 */
	public static DynamicValueRegistry getDynamicValueRegistry() {
		return DynamicValueRegistry.getInstance();
	}
	
	/**
	 * Gets the {@link HudElementRegistry} instance that allows you to register your own <b>vanilla-like</b> elements to the HUD.<br>
	 * Vanilla-like elements act like normal vanilla HUD elements. They are part of the HUD by default and can be customized using the editor.<br><br>
	 * 
	 * <b>NOTE:</b><br>
	 * Internally, these are called "elements" and stuff you can add to the HUD, like images, are called "items", but they are both called "elements" in the actual mod.<br>
	 * This was done to not forget that they are different things in the code, even if they look the same in the HUD.
	 */
	public static HudElementRegistry getHudElementRegistry() {
		return HudElementRegistry.getInstance();
	}
	
	/**
	 * Gets the {@link CustomizationItemRegistry} instance that allows you to register your own <b>customization items</b>.<br>
	 * Customization items are all elements you can add to the HUD, like images, texts and more.<br><br>
	 * 
	 * <b>NOTE:</b><br>
	 * Internally, these are called "items" and vanilla elements are called "elements", but they are both called "elements" in the actual mod.<br>
	 * This was done to not forget that they are different things in the code, even if they look the same in the HUD.
	 */
	public static CustomizationItemRegistry getCustomizationItemRegistry() {
		return CustomizationItemRegistry.getInstance();
	}
	
}
