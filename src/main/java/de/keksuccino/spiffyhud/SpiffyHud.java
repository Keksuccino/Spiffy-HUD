package de.keksuccino.spiffyhud;

import java.io.File;

import de.keksuccino.spiffyhud.customization.items.visibilityrequirements.VisibilityRequirementHandler;
import org.apache.commons.lang3.tuple.Pair;

import de.keksuccino.spiffyhud.api.DynamicValueRegistry;
import de.keksuccino.spiffyhud.api.IngameHud;
import de.keksuccino.spiffyhud.api.hud.HudElementRegistry;
import de.keksuccino.spiffyhud.api.item.CustomizationItemRegistry;
import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.dynamicvalues.DynamicValueHelper;
import de.keksuccino.spiffyhud.customization.helper.CustomizationHelper;
import de.keksuccino.spiffyhud.customization.rendering.slideshow.SlideshowHandler;
import de.keksuccino.spiffyhud.keybinding.Keybinding;
import de.keksuccino.spiffyhud.logger.Logging;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("spiffyhud")
public class SpiffyHud {
	
	public static final String VERSION = "1.4.4";
	
	public static final File HOME_DIR = new File("config/spiffyhud");
	public static final File CUSTOMIZATION_DIR = new File(HOME_DIR.getPath() + "/customization");
	public static final File SLIDESHOW_DIR = new File(HOME_DIR.getPath() + "/slideshows");
	
	public static final Logger LOGGER = LogManager.getLogger("spiffyhud");
	
	public static Config config;
	
	private static boolean fancymenuLoaded = false;

	public SpiffyHud() {
		
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
				LOGGER.info("[SPIFFY HUD] FancyMenu found!");
				fancymenuLoaded = true;
			}
			
			updateConfig();
			
			Logging.init();
			
			SlideshowHandler.init();

			CustomizationHandler.init();
			
			CustomizationHelper.init();

			VisibilityRequirementHandler.init();
			
			if (config.getOrDefault("enablekeybinds", true)) {
        		Keybinding.init();
        	}
			
			Konkrete.addPostLoadingEvent("spiffyhud", this::onClientSetup);

//			MinecraftForge.EVENT_BUS.register(new Test());

		} else {
			LOGGER.warn("## WARNING ## 'Spiffy HUD' is a client mod and has no effect when loaded on a server!");
		}
		
	}
	
	private void onClientSetup() {
		try {

			initLocals();
        	
        	DynamicValueHelper.init();
	    	
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
		
		Locals.copyLocalsFileToDir(new ResourceLocation("spiffyhud", baseresdir + "en_us.local"), "en_us", f.getPath());
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
