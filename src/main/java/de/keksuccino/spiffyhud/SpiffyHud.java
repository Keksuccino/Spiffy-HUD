package de.keksuccino.spiffyhud;

import java.io.File;

import de.keksuccino.spiffyhud.customization.items.visibilityrequirements.VisibilityRequirementHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import de.keksuccino.spiffyhud.api.DynamicValueRegistry;
import de.keksuccino.spiffyhud.api.IngameHud;
import de.keksuccino.spiffyhud.api.hud.HudElementRegistry;
import de.keksuccino.spiffyhud.api.item.CustomizationItemRegistry;
import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.dynamicvalues.DynamicValueHelper;
import de.keksuccino.spiffyhud.customization.helper.CustomizationHelper;
import de.keksuccino.spiffyhud.customization.rendering.slideshow.SlideshowHandler;
import de.keksuccino.spiffyhud.keybinding.Keybinding;
import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.config.Config;
import de.keksuccino.konkrete.localization.Locals;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpiffyHud implements ModInitializer {
	
	public static final String VERSION = "1.5.0";
	
	public static final File HOME_DIR = new File("config/spiffyhud");
	public static final File CUSTOMIZATION_DIR = new File(HOME_DIR.getPath() + "/customization");
	public static final File SLIDESHOW_DIR = new File(HOME_DIR.getPath() + "/slideshows");
	
	public static final Logger LOGGER = LogManager.getLogger("spiffyhud");
	
	public static Config config;
	
	private static boolean fancymenuLoaded = false;

	@Override
	public void onInitialize() {

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {

			if (!HOME_DIR.exists()) {
				HOME_DIR.mkdirs();
			}
			if (!CUSTOMIZATION_DIR.exists()) {
				CUSTOMIZATION_DIR.mkdirs();
			}
			if (!SLIDESHOW_DIR.exists()) {
				SLIDESHOW_DIR.mkdirs();
			}

			try {
				Class.forName("de.keksuccino.fancymenu.FancyMenu");
				fancymenuLoaded = true;
			} catch (Exception e) {}

			updateConfig();

			SlideshowHandler.init();

			CustomizationHandler.init();

			CustomizationHelper.init();

			VisibilityRequirementHandler.init();

			if (config.getOrDefault("enablekeybinds", true)) {
				Keybinding.init();
			}

			Konkrete.addPostLoadingEvent("spiffyhud", this::onClientSetup);

//			ExampleCustomDynamicVariables.registerDynamicValues();

//			MinecraftForge.EVENT_BUS.register(new Test());

		} else {
			LOGGER.warn("## WARNING ## 'Spiffy HUD' is a client mod and has no effect when loaded on a server!");
		}

	}
	
	private void onClientSetup() {
		try {

			initLocals();
        	
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
			
			config.clearUnusedValues();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void initLocals() {
		String baseresdir = "locals/";
		File f = new File("config/spiffyhud/locals");
		if (!f.exists()) {
			f.mkdirs();
		}
		
		Locals.copyLocalsFileToDir(new Identifier("spiffyhud", baseresdir + "en_us.local"), "en_us", f.getPath());
//		Locals.copyLocalsFileToDir(new ResourceLocation("spiffyhud", baseresdir + "de_de.local"), "de_de", f.getPath());
		
		Locals.getLocalsFromDir(f.getPath());
	}

	@Deprecated
	public static IngameHud getIngameHud() {
		return IngameHud.getInstance();
	}

	@Deprecated
	public static DynamicValueRegistry getDynamicValueRegistry() {
		return DynamicValueRegistry.getInstance();
	}

	@Deprecated
	public static HudElementRegistry getHudElementRegistry() {
		return HudElementRegistry.getInstance();
	}

	@Deprecated
	public static CustomizationItemRegistry getCustomizationItemRegistry() {
		return CustomizationItemRegistry.getInstance();
	}

}
